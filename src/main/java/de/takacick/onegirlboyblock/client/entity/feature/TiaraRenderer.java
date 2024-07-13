package de.takacick.onegirlboyblock.client.entity.feature;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.entity.model.FootballGearModel;
import de.takacick.onegirlboyblock.client.entity.model.TiaraModel;
import de.takacick.onegirlboyblock.registry.item.FootballGear;
import de.takacick.onegirlboyblock.registry.item.Tiara;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class TiaraRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/tiara.png");
    private final TiaraModel<T> tiaraModel;

    public TiaraRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
        this.tiaraModel = new TiaraModel<>(TiaraModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof Tiara) {
            RenderLayer renderLayer = tiaraModel.getLayer(TEXTURE);
            this.tiaraModel.copyFromBipedState(this.getContextModel());
            this.tiaraModel.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV);
        }
    }
}

