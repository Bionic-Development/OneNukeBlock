package de.takacick.deathmoney.registry.entity.projectiles.renderer;

import de.takacick.deathmoney.registry.entity.projectiles.TntNukeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class TntNukeEntityRenderer extends EntityRenderer<TntNukeEntity> {

    private final BlockRenderManager blockRenderManager;

    public TntNukeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.blockRenderManager = context.getBlockRenderManager();
    }

    public void render(TntNukeEntity tntNukeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0, 0.5, 0.0);
        int j = tntNukeEntity.age;
        matrixStack.scale(5, 5, 5);
        if ((float) j - g + 1.0f < 10.0f) {
            float h = 1.0f - ((float) j - g + 1.0f) / 10.0f;
            h = MathHelper.clamp(h, 0.0f, 1.0f);
            h *= h;
            h *= h;
            float k = 1.0f + h * 0.3f;
            matrixStack.scale(k, k, k);
        }
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0f));
        matrixStack.translate(-0.5, -0.5, 0.5);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
        TntMinecartEntityRenderer.renderFlashingBlock(this.blockRenderManager, Blocks.TNT.getDefaultState(), matrixStack, vertexConsumerProvider, i, j / 5 % 2 == 0);
        matrixStack.pop();

        super.render(tntNukeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(TntNukeEntity tntNukeEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
