package de.takacick.elementalblock.registry.item;

import de.takacick.elementalblock.registry.entity.custom.BlockBreakEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TerraDrill extends Item {

    public TerraDrill(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        float rotation = getRotation(stack, 1f);
        setRotation(stack, rotation + 2f);

        Vec3d pos = user.getPos().add(getRotationVector(0f, user.getYaw()).multiply(0.225f));
        BlockPos blockPos = BlockPos.ofFloored(pos).add(0, -1, 0);
        BlockState blockState = world.getBlockState(blockPos);
        if (world.isClient) {
            if (!blockState.isAir()) {
                BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState);

                for (int i = 0; i < 7; ++i) {
                    double g = 0.2 * world.getRandom().nextGaussian();
                    double h = 0.9 * world.getRandom().nextDouble();
                    double j = 0.2 * world.getRandom().nextGaussian();
                    world.addParticle(particleEffect,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }

                world.playSound(pos.getX(), pos.getY(), pos.getZ(), blockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1f, 1f + 0.3f, true);
            }
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1f, 1f, true);
        } else if (!blockState.isAir()) {
            boolean bl = false;
            float delta = blockState.getBlock().calcBlockBreakingDelta(blockState, playerEntity, world, blockPos);
            for (Entity entity : world.getOtherEntities(null, new Box(blockPos))) {
                if (entity instanceof BlockBreakEntity blockBreakEntity && entity.isAlive()) {
                    bl = true;
                    blockBreakEntity.setBreakProgress(blockBreakEntity.getBreakProgress() + delta);

                    if (blockBreakEntity.getBreakProgress() >= blockBreakEntity.getMaxBreakProgress()) {
                        ((ServerPlayerEntity) playerEntity).interactionManager.tryBreakBlock(blockPos);
                     //   world.breakBlock(blockPos, true, user);
                    }
                    break;
                }
            }

            if (!bl && !world.getBlockState(blockPos).isAir()) {
                BlockBreakEntity blockBreakEntity = new BlockBreakEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                blockBreakEntity.setBreakProgress(delta);
                world.spawnEntity(blockBreakEntity);
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return isSuitableFor(stack, state) ? 4.0f : 1.0f;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if (entity instanceof LivingEntity livingEntity && !(livingEntity.isUsingItem() && livingEntity.getActiveItem().equals(stack))) {
            float rotation = getRotation(stack, 1f);
            setRotation(stack, rotation);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float rotation = getRotation(stack, 1f);
        setRotation(stack, rotation);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return state.isIn(BlockTags.PICKAXE_MINEABLE) || state.isIn(BlockTags.AXE_MINEABLE) || state.isIn(BlockTags.SHOVEL_MINEABLE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!user.getMainHandStack().equals(itemStack)) {
            return TypedActionResult.pass(itemStack);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§ePierce the §2earth§e!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public static ItemStack setRotation(ItemStack itemStack, float rotation) {
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        nbtCompound.putFloat("prevRotation", nbtCompound.getFloat("rotation"));
        nbtCompound.putFloat("rotation", rotation);

        return itemStack;
    }

    public static float getRotation(ItemStack itemStack, float tickDelta) {
        NbtCompound nbtCompound = itemStack.getNbt();
        return nbtCompound != null ? MathHelper.lerp(tickDelta, nbtCompound.getFloat("prevRotation"), nbtCompound.getFloat("rotation")) : 0;
    }

    protected final Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
}
