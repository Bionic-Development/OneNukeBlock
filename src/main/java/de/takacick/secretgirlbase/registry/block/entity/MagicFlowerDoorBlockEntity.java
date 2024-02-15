package de.takacick.secretgirlbase.registry.block.entity;

import com.mojang.serialization.DataResult;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.ParticleRegistry;
import de.takacick.secretgirlbase.registry.block.MagicFlowerDoorBlock;
import de.takacick.secretgirlbase.registry.block.MagicFlowerDoorPart;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MagicFlowerDoorBlockEntity extends BlockEntity {

    public static final HashMap<Vec3d, BlockState> BLOCKS = new HashMap<>();

    static {
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                BLOCKS.put(new Vec3d(x, 0, z), Blocks.GRASS_BLOCK.getDefaultState());
            }
        }
        BLOCKS.put(new Vec3d(0, 1, 0), Blocks.POPPY.getDefaultState());
    }

    private BlockState blockState = Blocks.GRASS_BLOCK.getDefaultState();
    private BlockPos ownerBlockPos;
    private boolean isOwner;
    private MagicFlowerDoorBlockEntity owner;

    private float progress = 0f;
    private float prevProgress = 0f;

    private float alpha = 1f;
    private float prevAlpha = 1f;
    private int openTicks = 0;
    private boolean open;

    public MagicFlowerDoorBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.MAGIC_FLOWER_DOOR, pos, state);
        ownerBlockPos = pos;
        isOwner = false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, MagicFlowerDoorBlockEntity blockEntity) {
        if (blockPos == blockEntity.ownerBlockPos) {
            blockEntity.setOwner(true);
        }

        if (!blockEntity.isOwner()) {
            MagicFlowerDoorBlockEntity flowerDoorBlockEntity = null;
            if (blockEntity.ownerBlockPos != null
                    && world.getBlockEntity(blockEntity.ownerBlockPos) instanceof MagicFlowerDoorBlockEntity magicFlowerDoorBlockEntity) {
                flowerDoorBlockEntity = magicFlowerDoorBlockEntity;
                blockEntity.open = magicFlowerDoorBlockEntity.open;
            }

            blockEntity.owner = flowerDoorBlockEntity;

            if (!world.isClient && flowerDoorBlockEntity == null) {
                world.breakBlock(blockPos, true);
                blockEntity.markRemoved();
            }
        } else if (blockEntity.getBlockState() == null || blockEntity.getBlockState().isAir()) {
            if (!world.isClient) {
                world.breakBlock(blockPos, true);
                blockEntity.markRemoved();
            } else {
                blockEntity.setBlockState(Blocks.GRASS_BLOCK.getDefaultState());
            }
        }

        if (blockEntity.isOwner()) {
            if (blockEntity.isOpening()) {
                blockEntity.prevProgress = blockEntity.progress;
                if (blockEntity.progress >= 1f || blockEntity.getAlpha(0f) <= 0f) {
                    blockEntity.prevAlpha = blockEntity.alpha;
                    blockEntity.alpha = Math.max(blockEntity.alpha - 0.10f, 0);

                    if (blockEntity.alpha != 1f && blockEntity.prevAlpha == 1f) {
                        world.playSound(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.BLOCKS, 1f, 1f + world.getRandom().nextFloat() * 0.3f, true);
                    }

                    if (!world.isClient) {
                        if (!state.get(MagicFlowerDoorBlock.OPEN)) {
                            BLOCKS.forEach((vec3d, blockState1) -> {
                                BlockPos target = blockPos.add(BlockPos.ofFloored(vec3d));
                                BlockState blockState2 = world.getBlockState(target);

                                if (blockState2.contains(MagicFlowerDoorBlock.OPEN)) {
                                    world.setBlockState(target, blockState2.with(MagicFlowerDoorBlock.OPEN, true));
                                }
                            });
                        }
                    }

                    if (blockEntity.getAlpha(0f) <= 0f) {
                        blockEntity.prevProgress = blockEntity.progress;
                        blockEntity.progress = Math.max(blockEntity.progress - 0.1f, 0f);

                        if (!world.isClient) {
                            blockEntity.openTicks++;
                            if (blockEntity.openTicks >= 4) {
                                MagicFlowerDoorBlock.forPositions(vec3d -> {
                                    BlockPos pos = blockPos.add(BlockPos.ofFloored(vec3d));
                                    BlockState blockState = world.getBlockState(pos);
                                    if (blockState.getBlock() instanceof MagicFlowerDoorPart) {
                                        if (world.getBlockEntity(pos)
                                                instanceof MagicFlowerDoorBlockEntity magicFlowerDoorBlockEntity
                                                && magicFlowerDoorBlockEntity.getOwnerBlockPos().equals(blockEntity.getOwnerBlockPos())) {
                                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                            magicFlowerDoorBlockEntity.markRemoved();
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else {
                    blockEntity.progress = Math.min(blockEntity.progress + 0.025f, 1f);
                }
            } else {
                if (!world.isClient) {
                    if (blockEntity.alpha >= 0.95f && state.get(MagicFlowerDoorBlock.OPEN)) {
                        BLOCKS.forEach((vec3d, blockState1) -> {
                            BlockPos target = blockPos.add(BlockPos.ofFloored(vec3d));
                            BlockState blockState2 = world.getBlockState(target);

                            if (blockState2.contains(MagicFlowerDoorBlock.OPEN)) {
                                world.setBlockState(blockPos.add(BlockPos.ofFloored(vec3d)), blockState2.with(MagicFlowerDoorBlock.OPEN, false));
                            }
                        });
                    }
                }

                blockEntity.prevProgress = blockEntity.progress;
                blockEntity.progress = Math.max(blockEntity.progress - 0.05f, 0f);
                blockEntity.prevAlpha = blockEntity.alpha;
                blockEntity.alpha = Math.min(blockEntity.alpha + 0.05f, 1f);
            }
        }

        if (world.isClient) {
            Random random = Random.create();
            if (blockEntity.getProgress(1f) > 0f) {
                if (random.nextDouble() <= 0.15) {
                    world.playSound(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1f, 1f + random.nextFloat() * 0.3f, true);
                }

                if (random.nextDouble() <= 0.1) {
                    for (int i = 0; i < 1; i++) {
                        double g = random.nextGaussian();
                        double h = random.nextGaussian();
                        double j = random.nextGaussian();

                        Vec3d vec3d = new Vec3d(g, h, j).normalize().multiply(0.6);
                        Vec3d pos = Vec3d.ofCenter(blockPos).add(vec3d);
                        vec3d = vec3d.multiply(0.25);

                        world.addParticle(ParticleRegistry.MAGIC_SPARK, pos.getX(), pos.getY(), pos.getZ(), vec3d.getX() * 0.1, vec3d.getY() * 0.1, vec3d.getZ() * 0.1);
                    }
                }
            }

            float alpha = blockEntity.getAlpha(0.5f);
            if (alpha > 0 && alpha < 1) {
                for (int i = 0; i < 1; i++) {
                    double g = random.nextGaussian() * 0.25;
                    double h = random.nextGaussian() * 0.25;
                    double j = random.nextGaussian() * 0.25;

                    Vec3d vec3d = new Vec3d(g, h, j);
                    Vec3d pos = Vec3d.ofCenter(blockPos).add(vec3d);
                    vec3d = vec3d.multiply(0.25);

                    world.addParticle(ParticleRegistry.MAGIC_FLOWER, pos.getX(), pos.getY(), pos.getZ(), vec3d.getX(), vec3d.getY(), vec3d.getZ());
                }
            }
        }
    }

    public void setOwnerBlockPos(BlockPos ownerBlockPos) {
        this.ownerBlockPos = ownerBlockPos;

        if (ownerBlockPos.equals(getPos())) {
            setOwner(true);
        } else {
            setOwner(false);
        }
    }

    public void setOwner(boolean owner) {
        this.isOwner = owner;
    }

    public BlockPos getOwnerBlockPos() {
        return this.ownerBlockPos;
    }

    public MagicFlowerDoorBlockEntity getOwner() {
        return this.isOwner ? this : this.owner;
    }

    public boolean isOwner() {
        return this.isOwner;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("ownerBlockPos", NbtHelper.fromBlockPos(this.ownerBlockPos));
        nbt.put("blockState", NbtHelper.fromBlockState(this.blockState));
        nbt.putBoolean("isOwner", this.isOwner);
        nbt.putBoolean("open", this.open);

        nbt.putFloat("progress", this.progress);
        nbt.putFloat("prevProgress", this.prevProgress);
        nbt.putFloat("alpha", this.alpha);
        nbt.putFloat("prevAlpha", this.prevAlpha);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.ownerBlockPos = NbtHelper.toBlockPos(nbt.getCompound("ownerBlockPos"));
        DataResult<BlockState> blockStateResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockState"));
        this.blockState = blockStateResult.result().orElse(null);

        if (Math.abs(this.alpha - nbt.getFloat("alpha")) > 0.15f) {
            this.alpha = nbt.getFloat("alpha");
        }

        if (Math.abs(this.prevAlpha - nbt.getFloat("prevAlpha")) > 0.15f) {
            this.prevAlpha = nbt.getFloat("prevAlpha");
        }

        if (Math.abs(this.progress - nbt.getFloat("progress")) > 0.15f) {
            this.progress = nbt.getFloat("progress");
        }

        if (Math.abs(this.prevProgress - nbt.getFloat("prevProgress")) > 0.15f) {
            this.prevProgress = nbt.getFloat("prevProgress");
        }

        this.isOwner = nbt.getBoolean("isOwner");
        this.open = nbt.getBoolean("open");
        super.readNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha(float tickDelta) {

        if (!this.isOwner && this != this.getOwner() && this.getOwner() != null) {
            return this.getOwner().getAlpha(tickDelta);
        }

        return MathHelper.lerp(tickDelta, this.prevAlpha, this.alpha);
    }

    public float getProgress(float tickDelta) {

        if (!this.isOwner && this != this.getOwner() && this.getOwner() != null) {
            return this.getOwner().getProgress(tickDelta);
        }

        return MathHelper.lerp(tickDelta, this.prevProgress, this.progress);
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpening() {
        return open;
    }
}
