package de.takacick.onegirlfriendblock.client.renderer;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.access.LivingProperties;
import de.takacick.onegirlfriendblock.client.model.MaidSuitModel;
import de.takacick.onegirlfriendblock.client.shaders.OneGirlfriendBlockLayers;
import de.takacick.onegirlfriendblock.registry.item.MaidSuit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class MaidSuitRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/maid_suit.png");
    public static final MaidSuitModel MAID_SUIT = new MaidSuitModel(MaidSuitModel.getTexturedModelData().createModel());
    private final MaidSuitModel maidSuitModel;

    public MaidSuitRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext, MaidSuitModel maidSuitModel) {
        super(featureRendererContext);
        this.maidSuitModel = maidSuitModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() instanceof MaidSuit) {
            RenderLayer renderLayer;

            MinecraftClient minecraftClient = MinecraftClient.getInstance();

            boolean bl = this.isVisible(entity);
            boolean bl2 = !bl && !entity.isInvisibleTo(minecraftClient.player);
            boolean bl3 = minecraftClient.hasOutline(entity);

            renderLayer = getRenderLayer(bl, bl2, bl3);

            if (renderLayer != null) {
                if (entity instanceof LivingProperties livingProperties
                        && livingProperties.getLipstickStrength() > 0f) {
                    renderLayer = OneGirlfriendBlockLayers.getLipstick(getTexture(entity),
                            livingProperties.getLipstickStrength());
                }

                this.getContextModel().copyBipedStateTo(this.maidSuitModel);
                this.maidSuitModel.refreshOverlayState();
                this.maidSuitModel.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            }
        }
    }

    @Nullable
    protected RenderLayer getRenderLayer(boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = TEXTURE;
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showBody) {
            return this.getContextModel().getLayer(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }

    protected boolean isVisible(AbstractClientPlayerEntity entity) {
        return !entity.isInvisible();
    }
}

