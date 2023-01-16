package de.takacick.imagineanything.registry.entity.projectiles.renderer;

import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.entity.projectiles.GiantNetheriteFeatherEntity;
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
public class GiantNetheriteFeatherEntityRenderer extends EntityRenderer<GiantNetheriteFeatherEntity> {

    public GiantNetheriteFeatherEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(GiantNetheriteFeatherEntity giantNetheriteFeatherEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        matrixStack.scale(2.4F, 2.4F, 2.4F);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, giantNetheriteFeatherEntity.prevYaw, giantNetheriteFeatherEntity.getYaw()) - 90));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, giantNetheriteFeatherEntity.prevPitch, giantNetheriteFeatherEntity.getPitch()) - 45));

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(ItemRegistry.IMAGINED_GIANT_NETHERITE_FEATHER.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, giantNetheriteFeatherEntity.getId());
        matrixStack.pop();

        super.render(giantNetheriteFeatherEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(GiantNetheriteFeatherEntity giantNetheriteFeatherEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}