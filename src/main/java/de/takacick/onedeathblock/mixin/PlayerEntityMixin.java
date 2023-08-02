package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.client.DeathToast;
import de.takacick.onedeathblock.damage.DeathDamageSources;
import de.takacick.onedeathblock.registry.EntityRegistry;
import de.takacick.onedeathblock.registry.ItemRegistry;
import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import de.takacick.onedeathblock.registry.entity.living.SkullagerEntity;
import de.takacick.onedeathblock.registry.item.HeadSpawner;
import de.takacick.onedeathblock.registry.item.SpikyIronArmorSuit;
import de.takacick.onedeathblock.registry.item.VenomousPotato;
import de.takacick.onedeathblock.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "onedeathblock$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract SoundCategory getSoundCategory();

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void wakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers);

    @Shadow
    private int sleepTimer;

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow
    protected abstract SoundEvent getHurtSound(DamageSource source);

    @Shadow
    protected abstract void applyDamage(DamageSource source, float amount);

    @Shadow
    public abstract PlayerInventory getInventory();

    @Shadow
    public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    private static final TrackedData<Boolean> onedeathblock$DEATHS_DISPLAY = BionicDataTracker.registerData(new Identifier(OneDeathBlock.MOD_ID, "deaths_counter"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> onedeathblock$DEATHS = BionicDataTracker.registerData(new Identifier(OneDeathBlock.MOD_ID, "deaths"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> onedeathblock$TNT_EXPLOSION = BionicDataTracker.registerData(new Identifier(OneDeathBlock.MOD_ID, "tnt_explosion"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> onedeathblock$EXPLOSIVE_PLACING = BionicDataTracker.registerData(new Identifier(OneDeathBlock.MOD_ID, "explosive_placing"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> onedeathblock$SHOCKED = BionicDataTracker.registerData(new Identifier(OneDeathBlock.MOD_ID, "shocked"), TrackedDataHandlerRegistry.INTEGER);
    private final AnimationState onedeathblock$heartRemovalAnimationState = new AnimationState();
    private double onedeathblock$deathMultiplier = 1d;
    private List<DeathToast> onedeathblock$deathToasts;
    private int onedeathblock$deathTicks = -1;
    private int onedeathblock$heartRemovalTicks = 0;
    private ItemStack onedeathblock$heartCarver;
    private int onedeathblock$meteorShakeTicks = 0;
    private int onedeathblock$spawnerDelay = 600;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onedeathblock$DEATHS_DISPLAY, false);
        getDataTracker().startTracking(onedeathblock$DEATHS, 0);
        getDataTracker().startTracking(onedeathblock$TNT_EXPLOSION, 0);
        getDataTracker().startTracking(onedeathblock$EXPLOSIVE_PLACING, false);
        getDataTracker().startTracking(onedeathblock$SHOCKED, 0);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (this.onedeathblock$heartRemovalTicks > 0) {
                this.onedeathblock$heartRemovalTicks--;

                if (this.onedeathblock$heartRemovalTicks <= 0) {
                    Vec3d right = getRotationVector(getPitch(), getYaw() + 90).multiply(0.05);
                    Vec3d vel = getRotationVector().multiply(0.35).add(right.getX() * world.getRandom().nextGaussian(), 0.1, right.getZ() * world.getRandom().nextGaussian());
                    ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.65), getZ(), ItemRegistry.HEART.getDefaultStack(), vel.getX(), vel.getY(), vel.getZ());
                    itemEntity.setPickupDelay(30);
                    itemEntity.setOwner(getUuid());
                    world.spawnEntity(itemEntity);

                    OneDeathBlock.updateEntityHealth(this, getMaxHealth() - 6, false);

                    if (this.onedeathblock$heartCarver != null) {
                        if (!isCreative()) {
                            this.onedeathblock$heartCarver.damage(1, this, playerEntityMixin -> playerEntityMixin.sendToolBreakStatus(Hand.MAIN_HAND));
                        }
                        this.onedeathblock$heartCarver = null;
                    }
                }
            }

            if (onedeathblock$getShockedTicks() > 0) {
                onedeathblock$setShockedTicks(onedeathblock$getShockedTicks() - 1);
                if (onedeathblock$getShockedTicks() % 5 == 0) {
                    onedeathblock$resetDamageDelay();
                }

                damage(DeathDamageSources.ELECTRICUTIONER_DOOR, 10);
                world.playSoundFromEntity(null, this, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, getSoundCategory(), 1f, 1f);
            }

            if (getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SpikyIronArmorSuit) {
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    if (equipmentSlot.equals(EquipmentSlot.CHEST) || !equipmentSlot.getType().equals(EquipmentSlot.Type.ARMOR)) {
                        continue;
                    }

                    ItemStack itemStack = getEquippedStack(equipmentSlot);
                    if (itemStack.isEmpty()) {
                        continue;
                    }

                    getInventory().offerOrDrop(itemStack.copy());
                    this.equipStack(equipmentSlot, Items.AIR.getDefaultStack());
                }
            }

            if (getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof HeadSpawner) {
                if (this.onedeathblock$spawnerDelay > 0) {
                    this.onedeathblock$spawnerDelay--;
                    if (isSneaking()) {
                        this.onedeathblock$spawnerDelay -= 100;
                    }
                }

                if (this.onedeathblock$spawnerDelay <= 0) {
                    this.onedeathblock$spawnerDelay = getRandom().nextBetween(600, 700);

                    EntityType<SkullagerEntity> entityType = EntityRegistry.SKULLAGER;

                    for (int i = 0; i < world.getRandom().nextBetween(3, 6); i++) {
                        for (int i2 = 0; i2 < 5; i2++) {
                            double d = getX() + random.nextGaussian() * 6 + 0.5;
                            double e = (getY() + random.nextInt(3) - 1);
                            double f = getZ() + random.nextGaussian() * 6 + 0.5;

                            if (!world.isSpaceEmpty(entityType.createSimpleBoundingBox(d, e, f))) {
                                continue;
                            }

                            SkullagerEntity entity2 = entityType.create(world);

                            if (entity2 != null) {
                                entity2.setPos(d, e, f);
                                entity2.refreshPositionAndAngles(entity2.getX(), entity2.getY(), entity2.getZ(), random.nextFloat() * 360.0f, 0.0f);
                                entity2.initialize((ServerWorld) entity2.getWorld(), entity2.getWorld().getLocalDifficulty(entity2.getBlockPos()), SpawnReason.SPAWNER, null, null);
                                world.spawnEntity(entity2);
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            if (getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof HeadSpawner) {
                Vec3f vec3f = new Vec3f(new Vec3d(getX(), getBodyY(1), getZ()));
                Vec3f rot = new Vec3f(0, 0.6f, 0);
                rot.rotate(Vec3f.POSITIVE_Z.getDegreesQuaternion(getPitch()));
                rot.rotate(Vec3f.NEGATIVE_Y.getDegreesQuaternion(getHeadYaw() - 90));
                rot.multiplyComponentwise(2, 1, 2);
                vec3f.add(rot);

                double d = vec3f.getX() + random.nextGaussian() * 0.25;
                double e = vec3f.getY() + random.nextGaussian() * 0.25;
                double f = vec3f.getZ() + random.nextGaussian() * 0.25;
                world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
                world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
            }

            if (this.onedeathblock$deathTicks >= 0) {
                this.onedeathblock$deathTicks++;
                if (this.onedeathblock$deathTicks > 20) {
                    this.onedeathblock$deathTicks = -1;
                }

                this.deathTime = Math.max(this.onedeathblock$deathTicks, 0);
            }

            if (this.onedeathblock$heartRemovalAnimationState.isRunning()) {
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

                if (this.onedeathblock$heartRemovalAnimationState.getTimeRunning() >= 1.57f * 1000L) {
                    animateDamage();
                    this.onedeathblock$heartRemovalAnimationState.stop();
                }
            }

            if (this.onedeathblock$meteorShakeTicks > 0) {
                this.onedeathblock$meteorShakeTicks--;
            }
        }

        int tntExplosionTicks = getDataTracker().get(onedeathblock$TNT_EXPLOSION);

        if (tntExplosionTicks > 0) {
            if (this.world.isClient) {
                this.world.addParticle(ParticleTypes.SMOKE, getX(), getBodyY(0.9), getZ(), 0.0, 0.0, 0.0);
            }
            tntExplosionTicks--;
            onedeathblock$setTntExplosionTicks(tntExplosionTicks);

            if (tntExplosionTicks <= 0) {
                if (!this.world.isClient) {
                    this.lastDamageTaken = 0;
                    damage(DeathDamageSources.TNT_EXPLOSION, Integer.MAX_VALUE);
                    world.createExplosion(this, getX(), getBodyY(0.5), getZ(), 4.0f, Explosion.DestructionType.BREAK);
                }
            }
        }

        if (onedeathblock$hasExplosivePlacing()) {
            for (Hand hand : Hand.values()) {
                if (getStackInHand(hand).getItem() instanceof BlockItem) {
                    int index = hand == Hand.MAIN_HAND ? 2 : 1;

                    Vec3d vec3d = Vec3d.fromPolar(new Vec2f(0, this.bodyYaw)).multiply(0.35);
                    double x = VenomousPotato.getHeadX((PlayerEntity) (Object) this, index) + vec3d.getX();
                    double y = VenomousPotato.getHeadY((PlayerEntity) (Object) this, index) - (isSneaking() ? 0.4 : 0.1);
                    double z = VenomousPotato.getHeadZ((PlayerEntity) (Object) this, index) + vec3d.getZ();

                    double d = getRandom().nextGaussian() * 0.1;
                    double e = getRandom().nextGaussian() * 0.1;
                    double f = getRandom().nextGaussian() * 0.1;

                    world.addParticle(ParticleTypes.SMOKE, true, x + d, y + e, z + f, d * 0.001, e * 0.001, f * 0.001);
                }
            }
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;wakeUp(ZZ)V"))
    public void blockWakeUp(PlayerEntity instance, boolean skipSleepTimer, boolean updateSleepingPlayers) {
        if (!(getSleepingPosition().isPresent()
                && this.world.getBlockEntity(getSleepingPosition().get()) instanceof SpikedBedBlockEntity)) {
            this.sleepTimer = 0;
            wakeUp(skipSleepTimer, updateSleepingPlayers);
        } else {
            if (this.hurtTime <= 0) {
                BionicUtils.sendEntityStatus((ServerWorld) world, this, OneDeathBlock.IDENTIFIER, 3);
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), getHurtSound(DamageSource.GENERIC), this.getSoundCategory(), 1f, 1f);
                onedeathblock$addDeaths(getRandom().nextBetween(10, 30), true);
                this.hurtTime = 10;
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("onedeathblock$deathsDisplay", getDataTracker().get(onedeathblock$DEATHS_DISPLAY));
        nbt.putInt("onedeathblock$deaths", getDataTracker().get(onedeathblock$DEATHS));
        nbt.putInt("onedeathblock$tntExplosion", getDataTracker().get(onedeathblock$TNT_EXPLOSION));
        nbt.putBoolean("onedeathblock$explosivePlacing", getDataTracker().get(onedeathblock$EXPLOSIVE_PLACING));
        nbt.putInt("onedeathblock$shocked", getDataTracker().get(onedeathblock$SHOCKED));
        nbt.putDouble("onedeathblock$deathMultiplier", this.onedeathblock$deathMultiplier);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(onedeathblock$DEATHS_DISPLAY, nbt.getBoolean("onedeathblock$deathsDisplay"));
        getDataTracker().set(onedeathblock$DEATHS, nbt.getInt("onedeathblock$deaths"));
        getDataTracker().set(onedeathblock$TNT_EXPLOSION, nbt.getInt("onedeathblock$tntExplosion"));
        getDataTracker().set(onedeathblock$EXPLOSIVE_PLACING, nbt.getBoolean("onedeathblock$explosivePlacing"));
        getDataTracker().set(onedeathblock$SHOCKED, nbt.getInt("onedeathblock$shocked"));

        if (nbt.contains("onedeathblock$deathMultiplier", NbtElement.INT_TYPE)) {
            this.onedeathblock$deathMultiplier = nbt.getDouble("onedeathblock$deathMultiplier");
        }
    }

    public void onedeathblock$setDeaths(int deaths) {
        getDataTracker().set(onedeathblock$DEATHS, deaths);
    }

    public int onedeathblock$addDeaths(int deaths, boolean multiplier) {

        if (multiplier) {
            deaths *= onedeathblock$getDeathMultiplier();
        }

        if (!world.isClient) {
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(deaths);
            ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new Identifier(OneDeathBlock.MOD_ID, "adddeaths"), packetByteBuf);
        }

        deaths += onedeathblock$getDeaths();
        getDataTracker().set(onedeathblock$DEATHS, deaths);

        return deaths;
    }

    public int onedeathblock$getDeaths() {
        return getDataTracker().get(onedeathblock$DEATHS);
    }

    public void onedeathblock$setDeathMultiplier(double deathMultiplier) {
        this.onedeathblock$deathMultiplier = deathMultiplier;
    }

    public double onedeathblock$getDeathMultiplier() {
        return this.onedeathblock$deathMultiplier;
    }

    public List<DeathToast> onedeathblock$getDeathToasts() {
        if (this.onedeathblock$deathToasts == null) {
            this.onedeathblock$deathToasts = new ArrayList<>();
        }

        return this.onedeathblock$deathToasts;
    }

    public void onedeathblock$setDeathTicks(int deathTicks) {
        this.onedeathblock$deathTicks = deathTicks;
    }

    public AnimationState onedeathblock$getHeartRemovalState() {
        return onedeathblock$heartRemovalAnimationState;
    }

    public void onedeathblock$setHeartCarverStack(ItemStack heartCarver) {
        this.onedeathblock$heartCarver = heartCarver;
    }

    public void onedeathblock$setHeartRemovalTicks(int heartRemovalTicks) {
        this.onedeathblock$heartRemovalTicks = heartRemovalTicks;
    }

    public int onedeathblock$getHeartRemovalTicks() {
        return this.onedeathblock$heartRemovalTicks;
    }

    public void onedeathblock$setTntExplosionTicks(int tntExplosionTicks) {
        getDataTracker().set(onedeathblock$TNT_EXPLOSION, tntExplosionTicks);
    }

    public int onedeathblock$getTntExplosionTicks() {
        return getDataTracker().get(onedeathblock$TNT_EXPLOSION);
    }

    public int onedeathblock$getMeteorShakeTicks() {
        return this.onedeathblock$meteorShakeTicks;
    }

    public void onedeathblock$resetDamageDelay() {
        this.hurtTime = 0;
        this.lastDamageTaken = 0f;
    }

    public void onedeathblock$setDeathsDisplay(boolean deathsDisplay) {
        getDataTracker().set(onedeathblock$DEATHS_DISPLAY, deathsDisplay);
    }

    public boolean onedeathblock$hasDeathsDisplay() {
        return getDataTracker().get(onedeathblock$DEATHS_DISPLAY);
    }

    public void onedeathblock$setExplosivePlacing(boolean explosivePlacing) {
        getDataTracker().set(onedeathblock$EXPLOSIVE_PLACING, explosivePlacing);
    }

    public boolean onedeathblock$hasExplosivePlacing() {
        return getDataTracker().get(onedeathblock$EXPLOSIVE_PLACING);
    }

    public void onedeathblock$setShockedTicks(int shockedTicks) {
        getDataTracker().set(onedeathblock$SHOCKED, shockedTicks);
    }

    public int onedeathblock$getShockedTicks() {
        return getDataTracker().get(onedeathblock$SHOCKED);
    }
}

