package de.takacick.onegirlboyblock.registry.entity.living.renderer.feature;

import de.takacick.onegirlboyblock.client.utils.TurboBeamRenderer;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import de.takacick.onegirlboyblock.registry.entity.living.model.TurboBoardEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public class TurboBoardBeamFeatureRenderer extends FeatureRenderer<TurboBoardEntity, TurboBoardEntityModel<TurboBoardEntity>> {

    public TurboBoardBeamFeatureRenderer(FeatureRendererContext<TurboBoardEntity, TurboBoardEntityModel<TurboBoardEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, TurboBoardEntity ufoEntity, float f, float g, float tickDelta, float j, float k, float l) {
        Vector3f color = Vec3d.unpackRgb(0x0CCDFF).toVector3f();

        long time = ufoEntity.getWorld().getTime();

        for (boolean beamFront : List.of(false, true)) {
            matrixStack.push();
            getContextModel().rotateTurboBeam(matrixStack, beamFront);

            TurboBeamRenderer.renderBeam(matrixStack, vertexConsumerProvider, tickDelta, time, 0f, 0.35f, new float[]{color.x(), color.y(), color.z()}, 0.25f, 0.3333f, 0f);
            TurboBeamRenderer.renderBeam(matrixStack, vertexConsumerProvider, tickDelta, time, 0f, 0.45f, new float[]{color.x(), color.y(), color.z()}, 0.2f, 0.666f, 0f);
            TurboBeamRenderer.renderBeam(matrixStack, vertexConsumerProvider, tickDelta, time, 0f, 0.55f, new float[]{color.x(), color.y(), color.z()}, 0.15f, 1f, 0f);

            matrixStack.pop();
        }
    }
}

