package de.takacick.secretgirlbase.registry.block.entity;

import de.takacick.secretgirlbase.registry.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MagicDisappearingPlatformBlockEntity extends BlockEntity {

    private BlockPos ownerBlockPos;
    private boolean isOwner;

    private MagicDisappearingPlatformBlockEntity owner;
    private boolean disappearing = false;
    private float alpha = 1f;
    private float prevAlpha = 1f;
    private int delay = 40;

    public MagicDisappearingPlatformBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.MAGIC_DISAPPEARING_PLATFORM, pos, state);
        ownerBlockPos = pos;
        isOwner = false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, MagicDisappearingPlatformBlockEntity blockEntity) {
        if (!blockEntity.isOwner()) {
            MagicDisappearingPlatformBlockEntity disappearingPlatformBlockEntity = null;
            if (blockEntity.ownerBlockPos != null
                    && world.getBlockEntity(blockEntity.ownerBlockPos) instanceof MagicDisappearingPlatformBlockEntity magicDisappearingPlatformBlockEntity) {
                disappearingPlatformBlockEntity = magicDisappearingPlatformBlockEntity;
            }
            blockEntity.owner = disappearingPlatformBlockEntity;

            if (disappearingPlatformBlockEntity == null) {
                if (!world.isClient) {
                    world.breakBlock(pos, true);
                    blockEntity.markRemoved();
                }
            }
        } else {
            blockEntity.prevAlpha = blockEntity.alpha;

            if (blockEntity.isDisappearing()) {
                blockEntity.alpha = Math.max(blockEntity.alpha - 0.25f, 0);
            } else {
                blockEntity.alpha = Math.min(blockEntity.alpha + 0.25f, 1f);
            }

            if (!world.isClient) {
                if (blockEntity.delay > 0) {
                    blockEntity.delay--;
                }

                if (blockEntity.delay <= 0) {
                    blockEntity.delay = 40;
                    blockEntity.disappearing = !blockEntity.disappearing;
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.BLOCKS, 1f, 1f + 0.3f + world.getRandom().nextFloat() * 0.4f);
                    world.updateListeners(blockEntity.getPos(), blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
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
        isOwner = owner;
    }

    public BlockPos getOwnerBlockPos() {
        return ownerBlockPos;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("ownerBlockPos", NbtHelper.fromBlockPos(this.ownerBlockPos));
        nbt.putBoolean("isOwner", this.isOwner);
        nbt.putBoolean("disappearing", this.disappearing);
        nbt.putInt("delay", this.delay);

        nbt.putFloat("alpha", this.alpha);
        nbt.putFloat("prevAlpha", this.prevAlpha);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.ownerBlockPos = NbtHelper.toBlockPos(nbt.getCompound("ownerBlockPos"));
        this.isOwner = nbt.getBoolean("isOwner");
        this.disappearing = nbt.getBoolean("disappearing");
        this.delay = nbt.getInt("delay");

        if (Math.abs(this.alpha - nbt.getFloat("alpha")) > 0.5f) {
            this.alpha = nbt.getFloat("alpha");
        }
        if (Math.abs(this.prevAlpha - nbt.getFloat("prevAlpha")) > 0.5f) {
            this.prevAlpha = nbt.getFloat("prevAlpha");
        }

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

    public boolean isOwner() {
        return isOwner;
    }

    public boolean isDisappearing() {
        return this.disappearing;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public MagicDisappearingPlatformBlockEntity getOwner() {
        return owner;
    }

    public float getAlpha(float tickDelta) {

        if (!this.isOwner && this != this.getOwner() && this.getOwner() != null) {
            return this.getOwner().getAlpha(tickDelta);
        }

        return MathHelper.lerp(tickDelta, this.prevAlpha, this.alpha);
    }
}
