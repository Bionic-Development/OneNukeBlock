package de.takacick.illegalwars.registry.entity.projectiles;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class CyberLaserEntity extends AbstractFireballEntity {
    public static final Vector3f COLOR = Vec3d.unpackRgb(0xFFF261).toVector3f();
    public static final RegistryKey<DamageType> CYBER_LASER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(IllegalWars.MOD_ID, "cyber_laser"));

    public CyberLaserEntity(EntityType<? extends CyberLaserEntity> entityType, World world) {
        super(entityType, world);
    }

    public CyberLaserEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(EntityRegistry.CYBER_LASER, owner, velocityX, velocityY, velocityZ, world);
    }

    public CyberLaserEntity(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(EntityRegistry.CYBER_LASER, x, y, z, velocityX, velocityY, velocityZ, world);
    }

    public static CyberLaserEntity create(EntityType<CyberLaserEntity> entityType, World world) {
        return new CyberLaserEntity(entityType, world);
    }

    @Override
    public void tick() {
        HitResult hitResult;
        Entity entity = this.getOwner();
        if (!this.getWorld().isClient && (entity != null && entity.isRemoved() || !this.getWorld().isChunkLoaded(this.getBlockPos()))) {
            this.discard();
            return;
        }
        this.baseTick();
        if (this.isBurning()) {
            this.setOnFireFor(1);
        }
        if ((hitResult = ProjectileUtil.getCollision(this, this::canHit)).getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }
        this.checkBlockCollision();
        Vec3d vec3d = this.getVelocity();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.setVelocity(new Vec3d(this.powerX, this.powerY, this.powerZ).multiply(15));
        this.setPosition(d, e, f);
        ProjectileUtil.setRotationFromVelocity(this, 1f);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();

            entity.damage(getWorld().getDamageSources().create(CYBER_LASER), 2f);
            entity.setOnFireFor(5);

            BionicUtils.sendEntityStatus(getWorld(), entityHitResult.getEntity(), IllegalWars.IDENTIFIER, 7);
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        explode();
    }

    private void explode() {
        if (!getWorld().isClient) {
            BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 7);
            this.discard();
        }
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }
}

