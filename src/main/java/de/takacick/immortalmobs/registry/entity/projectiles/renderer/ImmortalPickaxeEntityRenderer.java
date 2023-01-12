package de.takacick.immortalmobs.registry.entity.projectiles.renderer;

import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalPickaxeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
public class ImmortalPickaxeEntityRenderer extends EntityRenderer<ImmortalPickaxeEntity> {

    private final ItemRenderer itemRenderer;

    public ImmortalPickaxeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ImmortalPickaxeEntity immortalPickaxeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, immortalPickaxeEntity.prevYaw, immortalPickaxeEntity.getYaw()) + 90));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, immortalPickaxeEntity.prevPitch, immortalPickaxeEntity.getPitch()) - 45));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(MathHelper.lerp(g, immortalPickaxeEntity.age - 1 + immortalPickaxeEntity.getId(), immortalPickaxeEntity.age + immortalPickaxeEntity.getId()) / 2));

        this.itemRenderer.renderItem(ItemRegistry.IMMORTAL_PICKAXE.getDefaultStack(), ModelTransformation.Mode.NONE, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, immortalPickaxeEntity.getId());
        matrixStack.pop();

        super.render(immortalPickaxeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(ImmortalPickaxeEntity immortalPickaxeEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
