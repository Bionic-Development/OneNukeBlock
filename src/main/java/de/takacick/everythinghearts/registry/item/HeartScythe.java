package de.takacick.everythinghearts.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartScytheEntity;
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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HeartScythe extends SwordItem {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public HeartScythe(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 9.5, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            double d = -MathHelper.sin(user.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(user.getYaw() * ((float) Math.PI / 180));
            if (user.world instanceof ServerWorld) {
                ((ServerWorld) user.world).spawnParticles(ParticleRegistry.HEART_SWEEP_ATTACK, user.getX() + d, user.getBodyY(0.5) + user.getRotationVector().getY() * 0.75, user.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            user.world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, user.getSoundCategory(), 1.0f, 1f);
            user.world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, user.getSoundCategory(), 0.2f, 1f);

            HeartScytheEntity heartScytheEntity = new HeartScytheEntity(EntityRegistry.HEART_SCYTHE, world, user);
            heartScytheEntity.setPos(user.getX(), user.getY() + 2, user.getZ());
            heartScytheEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.25F, 0.5F);
            world.spawnEntity(heartScytheEntity);
        }
        return TypedActionResult.success(user.getStackInHand(hand), true);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
}
