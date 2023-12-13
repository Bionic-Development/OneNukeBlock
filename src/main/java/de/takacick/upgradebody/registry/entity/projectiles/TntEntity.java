package de.takacick.upgradebody.registry.entity.projectiles;

import de.takacick.upgradebody.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TntEntity extends ThrownEntity {

    public TntEntity(EntityType<? extends TntEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public TntEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.TNT, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        setOwner(owner);
    }

    public static TntEntity create(EntityType<TntEntity> entityType, World world) {
        return new TntEntity(entityType, world);
    }

    public boolean shouldRender(double distance) {
        double d = 428.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

    @Override
    public void tick() {
        this.updateWaterState();
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
        }
        super.tick();
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!getWorld().isClient) {
            if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                getWorld().createExplosion(getOwner(), getX(), getY(), getZ(), 3f, World.ExplosionSourceType.TNT);
                this.discard();
            }
        }
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
