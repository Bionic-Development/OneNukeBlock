package de.takacick.elementalblock.registry.entity.projectile.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value = EnvType.CLIENT)
public class TsunamicTridentEntityModel extends Model {
    private final ModelPart root;

    public TsunamicTridentEntityModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}

