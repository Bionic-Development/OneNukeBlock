package de.takacick.illegalwars.registry.block.entity;

import de.takacick.illegalwars.registry.EntityRegistry;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BaseWarsMoneyWheelBlockEntity extends BlockEntity {

    private BlockPos ownerBlockPos;
    private boolean isOwner;
    private BaseWarsMoneyWheelBlockEntity owner;

    private float spin = 0f;
    private float rotation = 0f;
    private float prevRotation = 0f;
    private int lastSection = 0;
    private boolean done = true;

    public boolean gui = false;

    public BaseWarsMoneyWheelBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.BASE_WARS_MONEY_WHEEL, pos, state);
        ownerBlockPos = pos;
        isOwner = false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, BaseWarsMoneyWheelBlockEntity blockEntity) {
        if (!blockEntity.isOwner()) {
            BaseWarsMoneyWheelBlockEntity moneyWheelBlockEntity = null;
            if (blockEntity.ownerBlockPos != null
                    && world.getBlockEntity(blockEntity.ownerBlockPos) instanceof BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity) {
                moneyWheelBlockEntity = baseWarsMoneyWheelBlockEntity;
            }

            blockEntity.owner = moneyWheelBlockEntity;
            if (moneyWheelBlockEntity == null) {
                if (!world.isClient) {
                    world.breakBlock(blockPos, true);
                    blockEntity.markRemoved();
                }
            }
        } else {
            blockEntity.prevRotation = blockEntity.rotation;
            if (blockEntity.isSpinning()) {
                blockEntity.done = false;
                blockEntity.spin *= 0.95f;
                blockEntity.rotation += Math.min(blockEntity.spin * 0.5f, 200 * 0.5f);

                int section = (int) (Math.ceil((blockEntity.rotation - 22.5) / 45f));

                if (section != blockEntity.lastSection) {
                    blockEntity.lastSection = section;
                    Vec3d pos = Vec3d.ofCenter(blockPos);

                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.BLOCKS, 1f, 1f + blockEntity.spin * 0.25f, true);
                }

                if(!blockEntity.isSpinning()) {
                    world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                }
            } else {
                blockEntity.spin = 0f;
                float target = (blockEntity.lastSection) * 45f;
                blockEntity.rotation += (target - blockEntity.rotation) * 0.1f;

                if (!blockEntity.done) {
                    blockEntity.done = true;

                    if (!world.isClient) {
                        world.syncWorldEvent(6435123, blockPos, 2 + (blockEntity.lastSection % 2 == 0 ? 0 : 1));
                    }
                }
                world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
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

    public BaseWarsMoneyWheelBlockEntity getOwner() {
        return isOwner ? this : owner;
    }

    public float getRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevRotation, this.rotation);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("ownerBlockPos", NbtHelper.fromBlockPos(this.ownerBlockPos));
        nbt.putBoolean("isOwner", this.isOwner);
        nbt.putFloat("spin", this.spin);
        nbt.putFloat("rotation", this.rotation);
        nbt.putFloat("prevRotation", this.prevRotation);
        nbt.putInt("lastSection", this.lastSection);
        nbt.putBoolean("done", this.done);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.ownerBlockPos = NbtHelper.toBlockPos(nbt.getCompound("ownerBlockPos"));
        this.isOwner = nbt.getBoolean("isOwner");
        this.spin = nbt.getFloat("spin");

        if (Math.abs(this.rotation - nbt.getFloat("rotation")) > 10f) {
            this.rotation = nbt.getFloat("rotation");
        }

        if (Math.abs(this.prevRotation - nbt.getFloat("prevRotation")) > 10f) {
            this.prevRotation = nbt.getFloat("prevRotation");
        }

        this.lastSection = nbt.getInt("lastSection");
        this.done = nbt.getBoolean("done");

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

    public void setSpin(float spin) {
        this.spin = spin;
    }

    public float getSpin() {
        return spin;
    }

    public boolean isSpinning() {
        return this.spin >= 1.5f;
    }

    public boolean isOwner() {
        return this.isOwner;
    }
}
