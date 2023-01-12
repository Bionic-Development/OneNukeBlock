package de.takacick.immortalmobs.registry.entity.custom;

import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.living.ImmortalEntity;
import de.takacick.immortalmobs.registry.entity.projectiles.CustomBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class BlackHoleEntity extends HostileEntity {

    private Entity player;

    public BlackHoleEntity(EntityType<? extends BlackHoleEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.noClip = true;
        this.setNoGravity(true);
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Entity getPlayer() {
        return player;
    }

    @Override
    protected void initGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        if (age % 5 == 0 && age < 75) {
            world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.AMBIENT, 3, 1, false);
        }

        if (!world.isClient) {

            if (age > 80) {
                world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 3, 1);
                ((ServerWorld) world).spawnParticles(ParticleRegistry.IMMORTAL_EXPLOSION, getX(), getY(), getZ(),
                        10, 3, 3, 3, 0);
                this.discard();
                return;
            }

            Vec3d pos = new Vec3d(getX(), getY(), getZ());

            world.getOtherEntities(getPlayer(), new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ())
                    .expand(30)).forEach(entity -> {
                if (!(entity instanceof BlackHoleEntity) && !(entity instanceof ImmortalEntity) && entity.isAlive()) {
                    double distance = entity.getPos().distanceTo(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                    if (distance <= 1.7) {
                        entity.damage(DamageSource.MAGIC, 1f);
                    } else {
                        entity.damage(DamageSource.MAGIC, 10f);
                    }

                    Vec3d pull = pos.subtract(entity.getPos()).normalize();
                    pull = pull.multiply(Math.pow(1f, (1.5F) / (1 * 2.5F))).multiply(0.4f);

                    entity.addVelocity(MathHelper.clamp(pull.getX(), -1F, +1F), MathHelper.clamp(pull.getY(), -1F, +1F), MathHelper.clamp(pull.getZ(), -1F, +1F));
                    entity.velocityModified = true;
                    entity.velocityDirty = true;
                }
            });
            destroy(world, getBlockX(), getBlockY(), getBlockZ(), 25);
            destroy(world, getBlockX(), getBlockY(), getBlockZ(), 25);
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {

    }

    public boolean shouldRender(double distance) {
        double d = 64.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    @Override
    public boolean isPartOfGame() {
        return false;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_MAX_HEALTH, 500).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.11).add(EntityAttributes.GENERIC_ARMOR).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    private final BlockState AIR = Blocks.AIR.getDefaultState();

    public boolean isWithinDistance(int x1, int x2, int y1, int y2, int distance) {
        return ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) < distance * distance;
    }

    public static void destroy(World world, int x, int y, int z, int radius) {
        int clearRadius = (int) (radius * 1.5F);

        int pickupRadius = (int) (radius * 2.5F);

        int chunkRadius = Math.max(1, radius / 16) + 6;

        for (int chunkX = (x >> 4) - chunkRadius; chunkX < (x >> 4) + chunkRadius; ++chunkX) {
            for (int chunkZ = (z >> 4) - chunkRadius; chunkZ < (z >> 4) + chunkRadius; ++chunkZ) {
                WorldChunk chunk = world.getChunk(chunkX, chunkZ);

                int rX = chunk.getWorld().random.nextInt(16);
                int rZ = chunk.getWorld().random.nextInt(16);

                int rCX = (chunkX * 16 + rX - x) * (chunkX * 16 + rX - x);
                int rCZ = (chunkZ * 16 + rZ - z) * (chunkZ * 16 + rZ - z);

                if (rCX + rCZ < pickupRadius * pickupRadius) {
                    BlockPos topPosition = chunk.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(chunkX * 16 + rX, 0, chunkZ * 16 + rZ)).add(0, -1, 0);

                    BlockState state = chunk.getBlockState(topPosition);

                    if (!state.isAir() && !(state.getBlock() instanceof FluidBlock)) {
                        if (world.random.nextInt((int) Math.sqrt(4)) == 0) {
                            world.setBlockState(topPosition, Blocks.AIR.getDefaultState());

                            CustomBlockEntity customBlockEntity = new CustomBlockEntity(EntityRegistry.FALLING_BLOCK, world);
                            customBlockEntity.setItemStack(state, state.getBlock().asItem().getDefaultStack());
                            customBlockEntity.noClip = true;
                            customBlockEntity.setPos(topPosition.getX(), topPosition.getY(), topPosition.getZ());
                            world.spawnEntity(customBlockEntity);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isUndead() {
        return false;
    }
}