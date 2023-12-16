package de.takacick.tinyhouse.registry.block.entity;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.registry.EffectRegistry;
import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.block.SpinningPeepeeChoppa;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpinningPeepeeChoppaBlockEntity extends BlockEntity {

    public static final RegistryKey<DamageType> SPINNING_PEEPEE_CHOPPA = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(TinyHouse.MOD_ID, "spinning_peepee_choppa"));

    private int rotation = 0;
    private int prevRotation = 0;

    public SpinningPeepeeChoppaBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SPINNING_PEEPEE_CHOPPA, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, SpinningPeepeeChoppaBlockEntity blockEntity) {
        blockEntity.prevRotation = blockEntity.rotation;
        if (state.get(SpinningPeepeeChoppa.POWERED)) {
            Vec3d center = Vec3d.ofCenter(pos).add(0, 0.98d, 0);

            if (!world.isClient) {
                Box box = new Box(pos.getX(), pos.getY() + 0.98, pos.getZ(), pos.getX() + 1,
                        pos.getY() + 1, pos.getZ() + 1).expand(1, 0, 1);

                world.getOtherEntities(null, box).forEach(entity -> {
                    if (new Vec3d(entity.getX(), center.getY(), entity.getZ())
                            .distanceTo(center) <= 1.2 + entity.getWidth() / 2f) {
                        if(  entity.damage(world.getDamageSources().create(SPINNING_PEEPEE_CHOPPA), 5f)) {
                            BionicUtils.sendEntityStatus((ServerWorld) world, entity, TinyHouse.IDENTIFIER, 2);
                            if (entity instanceof LivingEntity livingEntity) {
                                livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
                            }
                        }
                    }
                });

            } else {
                if(blockEntity.rotation % 2 == 0) {
                    world.playSound( center.getX(), center.getY(), center.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.AMBIENT, 1f, 1f, true);
                }
                world.playSound( center.getX(), center.getY(), center.getZ(), SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.AMBIENT, 1f, 1f, true);
            }

            blockEntity.rotation++;
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("rotation", this.rotation);
        nbt.putInt("prevRotation", this.prevRotation);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.rotation = nbt.getInt("rotation");
        this.prevRotation = nbt.getInt("prevRotation");

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

    public float getRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, (float) this.prevRotation, (float) this.rotation);
    }
}
