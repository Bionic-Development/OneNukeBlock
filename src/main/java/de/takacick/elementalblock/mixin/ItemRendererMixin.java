package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MatrixUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private ItemModels models;
    @Shadow
    @Final
    private BuiltinModelItemRenderer builtinModelItemRenderer;

    @Shadow
    protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

    @Shadow
    private static boolean usesDynamicDisplay(ItemStack stack) {
        return false;
    }

    @Unique
    private static final ModelIdentifier elementalblock$TRIDENT_IN_HAND = new ModelIdentifier(OneElementalBlock.MOD_ID, "tsunamic_trident_in_hand", "inventory");
    @Unique
    private static final ModelIdentifier elementalblock$TRIDENT = new ModelIdentifier(OneElementalBlock.MOD_ID, "tsunamic_trident", "inventory");

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedModel;getTransformation()Lnet/minecraft/client/render/model/json/ModelTransformation;", shift = At.Shift.BEFORE), cancellable = true)
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.TSUNAMIC_TRIDENT)) {
            boolean bl = renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED;
            if (bl) {
                model = this.models.getModelManager().getModel(elementalblock$TRIDENT);
            }

            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5f, -0.5f, -0.5f);
            if (!bl) {
                this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
            } else {
                VertexConsumer vertexConsumer;
                Block block;
                boolean bl22 = renderMode == ModelTransformationMode.GUI || renderMode.isFirstPerson() || !(stack.getItem() instanceof BlockItem) || !((block = ((BlockItem) stack.getItem()).getBlock()) instanceof TransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                RenderLayer renderLayer = RenderLayers.getItemLayer(stack, bl22);
                if (usesDynamicDisplay(stack) && stack.hasGlint()) {
                    matrices.push();
                    MatrixStack.Entry entry = matrices.peek();
                    if (renderMode == ModelTransformationMode.GUI) {
                        MatrixUtil.scale(entry.getPositionMatrix(), 0.5f);
                    } else if (renderMode.isFirstPerson()) {
                        MatrixUtil.scale(entry.getPositionMatrix(), 0.75f);
                    }
                    vertexConsumer = bl22 ? ItemRenderer.getDirectDynamicDisplayGlintConsumer(vertexConsumers, renderLayer, entry) : ItemRenderer.getDynamicDisplayGlintConsumer(vertexConsumers, renderLayer, entry);
                    matrices.pop();
                } else {
                    vertexConsumer = bl22 ? ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint()) : ItemRenderer.getItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
                }
                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            }
            matrices.pop();
            info.cancel();
        } else if (stack.isOf(ItemRegistry.TERRA_DRILL)) {
            matrices.push();
            if (!renderMode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
                matrices.translate(0f, 1f, 0f);
            } else {
                matrices.translate(0f, 0.0825f, -0.425f);
            }

            this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
            matrices.pop();
            matrices.pop();
            info.cancel();
        }
    }

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    public void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> info) {
        if (stack.isOf(ItemRegistry.TSUNAMIC_TRIDENT)) {
            BakedModel bakedModel = this.models.getModelManager().getModel(elementalblock$TRIDENT_IN_HAND);
            ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
            BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
            info.setReturnValue(bakedModel2 == null ? this.models.getModelManager().getMissingModel() : bakedModel2);
        }
    }
}

