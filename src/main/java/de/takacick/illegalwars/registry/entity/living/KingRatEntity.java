package de.takacick.illegalwars.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.EffectRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.entity.living.brain.KingRatBrain;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KingRatEntity extends WardenEntity {

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.translatable(getType().getTranslationKey()), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);

    public KingRatEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {

        if (!getWorld().isClient) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }

        super.tick();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return ParticleRegistry.RAT_SQUEAK;
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
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParticleRegistry.RAT_HURT;
    }

    @Override
    public float getSoundPitch() {
        return 0.8f;
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return KingRatBrain.create(this);
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

        if(sound == SoundEvents.ENTITY_WARDEN_ATTACK_IMPACT) {
            sound = SoundEvents.ENTITY_PLAYER_ATTACK_STRONG;
        }

        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public boolean tryAttack(Entity target) {

        boolean bl = super.tryAttack(target);

        if (bl && target instanceof LivingEntity livingEntity) {
            if (getWorld() instanceof ServerWorld serverWorld) {
                BionicUtils.sendEntityStatus(serverWorld, livingEntity, IllegalWars.IDENTIFIER, 2);
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
        }

        return bl;
    }

}