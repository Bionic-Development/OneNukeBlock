package de.takacick.onegirlfriendblock.mixin;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.access.LivingProperties;
import de.takacick.onegirlfriendblock.access.PlayerProperties;
import de.takacick.onegirlfriendblock.registry.EffectRegistry;
import de.takacick.onegirlfriendblock.registry.item.LipstickKatana;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "onegirlfriendblock$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique
    private static final TrackedData<Boolean> onegirlfriendblock$ONE_GIRLFRIEND_BLOCK = BionicDataTracker.registerData(new Identifier(OneGirlfriendBlock.MOD_ID, "one_girlfriend_block"), TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> onegirlfriendblock$LIPSTICK_RIPTIDE = BionicDataTracker.registerData(new Identifier(OneGirlfriendBlock.MOD_ID, "lipstick_riptide"), TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private int onegirlfriendblock$lipstickRiptideTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onegirlfriendblock$ONE_GIRLFRIEND_BLOCK, false);
        getDataTracker().startTracking(onegirlfriendblock$LIPSTICK_RIPTIDE, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (this.onegirlfriendblock$lipstickRiptideTicks > 0) {
                onegirlfriendblock$setLipstickRiptideTicks(this.riptideTicks > 0 ? this.onegirlfriendblock$lipstickRiptideTicks - 1 : 0);

                if (onegirlfriendblock$hasLipstickRiptide()) {
                    BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 5);
                }

                this.fallDistance = 0;

                this.getWorld().playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (this.onegirlfriendblock$lipstickRiptideTicks % 3 == 0) {
                    this.getEntityWorld().playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 0.25F, 3.0F);
                }

                this.getWorld().getOtherEntities(this, new Box(getX() - 4, getY() - 4, getZ() - 4, getX() + 4, getY() + 4, getZ() + 4)).forEach(entity -> {
                    if (entity instanceof LivingEntity target
                            && target.isPartOfGame()
                            && target.damage(getWorld().getDamageSources().mobAttack(this), 7f)) {
                        BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 6);
                        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 100, 0, false, false, true));

                        if (target instanceof LivingProperties livingProperties) {
                            livingProperties.setLipstickStrength(livingProperties.getLipstickStrength() + 0.334f);
                        }
                    }
                });
            }
        }
    }

    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    public void spawnSweepAttackParticles(CallbackInfo info) {
        if (getMainHandStack().getItem() instanceof LipstickKatana) {
            info.cancel();
        }
    }
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (onegirlfriendblock$hasOneGirlfriendBlock()) {
            nbt.putBoolean("onegirlfriendblock$onegirlfriendblock", onegirlfriendblock$hasOneGirlfriendBlock());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("onegirlfriendblock$onegirlfriendblock", NbtElement.BYTE_TYPE)) {
            getDataTracker().set(onegirlfriendblock$ONE_GIRLFRIEND_BLOCK, nbt.getBoolean("onegirlfriendblock$onegirlfriendblock"));
        }
    }

    public void onegirlfriendblock$setOneGirlfriendBlock(boolean oneGirlfriendBlock) {
        getDataTracker().set(onegirlfriendblock$ONE_GIRLFRIEND_BLOCK, oneGirlfriendBlock);
    }

    public boolean onegirlfriendblock$hasOneGirlfriendBlock() {
        return getDataTracker().get(onegirlfriendblock$ONE_GIRLFRIEND_BLOCK);
    }

    public void onegirlfriendblock$setLipstickRiptideTicks(int lipstickRiptideTicks) {
        this.onegirlfriendblock$lipstickRiptideTicks = lipstickRiptideTicks;
        getDataTracker().set(onegirlfriendblock$LIPSTICK_RIPTIDE, this.onegirlfriendblock$lipstickRiptideTicks > 0);
    }

    public boolean onegirlfriendblock$hasLipstickRiptide() {
        return getDataTracker().get(onegirlfriendblock$LIPSTICK_RIPTIDE);
    }
}

