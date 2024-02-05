package de.takacick.secretcraftbase.registry.block.entity;

import com.mojang.serialization.DataResult;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.block.SecretMagicWellBlock;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SecretMagicWellBlockEntity extends BlockEntity {

    public static final HashMap<Vec3d, BlockState> FLUIDS = new HashMap<>();
    public static final HashMap<Vec3d, BlockState> BLOCKS = new HashMap<>();

    public static boolean isCenterPos(int j) {
        return j == 0 || j == 1;
    }

    static {
        for (int x = -1; x < 3; x++) {
            for (int z = -1; z < 3; z++) {
                FLUIDS.put(new Vec3d(x, 1, z), Blocks.WATER.getDefaultState());
            }
        }

        BLOCKS.putAll(FLUIDS);

        for (int x = -1; x < 3; x++) {
            for (int z = -1; z < 3; z++) {
                BLOCKS.put(new Vec3d(x, 0, z), Blocks.COBBLESTONE.getDefaultState());
                BLOCKS.put(new Vec3d(x, 5, z), Blocks.COBBLESTONE.getDefaultState());

                if (!(isCenterPos(x) && isCenterPos(z))) {
                    BLOCKS.put(new Vec3d(x, 2, z), Blocks.COBBLESTONE.getDefaultState());
                }
            }
        }

        for (int x = -2; x < 4; x++) {
            for (int z = -2; z < 4; z++) {
                if (isCenterPos(x) && isCenterPos(z)) {
                    continue;
                }

                BLOCKS.put(new Vec3d(x, 1, z), Blocks.COBBLESTONE.getDefaultState());
            }
        }

        for (int y = 0; y < 2; y++) {
            BLOCKS.put(new Vec3d(-1, 3 + y, -1), Blocks.OAK_FENCE.getDefaultState());
            BLOCKS.put(new Vec3d(-1, 3 + y, 2), Blocks.OAK_FENCE.getDefaultState());
            BLOCKS.put(new Vec3d(2, 3 + y, -1), Blocks.OAK_FENCE.getDefaultState());
            BLOCKS.put(new Vec3d(2, 3 + y, 2), Blocks.OAK_FENCE.getDefaultState());
        }

        BLOCKS.put(new Vec3d(-1, 6, -1), Blocks.TORCH.getDefaultState());
        BLOCKS.put(new Vec3d(-1, 6, 2), Blocks.TORCH.getDefaultState());
        BLOCKS.put(new Vec3d(2, 6, -1), Blocks.TORCH.getDefaultState());
        BLOCKS.put(new Vec3d(2, 6, 2), Blocks.TORCH.getDefaultState());
    }

    private BlockState blockState = Blocks.COBBLESTONE.getDefaultState();
    private BlockPos ownerBlockPos;
    private boolean isOwner;
    private SecretMagicWellBlockEntity owner;

    private float alpha = 1f;
    private float prevAlpha = 1f;
    private int openTicks = 0;
    private boolean open;

    public SecretMagicWellBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SECRET_MAGIC_WELL, pos, state);
        ownerBlockPos = pos;
        isOwner = false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, SecretMagicWellBlockEntity blockEntity) {
        if (blockPos == blockEntity.ownerBlockPos) {
            blockEntity.setOwner(true);
        }

        if (!blockEntity.isOwner()) {
            SecretMagicWellBlockEntity magicWellBlockEntity = null;
            if (blockEntity.ownerBlockPos != null
                    && world.getBlockEntity(blockEntity.ownerBlockPos) instanceof SecretMagicWellBlockEntity secretMagicWellBlockEntity) {
                magicWellBlockEntity = secretMagicWellBlockEntity;
            }

            blockEntity.owner = magicWellBlockEntity;

            if (!world.isClient && magicWellBlockEntity == null) {
                world.breakBlock(blockPos, true);
                blockEntity.markRemoved();
            }
        } else if (blockEntity.getBlockState() == null || blockEntity.getBlockState().isAir()) {
            if (!world.isClient) {
                world.breakBlock(blockPos, true);
                blockEntity.markRemoved();
            } else {
                blockEntity.setBlockState(Blocks.COBBLESTONE.getDefaultState());
            }
        }

        if (blockEntity.isOwner()) {
            if (blockEntity.isOpen()) {
                blockEntity.prevAlpha = blockEntity.alpha;
                blockEntity.alpha = Math.max(blockEntity.alpha - 0.05f, 0);
                if( !world.isClient) {
                    if (!state.get(SecretMagicWellBlock.OPEN)) {
                        BLOCKS.forEach((vec3d, blockState1) -> {
                            BlockPos target = blockPos.add(BlockPos.ofFloored(vec3d));
                            BlockState blockState2 = world.getBlockState(target);

                            if(blockState2.contains(SecretMagicWellBlock.OPEN)) {
                                world.setBlockState(blockPos.add(BlockPos.ofFloored(vec3d)), blockState2.with(SecretMagicWellBlock.OPEN, true));
                            }
                        });
                    }
                }

                if (blockEntity.getAlpha(0f) <= 0f) {
                    if (!world.isClient) {
                        blockEntity.openTicks++;
                        if (blockEntity.openTicks >= 100) {
                            blockEntity.open = false;
                            blockEntity.openTicks = 0;

                            world.playSound(null, blockPos, ParticleRegistry.MAGIC_VANISH, SoundCategory.BLOCKS, 1f, 1f);
                            world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            } else {
                if( !world.isClient) {
                    if (blockEntity.alpha >= 0.95f && state.get(SecretMagicWellBlock.OPEN)) {
                        BLOCKS.forEach((vec3d, blockState1) -> {
                            BlockPos target = blockPos.add(BlockPos.ofFloored(vec3d));
                            BlockState blockState2 = world.getBlockState(target);

                            if(blockState2.contains(SecretMagicWellBlock.OPEN)) {
                                world.setBlockState(blockPos.add(BlockPos.ofFloored(vec3d)), blockState2.with(SecretMagicWellBlock.OPEN, false));
                            }
                        });
                    }
                }

                blockEntity.prevAlpha = blockEntity.alpha;
                blockEntity.alpha = Math.min(blockEntity.alpha + 0.05f, 1f);
            }
        }

        if (world.isClient) {
            float alpha = blockEntity.getAlpha(0.5f);
            if (alpha > 0 && alpha < 1) {
                for(int i = 0; i < 1; i++) {
                    double g = world.getRandom().nextGaussian() * 0.25;
                    double h = world.getRandom().nextGaussian() * 0.25;
                    double j = world.getRandom().nextGaussian() * 0.25;

                    Vec3d vec3d = new Vec3d(g, h, j);
                    Vec3d pos = Vec3d.ofCenter(blockPos).add(vec3d);
                    vec3d = vec3d.multiply(0.25);

                    world.addParticle(ParticleRegistry.MAGIC, pos.getX(), pos.getY(), pos.getZ(), vec3d.getX(), vec3d.getY(), vec3d.getZ());
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

    public SecretMagicWellBlockEntity getOwner() {
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

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }
}
