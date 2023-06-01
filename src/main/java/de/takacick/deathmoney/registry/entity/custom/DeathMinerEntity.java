package de.takacick.deathmoney.registry.entity.custom;

import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.particles.ColoredParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class DeathMinerEntity extends Entity {

    public DeathMinerEntity(EntityType<? extends DeathMinerEntity> entityType, World world) {
        super(entityType, world);
    }

    public DeathMinerEntity(World world, double x, double y, double z) {
        this(EntityRegistry.DEATH_MINER, world);
        this.setPosition(x, y, z);
    }

    public static DeathMinerEntity create(EntityType<DeathMinerEntity> entityType, World world) {
        return new DeathMinerEntity(entityType, world);
    }

    @Override
    public void tick() {

        if (!world.isClient) {
            for (int x = -8; x < 8; x++) {
                for (int z = -8; z < 8; z++) {
                    BlockPos blockPos = new BlockPos(getX() + x, getY(), getZ() + z);
                    BlockState blockState = world.getBlockState(blockPos);

                    if (!blockState.isAir() && blockState.getBlock().getBlastResistance() < Blocks.BEDROCK.getBlastResistance()) {
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    }
                }
            }

            setPos(getX(), getY() - 1, getZ());
        } else {
            for (int x = -8; x < 8; x++) {
                for (int z = -8; z < 8; z++) {
                    Vec3d pos = new Vec3d(getX() + x, getY(), getZ() + z);
                    if (world.getRandom().nextDouble() <= 0.1) {
                        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WITHER_SKELETON_STEP, SoundCategory.BLOCKS, 0.3f, 1f + (float) world.getRandom().nextDouble() * 0.3f, false);
                    }

                    if (world.getRandom().nextDouble() <= 0.4) {
                        for (int i = 0; i < 20; i++) {
                            double d = world.getRandom().nextGaussian() * 0.02;
                            double e = world.getRandom().nextGaussian() * 0.02;
                            double f = world.getRandom().nextGaussian() * 0.02;
                            world.addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COAL_BLOCK.getDefaultState()), true, pos.getX() + d * 8, pos.getY() + e * 8, pos.getZ() + f * 8, d, e, f);
                        }
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x4DFF17));
                        for (int i = 0; i < 2; i++) {
                            double d = world.getRandom().nextGaussian() * 0.08;
                            double e = world.getRandom().nextGaussian() * 0.08;
                            double f = world.getRandom().nextGaussian() * 0.08;
                            world.addImportantParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, color), true, pos.getX() + d * 6, pos.getY() + e * 6, pos.getZ() + f * 6, d, e, f);
                        }
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return !this.isRemoved();
    }


    @Override
    protected void initDataTracker() {

    }

    @Override
    public boolean shouldRender(double distance) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void kill() {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}

