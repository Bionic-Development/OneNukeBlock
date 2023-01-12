package de.takacick.immortalmobs.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.ImmortalMobsClient;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.item.ImmortalItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private BuiltinModelItemRenderer builtinModelItemRenderer;

    @Shadow
    @Final
    private ItemModels models;

    @Shadow
    public abstract void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model);

    @Shadow
    public abstract BakedModel getModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed);

    @Shadow
    protected abstract void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model);

    @Shadow
    public float zOffset;

    @Shadow
    @Final
    private TextureManager textureManager;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE, ordinal = 1), cancellable = true)
    public void renderBuiltinModel(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.getItem() instanceof ImmortalItem) {
            matrices.push();
            this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
            matrices.pop();
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At(value = "HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        if (stack.isOf(Items.BOW) && entity != null) {
            if (entity.getActiveItem().equals(stack) && entity.getArrowType(stack).isOf(ItemRegistry.IMMORTAL_ARROW)) {
                BakedModel bakedModel = this.getModel(stack, world, entity, seed);
                renderItem(stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, bakedModel);
                ImmortalMobsClient.renderBow(entity, world, seed, stack, leftHanded, renderMode, matrices, vertexConsumers, light, overlay);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getModel", at = @At(value = "HEAD"), cancellable = true)
    public void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.isOf(Items.BOW) && entity != null) {
            if (entity.getArrowType(stack).isOf(ItemRegistry.IMMORTAL_ARROW)) {
                BakedModel bakedModel = entity.isUsingItem() && entity.getActiveItem().equals(stack) ?
                        this.models.getModelManager().getModel(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow#inventory")) :
                        this.models.getModel(stack);
                ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
                BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
                cir.setReturnValue(bakedModel2 == null ? this.models.getModelManager().getMissingModel() : bakedModel2);
            }
        }
    }

    @Inject(method = "innerRenderInGui(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;", shift = At.Shift.AFTER), cancellable = true)
    public void innerRenderInGui(LivingEntity entity, ItemStack stack, int x, int y, int seed, int depth, CallbackInfo info) {
        if (stack.isOf(Items.BOW) && entity != null) {
            if (entity.getActiveItem().equals(stack) && entity.getArrowType(stack).isOf(ItemRegistry.IMMORTAL_ARROW)) {
                BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModelManager().getModel(new ModelIdentifier(ImmortalMobs.MOD_ID + ":bow_arrow#inventory"));
                ClientWorld clientWorld = (ClientWorld) entity.getWorld();
                BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
                renderBowGuiItemModel(stack, x, y, bakedModel2);
            }
        }
    }

    private void renderBowGuiItemModel(ItemStack stack, int x, int y, BakedModel model) {
        boolean bl;
        this.textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 100.0f + this.zOffset);
        matrixStack.translate(8.0, 8.0, 0.0);
        matrixStack.scale(1.0f, -1.0f, 1.0f);
        matrixStack.scale(16.0f, 16.0f, 16.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean bl2 = bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        matrixStack2.translate(-0.5, -0.5, -0.5);


        ImmortalMobsClient.renderBakedItemQuads(matrixStack2,
                immediate.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), model.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
        ImmortalMobsClient.renderBakedItemQuads(matrixStack2,
                immediate.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), model.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
        ImmortalMobsClient.renderBakedItemQuads(matrixStack2,
                immediate.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), model.getQuads(null, null, Random.create()),
                stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);

        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }
}