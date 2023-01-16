package de.takacick.imagineanything.registry.entity.custom.renderer;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.ItemRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class IronManSuitFeatureRenderer<T extends PlayerEntity, M extends PlayerEntityModel<T>>
        extends FeatureRenderer<T, M> {
    private final Identifier IRON_MAN_SUIT = new Identifier(ImagineAnything.MOD_ID, "textures/entity/iron_man_suit_glow.png");

    public IronManSuitFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!livingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
            return;
        }

        matrixStack.push();
        getContextModel().render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEyes(IRON_MAN_SUIT)), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }
}