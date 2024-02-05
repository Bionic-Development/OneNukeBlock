package de.takacick.secretcraftbase.registry.block.entity;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.SecretRedstoneMirrorMelterOre;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SecretRedstoneMirrorMelterOreBlockEntity extends BlockEntity {
    public static final RegistryKey<DamageType> SECRET_REDSTONE_MIRROR_MELTER_ORE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(SecretCraftBase.MOD_ID, "secret_redstone_mirror_melter_ore"));

    private int powerTicks = 0;
    private boolean enabled;
    private int length;
    private int blockHitTicks = 0;

    public SecretRedstoneMirrorMelterOreBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SECRET_REDSTONE_MIRROR_MELTER_ORE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, SecretRedstoneMirrorMelterOreBlockEntity blockEntity) {
        BlockPos blockPos = pos;

        boolean powered = state.get(SecretRedstoneMirrorMelterOre.LIT);

        if (!blockEntity.enabled && powered) {
            blockEntity.powerTicks = 60;
            playSound(world, pos, SoundEvents.BLOCK_BEACON_ACTIVATE);
        }

        blockEntity.enabled = powered;

        if (!world.isClient) {
            if (blockEntity.powerTicks > 0) {
                blockEntity.powerTicks -= 1;
                if (blockEntity.powerTicks <= 0) {
                    world.setBlockState(pos, state.with(SecretRedstoneMirrorMelterOre.LIT, false));
                }
            }
        }

        Direction direction = state.get(SecretRedstoneMirrorMelterOre.FACING);
        if (blockEntity.enabled) {
            int m;
            for (m = 1; m < 35; ++m) {
                blockPos = blockPos.add(direction.getVector());
                BlockState blockState = world.getBlockState(blockPos);

                if (blockEntity.canHit(blockPos, blockState)) {
                    m -= 1;
                    break;
                }

                if (!world.isClient) {
                    world.getOtherEntities(null, new Box(blockPos).contract(0.25)).forEach(entity -> {
                        if (entity.isInvisible() || entity.isSpectator()
                                || entity.isInvulnerable() || !entity.isAlive()
                                || entity instanceof PlayerEntity player && player.isCreative()) {
                            return;
                        }

                        BionicUtils.sendEntityStatus(world, entity, SecretCraftBase.IDENTIFIER, 7);
                        entity.damage(world.getDamageSources().create(SECRET_REDSTONE_MIRROR_MELTER_ORE), 2f);
                        if (!entity.isLiving()) {
                            entity.discard();
                        }
                    });
                }
            }

            if (blockEntity.length != m) {
                blockEntity.blockHitTicks = 0;
            }

            blockEntity.hitBlock(blockPos);

            blockEntity.length = m;
        } else {
            blockEntity.blockHitTicks = 0;
        }

        if (world.getTime() % 80L == 0L) {
            if (blockEntity.enabled) {
                playSound(world, pos, SoundEvents.BLOCK_BEACON_AMBIENT);
            }
        }

        boolean bl = blockEntity.enabled;
        if (!world.isClient) {
            boolean bl2 = blockEntity.getCachedState().get(SecretRedstoneMirrorMelterOre.LIT);
            blockEntity.enabled = blockEntity.getCachedState().get(SecretRedstoneMirrorMelterOre.LIT);
            if (bl && !bl2) {
                playSound(world, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
            }
        }
    }

    private boolean canHit(BlockPos blockPos, BlockState blockState) {
        return blockState.getOpacity(this.world, blockPos) >= 15
                || blockState.contains(SecretRedstoneMirrorMelterOre.LIT) || blockState.isOf(ItemRegistry.MELTING_BLOCK)
                || blockState.isOf(ItemRegistry.DIAMOND_ORE_CHUNKS);
    }

    private void hitBlock(BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockPos.equals(pos) || blockState.isAir()) {
            blockHitTicks = 0;
            return;
        }

        if (blockState.getBlock() instanceof SecretRedstoneMirrorMelterOre) {
            blockHitTicks++;
            if (blockHitTicks > 5) {
                world.setBlockState(blockPos, blockState.with(SecretRedstoneMirrorMelterOre.LIT, true), Block.NOTIFY_ALL);
            }
            return;
        }

        float delta = blockState.getHardness(world, blockPos);

        if (!blockState.isOf(ItemRegistry.DIAMOND_ORE_CHUNKS) && delta != -1) {
            if (!blockState.isOf(ItemRegistry.MELTING_BLOCK)) {
                world.setBlockState(blockPos, ItemRegistry.MELTING_BLOCK.getDefaultState(), ~Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.REDRAW_ON_MAIN_THREAD | ~Block.NO_REDRAW);
                if (world.getBlockEntity(blockPos) instanceof MeltingBlockEntity meltingBlockEntity) {
                    meltingBlockEntity.setBlockState(blockState);
                }
            }

            if (world.getBlockEntity(blockPos) instanceof MeltingBlockEntity meltingBlockEntity) {
                meltingBlockEntity.setLaserTicks(2);
            }
        }
        world.syncWorldEvent(82139123, blockPos, 2);
    }

    public void markRemoved() {
        if (enabled) {
            playSound(this.world, this.pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
        }
        super.markRemoved();
    }

    public static void playSound(World world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.powerTicks = nbt.getInt("powerTicks");
        this.blockHitTicks = nbt.getInt("blockHitTicks");
        this.length = nbt.getInt("length");
        this.enabled = nbt.getBoolean("enabled");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("powerTicks", this.powerTicks);
        nbt.putInt("blockHitTicks", this.blockHitTicks);
        nbt.putInt("length", this.length);
        nbt.putBoolean("enabled", this.enabled);
    }

    public void setWorld(World world) {
        super.setWorld(world);
    }

    public int getLength() {
        return length;
    }
}
