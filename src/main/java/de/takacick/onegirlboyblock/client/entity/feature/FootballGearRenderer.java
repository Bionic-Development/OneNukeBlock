package de.takacick.onegirlboyblock.client.entity.feature;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.client.entity.model.FootballGearModel;
import de.takacick.onegirlboyblock.registry.item.FootballGear;
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

public class FootballGearRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = Identifier.of(OneGirlBoyBlock.MOD_ID, "textures/entity/football_gear.png");
    private final FootballGearModel<T> footballGearModel;

    public FootballGearRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
        this.footballGearModel = new FootballGearModel<>(FootballGearModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() instanceof FootballGear) {
            RenderLayer renderLayer = footballGearModel.getLayer(TEXTURE);
            this.getContextModel().copyBipedStateTo(this.footballGearModel);
            this.footballGearModel.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV);
        }
    }
}

