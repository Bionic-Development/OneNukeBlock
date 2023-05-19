package de.takacick.heartmoney.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.entity.projectiles.LifeStealScytheEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LifeStealScythe extends SwordItem {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public LifeStealScythe(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 9.5f, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if (!world.isClient) {
            user.getItemCooldownManager().set(this, 30);

            double d = -MathHelper.sin(user.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(user.getYaw() * ((float) Math.PI / 180));
            if (user.world instanceof ServerWorld) {
                ((ServerWorld) user.world).spawnParticles(ParticleRegistry.HEART_SWEEP_ATTACK, user.getX() + d, user.getBodyY(0.5) + user.getRotationVector().getY() * 0.75, user.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            user.world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, user.getSoundCategory(), 1.0f, 1f);
            user.world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, user.getSoundCategory(), 0.2f, 1f);

            LifeStealScytheEntity lifeStealScytheEntity = new LifeStealScytheEntity(EntityRegistry.LIFE_STEAL_SCYTHE, world, user);
            lifeStealScytheEntity.setPos(user.getX(), user.getY() + 1.3, user.getZ());
            lifeStealScytheEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.75F, 0.9F);
            world.spawnEntity(lifeStealScytheEntity);
        }
        return TypedActionResult.success(user.getStackInHand(hand), true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eWhen thrown, you steal your enemies"));
        tooltip.add(Text.of("§estrength and health!"));
        tooltip.add(Text.of("§c§oHigh Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
}
