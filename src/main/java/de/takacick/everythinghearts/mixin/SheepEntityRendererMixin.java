package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.registry.entity.custom.renderer.HeartSheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntityRenderer.class)
public abstract class SheepEntityRendererMixin extends MobEntityRenderer<SheepEntity, SheepEntityModel<SheepEntity>> {

    public SheepEntityRendererMixin(EntityRendererFactory.Context context, SheepEntityModel<SheepEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new HeartSheepWoolFeatureRenderer(this, context.getModelLoader()));
    }
}
