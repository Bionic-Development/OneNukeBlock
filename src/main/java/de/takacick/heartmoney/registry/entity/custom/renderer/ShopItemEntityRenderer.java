package de.takacick.heartmoney.registry.entity.custom.renderer;

import de.takacick.heartmoney.registry.entity.custom.ShopItemEntity;
import de.takacick.utils.BionicUtilsClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class ShopItemEntityRenderer extends EntityRenderer<ShopItemEntity> {

    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public ShopItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ShopItemEntity shopItemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (shopItemEntity.getItemStack() == null) {
            return;
        }

        matrixStack.push();
        matrixStack.scale(shopItemEntity.getScale(), shopItemEntity.getScale(), shopItemEntity.getScale());
        renderStack(shopItemEntity, f, g, matrixStack, vertexConsumerProvider, light);

        matrixStack.pop();

        matrixStack.translate(0, -0.105 + shopItemEntity.getOffset(), 0);

        float[] endColor = {1f, 0.5f, 0.5f};

        renderLabelIfPresent(shopItemEntity, Text.literal(shopItemEntity.getPrice() + "")
                        .append(Text.literal(" §4❤")
                                .setStyle(Style.EMPTY.withColor(BionicUtilsClient.getRainbow()
                                        .getIntFromColor((int) (endColor[0] * 255), (int) (endColor[1] * 255), (int) (endColor[2] * 255)))))
                , matrixStack, vertexConsumerProvider, light);
        matrixStack.translate(0, 0.25, 0);
        renderLabelIfPresent(shopItemEntity, shopItemEntity.getItemStack().getName(), matrixStack, vertexConsumerProvider, light);

        super.render(shopItemEntity, f, g, matrixStack, vertexConsumerProvider, light);
    }

    public void renderStack(ShopItemEntity shopItemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float t;
        float s;
        matrixStack.push();
        ItemStack itemStack = shopItemEntity.getItemStack();
        int j = itemStack.isEmpty() ? 187 : Item.getRawId(itemStack.getItem()) + itemStack.getDamage();
        this.random.setSeed(j);
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, shopItemEntity.world, null, shopItemEntity.getId());
        boolean bl = bakedModel.hasDepth();
        int k = this.getRenderedAmount(itemStack);
        float h = 0.25f;
        float l = MathHelper.sin(((float) shopItemEntity.age + g) / 10.0f) * 0.01f + 0.1f;
        float m = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
        matrixStack.translate(0.0, l + 0.25f * m, 0.0);
        float n = shopItemEntity.getRotation(g);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(n));
        float o = bakedModel.getTransformation().ground.scale.getX();
        float p = bakedModel.getTransformation().ground.scale.getY();
        float q = bakedModel.getTransformation().ground.scale.getZ();
        if (!bl) {
            float r = -0.0f * (float) (k - 1) * 0.5f * o;
            s = -0.0f * (float) (k - 1) * 0.5f * p;
            t = -0.09375f * (float) (k - 1) * 0.5f * q;
            matrixStack.translate(r, s, t);
        }
        for (int u = 0; u < k; ++u) {
            matrixStack.push();
            if (u > 0) {
                if (bl) {
                    s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float v = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    matrixStack.translate(s, t, v);
                } else {
                    s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    matrixStack.translate(s, t, 0.0);
                }
            }
            this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);
            matrixStack.pop();
            if (bl) continue;
            matrixStack.translate(0.0f * o, 0.0f * p, 0.09375f * q);
        }
        matrixStack.pop();
    }

    private int getRenderedAmount(ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }
        return i;
    }

    @Override
    public Identifier getTexture(ShopItemEntity shopItemEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
