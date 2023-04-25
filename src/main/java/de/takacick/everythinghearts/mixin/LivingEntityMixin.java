package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.LivingProperties;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.entity.living.LoverWardenEntity;
import de.takacick.everythinghearts.registry.entity.living.ProtoEntity;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "everythinghearts$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getMainHandStack();

    private static final TrackedData<Boolean> everythinghearts$HEART_INFECTED = BionicDataTracker.registerData(new Identifier(EverythingHearts.MOD_ID, "heart_infected"), TrackedDataHandlerRegistry.BOOLEAN);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(everythinghearts$HEART_INFECTED, false);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (damageSource.getSource() instanceof PlayerEntity playerEntity) {
            if (playerEntity.getMainHandStack().isOf(ItemRegistry.HEART_SCYTHE)) {
                playerEntity.sendMessage(Text.of("§a[+1 §c❤§a]"));
                BionicUtils.sendEntityStatus((ServerWorld) world, playerEntity, EverythingHearts.IDENTIFIER, 3);
            }
        }
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "HEAD"))
    public void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.WOODEN_HEART_SWORD)) {
            double d = -MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticles(ParticleRegistry.HEART_SWEEP_ATTACK, this.getX() + d, this.getBodyY(0.5) + getRotationVector().getY() * 0.75, this.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0f, 1f);
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, this.getSoundCategory(), 0.2f, 1f);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isClient && source.getSource() instanceof PlayerEntity player) {
            everythinghearts$tryToHeartInfect(player);
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"))
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        everythinghearts$tryToHeartInfect(entity);
    }

    @Inject(method = "pushAway", at = @At("HEAD"))
    public void pushAway(Entity entity, CallbackInfo info) {
        everythinghearts$tryToHeartInfect(entity);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("everythinghearts$heartinfected", getDataTracker().get(everythinghearts$HEART_INFECTED));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        everythinghearts$setHeartInfected(nbt.getBoolean("everythinghearts$heartinfected"));
    }

    public void everythinghearts$tryToHeartInfect(@Nullable Entity entity) {
        if (!world.isClient && !everythinghearts$isHeartInfected()) {
            if (getType().getTranslationKey().contains("everythinghearts") || ((Object) this instanceof PlayerEntity)) {
                return;
            }

            PlayerProperties playerProperties = entity instanceof PlayerEntity player ? (PlayerProperties) player : null;

            if (entity instanceof ProtoEntity protoEntity) {
                playerProperties = protoEntity.getOwner() instanceof PlayerProperties properties ? properties : protoEntity.getFirstPassenger() instanceof PlayerProperties properties ? properties : null;
            }

            if (entity instanceof ProtoEntity || playerProperties != null && playerProperties.hasHeartTouch() || entity == null) {
                BionicUtils.sendEntityStatus((ServerWorld) getEntityWorld(), this, EverythingHearts.IDENTIFIER, 7);
                if (!EverythingHearts.HEART_TOUCH_ENTITIES.containsKey(getType())) {
                    if (playerProperties != null) {
                        world.setBlockState(getBlockPos(), (playerProperties.getHeartTouchLevel() == 1 ? ItemRegistry.BASIC_HEART_BLOCK : ItemRegistry.MULTI_HEART_BLOCK).getDefaultState());
                    }
                    this.discard();
                } else {
                    EntityType<?> entityType = EverythingHearts.HEART_TOUCH_ENTITIES.get(getType());

                    if (entityType.equals(getType())) {
                        everythinghearts$setHeartInfected(true);
                    } else {
                        Entity heartEntity = entityType.create(world);
                        if (heartEntity != null) {
                            UUID uuid1 = heartEntity.getUuid();
                            int id = heartEntity.getId();
                            heartEntity.copyFrom(this);
                            heartEntity.copyPositionAndRotation(this);
                            heartEntity.setUuid(uuid1);
                            heartEntity.setId(id);
                            world.spawnEntity(heartEntity);
                        }
                        this.discard();
                    }
                }
            }
        }
    }

    @Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
    public void handleStatus(byte status, CallbackInfo info) {
        if ((everythinghearts$isHeartInfected() || (Object) this instanceof LoverWardenEntity) && status == EntityStatuses.ADD_DEATH_PARTICLES) {
            world.playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, getSoundCategory(), 0.3f, 5f, true);
            for (int i = 0; i < 20; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.world.addParticle(ParticleRegistry.HEART_POOF, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
            for (int i = 0; i < 30; ++i) {
                double d = this.random.nextGaussian() * 0.2;
                double e = this.random.nextGaussian() * 0.2;
                double f = this.random.nextGaussian() * 0.2;
                this.world.addParticle(ParticleRegistry.HEART, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
            info.cancel();
        }
    }

    public void everythinghearts$setHeartInfected(boolean heartInfected) {
        getDataTracker().set(everythinghearts$HEART_INFECTED, heartInfected);
    }

    public boolean everythinghearts$isHeartInfected() {
        return getDataTracker().get(everythinghearts$HEART_INFECTED);
    }
}