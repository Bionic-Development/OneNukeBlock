package de.takacick.onenukeblock.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.entity.living.ai.MutatedCreeperBrain;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class MutatedCreeperEntity extends WardenEntity {

    private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(MutatedCreeperEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IGNITED = DataTracker.registerData(MutatedCreeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int lastFuseTime;
    private int currentFuseTime;
    private int fuseTime = 30;
    private int fuseDelay = 0;

    public MutatedCreeperEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE_SPEED, -1);
        builder.add(IGNITED, false);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return MutatedCreeperBrain.create(this);
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

        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);

        if (bl) {
            EventHandler.sendEntityStatus(getWorld(), target, OneNukeBlock.IDENTIFIER, 8, 0);
            target.addVelocity(0, 0.3, 0);

            if (target instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
            }
        }

        return bl;
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            int i;

            if (this.fuseDelay > 0) {
                this.fuseDelay--;
            }

            this.lastFuseTime = this.currentFuseTime;
            if (this.isIgnited()) {
                this.setFuseSpeed(1);
            } else {
                this.setFuseSpeed(-1);
            }

            if ((i = this.getFuseSpeed()) > 0 && this.currentFuseTime == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.45f);
                this.emitGameEvent(GameEvent.PRIME_FUSE);
            }
            this.currentFuseTime += i;
            if (this.currentFuseTime < 0) {
                this.currentFuseTime = 0;
            }
            if (this.currentFuseTime >= this.fuseTime) {
                this.currentFuseTime = this.fuseTime;
                this.explode();
            }
        }
        super.tick();
    }

    private void explode() {
        if (!this.getWorld().isClient) {
            EventHandler.sendEntityStatus(getWorld(), this, OneNukeBlock.IDENTIFIER, 9, Block.getRawIdFromState(getSteppingBlockState()));
            this.getWorld().createExplosion(this, null, null, this.getX(), this.getY(), this.getZ(),
                    6f, false, World.ExplosionSourceType.MOB, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, RegistryEntry.of(SoundEvents.INTENTIONALLY_EMPTY));
            this.onRemoval(Entity.RemovalReason.KILLED);
            this.getDataTracker().set(IGNITED, false);
            this.currentFuseTime = 0;
            this.fuseDelay = 100;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    public boolean hasFuseDelay() {
        return this.fuseDelay > 0;
    }

    public boolean isIgnited() {
        return this.dataTracker.get(IGNITED);
    }

    public void ignite() {
        this.dataTracker.set(IGNITED, true);
    }

    public float getClientFuseTime(float timeDelta) {
        return MathHelper.lerp(timeDelta, (float) this.lastFuseTime, (float) this.currentFuseTime) / (float) (this.fuseTime - 2);
    }

    public int getFuseSpeed() {
        return this.dataTracker.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.dataTracker.set(FUSE_SPEED, fuseSpeed);
    }

    @Override
    public void onDeath(DamageSource damageSource) {


        super.onDeath(damageSource);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {

        dropStack(Items.GUNPOWDER.getDefaultStack().copyWithCount(getRandom().nextBetween(20, 32)));
        dropStack(Items.TNT.getDefaultStack().copyWithCount(getRandom().nextBetween(20, 32)));

        super.dropLoot(damageSource, causedByPlayer);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putShort("Fuse", (short) this.fuseTime);
        nbt.putInt("fuseDelay", this.fuseDelay);
        nbt.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Fuse", NbtElement.NUMBER_TYPE)) {
            this.fuseTime = nbt.getShort("Fuse");
        }
        if (nbt.contains("fuseDelay", NbtElement.INT_TYPE)) {
            this.fuseDelay = nbt.getInt("fuseDelay");
        }
        if (nbt.getBoolean("ignited")) {
            this.ignite();
        }
    }
}