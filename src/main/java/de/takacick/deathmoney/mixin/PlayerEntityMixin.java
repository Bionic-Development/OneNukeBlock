package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.entity.custom.DeathShopPortalEntity;
import de.takacick.deathmoney.registry.entity.custom.EarthFangsEntity;
import de.takacick.deathmoney.registry.entity.custom.ShopItemEntity;
import de.takacick.deathmoney.registry.entity.projectiles.MeteorEntity;
import de.takacick.deathmoney.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.deathmoney.utils.DeathToast;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "deathmoney$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract SoundCategory getSoundCategory();

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    private static final TrackedData<Integer> deathmoney$DEATHS = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "heart"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> deathmoney$CAKE_EXPLOSION = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "cake_explosion"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> deathmoney$METEOR = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "meteor"), TrackedDataHandlerRegistry.INTEGER);
    private final AnimationState deathmoney$heartRemovalAnimationState = new AnimationState();
    private DeathShopPortalEntity deathmoney$deathShopPortal;
    private double deathmoney$deathMultiplier = 1d;
    private List<DeathToast> deathmoney$deathToasts;
    private int deathmoney$deathTicks = -1;
    private int deathmoney$heartRemovalTicks = 0;
    private ItemStack deathmoney$heartCarver;
    private int deathmoney$gamerAllergyTicks = 0;
    private int deathmoney$meteorShakeTicks = 0;
    private boolean deathmoney$deathDrop = false;
    private int deathmoney$earthFangsTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(deathmoney$DEATHS, 0);
        getDataTracker().startTracking(deathmoney$CAKE_EXPLOSION, 0);
        getDataTracker().startTracking(deathmoney$METEOR, 0);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (this.deathmoney$heartRemovalTicks > 0) {
                this.deathmoney$heartRemovalTicks--;

                if (this.deathmoney$heartRemovalTicks <= 0) {
                    Vec3d right = getRotationVector(getPitch(), getYaw() + 90).multiply(0.05);
                    Vec3d vel = getRotationVector().multiply(0.35).add(right.getX() * world.getRandom().nextGaussian(), 0.1, right.getZ() * world.getRandom().nextGaussian());
                    ItemEntity itemEntity
                            = new ItemEntity(world, getX(), getBodyY(0.65), getZ(), ItemRegistry.HEART.getDefaultStack(), vel.getX(), vel.getY(), vel.getZ());
                    itemEntity.setPickupDelay(30);
                    itemEntity.setOwner(getUuid());
                    world.spawnEntity(itemEntity);

                    DeathMoney.updateEntityHealth(this, getMaxHealth() - 6, false);

                    if (this.deathmoney$heartCarver != null) {
                        if (!isCreative()) {
                            this.deathmoney$heartCarver.damage(1, this, playerEntityMixin -> playerEntityMixin.sendToolBreakStatus(Hand.MAIN_HAND));
                        }
                        this.deathmoney$heartCarver = null;
                    }
                }
            }

            if (this.deathmoney$gamerAllergyTicks > 0) {
                this.deathmoney$gamerAllergyTicks--;

                if (this.deathmoney$gamerAllergyTicks <= 0) {
                    world.playSound(null, getX(), getBodyY(0.65), getZ(), SoundEvents.ITEM_BONE_MEAL_USE, getSoundCategory(), 1f, 1f);
                    ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getBodyY(0.5), getZ(), 20, getWidth(), getHeight() / 2, getWidth(), 0.2);
                } else {
                    DeathMoney.generateSphere(getBlockPos().add(0, 1, 0), 5, false).forEach(blockPos -> {
                        BlockState blockState = world.getBlockState(blockPos);

                        if (blockState.getMaterial().blocksMovement() || (blockState.getMaterial().isLiquid()
                                || blockState.getBlock() instanceof FluidFillable)) {
                            world.setBlockState(blockPos, Blocks.GRASS_BLOCK.getDefaultState(),
                                    ~Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS
                                            | Block.FORCE_STATE | Block.REDRAW_ON_MAIN_THREAD | ~Block.NO_REDRAW | ~Block.SKIP_LIGHTING_UPDATES);

                            BlockState state = world.getBlockState(blockPos.add(0, 1, 0));
                            boolean tall = world.getBlockState(blockPos.add(0, 2, 0)).isAir();

                            if (state.isAir()) {
                                if (tall && world.getRandom().nextDouble() <= 0.2) {
                                    world.setBlockState(blockPos.add(0, 1, 0), Blocks.TALL_GRASS.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER));
                                    world.setBlockState(blockPos.add(0, 2, 0), Blocks.TALL_GRASS.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER));
                                } else {
                                    world.setBlockState(blockPos.add(0, 1, 0), Blocks.GRASS.getDefaultState());
                                }
                                if (world.getRandom().nextDouble() <= 0.5) {
                                    world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos.add(0, 1, 0), 0);
                                }
                            }
                        }
                    });

                    ((ServerWorld) world).spawnParticles(ParticleTypes.FALLING_SPORE_BLOSSOM, getX(), getBodyY(0.5), getZ(), 20, 5, 5, 5, 0.01);
                }
            }

            int meteorTicks = getDataTracker().get(deathmoney$METEOR);
            if (meteorTicks > 0) {
                deathmoney$setMeteorTicks(meteorTicks - 1);

                if (meteorTicks % 10 == 0) {
                    world.playSoundFromEntity(null, this, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundCategory.AMBIENT, 1f, 1f);
                }

                BlockPos blockPos = getBlockPos();

                for (int i = 0; i < 2; i++) {
                    MeteorEntity meteorEntity = new MeteorEntity(EntityRegistry.METEOR, getWorld());
                    meteorEntity.setPos(blockPos.getX() + 0.5 + getRandom().nextGaussian() * 50, blockPos.getY() + 0.5 + 100, blockPos.getZ() + 0.5 + getRandom().nextGaussian() * 50);
                    Vec3d velocity = new Vec3d(blockPos.getX(), 0, blockPos.getZ()).subtract(meteorEntity.getPos().multiply(1, 0, 1)).normalize();
                    meteorEntity.setVelocity(velocity.add(0, -2.8, 0));
                    getWorld().spawnEntity(meteorEntity);
                }

                BionicUtils.sendEntityStatus((ServerWorld) world, this, DeathMoney.IDENTIFIER, 8);
            }

            if (this.deathmoney$earthFangsTicks > 0) {
                this.deathmoney$earthFangsTicks--;

                Vec3d pos = new Vec3d(getX() + world.getRandom().nextGaussian() * 0.5, getY(), getZ() + world.getRandom().nextGaussian() * 0.5);

                EarthFangsEntity earthFangsEntity = new EarthFangsEntity(world, pos.getX(), pos.getY(), pos.getZ(), (float) getRandom().nextGaussian() * 180f, 8, null);

                double d = getPos().x - pos.x;
                double e = getPos().y - pos.y;
                double f = getPos().z - pos.z;
                double g = Math.sqrt(d * d + f * f);
                earthFangsEntity.setPitch(MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875))));
                earthFangsEntity.setYaw(MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f));
                earthFangsEntity.setHeadYaw(earthFangsEntity.getYaw());
                earthFangsEntity.prevPitch = earthFangsEntity.getPitch();
                earthFangsEntity.prevYaw = earthFangsEntity.getYaw();
                earthFangsEntity.refreshPositionAndAngles(earthFangsEntity.getX(), earthFangsEntity.getY(), earthFangsEntity.getZ(), earthFangsEntity.getYaw(), earthFangsEntity.getPitch());
                world.spawnEntity(earthFangsEntity);
            }
        } else {
            if (this.deathmoney$deathTicks >= 0) {
                this.deathmoney$deathTicks++;
                if (this.deathmoney$deathTicks > 20) {
                    this.deathmoney$deathTicks = -1;
                }

                this.deathTime = Math.max(this.deathmoney$deathTicks, 0);
            }

            if (this.deathmoney$heartRemovalAnimationState.isRunning()) {
                Vec3d vec3d = getRotationVector().multiply(0.15);
                Vec3d right = getRotationVector(getPitch(), getYaw() + 90).multiply(0.05);
                if (age % 15 == 0) {
                    world.playSound(getX(), getBodyY(0.65), getZ(), SoundEvents.ENTITY_GENERIC_HURT, getSoundCategory(), 1f, 1f, true);
                    animateDamage();
                }

                for (int i = 0; i < 2; i++) {
                    Vec3d vel = vec3d.add(right.getX() * world.getRandom().nextGaussian(), 0, right.getZ() * world.getRandom().nextGaussian());

                    world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x8a0303)),
                                    (float) (world.getRandom().nextDouble() * 0.5f)), getX(), getBodyY(0.65), getZ(),
                            vel.getX(), vel.getY(), vel.getZ());
                }

                if (this.deathmoney$heartRemovalAnimationState.getTimeRunning() >= 1.57f * 1000L) {
                    animateDamage();
                    this.deathmoney$heartRemovalAnimationState.stop();
                }
            }

            if (this.deathmoney$meteorShakeTicks > 0) {
                this.deathmoney$meteorShakeTicks--;
            }
        }

        int cakeExplosionTicks = getDataTracker().get(deathmoney$CAKE_EXPLOSION);

        if (cakeExplosionTicks > 0) {
            if (this.world.isClient) {
                this.world.addParticle(ParticleTypes.SMOKE, getX(), getBodyY(0.8), getZ(), 0.0, 0.0, 0.0);
            }
            cakeExplosionTicks--;
            deathmoney$setCakeExplosionTicks(cakeExplosionTicks);

            if (cakeExplosionTicks <= 0) {
                if (!this.world.isClient) {

                    this.lastDamageTaken = 0;
                    damage(DeathDamageSources.CAKE_EXPLOSION, Integer.MAX_VALUE);
                    world.createExplosion(this, getX(), getBodyY(0.5), getZ(), 4.0f, Explosion.DestructionType.BREAK);
                }
            }
        }

    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    public void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> info) {
        if (source == DeathDamageSources.SWEET_BERRY_SUIT) {
            info.setReturnValue(SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH);
        }
    }

    @ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), argsOnly = true)
    public DamageSource handleFallDamage(DamageSource damageSource) {
        if (this.deathmoney$deathDrop) {
            if (!hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                this.deathmoney$deathDrop = false;
            }
            return DeathDamageSources.DEATH_DROP;
        }

        return damageSource;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("deathmoney$deaths", getDataTracker().get(deathmoney$DEATHS));
        nbt.putInt("deathmoney$cakeExplosion", getDataTracker().get(deathmoney$CAKE_EXPLOSION));
        nbt.putInt("deathmoney$meteorTicks", getDataTracker().get(deathmoney$METEOR));
        nbt.putDouble("deathmoney$deathMultiplier", this.deathmoney$deathMultiplier);
        nbt.putInt("deathmoney$gamerAllergyTicks", this.deathmoney$gamerAllergyTicks);
        nbt.putInt("deathmoney$earthFangsTicks", this.deathmoney$earthFangsTicks);
        nbt.putBoolean("deathmoney$deathDrop", this.deathmoney$deathDrop);

        if (this.deathmoney$deathShopPortal != null && !world.isClient) {
            this.deathmoney$deathShopPortal.setDead(true);
            this.deathmoney$deathShopPortal = null;
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(deathmoney$DEATHS, nbt.getInt("deathmoney$deaths"));
        getDataTracker().set(deathmoney$CAKE_EXPLOSION, nbt.getInt("deathmoney$cakeExplosion"));
        getDataTracker().set(deathmoney$METEOR, nbt.getInt("deathmoney$meteorTicks"));
        this.deathmoney$gamerAllergyTicks = nbt.getInt("deathmoney$gamerAllergyTicks");
        this.deathmoney$deathDrop = nbt.getBoolean("deathmoney$deathDrop");
        this.deathmoney$earthFangsTicks = nbt.getInt("deathmoney$earthFangsTicks");

        if (nbt.contains("deathmoney$deathMultiplier", NbtElement.INT_TYPE)) {
            this.deathmoney$deathMultiplier = nbt.getDouble("deathmoney$deathMultiplier");
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        if (target instanceof ShopItemEntity && isCreative()) {
            target.discard();
            info.cancel();
        }
    }

    public void deathmoney$setDeaths(int deaths) {
        getDataTracker().set(deathmoney$DEATHS, deaths);
    }

    public int deathmoney$addDeaths(int deaths, boolean multiplier) {

        if (multiplier) {
            deaths *= deathmoney$getDeathMultiplier();
        }

        if (!world.isClient) {
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(deaths);
            ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new Identifier(DeathMoney.MOD_ID, "adddeaths"), packetByteBuf);
        }

        deaths += deathmoney$getDeaths();
        getDataTracker().set(deathmoney$DEATHS, deaths);

        return deaths;
    }

    public int deathmoney$getDeaths() {
        return getDataTracker().get(deathmoney$DEATHS);
    }

    public void deathmoney$setDeathShopPortal(DeathShopPortalEntity deathShopPortalEntity) {
        if (this.deathmoney$deathShopPortal != null && this.deathmoney$deathShopPortal.isAlive()) {
            this.deathmoney$deathShopPortal.setDead(true);
        }

        this.deathmoney$deathShopPortal = deathShopPortalEntity;
    }

    public void deathmoney$setDeathMultiplier(double deathMultiplier) {
        this.deathmoney$deathMultiplier = deathMultiplier;
    }

    public double deathmoney$getDeathMultiplier() {
        return this.deathmoney$deathMultiplier;
    }

    public List<DeathToast> deathmoney$getDeathToasts() {
        if (this.deathmoney$deathToasts == null) {
            this.deathmoney$deathToasts = new ArrayList<>();
        }

        return this.deathmoney$deathToasts;
    }

    public void deathmoney$setDeathTicks(int deathTicks) {
        this.deathmoney$deathTicks = deathTicks;
    }

    public AnimationState deathmoney$getHeartRemovalState() {
        return deathmoney$heartRemovalAnimationState;
    }

    public void deathmoney$setHeartCarverStack(ItemStack heartCarver) {
        this.deathmoney$heartCarver = heartCarver;
    }

    public void deathmoney$setHeartRemovalTicks(int heartRemovalTicks) {
        this.deathmoney$heartRemovalTicks = heartRemovalTicks;
    }

    public int deathmoney$getHeartRemovalTicks() {
        return this.deathmoney$heartRemovalTicks;
    }

    public void deathmoney$setCakeExplosionTicks(int cakeExplosionTicks) {
        getDataTracker().set(deathmoney$CAKE_EXPLOSION, cakeExplosionTicks);
    }

    public int deathmoney$getCakeExplosionTicks() {
        return getDataTracker().get(deathmoney$CAKE_EXPLOSION);
    }

    public void deathmoney$setGamerAllergyTicks(int gamerAllergyTicks) {
        this.deathmoney$gamerAllergyTicks = gamerAllergyTicks;
    }

    public int deathmoney$getGamerAllergyTicks() {
        return this.deathmoney$gamerAllergyTicks;
    }

    public void deathmoney$setMeteorTicks(int meteorTicks) {
        getDataTracker().set(deathmoney$METEOR, meteorTicks);
    }

    public void deathmoney$setMeteorShakeTicks(int meteorShakeTicks) {
        this.deathmoney$meteorShakeTicks = meteorShakeTicks;
    }

    public int deathmoney$getMeteorShakeTicks() {
        return this.deathmoney$meteorShakeTicks;
    }

    public void deathmoney$setDeathDrop(boolean deathDrop) {
        this.deathmoney$deathDrop = deathDrop;
    }

    public void deathmoney$resetDamageDelay() {
        this.hurtTime = 0;
        this.lastDamageTaken = 0f;
    }

    public void deathmoney$setEarthFangsTicks(int earthFangsTicks) {
        this.deathmoney$earthFangsTicks = earthFangsTicks;
    }
}

