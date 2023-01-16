package de.takacick.imagineanything.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.imagineanything.registry.item.HeadItem;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.resource.SynchronousResourceReloader;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin implements SynchronousResourceReloader {


    @Shadow
    private Map<SkullBlock.SkullType, SkullBlockEntityModel> skullModels;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item instanceof HeadItem && !(mode.equals(ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND)
                || mode.equals(ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND))) {
            GameProfile gameProfile = null;
            if (stack.hasNbt()) {
                NbtCompound nbtCompound = stack.getNbt();
                if (nbtCompound.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
                    gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
                } else if (nbtCompound.contains("SkullOwner", NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
                    gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
                    nbtCompound.remove("SkullOwner");
                    SkullBlockEntity.loadProperties(gameProfile, profile -> nbtCompound.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile)));
                }
            }

            SkullBlock.SkullType skullType = SkullBlock.Type.PLAYER;
            SkullBlockEntityModel skullBlockEntityModel = this.skullModels.get(skullType);
            RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, gameProfile);
            SkullBlockEntityRenderer.renderSkull(null, 0.0f, 0.0f, matrices, vertexConsumers, light, skullBlockEntityModel, renderLayer);
            info.cancel();
        }
    }
}