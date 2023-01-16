package de.takacick.imagineanything.registry.entity.custom.renderer;

import com.mojang.authlib.GameProfile;
import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.registry.item.HeadItem;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.Vec3f;
import org.apache.commons.lang3.StringUtils;

public class HoldingHeadFeatureRenderer<T extends PlayerEntity, M extends PlayerEntityModel<T>>
        extends FeatureRenderer<T, M> {

    private final ModelPart head;

    public HoldingHeadFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        this.head = getTexturedModelData().createModel().getChild("head");
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack stack = livingEntity.getMainHandStack().getItem() instanceof HeadItem ? livingEntity.getMainHandStack() : livingEntity.getOffHandStack();
        if (!(stack.getItem() instanceof HeadItem) || ((PlayerProperties) livingEntity).getHeadRemovalState().isRunning()) {
            return;
        }
        matrixStack.push();

        GameProfile gameProfile = null;
        if (stack.hasNbt()) {
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
                gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
            } else if (nbtCompound.contains("SkullOwner", NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
                gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
                nbtCompound.remove("SkullOwner");
                SkullBlockEntity.loadProperties(gameProfile, profile -> nbtCompound.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile)));
            }
        }
        SkullBlock.SkullType skullType = SkullBlock.Type.PLAYER;
        RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, gameProfile);
        head.resetTransform();
        if (livingEntity.isInSneakingPose()) {
            head.translate(new Vec3f(0, 3, 0));
        }
        head.render(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.of(-5.0F, 2.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        head.addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(5.0F, 9.0F, 4.5F, 1.5708F, 0.0F, 0.0F));
        head.addChild("head_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 9.0F, 4.5F, 1.5708F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

