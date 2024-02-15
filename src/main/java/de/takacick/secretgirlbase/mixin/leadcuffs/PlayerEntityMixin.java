package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEntity.class, priority = 1001)
@Implements({@Interface(iface = LeadCuffProperties.class, prefix = "secretgirlbase$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract boolean isSpectator();

    private static final TrackedData<Integer> secretgirlbase$LEAD_TARGET = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> secretgirlbase$LEAD_CUFFED = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    private PlayerEntity secretgirlbase$leadCuffOwner;
    private PlayerEntity secretgirlbase$leadCuffTarget;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(secretgirlbase$LEAD_TARGET, -1);
        getDataTracker().startTracking(secretgirlbase$LEAD_CUFFED, -1);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (secretgirlbase$leadCuffOwner != null) {
            if (secretgirlbase$leadCuffOwner.isAlive() && !secretgirlbase$getLeadCuffedOwner().isSpectator()) {
                Entity entity = this.secretgirlbase$leadCuffOwner;
                if (entity != null && entity.getWorld() == this.getWorld()) {
                    float f = this.distanceTo(entity);
                    if (f > 4.0f) {
                        double d = (entity.getX() - this.getX()) / (double) f;
                        double e = (entity.getY() - this.getY()) / (double) f;
                        double g = (entity.getZ() - this.getZ()) / (double) f;
                        this.setVelocity(this.getVelocity().add(Math.copySign(d * d * 0.4, d), Math.copySign(e * e * 0.4, e), Math.copySign(g * g * 0.4, g)));
                    }
                }
            } else {
                secretgirlbase$setLeadCuffed(null);
            }
        }

        if (secretgirlbase$getLeadCuffedTarget() != null && (!secretgirlbase$getLeadCuffedTarget().isAlive() || secretgirlbase$getLeadCuffedTarget().isSpectator())) {
            secretgirlbase$leadCuff(null);
        }
    }

    public void secretgirlbase$setLeadCuffed(PlayerEntity playerEntity) {
        this.secretgirlbase$leadCuffOwner = playerEntity;
        getDataTracker().set(secretgirlbase$LEAD_CUFFED, playerEntity != null ? playerEntity.getId() : -1);
    }

    public PlayerEntity secretgirlbase$getLeadCuffedOwner() {
        return this.secretgirlbase$leadCuffOwner;
    }

    public void secretgirlbase$leadCuff(PlayerEntity playerEntity) {
        this.secretgirlbase$leadCuffTarget = playerEntity;
        getDataTracker().set(secretgirlbase$LEAD_TARGET, playerEntity != null ? playerEntity.getId() : -1);
    }

    public PlayerEntity secretgirlbase$getLeadCuffedTarget() {
        return this.secretgirlbase$leadCuffTarget;
    }

    public boolean secretgirlbase$isLeadCuffed() {
        return this.secretgirlbase$leadCuffOwner != null && this.secretgirlbase$leadCuffOwner.isAlive();
    }

    public void secretgirlbase$trackedDataSet(TrackedData<?> data) {
        if (data.equals(secretgirlbase$LEAD_CUFFED)) {
            this.secretgirlbase$leadCuffOwner = null;

            if (getWorld().getEntityById(getDataTracker().get(secretgirlbase$LEAD_CUFFED)) instanceof PlayerEntity playerEntity) {
                this.secretgirlbase$leadCuffOwner = playerEntity;
            }
        } else if (data.equals(secretgirlbase$LEAD_TARGET)) {
            this.secretgirlbase$leadCuffTarget = null;

            if (getWorld().getEntityById(getDataTracker().get(secretgirlbase$LEAD_TARGET)) instanceof PlayerEntity playerEntity) {
                this.secretgirlbase$leadCuffTarget = playerEntity;
            }
        }
    }
}