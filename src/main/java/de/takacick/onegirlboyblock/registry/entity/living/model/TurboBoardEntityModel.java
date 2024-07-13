package de.takacick.onegirlboyblock.registry.entity.living.model;

import de.takacick.onegirlboyblock.client.utils.AdvancedAnimationHelper;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import de.takacick.onegirlboyblock.registry.entity.living.model.animation.TurboBoardAnimations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;

import java.util.Optional;

public class TurboBoardEntityModel<T extends TurboBoardEntity> extends SinglePartEntityModel<T> {

    public final ModelPart root;
    private final ModelPart turboBoard;
    private final ModelPart turboBeamFront;
    private final ModelPart turboBeamBack;

    public TurboBoardEntityModel(ModelPart root) {
        this.root = root.getChild("bone");
        this.turboBoard = this.root.getChild("turbo_board");
        this.turboBeamFront = this.turboBoard.getChild("turbo_beam_front");
        this.turboBeamBack = this.turboBoard.getChild("turbo_beam_back");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        Vector3f TEMP = new Vector3f();
        float speed = entity.limbAnimator.getSpeed(MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        AdvancedAnimationHelper.animateLerp(this, TurboBoardAnimations.IDLE, TurboBoardAnimations.TURBO_BOARD, speed, (long) (animationProgress / 20f * 1000f), 1.0f, TEMP, true);
    }

    public void rotateTurboBeam(MatrixStack matrixStack, boolean front) {
        getPart().rotate(matrixStack);
        this.turboBoard.rotate(matrixStack);
        if (front) {
            this.turboBeamFront.rotate(matrixStack);
        } else {
            this.turboBeamBack.rotate(matrixStack);
        }
    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return name.equals("bone") ? Optional.of(getPart()) : super.getChild(name);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData turbo_board = bone.addChild("turbo_board", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -3.75F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(37, 38).cuboid(-7.0F, -4.5F, 16.0F, 14.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(41, 22).cuboid(-6.0F, -5.0F, 20.0F, 12.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 31).cuboid(-4.0F, -5.5F, 22.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-8.0F, -4.0F, 8.0F, 16.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 38).cuboid(-7.0F, -4.5F, -20.0F, 14.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(41, 18).cuboid(-6.0F, -5.0F, -22.0F, 12.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 28).cuboid(-4.0F, -5.5F, -23.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 18).cuboid(-8.0F, -4.0F, -16.0F, 16.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData turbo_beam_front = turbo_board.addChild("turbo_beam_front", ModelPartBuilder.create(), ModelTransform.of(0.0F, -3.0F, -12.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData turbo_beam_back = turbo_board.addChild("turbo_beam_back", ModelPartBuilder.create(), ModelTransform.of(0.0F, -3.0F, 12.0F, 3.1416F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}

