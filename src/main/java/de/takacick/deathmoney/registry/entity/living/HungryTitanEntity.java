package de.takacick.deathmoney.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.entity.living.brain.HungryTitanBrain;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HungryTitanEntity extends WardenEntity {

    public HungryTitanEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return HungryTitanBrain.create(this);
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
    public boolean tryAttack(Entity target) {
        this.world.sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
        this.playSound(SoundEvents.ENTITY_WARDEN_ATTACK_IMPACT, 10.0f, this.getSoundPitch());
        SonicBoomTask.cooldown(this, 40);

        boolean bl;
        int i;
        float f = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float g = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity) target).getGroup());
            g += (float) EnchantmentHelper.getKnockback(this);
        }
        if ((i = EnchantmentHelper.getFireAspect(this)) > 0) {
            target.setOnFireFor(i * 4);
        }
        if (bl = target.damage(DeathDamageSources.TITAN_PUNCH, f)) {
            if (g > 0.0f && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5f, MathHelper.sin(this.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(this.getYaw() * ((float) Math.PI / 180)));
                this.setVelocity(this.getVelocity().multiply(0.6, 1.0, 0.6));
            }
            if (target instanceof PlayerEntity playerEntity) {
                this.disablePlayerShield(playerEntity, this.getMainHandStack(), playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
            }
            this.applyDamageEffects(this, target);
            this.onAttacking(target);
        }
        return bl;
    }

    private void disablePlayerShield(PlayerEntity player, ItemStack mobStack, ItemStack playerStack) {
        if (!mobStack.isEmpty() && !playerStack.isEmpty() && mobStack.getItem() instanceof AxeItem && playerStack.isOf(Items.SHIELD)) {
            float f = 0.25f + (float) EnchantmentHelper.getEfficiency(this) * 0.05f;
            if (this.random.nextFloat() < f) {
                player.getItemCooldownManager().set(Items.SHIELD, 100);
                this.world.sendEntityStatus(player, EntityStatuses.BREAK_SHIELD);
            }
        }
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.equals(SoundEvents.ENTITY_WARDEN_TENDRIL_CLICKS) || sound.equals(SoundEvents.ENTITY_WARDEN_HEARTBEAT)) {
            return;
        }

        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }
}