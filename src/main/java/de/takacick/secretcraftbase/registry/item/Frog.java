package de.takacick.secretcraftbase.registry.item;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.SecretCraftBaseClient;
import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class Frog extends Item {

    public Frog(Settings settings) {
        super(settings);
    }

    public static void eatBlock(ServerPlayerEntity player, ItemStack itemStack, ServerWorld world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);

        float delta = blockState.isOf(Blocks.NETHER_PORTAL) ? 0f : blockState.getHardness(world, blockPos);
        if (delta != -1) {

            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
            FallingBlockEntity fallingBlockEntity = spawnFromBlock(player.getWorld(), blockPos, blockState);
            eatEntity(player, itemStack, world, fallingBlockEntity);
        } else {
            float length = (float) Vec3d.ofCenter(blockPos).distanceTo(player.getPos());
            if (player instanceof PlayerProperties playerProperties) {
                playerProperties.setFrogTongueLength(length + 0.5f);
                BionicUtils.sendEntityStatus(world, player, SecretCraftBase.IDENTIFIER, 8);
            }
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return super.allowNbtUpdateAnimation(player, hand, oldStack, newStack);
    }

    public static void eatEntity(ServerPlayerEntity player, ItemStack itemStack, ServerWorld world, Entity entity) {
        if (player instanceof PlayerProperties playerProperties) {
            float length = (float) entity.getPos().distanceTo(player.getPos().add(0, player.getHeight() * 0.5, 0));

            Vec3d vec3d = player.getPos().add(0, player.getHeight() * 0.8, 0).subtract(entity.getPos())
                    .multiply(1f / 7.5f).add(0, 0, 0);

            if (entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame()) {
                livingEntity.setVelocity(vec3d);
                livingEntity.velocityModified = true;
                livingEntity.velocityDirty = true;
            } else {
                BionicUtils.sendEntityStatus(world, entity, SecretCraftBase.IDENTIFIER, 9);
                entity.noClip = true;
                entity.setVelocity(vec3d);
                entity.velocityModified = true;
            }
            playerProperties.setFrogTongueLength(length + 0.5f);
            playerProperties.setFrogTarget(entity);
            BionicUtils.sendEntityStatus(world, player, SecretCraftBase.IDENTIFIER, 8);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world.isClient) {
            SecretCraftBaseClient.useFrog(user);
        }

        return TypedActionResult.fail(itemStack);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return EntityNbtHelper.getEntityType(EntityType.FROG, stack).getTranslationKey();
    }

    public static FallingBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, state.contains(Properties.WATERLOGGED) ? (BlockState) state.with(Properties.WATERLOGGED, false) : state);
        if(state.isOf(Blocks.NETHER_PORTAL)) {
            world.setBlockState(pos, state.getFluidState().getBlockState(), ~Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.REDRAW_ON_MAIN_THREAD | ~Block.NO_REDRAW);
        } else {
            world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
        }
        world.spawnEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }

}
