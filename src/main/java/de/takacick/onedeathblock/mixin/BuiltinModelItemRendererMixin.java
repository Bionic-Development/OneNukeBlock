package de.takacick.onedeathblock.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.onedeathblock.client.DeathCounter;
import de.takacick.onedeathblock.registry.ItemRegistry;
import de.takacick.onedeathblock.registry.block.SpikedBedBlock;
import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import de.takacick.onedeathblock.registry.item.SpikedBedItem;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Shadow
    private Map<SkullBlock.SkullType, SkullBlockEntityModel> skullModels;

    @Shadow
    @Final
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final SpikedBedBlockEntity onedeathblock$renderSpikedBed = new SpikedBedBlockEntity(BlockPos.ORIGIN, ItemRegistry.BLACK_SPIKED_BED.getDefaultState());

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        if (stack.isOf(Items.SKELETON_SKULL)
                && stack.hasNbt()
                && stack.getNbt().contains("onedeathblock")) {
            Item item = stack.getItem();
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof AbstractSkullBlock) {
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
                SkullBlock.SkullType skullType = ((AbstractSkullBlock) block).getSkullType();
                SkullBlockEntityModel skullBlockEntityModel = this.skullModels.get(skullType);
                RenderLayer renderLayer = RenderLayer.getEntityTranslucent(new Identifier("textures/entity/skeleton/skeleton.png"));
                DeathCounter.renderSkull(null, stack, 180.0f, 0.0f, matrices, vertexConsumers, light, skullBlockEntityModel, renderLayer);
                info.cancel();
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderSpikedBed(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item instanceof SpikedBedItem spikedBedItem) {
            BlockState blockState = spikedBedItem.getBlock().getDefaultState();

            if (blockState.getBlock() instanceof SpikedBedBlock spikedBedBlock) {
                this.onedeathblock$renderSpikedBed.setColor(spikedBedBlock.getColor());
                this.blockEntityRenderDispatcher.renderEntity(this.onedeathblock$renderSpikedBed, matrices, vertexConsumers, light, overlay);
                info.cancel();
            }
        }
    }
}

