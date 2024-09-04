package de.takacick.onenukeblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BladedTntBlockItemModel extends SinglePartItemModel {

    public BladedTntBlockItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.0F, 2.0F));

        ModelPartData tnt = bone.addChild("tnt", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -3.0F, -10.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData diamond_swords = bone.addChild("diamond_swords", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData diamond_sword_bone_1 = diamond_swords.addChild("diamond_sword_bone_1", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData diamond_sword20 = diamond_sword_bone_1.addChild("diamond_sword20", ModelPartBuilder.create().uv(4, 35).cuboid(-5.9F, -5.3F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-2.9F, -4.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-1.9F, -3.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-0.9F, -2.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-4.9F, -2.3F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(0.1F, -1.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-3.9F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(1.1F, -0.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(6.1F, 2.7F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0256F, 6.742F, -0.0168F, -1.5074F, -0.2324F, -1.4217F));

        ModelPartData diamond_sword6 = diamond_sword_bone_1.addChild("diamond_sword6", ModelPartBuilder.create().uv(4, 35).cuboid(-5.9F, -5.3F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-2.9F, -4.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-1.9F, -3.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-0.9F, -2.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-4.9F, -2.3F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(0.1F, -1.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-3.9F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(1.1F, -0.3F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(6.1F, 2.7F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0256F, 3.242F, 4.9832F, -1.8079F, 0.6679F, -0.7827F));

        ModelPartData diamond_sword5 = diamond_sword_bone_1.addChild("diamond_sword5", ModelPartBuilder.create().uv(4, 35).cuboid(-7.3824F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-4.3824F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-3.3824F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-2.3824F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-6.3824F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-1.3824F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-5.3824F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-0.3824F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(19, 56).cuboid(0.6176F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 50).cuboid(1.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 41).cuboid(2.6176F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 46).cuboid(4.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 50).cuboid(-0.3824F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.4356F, 5.9365F, -3.2475F, -1.7411F, -1.0022F, -1.6949F));

        ModelPartData diamond_sword11 = diamond_sword_bone_1.addChild("diamond_sword11", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(1.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(0.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-0.8947F, -2.3684F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-3.8947F, -1.3684F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-1.8947F, -1.3684F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-4.8947F, -0.3684F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-2.8947F, -0.3684F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(0.1053F, 2.6316F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(1.1053F, 3.6316F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.984F, -3.745F, -1.5985F, -1.8408F, -1.2618F, 3.0171F));

        ModelPartData diamond_sword10 = diamond_sword_bone_1.addChild("diamond_sword10", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3824F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3824F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3824F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3824F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3824F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3824F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3824F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6176F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6176F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6176F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6176F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3824F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(7.8983F, 2.8485F, -1.5084F, -1.3377F, 0.2674F, 0.6352F));

        ModelPartData diamond_sword8 = diamond_sword_bone_1.addChild("diamond_sword8", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(1.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(0.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-0.8947F, -2.3684F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-1.8947F, -1.3684F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(0.1053F, 2.6316F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(1.1053F, 3.6316F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.484F, -0.2451F, -4.5985F, 1.7147F, -0.8815F, -0.1562F));

        ModelPartData diamond_sword30 = diamond_sword_bone_1.addChild("diamond_sword30", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3823F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3823F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3823F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3823F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3824F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3823F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6176F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6176F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6177F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6176F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6177F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3824F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.8027F, -0.1077F, -9.6517F, 1.5037F, 0.523F, 0.3875F));

        ModelPartData diamond_sword13 = diamond_sword_bone_1.addChild("diamond_sword13", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3823F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3824F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3824F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3823F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3823F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3823F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6177F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6176F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6177F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6176F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6176F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6177F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3823F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.6973F, -2.4077F, 3.2483F, -2.224F, -1.0711F, 2.1197F));

        ModelPartData diamond_sword29 = diamond_sword_bone_1.addChild("diamond_sword29", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3823F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3823F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3823F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3823F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3824F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3823F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6176F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6176F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6177F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6176F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6177F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3824F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.8027F, -6.1077F, -8.1517F, 1.7383F, 0.4078F, -0.5112F));

        ModelPartData diamond_sword28 = diamond_sword_bone_1.addChild("diamond_sword28", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3824F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3824F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3823F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3824F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3823F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3824F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6176F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6176F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6176F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6177F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6177F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6177F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3823F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.1973F, -4.1077F, -9.6517F, 1.5827F, 0.922F, -0.248F));

        ModelPartData diamond_sword27 = diamond_sword_bone_1.addChild("diamond_sword27", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3823F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3823F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3824F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3823F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3824F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3823F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6177F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6177F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6177F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6176F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6177F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6177F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3824F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.8027F, 1.8923F, -8.6517F, 2.1886F, 0.7652F, 0.0546F));

        ModelPartData diamond_sword26 = diamond_sword_bone_1.addChild("diamond_sword26", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3823F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3823F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3824F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3823F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3824F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3823F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6177F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6176F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6177F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6176F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6176F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6177F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6177F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.3824F, 4.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.1973F, 0.8923F, -11.6517F, 1.1631F, 0.7348F, -1.6567F));

        ModelPartData diamond_sword9 = diamond_sword_bone_1.addChild("diamond_sword9", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.3824F, -7.6765F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.3823F, -6.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.3823F, -5.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.3823F, -4.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.3824F, -4.6765F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.3823F, -3.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.3824F, -3.6765F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.6176F, -2.6765F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(15, 38).mirrored().cuboid(-6.6176F, -1.6765F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.6176F, -1.6765F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.6177F, -0.6765F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.6177F, 0.3235F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.6176F, 0.3235F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.6177F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.6176F, 3.3235F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.8027F, 1.8923F, 3.8483F, -1.2225F, -0.5318F, 1.5847F));

        ModelPartData diamond_sword7 = diamond_sword_bone_1.addChild("diamond_sword7", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(4.0313F, -7.7188F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(3.0313F, -6.7188F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(2.0313F, -5.7188F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(1.0313F, -4.7188F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(4.0313F, -4.7188F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(0.0313F, -3.7188F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(4.0313F, -3.7188F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(-0.9688F, -2.7188F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(19, 56).mirrored().cuboid(-1.9688F, -1.7188F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 55).mirrored().cuboid(-4.9688F, -0.7188F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(24, 50).mirrored().cuboid(-2.9688F, -0.7188F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(11, 46).mirrored().cuboid(-5.9688F, 0.2813F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(5, 41).mirrored().cuboid(-3.9688F, 0.2813F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 46).mirrored().cuboid(-5.9688F, 3.2813F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 50).mirrored().cuboid(-0.9688F, 3.2813F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 60).mirrored().cuboid(0.0313F, 4.2813F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(9.101F, -9.1998F, -2.7502F, -1.7931F, 0.4876F, -0.6193F));

        ModelPartData diamond_sword4 = diamond_sword_bone_1.addChild("diamond_sword4", ModelPartBuilder.create().uv(4, 35).cuboid(-8.0F, -8.0F, -8.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-5.0F, -7.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-4.0F, -6.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-3.0F, -5.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-7.0F, -5.0F, -8.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-2.0F, -4.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-6.0F, -4.0F, -8.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-1.0F, -3.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(19, 56).cuboid(0.0F, -2.0F, -8.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 50).cuboid(1.0F, -1.0F, -8.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(4.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 41).cuboid(2.0F, 0.0F, -8.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 46).cuboid(4.0F, 3.0F, -8.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 50).cuboid(-1.0F, 3.0F, -8.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 60).cuboid(-2.0F, 4.0F, -8.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -14.5F, -1.5F, 1.7425F, 1.3174F, 1.0719F));

        ModelPartData diamond_sword25 = diamond_sword_bone_1.addChild("diamond_sword25", ModelPartBuilder.create().uv(4, 35).cuboid(-8.0F, -8.0F, -8.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-5.0F, -7.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-4.0F, -6.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-3.0F, -5.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-7.0F, -5.0F, -8.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-2.0F, -4.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-6.0F, -4.0F, -8.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-1.0F, -3.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 55).cuboid(3.0F, -1.0F, -8.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 50).cuboid(1.0F, -1.0F, -8.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(4.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 41).cuboid(2.0F, 0.0F, -8.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 46).cuboid(4.0F, 3.0F, -8.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, -11.25F, -1.75F, 0.946F, 1.2488F, -0.253F));

        ModelPartData diamond_sword3 = diamond_sword_bone_1.addChild("diamond_sword3", ModelPartBuilder.create().uv(4, 35).cuboid(-8.0F, -8.0F, -8.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-5.0F, -7.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-4.0F, -6.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-3.0F, -5.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-7.0F, -5.0F, -8.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-2.0F, -4.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-6.0F, -4.0F, -8.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-1.0F, -3.0F, -8.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 55).cuboid(3.0F, -1.0F, -8.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 50).cuboid(1.0F, -1.0F, -8.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(4.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 41).cuboid(2.0F, 0.0F, -8.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 46).cuboid(4.0F, 3.0F, -8.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -8.75F, -1.75F, 1.5022F, 0.33F, 0.0136F));

        ModelPartData diamond_sword = diamond_sword_bone_1.addChild("diamond_sword", ModelPartBuilder.create().uv(4, 35).cuboid(-7.8056F, -8.0556F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-4.8056F, -7.0556F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-3.8056F, -6.0556F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-2.8056F, -5.0556F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-6.8056F, -5.0556F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-1.8056F, -4.0556F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-5.8056F, -4.0556F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-0.8056F, -3.0556F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(15, 38).cuboid(4.1944F, -2.0556F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(19, 56).cuboid(0.1944F, -2.0556F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 55).cuboid(3.1944F, -1.0556F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 50).cuboid(1.1944F, -1.0556F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(4.1944F, -0.0556F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 41).cuboid(2.1944F, -0.0556F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 46).cuboid(4.1944F, 2.9444F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(23, 40).cuboid(6.1944F, 4.9444F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-9.4897F, 3.6447F, -3.0336F, 1.5878F, 0.6265F, -0.2135F));

        ModelPartData diamond_sword2 = diamond_sword_bone_1.addChild("diamond_sword2", ModelPartBuilder.create().uv(4, 35).cuboid(-8.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-5.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-4.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-3.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-7.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-2.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-6.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-1.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(19, 56).cuboid(-0.1053F, -2.3684F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 46).cuboid(3.8947F, -0.3684F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 41).cuboid(1.8947F, -0.3684F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.6283F, 5.593F, -4.0F, -1.7629F, -0.2504F, -0.519F));

        ModelPartData diamond_sword_bone_2 = diamond_swords.addChild("diamond_sword_bone_2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, -8.0F));

        ModelPartData diamond_sword12 = diamond_sword_bone_2.addChild("diamond_sword12", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(1.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(0.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.984F, 0.2549F, 0.4015F, 1.2243F, -0.0695F, -1.4911F));

        ModelPartData diamond_sword24 = diamond_sword_bone_2.addChild("diamond_sword24", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(1.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(0.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.0159F, 9.755F, 0.4015F, 1.5054F, 0.2758F, -1.8832F));

        ModelPartData diamond_sword17 = diamond_sword_bone_2.addChild("diamond_sword17", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(1.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(0.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0159F, 0.7549F, -0.0985F, 1.4101F, 0.3713F, -1.8735F));

        ModelPartData diamond_sword18 = diamond_sword_bone_2.addChild("diamond_sword18", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 54).mirrored().cuboid(1.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 57).mirrored().cuboid(0.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.016F, 4.755F, -1.0985F, 0.5645F, 0.4343F, -2.0948F));

        ModelPartData diamond_sword19 = diamond_sword_bone_2.addChild("diamond_sword19", ModelPartBuilder.create().uv(4, 35).cuboid(-8.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-5.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-4.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-3.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-7.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 54).cuboid(-2.1053F, -4.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-6.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 57).cuboid(-1.1053F, -3.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5159F, 4.755F, -1.0985F, 0.6459F, -0.2846F, 1.865F));

        ModelPartData diamond_sword_bone_3 = diamond_swords.addChild("diamond_sword_bone_3", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 7.0F, 5.0F));

        ModelPartData diamond_sword16 = diamond_sword_bone_3.addChild("diamond_sword16", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.984F, 3.255F, -0.5985F, -0.1588F, -0.0256F, -2.2395F));

        ModelPartData diamond_sword21 = diamond_sword_bone_3.addChild("diamond_sword21", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.984F, 0.755F, -3.0985F, -1.088F, -1.0483F, -2.111F));

        ModelPartData diamond_sword_bone_4 = diamond_swords.addChild("diamond_sword_bone_4", ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 7.0F, 5.0F));

        ModelPartData diamond_sword23 = diamond_sword_bone_4.addChild("diamond_sword23", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.016F, 3.255F, -0.0985F, 3.1121F, 0.119F, -1.1601F));

        ModelPartData diamond_sword22 = diamond_sword_bone_4.addChild("diamond_sword22", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.5159F, 4.755F, -3.0985F, -2.0363F, -0.5305F, -1.5667F));

        ModelPartData diamond_sword_bone_5 = diamond_swords.addChild("diamond_sword_bone_5", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 7.0F, -7.0F));

        ModelPartData diamond_sword15 = diamond_sword_bone_5.addChild("diamond_sword15", ModelPartBuilder.create().uv(4, 35).mirrored().cuboid(5.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(6, 47).mirrored().cuboid(4.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(35, 57).mirrored().cuboid(3.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(32, 43).mirrored().cuboid(2.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(13, 43).mirrored().cuboid(5.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(1, 42).mirrored().cuboid(5.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.016F, 2.255F, -0.5985F, 0.1047F, -0.1254F, -2.312F));

        ModelPartData diamond_sword_bone_6 = diamond_swords.addChild("diamond_sword_bone_6", ModelPartBuilder.create(), ModelTransform.pivot(7.0F, 7.0F, -7.0F));

        ModelPartData diamond_sword14 = diamond_sword_bone_6.addChild("diamond_sword14", ModelPartBuilder.create().uv(4, 35).cuboid(-8.1053F, -8.3684F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 47).cuboid(-5.1053F, -7.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(35, 57).cuboid(-4.1053F, -6.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 43).cuboid(-3.1053F, -5.3684F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(13, 43).cuboid(-7.1053F, -5.3684F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 42).cuboid(-6.1053F, -4.3684F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.484F, 2.755F, -1.0985F, -0.4613F, -0.0333F, 2.4618F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
