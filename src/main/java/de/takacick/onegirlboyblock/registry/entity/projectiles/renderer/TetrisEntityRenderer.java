package de.takacick.onegirlboyblock.registry.entity.projectiles.renderer;

import com.google.common.collect.Maps;
import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import de.takacick.onegirlboyblock.registry.entity.projectiles.model.TetrisModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class TetrisEntityRenderer extends EntityRenderer<TetrisEntity> {

    private static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/tetris/yellow.png");

    public static final Map<TetrisEntity.Variant, Identifier> TEXTURES = Util.make(Maps.newHashMap(), variants -> {
        for (TetrisEntity.Variant variant : TetrisEntity.Variant.values()) {
            variants.put(variant, Identifier.of(OneGirlBoyBlock.MOD_ID, String.format(Locale.ROOT, "textures/entity/tetris/%s.png", variant.getName())));
        }
    });

    public static final HashMap<TetrisEntity.Variant, TetrisModel> MODELS = Util.make(Maps.newHashMap(), variants -> {
        variants.put(TetrisEntity.Variant.YELLOW, new TetrisModel(TetrisModel.getYellowTexturedModelData().createModel()));
        variants.put(TetrisEntity.Variant.PURPLE, new TetrisModel(TetrisModel.getPurpleTexturedModelData().createModel()));
        variants.put(TetrisEntity.Variant.ORANGE, new TetrisModel(TetrisModel.getOrangeTexturedModelData().createModel()));
        variants.put(TetrisEntity.Variant.BLUE, new TetrisModel(TetrisModel.getBlueTexturedModelData().createModel()));
        variants.put(TetrisEntity.Variant.CYAN, new TetrisModel(TetrisModel.getCyanTexturedModelData().createModel()));
        variants.put(TetrisEntity.Variant.RED, new TetrisModel(TetrisModel.getRedTexturedModelData().createModel()));
        variants.put(TetrisEntity.Variant.GREEN, new TetrisModel(TetrisModel.getGreenTexturedModelData().createModel()));
    });

    public TetrisEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(TetrisEntity tetrisEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, tetrisEntity.prevYaw, tetrisEntity.getYaw())));
        matrixStack.translate(0, 0.125f, 0);
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(MathHelper.lerp(g, tetrisEntity.prevPitch, tetrisEntity.getPitch()) + 180f));
        Random random = Random.create(tetrisEntity.getId() * 30L);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(random.nextInt() % 4 * 90f));

        float scale = 0.3f;
        matrixStack.scale(-scale, -scale, scale);
        matrixStack.translate(0, -1.501f, 0);

        TetrisModel tetrisModel = MODELS.get(tetrisEntity.getVariant());
        if (tetrisModel != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(tetrisEntity)));
            tetrisModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        }
        matrixStack.pop();

        super.render(tetrisEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(TetrisEntity tetrisEntity) {
        return TEXTURES.getOrDefault(tetrisEntity.getVariant(), TEXTURE);
    }

    @Override
    protected int getBlockLight(TetrisEntity tetrisEntity, BlockPos blockPos) {
        return 15;
    }

}
