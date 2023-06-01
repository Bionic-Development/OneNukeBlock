package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.item.FireSuit;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract void setHealth(float health);

    @Shadow protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if ((Object) this instanceof PlayerEntity playerEntity
                && playerEntity instanceof PlayerProperties playerProperties) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = playerEntity.getInventory().getStack(i);

                if (itemStack.isOf(ItemRegistry.INFINITE_DEATH_TOTEM)) {
                    this.velocityModified = false;
                    this.setHealth(1.0f);
                    int deaths = source instanceof DeathDamageSources.DeathDamageSource deathDamageSource ? deathDamageSource.getDeaths() : 1;
                    playerProperties.addDeaths(deaths, true);
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, DeathMoney.IDENTIFIER, 1);
                    info.setReturnValue(true);
                    break;
                }
            }
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At("HEAD"), cancellable = true)
    private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (stack.isOf(ItemRegistry.SWEET_BERRY_SUIT)
                || stack.isOf(ItemRegistry.FIRE_SUIT)
                || stack.isOf(ItemRegistry.CACTUS_ONESIE)) {
            info.setReturnValue(EquipmentSlot.CHEST);
        }
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V", ordinal = 2))
    private void damage(World instance, Entity entity, byte status, DamageSource source, float amount) {
        if (source == DeathDamageSources.SWEET_BERRY_SUIT
                || source == DeathDamageSources.FIRE_SUIT
                || source == DeathDamageSources.CACTUS_ONESIE) {
            return;
        }

        instance.sendEntityStatus(entity, status);
    }

    @Inject(method = "playEquipmentBreakEffects", at = @At("HEAD"), cancellable = true)
    private void playEquipmentBreakEffects(ItemStack stack, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.SWEET_BERRY_SUIT)) {
            if (!this.isSilent()) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_SWEET_BERRY_BUSH_BREAK, this.getSoundCategory(), 1f, 0.8f + this.world.random.nextFloat() * 0.4f, false);
            }
            ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SWEET_BERRY_BUSH.getDefaultState().with(SweetBerryBushBlock.AGE, 3));

            for (int i = 0; i < 5; ++i) {
                Vec3d vec3d = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3d = vec3d.rotateX(-this.getPitch() * ((float) Math.PI / 180));
                vec3d = vec3d.rotateY(-this.getYaw() * ((float) Math.PI / 180));
                double d = (double) (-this.random.nextFloat()) * 0.6 - 0.3;
                Vec3d vec3d2 = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
                vec3d2 = vec3d2.rotateX(-this.getPitch() * ((float) Math.PI / 180));
                vec3d2 = vec3d2.rotateY(-this.getYaw() * ((float) Math.PI / 180));
                vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
                this.world.addParticle(particleEffect, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
            }
            info.cancel();
        } else if (stack.isOf(ItemRegistry.FIRE_SUIT)) {
            if (!this.isSilent()) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, this.getSoundCategory(), 1f, 0.8f + this.world.random.nextFloat() * 0.4f, false);
            }

            for (int i = 0; i < 15; ++i) {
                Vec3d vec3d = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3d = vec3d.rotateX(-this.getPitch() * ((float) Math.PI / 180));
                vec3d = vec3d.rotateY(-this.getYaw() * ((float) Math.PI / 180));
                double d = (double) (-this.random.nextFloat()) * 0.6 - 0.3;
                Vec3d vec3d2 = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
                vec3d2 = vec3d2.rotateX(-this.getPitch() * ((float) Math.PI / 180));
                vec3d2 = vec3d2.rotateY(-this.getYaw() * ((float) Math.PI / 180));
                vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
                this.world.addParticle(ParticleTypes.SMOKE, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
            }
            info.cancel();
        } else if (stack.isOf(ItemRegistry.CACTUS_ONESIE)) {
            if (!this.isSilent()) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_WOOL_BREAK, this.getSoundCategory(), 1f, 0.8f + this.world.random.nextFloat() * 0.4f, false);
            }
            ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.CACTUS.getDefaultState());

            for (int i = 0; i < 5; ++i) {
                Vec3d vec3d = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3d = vec3d.rotateX(-this.getPitch() * ((float) Math.PI / 180));
                vec3d = vec3d.rotateY(-this.getYaw() * ((float) Math.PI / 180));
                double d = (double) (-this.random.nextFloat()) * 0.6 - 0.3;
                Vec3d vec3d2 = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
                vec3d2 = vec3d2.rotateX(-this.getPitch() * ((float) Math.PI / 180));
                vec3d2 = vec3d2.rotateY(-this.getYaw() * ((float) Math.PI / 180));
                vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
                this.world.addParticle(particleEffect, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
            }
            info.cancel();
        }
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "RETURN"))
    private void getEquipmentChanges(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> info) {
        if (info.getReturnValue() != null) {

            Map<EquipmentSlot, ItemStack> map = info.getReturnValue();

            for (EquipmentSlot equipmentSlot : map.keySet()) {
                if (!equipmentSlot.getType().equals(EquipmentSlot.Type.ARMOR)) {
                    continue;
                }

                ItemStack oldStack = this.getSyncedArmorStack(equipmentSlot);
                ItemStack itemStack = this.getEquippedStack(equipmentSlot);

                if (!(oldStack.getItem() instanceof FireSuit) && itemStack.getItem() instanceof FireSuit) {
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, DeathMoney.IDENTIFIER, 7);
                }
            }
        }
    }
}