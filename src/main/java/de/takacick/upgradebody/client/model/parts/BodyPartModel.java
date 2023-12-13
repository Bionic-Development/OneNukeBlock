package de.takacick.upgradebody.client.model.parts;

import de.takacick.upgradebody.client.model.BodyEntityModel;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashSet;

public abstract class BodyPartModel {

    abstract public ModelPart getRoot();

    public void setAngles(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.render(matrices, vertexConsumerProvider.getBuffer(renderLayer), light, overlay, red, green, blue, alpha);
    }

    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {

    }

    public void resetTransform() {

    }

    public Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return abstractClientPlayerEntity.getSkinTexture();
    }

    public RenderLayer getRenderLayer(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return null;
    }

    public static float getPivotY(BodyPart bodyPart, Collection<BodyPart> bodyPartList) {
        float pivotY = 0f;

        HashSet<Integer> heightIndexes = new HashSet<>();
        bodyPartList.forEach(part -> heightIndexes.add(part.getHeightIndex()));

        for (Integer index : heightIndexes) {
            if (index == bodyPart.getHeightIndex() || index < bodyPart.getHeightIndex()) {
                continue;
            }
            pivotY += (float) bodyPartList.stream()
                    .filter(part -> part.getHeightIndex() == index && part.affectModelOrdering())
                    .mapToDouble(BodyPart::getPivotYOffset).max().orElse(0);
        }

        return pivotY;
    }
}
