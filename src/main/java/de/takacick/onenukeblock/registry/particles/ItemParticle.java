package de.takacick.onenukeblock.registry.particles;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;

public interface ItemParticle {

    void setMatrixEntry(MatrixStack.Entry entry, ModelTransformationMode modelTransformationMode, boolean force);

    boolean isAlive();

    void tick();

    ParticleTextureSheet getType();

    void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta);

}
