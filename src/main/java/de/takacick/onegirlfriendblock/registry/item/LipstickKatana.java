package de.takacick.onegirlfriendblock.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.access.LivingProperties;
import de.takacick.onegirlfriendblock.access.PlayerProperties;
import de.takacick.onegirlfriendblock.registry.EffectRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LipstickKatana extends SwordItem {

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public LipstickKatana(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 6f, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (target instanceof LivingProperties livingProperties) {
            livingProperties.setLipstickStrength(livingProperties.getLipstickStrength() + 0.334f);
        }

        BionicUtils.sendEntityStatus(target.getWorld(), target, OneGirlfriendBlock.IDENTIFIER, 3);
        BionicUtils.sendEntityStatus(target.getWorld(), target, OneGirlfriendBlock.IDENTIFIER, 4);
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 100, 0, false, false, true));

        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient) {
            user.setPose(EntityPose.SPIN_ATTACK);
            if (user instanceof PlayerEntity playerEntity) {
                playerEntity.useRiptide(12);
                ((PlayerProperties) playerEntity).setLipstickRiptideTicks(13);
            }

            Vec3d vec3d = user.getPos().add(0, user.getHeight() / 2d, 0);
            Vec3d vec3d2 = user.getRotationVec(0).normalize().multiply(0.75);

            user.setVelocity(vec3d2.multiply(2.25));
            user.velocityModified = true;
            user.velocityDirty = true;
            world.playSoundFromEntity(null, user, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1, 2);
            BionicUtils.sendEntityStatus(world, user, OneGirlfriendBlock.IDENTIFIER, 5);

            for (int x = 0; x < 8; x++) {
                BlockState blockState = world.getBlockState(BlockPos.ofFloored(vec3d.add(vec3d2)));
                if (!(blockState.isAir() || blockState.isReplaceable() || blockState.getBlock() instanceof FluidBlock)) {
                    break;
                }
                user.fallDistance = 0;
                vec3d = vec3d.add(vec3d2);

                world.playSound(null, BlockPos.ofFloored(user.getX(), user.getY(), user.getZ()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                world.playSound(null, BlockPos.ofFloored(user.getX(), user.getY(), user.getZ()), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                world.getOtherEntities(user, new Box(vec3d.getX() - 4, vec3d.getY() - 4, vec3d.getZ() - 4, vec3d.getX() + 4, vec3d.getY() + 4, vec3d.getZ() + 4)).forEach(entity -> {
                    if (entity instanceof LivingEntity target && target.isPartOfGame()
                            && target.damage(world.getDamageSources().mobAttack(user), 7f)) {
                        BionicUtils.sendEntityStatus(user.getWorld(), user, OneGirlfriendBlock.IDENTIFIER, 6);

                        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 100, 0, false, false, true));
                        if (target instanceof LivingProperties livingProperties) {
                            livingProperties.setLipstickStrength(livingProperties.getLipstickStrength() + 0.334f);
                        }
                    }
                });
            }
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eA blade that aids you in battle and"));
        tooltip.add(Text.of("§ebeauty!"));

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
