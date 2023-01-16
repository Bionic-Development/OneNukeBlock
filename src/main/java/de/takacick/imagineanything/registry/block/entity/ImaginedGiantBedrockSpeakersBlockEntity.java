package de.takacick.imagineanything.registry.block.entity;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.ImagineAnythingClient;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.projectiles.GiantVibrationEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ImaginedGiantBedrockSpeakersBlockEntity extends BlockEntity {

    private boolean activated = false;
    public boolean playedSound = false;

    public ImaginedGiantBedrockSpeakersBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.IMAGINED_GIANT_BEDROCK_SPEAKERS_ENTITY, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ImaginedGiantBedrockSpeakersBlockEntity blockEntity) {
        if (blockEntity.activated) {

            world.getOtherEntities(null, new Box(pos).expand(18)).forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity.damage(DamageSource.GENERIC, 0.0001f)) {
                        BionicUtils.sendEntityStatus((ServerWorld) world, livingEntity, ImagineAnything.IDENTIFIER, 18);
                    }
                }
            });

            for (int i = 0; i < 20; i++) {
                GiantVibrationEntity giantVibrationEntity = new GiantVibrationEntity(world, pos.getX() + 0.5, pos.getY() + world.getRandom().nextDouble() * 3, pos.getZ() + 0.5, null);
                giantVibrationEntity.setVelocity(null, (float) world.getRandom().nextGaussian() * 10f,
                        (float) world.getRandom().nextGaussian() * 180f, 0.0F, 1.5F + (float) 0.5F, 1.0F);
                giantVibrationEntity.setPosition(giantVibrationEntity.getPos().add(giantVibrationEntity.getVelocity().multiply(0.6)));
                world.spawnEntity(giantVibrationEntity);
            }
        }
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, ImaginedGiantBedrockSpeakersBlockEntity blockEntity) {
        if (blockEntity.activated && !blockEntity.playedSound) {
            blockEntity.playedSound = true;
            ImagineAnythingClient.playSound(blockEntity);
        }
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("activated", activated);
        super.writeNbt(nbt);
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        activated = nbt.getBoolean("activated");
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
}
