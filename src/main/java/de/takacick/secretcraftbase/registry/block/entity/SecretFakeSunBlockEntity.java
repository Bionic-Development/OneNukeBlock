package de.takacick.secretcraftbase.registry.block.entity;

import de.takacick.secretcraftbase.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SecretFakeSunBlockEntity extends BlockEntity {

    private BlockPos ownerBlockPos;
    private boolean isOwner;

    public SecretFakeSunBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SECRET_FAKE_SUN, pos, state);
        ownerBlockPos = pos;
        isOwner = false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, SecretFakeSunBlockEntity blockEntity) {
        if (!blockEntity.isOwner()) {
            SecretFakeSunBlockEntity fakeSunBlockEntity = null;
            if (blockEntity.ownerBlockPos != null
                    && world.getBlockEntity(blockEntity.ownerBlockPos) instanceof SecretFakeSunBlockEntity secretFakeSunBlockEntity) {
                fakeSunBlockEntity = secretFakeSunBlockEntity;
            }

            if (fakeSunBlockEntity == null) {
                if (!world.isClient) {
                    world.breakBlock(pos, true);
                    blockEntity.markRemoved();
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

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.ownerBlockPos = NbtHelper.toBlockPos(nbt.getCompound("ownerBlockPos"));
        this.isOwner = nbt.getBoolean("isOwner");

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
}
