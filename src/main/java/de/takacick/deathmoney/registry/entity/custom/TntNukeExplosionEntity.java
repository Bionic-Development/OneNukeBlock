package de.takacick.deathmoney.registry.entity.custom;

import com.google.common.collect.Lists;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.utils.TntNukeExplosionHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Random;

public class TntNukeExplosionEntity extends Entity {

    public TntNukeExplosionEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        age = nbt.getInt("age");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("age", age);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public static int getStageOneTick() {
        return 0;
    }

    public static int getMaxStageOneTick() {
        return 10;
    }

    public static float getBlastRadius() {
        return 5;
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isClient()) {
            if (age >= getStageOneTick() && age <= getMaxStageOneTick()) {
                if (age % 1 == 0) {
                    List<BlockPos> affectedBlockPositions = getAffectedBlockPositions(world, getX(), getY(), getZ(), age + 9, 35);

                    TntNukeExplosionHandler.createExplosion((ServerWorld) world, this,  DeathDamageSources.TNT_NUKE, null, getX(), getY(), getZ(), getBlastRadius(), false, Explosion.DestructionType.DESTROY, affectedBlockPositions);
                }
            } else {
                this.setRemoved(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public static final float Y_SHORTEN = 1.5f;

    public static List<BlockPos> getAffectedBlockPositions(World world, double x, double y, double z, float radius, double max_blast_power) {
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        int radius_int = (int) Math.ceil(radius);
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {

            int y_lim = (int) (Math.sqrt(radius_int * radius_int - dx * dx) / Y_SHORTEN);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int * radius_int - dx * dx - dy * dy * Y_SHORTEN * Y_SHORTEN);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    BlockState blockState = world.getBlockState(blockPos);
                    double power = getBlastPower(Math.sqrt(dx * dx + dy * dy * Y_SHORTEN * Y_SHORTEN + dz * dz), radius);
                    if (blockState != Blocks.AIR.getDefaultState() && ((power > 1) || (power > new Random().nextDouble()))) {
                        float resistance = blockState.getBlock().getBlastResistance();
                        if (resistance < max_blast_power) {
                            affectedBlockPositions.add(blockPos);
                        }
                    }
                }
            }
        }
        return affectedBlockPositions;
    }

    public static double getBlastPower(double dist, double radius) {
        double decay_rd = radius * 0.95;
        if (dist < decay_rd) {
            return 1.1d;
        } else {
            return -(1 / (radius - decay_rd)) * (dist - decay_rd) + 1;
        }
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}
