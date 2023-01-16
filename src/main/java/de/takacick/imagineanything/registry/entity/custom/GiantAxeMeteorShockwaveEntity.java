package de.takacick.imagineanything.registry.entity.custom;

import de.takacick.imagineanything.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class GiantAxeMeteorShockwaveEntity extends Entity {

    private LivingEntity owner;
    private Vec3d vec3d;

    public GiantAxeMeteorShockwaveEntity(EntityType<? extends GiantAxeMeteorShockwaveEntity> entityType, World world) {
        super(entityType, world);
    }

    public GiantAxeMeteorShockwaveEntity(World world, Vec3d vec3d, double x, double y, double z) {
        this(EntityRegistry.GIANT_AXE_METEOR_SHOCKWAVE, world);
        this.setPosition(x, y, z);
        this.vec3d = vec3d;
        this.noClip = true;
    }

    public static GiantAxeMeteorShockwaveEntity create(EntityType<GiantAxeMeteorShockwaveEntity> entityType, World world) {
        return new GiantAxeMeteorShockwaveEntity(entityType, world);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0D;
        if (Double.isNaN(d)) {
            d = 4.0D;
        }

        d *= 64.0D;
        return distance < d * d;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    public void setVelocityClient(double x, double y, double z) {
        this.setVelocity(x, y, z);
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            double d = Math.sqrt(x * x + z * z);
            this.setYaw((float) (MathHelper.atan2(x, z) * 57.2957763671875D));
            this.setPitch((float) (MathHelper.atan2(y, d) * 57.2957763671875D));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
    }

    public void tick() {
        super.tick();

        if (!world.isClient) {
            if (age >= 35) {
                remove(RemovalReason.DISCARDED);
                return;
            }

            playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);
            ((ServerWorld) getEntityWorld()).spawnParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0.5, 0.5, 0.5, 0);

            world.getOtherEntities(owner, new Box(getPos(), getPos()).expand(1.3, 15, 1.3)).forEach(entity -> {
                if (!entity.isPlayer() && entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame()) {
                    livingEntity.damage(DamageSource.GENERIC, 4);
                    livingEntity.setVelocity(vec3d.multiply(1.5).add(0, 0.5, 0));
                }
            });

            for (int x = -1; x < 2; x++) {
                for (int y = -8; y < 13; y++) {
                    for (int z = -1; z < 2; z++) {

                        BlockPos target = getBlockPos().add(x, y, z);
                        BlockState state = world.getBlockState(target);
                        if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES)) {
                            world.breakBlock(target, true);


                            if(y == 7) {
                                breakTree(world, target);
                            }
                        }
                    }
                }
            }

            move(MovementType.SELF, vec3d);
            velocityModified = true;
            velocityDirty = true;
        }
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    public boolean isAttackable() {
        return false;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public void breakTree(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos target = pos.add(direction.getVector());
            BlockState blockState = world.getBlockState(target);
            if (blockState.isIn(BlockTags.LOGS)) {
                world.breakBlock(target, true);

                breakTree(world, target);

                for (int x = -4; x < 4; x++) {
                    for (int y = -4; y < 4; y++) {
                        for (int z = -4; z < 4; z++) {
                            BlockPos leaveTarget = target.add(x, y, z);
                            BlockState state = world.getBlockState(leaveTarget);
                            if (state.isIn(BlockTags.LEAVES)) {
                                world.breakBlock(leaveTarget, true);
                            }
                        }
                    }
                }

            } else if (blockState.isIn(BlockTags.LEAVES)) {
                world.breakBlock(target, true);
            }
        }
    }
}