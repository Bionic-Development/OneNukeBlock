package de.takacick.onedeathblock.registry.entity.projectiles;

import de.takacick.onedeathblock.registry.EntityRegistry;
import de.takacick.onedeathblock.registry.ParticleRegistry;
import de.takacick.onedeathblock.server.BuildMeteorExplosionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class BuildMeteorEntity extends AbstractFireballEntity {

    public BuildMeteorEntity(EntityType<? extends BuildMeteorEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public BuildMeteorEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.BUILD_METEOR, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.ignoreCameraFrustum = true;
        setOwner(owner);
    }

    public static BuildMeteorEntity create(EntityType<BuildMeteorEntity> entityType, World world) {
        return new BuildMeteorEntity(entityType, world);
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
    public void tick() {
        HitResult hitResult;
        Entity entity = this.getOwner();
        if (!this.world.isClient && (entity != null && entity.isRemoved() || !this.world.isChunkLoaded(this.getBlockPos()))) {
            this.discard();
            return;
        }
        this.baseTick();
        if ((hitResult = ProjectileUtil.getCollision(this, this::canHit)).getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }
        this.checkBlockCollision();

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(1f));
        ProjectileUtil.setRotationFromVelocity(this, 1f);

        if (!world.isClient) {
            if (age >= 300 || isOnGround() || isInsideWall()) {
                this.discard();
                BuildMeteorExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                        getX(), getBodyY(0.5), getZ(), 3f, false,
                        Explosion.DestructionType.NONE);
                spawnBlocks();
            }
        } else {
            Vec3d vec3d = getVelocity();
            for (int i = 0; i < 5; i++) {
                world.addImportantParticle(ParticleRegistry.SMOKE, true, getX() + world.getRandom().nextGaussian() * 0.7, getBodyY(0.5) + world.getRandom().nextGaussian() * 0.7, getZ() + world.getRandom().nextGaussian() * 0.7, -vec3d.getX() * 0.001, -vec3d.getY() * 0.001, -vec3d.getZ() * 0.001);
            }

            for (int i = 0; i < 15; i++) {
                world.addImportantParticle(ParticleTypes.FLAME, true, getX() + world.getRandom().nextGaussian() * 0.7, getBodyY(0.5) + world.getRandom().nextGaussian() * 0.7, getZ() + world.getRandom().nextGaussian() * 0.7, -vec3d.getX() * 0.001, -vec3d.getY() * 0.07, -vec3d.getZ() * 0.001);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!hitResult.getType().equals(HitResult.Type.MISS)) {
            if (!world.isClient) {

                if(hitResult instanceof EntityHitResult entityHitResult) {
                    setPosition(entityHitResult.getPos());
                }

                this.discard();
                BuildMeteorExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                        getX(), getBodyY(0.5), getZ(), 6f, false,
                        Explosion.DestructionType.NONE);
                spawnBlocks();
            }
        }
    }

    public void spawnBlocks() {
        List<Block> blocks = Arrays.asList(Blocks.PACKED_ICE, Blocks.ANCIENT_DEBRIS, Blocks.BEDROCK, Blocks.MAGMA_BLOCK);

        for (int i = 0; i < 25; i++) {
            StateManager<Block, BlockState> stateManager = blocks.get(world.getRandom().nextInt(blocks.size())).getStateManager();
            BlockState blockState = stateManager.getStates().get(world.getRandom().nextInt(stateManager.getStates().size()));

            BlockPos blockPos = getBlockPos().add(world.getRandom().nextGaussian() * 3, 1 + world.getRandom().nextDouble() * 3, world.getRandom().nextGaussian() * 3);
            CustomBlockEntity customBlockEntity = new CustomBlockEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, null);
            customBlockEntity.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            customBlockEntity.setItemStack(blockState, blockState.getBlock().asItem().getDefaultStack());

            Vec3d vec3d = customBlockEntity.getPos().subtract(getPos()).normalize();
            customBlockEntity.setVelocity(vec3d.getX() + world.getRandom().nextGaussian() * 0.2,
                    0.2f + world.getRandom().nextDouble() * 1.2, vec3d.getZ() + world.getRandom().nextGaussian() * 0.2);
            customBlockEntity.velocityDirty = true;
            customBlockEntity.drop = true;
            world.spawnEntity(customBlockEntity);
        }
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if (!world.isClient && state.getMaterial().blocksMovement() && isAlive()) {
            this.discard();
            BuildMeteorExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                    getX(), getBodyY(0.5), getZ(), 3f, false,
                    Explosion.DestructionType.NONE);
            spawnBlocks();
        }
        super.onBlockCollision(state);
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15f;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (world.isClient) {
            return false;
        }

        return !getType().equals(entity.getType()) && !entity.equals(getOwner()) && super.canHit(entity);
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (!world.isClient) {
            this.discard();
            BuildMeteorExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                    getX(), getBodyY(0.5), getZ(), 3f, false,
                    Explosion.DestructionType.NONE);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }
}