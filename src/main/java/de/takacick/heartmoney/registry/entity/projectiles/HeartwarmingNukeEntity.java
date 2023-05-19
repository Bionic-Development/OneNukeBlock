package de.takacick.heartmoney.registry.entity.projectiles;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HeartwarmingNukeEntity extends ThrowProjectileEntity {

    protected Vec3d vec3d;
    protected static final TrackedData<Boolean> FALLING = DataTracker.registerData(HeartwarmingNukeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int despawnTicks = 0;

    public HeartwarmingNukeEntity(EntityType<? extends HeartwarmingNukeEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public HeartwarmingNukeEntity(World world, LivingEntity owner) {
        super(EntityRegistry.HEARTWARMING_NUKE, owner, world);
    }

    public static HeartwarmingNukeEntity create(EntityType<HeartwarmingNukeEntity> entityType, World world) {
        return new HeartwarmingNukeEntity(entityType, world);
    }

    @Override
    public void setProperties(Entity user, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ) {
        super.setProperties(user, pitch, yaw, roll, modifierZ, modifierXYZ);

        vec3d = getVelocity();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(FALLING, false);
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    public void tick() {

        if (vec3d != null) {
            setVelocity(vec3d.multiply(isFalling() ? -1.2 : 1));
            this.velocityDirty = true;
            this.velocityModified = true;
        }

        if (!world.isClient) {
            if (age >= 300) {
                getDataTracker().set(FALLING, true);
            }
        } else if (!isFalling()) {
            for (int y = 0; y <= 5; y++) {
                for (int i = 0; i < 30; i++) {
                    this.world.addImportantParticle(ParticleRegistry.HEART_FIRE, true, this.getParticleX(0.5f), this.getY() + 0.2 * y,
                            this.getParticleZ(0.5f), 0.0D, -0.275D, 0.0D);
                }
            }
        }

        if (!isFalling()) {
            playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2f, 1f);
        } else {
            this.noClip = false;
        }

        if (despawnTicks > 0 && !world.isClient) {
            despawnTicks--;
            if (despawnTicks <= 0) {
                this.discard();
                world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    serverPlayerEntity.networkHandler.disconnect(Text.of("You got loved on too hard!"));
                });
            } else {
                world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    BionicUtils.sendEntityStatus((ServerWorld) world, serverPlayerEntity, HeartMoney.IDENTIFIER, 17);
                });
            }
        }

        super.tick();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

    @Override
    public double getDamage() {
        return 4.0f;
    }

    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    protected SoundEvent getHitSound() {
        return null;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (isFalling() && !world.isClient && !hitResult.getType().equals(HitResult.Type.MISS)) {
            if (despawnTicks <= 0) {
                despawnTicks = 2;
            }
            world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                BionicUtils.sendEntityStatus((ServerWorld) world, serverPlayerEntity, HeartMoney.IDENTIFIER, 17);
            });
        }
    }

    public boolean shouldRender(double distance) {
        double d = 428.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    public boolean isFalling() {
        return getDataTracker().get(FALLING);
    }
}
