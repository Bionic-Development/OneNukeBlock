package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.ClientPlayerProperties;
import de.takacick.upgradebody.access.ExperienceOrbProperties;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import de.takacick.upgradebody.registry.entity.living.Upgraded;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicTrackedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract Random getRandom();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo info) {
        if (data instanceof BionicTrackedData<?> bionicTrackedData) {
            if (bionicTrackedData.getIdentifier().equals(new Identifier(UpgradeBody.MOD_ID, "body_parts"))) {
                this.calculateDimensions();
                this.reinitDimensions();

                if (this instanceof PlayerProperties playerProperties) {
                    playerProperties.getBodyPartManager().reloadAttributes();
                }

                if (getWorld().isClient) {
                    if (this instanceof ClientPlayerProperties clientPlayerProperties) {
                        clientPlayerProperties.refreshEntityModel();
                    }
                }
            }
        }
    }

    @Inject(method = "pushAway", at = @At("HEAD"), cancellable = true)
    public void pushAway(Entity entity, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties
                && !entity.equals(this)
                && playerProperties.isUsingHeadbutt()) {
            upgradebody$pushAway(this, entity);
            playerProperties.setHeadbutt(false);
        } else if (this instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()
                && playerProperties.hasBodyPart(BodyParts.TANK_TRACKS)) {
            if (entity instanceof Upgraded
                    || entity instanceof PlayerProperties upgradingPlayer
                    && upgradingPlayer.isUpgrading()) {
                return;
            }

            if (isSprinting()) {
                entity.damage(getWorld().getDamageSources().generic(), 5f);
                entity.setVelocity(0, 0, 0);
                entity.velocityDirty = true;
                entity.velocityModified = true;

                ScaleData scaleDataWidth = ScaleTypes.WIDTH.getScaleData(entity);
                ScaleData scaleDataHeight = ScaleTypes.HEIGHT.getScaleData(entity);
                if (scaleDataHeight.getScale() >= 0.15f) {
                    final float width = scaleDataWidth.getScale() * 1.2f;
                    final float height = scaleDataHeight.getScale() * 0.15f;

                    BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
                    scaleDataWidth.setScale(width);
                    scaleDataHeight.setScale(height);

                    for (int i = getRandom().nextBetween(5, 10); i > 0; ) {
                        int level = MathHelper.clamp(getRandom().nextBetween(1, 2), 0, i);
                        i -= level;

                        ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(getWorld(), getX(), getBodyY(0.5), getZ(), level);
                        ((ExperienceOrbProperties) experienceOrbEntity).setLevelOrb(true);
                        ((ExperienceOrbProperties) experienceOrbEntity).setCooldown(5);
                        experienceOrbEntity.setVelocity(getRandom().nextGaussian() * 0.15, getRandom().nextDouble() * 0.25, getRandom().nextGaussian() * 0.15);
                        getWorld().spawnEntity(experienceOrbEntity);
                    }
                }
                info.cancel();
            }
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        if (entity instanceof PlayerProperties playerProperties
                && !entity.equals(this)
                && playerProperties.isUsingHeadbutt()) {
            upgradebody$pushAway(entity, this);
            playerProperties.setHeadbutt(false);
        } else if (this instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()
                && playerProperties.hasBodyPart(BodyParts.TANK_TRACKS)) {
            ScaleData scaleDataHeight = ScaleTypes.HEIGHT.getScaleData(entity);
            if (scaleDataHeight.getScale() >= 0.15f) {
                info.cancel();
            }
        }
    }

    @Inject(method = "getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", at = @At("RETURN"), cancellable = true)
    public void getAttributeValue(EntityAttribute attribute, CallbackInfoReturnable<Double> info) {
        if (this instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()
                && attribute.equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                && playerProperties.hasBodyPart(BodyParts.CYBER_CHAINSAWS)) {
            if (getMainHandStack().isEmpty()) {
                info.setReturnValue(info.getReturnValue() + 3);
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (!getWorld().isClient) {
            if (damageSource.getSource() instanceof PlayerProperties playerProperties &&
                    playerProperties.isUsingCyberSlice()) {
                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, UpgradeBody.IDENTIFIER, 6);
                for (int i = getRandom().nextBetween(200, 500); i > 0; ) {
                    int level = MathHelper.clamp(getRandom().nextBetween(10, 40), 0, i);
                    i -= level;

                    ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(getWorld(), getX(), getBodyY(0.5), getZ(), level);
                    ((ExperienceOrbProperties) experienceOrbEntity).setLevelOrb(true);
                    ((ExperienceOrbProperties) experienceOrbEntity).setCooldown(5);
                    experienceOrbEntity.setVelocity(getRandom().nextGaussian() * 0.15, getRandom().nextDouble() * 0.25, getRandom().nextGaussian() * 0.15);
                    getWorld().spawnEntity(experienceOrbEntity);
                }
            }
        }
    }

    private static void upgradebody$pushAway(Entity entity, Entity target) {
        target.setVelocity(entity.getVelocity().multiply(1.5).add(0, 0.3, 0));
        target.velocityDirty = true;
        target.velocityModified = true;

        entity.setVelocity(entity.getVelocity().multiply(-0.5));
        entity.velocityDirty = true;
        entity.velocityModified = true;

        if (!entity.getWorld().isClient) {
            target.damage(entity instanceof PlayerEntity player ? entity.getWorld().getDamageSources().playerAttack(player) : entity.getWorld().getDamageSources().cramming(), 5f);
            BionicUtils.sendEntityStatus((ServerWorld) target.getWorld(), target, UpgradeBody.IDENTIFIER, 3);
        }
    }
}