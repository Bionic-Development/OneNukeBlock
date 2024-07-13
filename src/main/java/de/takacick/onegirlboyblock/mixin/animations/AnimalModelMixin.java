package de.takacick.onegirlboyblock.mixin.animations;

import de.takacick.onegirlboyblock.access.PlayerModelProperties;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalModel.class)
public abstract class AnimalModelMixin<E extends Entity>
        extends EntityModel<E> {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo info) {
        if (this instanceof PlayerModelProperties playerModelProperties) {
            playerModelProperties.getAnimationBodyModel().getPart().getChild("bone").rotate(matrices);
            matrices.translate(0, -1.501f, 0);
        }
    }
}
