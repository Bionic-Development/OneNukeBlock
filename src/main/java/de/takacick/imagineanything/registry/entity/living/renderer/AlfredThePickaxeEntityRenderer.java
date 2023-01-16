package de.takacick.imagineanything.registry.entity.living.renderer;

import de.takacick.imagineanything.registry.entity.living.AlfredThePickaxeEntity;
import de.takacick.imagineanything.registry.entity.living.model.ItemEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AlfredThePickaxeEntityRenderer extends LivingEntityRenderer<AlfredThePickaxeEntity, ItemEntityModel<AlfredThePickaxeEntity>> {

    public AlfredThePickaxeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ItemEntityModel<>(Items.DIAMOND_PICKAXE.getDefaultStack(), 45f, 90f, 1.9f), 0.5f);
    }

    @Override
    public void render(AlfredThePickaxeEntity alfredThePickaxeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(alfredThePickaxeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(AlfredThePickaxeEntity alfredThePickaxeEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    protected void renderLabelIfPresent(AlfredThePickaxeEntity alfredThePickaxeEntity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }
}
