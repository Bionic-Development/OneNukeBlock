package de.takacick.onedeathblock.registry.entity.projectiles.renderer;

import de.takacick.onedeathblock.registry.ItemRegistry;
import de.takacick.onedeathblock.registry.entity.projectiles.BuildMeteorEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class BuildMeteorEntityRenderer extends EntityRenderer<BuildMeteorEntity> {

    public BuildMeteorEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(BuildMeteorEntity buildMeteorEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.scale(16f, 16f, 16f);
        matrixStack.translate(0, 0.25, 0);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(buildMeteorEntity.getYaw() + buildMeteorEntity.getId()));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, buildMeteorEntity.prevPitch, buildMeteorEntity.getPitch())));
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getRadialQuaternion(MathHelper.lerp(g, buildMeteorEntity.age - 1 + buildMeteorEntity.getId(), buildMeteorEntity.age + buildMeteorEntity.getId()) / 2));

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(ItemRegistry.BUILD_METEOR.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, buildMeteorEntity.getId());
        matrixStack.pop();

        super.render(buildMeteorEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(BuildMeteorEntity buildMeteorEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}