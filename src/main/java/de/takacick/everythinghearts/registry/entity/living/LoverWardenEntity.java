package de.takacick.everythinghearts.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.registry.entity.living.brain.LoverWardenBrain;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartEntity;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LoverWardenEntity extends WardenEntity {

    private static final TrackedData<Boolean> EXPLODING = BionicDataTracker.registerData(new Identifier(EverythingHearts.MOD_ID, "lover_warden_exploding"), TrackedDataHandlerRegistry.BOOLEAN);

    public int fuseTicks = 0;
    public int heartLevel = 1;

    public LoverWardenEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        getDataTracker().startTracking(EXPLODING, false);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return LoverWardenBrain.create(this);
    }

    @Override
    public Brain<WardenEntity> getBrain() {
        return super.getBrain();
    }

    @Override
    public boolean isValidTarget(@Nullable Entity entity) {
        return entity instanceof PlayerEntity && super.isValidTarget(entity);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public void tick() {

        if (isExploding()) {
            ++this.fuseTicks;
            addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20, 0, false, false, false));

            if (this.fuseTicks % 2 == 0) {
                world.playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, getSoundCategory(), 5f, 2f, true);
            }

            if (this.fuseTicks % 5 == 0) {
                world.playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, getSoundCategory(), 1f, 3f, true);
            }

            if (!world.isClient && this.fuseTicks > 119) {
                BionicUtils.sendEntityStatus((ServerWorld) world, this, EverythingHearts.IDENTIFIER, 2);

                world.getOtherEntities(this, getBoundingBox().expand(50)).forEach(entity -> {
                    entity.sendMessage(Text.of("§f<Lover Warden> I have been filled with the bliss touch of love. I no longer have §bsouls of §4hatred§f... I am §afree§f..."));
                });
                world.playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, getSoundCategory(), 5f, 2f);
                this.discard();

                for (int i = 0; i < 60; i++) {
                    HeartEntity heartEntity = new HeartEntity(world, getX() + world.getRandom().nextGaussian() * 2d, getBodyY(0.5) + world.getRandom().nextGaussian() * 3d + 1, getZ() + world.getRandom().nextGaussian() * 2d, null);
                    heartEntity.setVelocity(heartEntity.getPos().subtract(getPos().add(0, getHeight() / 2, 0)).normalize().multiply(1.2));
                    world.spawnEntity(heartEntity);
                }
            }
        } else {
            if (this.age % 40 == 0) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, this.getSoundCategory(), 3.0f, this.getSoundPitch(), false);
            }
        }

        super.tick();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("loverwardenexploding", getDataTracker().get(EXPLODING));
        nbt.putInt("heartLevel", this.heartLevel);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.heartLevel = Math.min(Math.max(nbt.getInt("heartLevel"), 1), 2);
        getDataTracker().set(EXPLODING, nbt.getBoolean("loverwardenexploding"));
    }

    @Override
    public float getMovementSpeed() {
        return isExploding() ? 0f : super.getMovementSpeed();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {

        if (!isExploding()) {
            if (player instanceof PlayerProperties playerProperties && playerProperties.isHeart()) {

                if (!world.isClient) {
                    playSound(SoundEvents.ENTITY_WARDEN_HEARTBEAT, 1f, 1f);
                    setExploding(true);
                    this.heartLevel = playerProperties.getHeartTouchLevel();
                }

                return ActionResult.SUCCESS;
            }
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.equals(SoundEvents.ENTITY_WARDEN_TENDRIL_CLICKS)) {
            return;
        }

        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }

    public boolean isExploding() {
        return getDataTracker().get(EXPLODING);
    }


    public void setExploding(boolean exploding) {
        getDataTracker().set(EXPLODING, exploding);
    }
}