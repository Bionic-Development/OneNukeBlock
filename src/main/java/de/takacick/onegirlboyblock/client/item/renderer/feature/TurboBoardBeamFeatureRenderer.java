package de.takacick.onegirlboyblock.client.item.renderer.feature;

import de.takacick.onegirlboyblock.client.item.model.TurboBoardItemModel;
import de.takacick.onegirlboyblock.client.utils.TurboBeamRenderer;
import de.takacick.utils.item.client.render.feature.ItemFeatureRenderer;
import de.takacick.utils.item.client.render.feature.ItemFeatureRendererContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public class TurboBoardBeamFeatureRenderer extends ItemFeatureRenderer<TurboBoardItemModel> {

    public TurboBoardBeamFeatureRenderer(ItemFeatureRendererContext<TurboBoardItemModel> context) {
        super(context);
    }

    @Override
    public void render(ItemStack itemStack, @Nullable LivingEntity livingEntity, float tickDelta, long time, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, ModelTransformationMode modelTransformationMode, int i, int i1) {
        Vector3f color = Vec3d.unpackRgb(0x0CCDFF).toVector3f();

        for(boolean beamFront : List.of(false, true)) {
            matrixStack.push();
            getContextModel().rotateTurboBeam(matrixStack, beamFront);

            TurboBeamRenderer.renderBeam(matrixStack, vertexConsumerProvider, tickDelta, time, 0f, 0.35f, new float[]{color.x(), color.y(), color.z()}, 0.25f, 0.3333f, 0f);
            TurboBeamRenderer.renderBeam(matrixStack, vertexConsumerProvider, tickDelta, time, 0f, 0.45f, new float[]{color.x(), color.y(), color.z()}, 0.2f, 0.666f, 0f);
            TurboBeamRenderer.renderBeam(matrixStack, vertexConsumerProvider, tickDelta, time, 0f, 0.55f, new float[]{color.x(), color.y(), color.z()}, 0.15f, 1f, 0f);

            matrixStack.pop();
        }
    }
}

