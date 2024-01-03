package de.takacick.raidbase.mixin;

import de.takacick.raidbase.access.SlimeProperties;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntityRenderer.class)
public abstract class SlimeEntityRendererMixin extends MobEntityRenderer<SlimeEntity, SlimeEntityModel<SlimeEntity>> {

    public SlimeEntityRendererMixin(EntityRendererFactory.Context context, SlimeEntityModel<SlimeEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "scale(Lnet/minecraft/entity/mob/SlimeEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("TAIL"))
    public void scale(SlimeEntity slimeEntity, MatrixStack matrixStack, float f, CallbackInfo info) {
        if (slimeEntity instanceof SlimeProperties slimeProperties && slimeProperties.isSlimeSheared()) {
            matrixStack.translate(0, 0.0625, 0);
        }
    }
}
