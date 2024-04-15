package de.takacick.onegirlfriendblock.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.EffectRegistry;
import de.takacick.onegirlfriendblock.registry.EntityRegistry;
import de.takacick.onegirlfriendblock.registry.entity.living.ai.ZukoHumanoidBrain;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ZukoHumanoidEntity extends WardenEntity {

    private UUID killerUuid;

    public ZukoHumanoidEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        BlockSoundGroup blockSoundGroup = state.getSoundGroup();
        this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch());
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundEvent = this.getAmbientSound();
        if (soundEvent != null) {
            this.playSound(soundEvent, this.getSoundVolume(), this.getSoundPitch() - getRandom().nextFloat() * 0.3f);
        }
    }

    @Override
    protected float getSoundVolume() {
        return 2.0f;
    }

    @Override
    public float getSoundPitch() {
        return 0.8f;
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return ZukoHumanoidBrain.create(this);
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
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.equals(SoundEvents.ENTITY_WARDEN_TENDRIL_CLICKS) || sound.equals(SoundEvents.ENTITY_WARDEN_HEARTBEAT)) {
            return;
        }

        if (sound == SoundEvents.ENTITY_WARDEN_ATTACK_IMPACT) {
            sound = SoundEvents.ENTITY_PLAYER_ATTACK_STRONG;
        }

        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (damageSource.getSource() instanceof PlayerEntity playerEntity) {
            this.killerUuid = playerEntity.getUuid();
        }

        super.onDeath(damageSource);
    }

    @Override
    public boolean tryAttack(Entity target) {

        boolean bl = super.tryAttack(target);

        if (bl && target instanceof LivingEntity livingEntity) {
            BionicUtils.sendEntityStatus(getWorld(), livingEntity, OneGirlfriendBlock.IDENTIFIER, 18);
            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
        }

        return bl;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!getWorld().isClient && reason.equals(RemovalReason.KILLED)) {
            BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 20);
            ZukoEntity zukoEntity = new ZukoEntity(EntityRegistry.ZUKO, getWorld());
            zukoEntity.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());
            if (this.killerUuid != null) {
                zukoEntity.setTamed(true);
                zukoEntity.setOwnerUuid(this.killerUuid);
            }
            getWorld().spawnEntity(zukoEntity);

            BionicUtils.sendEntityStatus(getWorld(), zukoEntity, OneGirlfriendBlock.IDENTIFIER, 19);
        }

        super.remove(reason);
    }

    @Override
    public boolean shouldDropXp() {
        return false;
    }
}