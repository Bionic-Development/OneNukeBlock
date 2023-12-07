package de.takacick.upgradebody.registry.entity.custom.model;

import de.takacick.upgradebody.registry.entity.custom.EmeraldShopPortalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(EnvType.CLIENT)
public class EmeraldShopPortalEntityModel<T extends EmeraldShopPortalEntity> extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart portal;

    public EmeraldShopPortalEntityModel() {
        super(RenderLayer::getEntityCutoutNoCull);
        this.root = getTexturedModelData().createModel();
        this.portal = getPortalModelData().createModel();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(129, 322).cuboid(-8.0F, -30.3571F, -36.2867F, 16.0F, 31.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 257).cuboid(24.0F, -13.3571F, -20.2857F, 16.0F, 64.0F, 48.0F, new Dilation(0.0F))
                .uv(0, 257).mirrored().cuboid(-40.0F, -13.3571F, -20.2857F, 16.0F, 64.0F, 48.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 144).cuboid(-40.0F, -13.3571F, 27.7143F, 80.0F, 64.0F, 15.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-40.0F, -93.3581F, -20.2857F, 80.0F, 80.0F, 63.0F, new Dilation(0.0F))
                .uv(0, 370).cuboid(-24.0F, -13.3571F, -20.2857F, 48.0F, 14.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -26.6429F, -3.7143F));
        return TexturedModelData.of(modelData, 400, 400);
    }

    public static TexturedModelData getPortalModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("portal", ModelPartBuilder.create().uv(0, 0).cuboid(10.0F, -16.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(10.0F, -32.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(10.0F, -48.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(10.0F, -64.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -48.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -32.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -16.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -64.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-22.0F, -48.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-22.0F, -32.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-22.0F, -16.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-22.0F, -64.0F, -8.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 24.0F, -1.0F));

        return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void setAngles(T diamondShopPortalEntity, float f, float g, float h, float i, float j) {

    }

    public ModelPart getPortal() {
        return portal;
    }

    @Override
    public ModelPart getPart() {
        return root;
    }
}
