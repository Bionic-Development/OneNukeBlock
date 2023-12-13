package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.client.renderer.BodyEntityRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin implements SynchronousResourceReloader {

    @Shadow
    @Final
    private ItemRenderer itemRenderer;
    @Shadow
    @Final
    private BlockRenderManager blockRenderManager;
    @Shadow
    @Final
    private TextRenderer textRenderer;
    @Shadow
    @Final
    private EntityModelLoader modelLoader;
    @Shadow
    @Final
    private HeldItemRenderer heldItemRenderer;

    @Unique
    private BodyEntityRenderer upgradebody$bodyEntityRenderer;

    @Inject(method = "reload", at = @At(value = "TAIL"))
    public void reload(ResourceManager manager, CallbackInfo info) {
        EntityRendererFactory.Context context = new EntityRendererFactory.Context((EntityRenderDispatcher) (Object) this, this.itemRenderer, this.blockRenderManager, this.heldItemRenderer, manager, this.modelLoader, this.textRenderer);
        this.upgradebody$bodyEntityRenderer = new BodyEntityRenderer(context);
    }

    @Inject(method = "getRenderer", at = @At(value = "HEAD"), cancellable = true)
    public <T extends Entity> void getRenderer(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> info) {
        if (entity instanceof PlayerProperties playerProperties && playerProperties.isUpgrading()) {
            info.setReturnValue((EntityRenderer<? super T>) this.upgradebody$bodyEntityRenderer);
        }
    }
}
