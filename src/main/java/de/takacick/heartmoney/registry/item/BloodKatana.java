package de.takacick.heartmoney.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.access.PlayerProperties;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
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

public class BloodKatana extends SwordItem {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public BloodKatana(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 8.5f, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) {
            for (int i = 0; i < 2; i++) {
                world.addParticle(ParticleRegistry.BLOOD_PORTAL, user.getX(), user.getBodyY(1) + 0.2, user.getZ(),
                        (float) world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8);
            }

            if (remainingUseTicks % 4 == 0) {
                world.playSound(user.getX(), user.getBodyY(0.5), user.getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 0.25F, 3.0F, false);
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient) {
            user.setPose(EntityPose.SPIN_ATTACK);
            if (user instanceof PlayerEntity playerEntity) {
                playerEntity.useRiptide(12);
                ((PlayerProperties) playerEntity).setBloodRiptideTicks(13);
            }

            Vec3d vec3d = user.getPos().add(0, user.getHeight() / 2d, 0);
            Vec3d vec3d2 = user.getRotationVec(0).normalize().multiply(0.75);

            user.setVelocity(vec3d2.multiply(2.25));
            user.velocityModified = true;
            user.velocityDirty = true;
            world.playSoundFromEntity(null, user, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1, 2);
            BionicUtils.sendEntityStatus((ServerWorld) world, user, HeartMoney.IDENTIFIER, 13);

            for (int x = 0; x < 8; x++) {
                BlockState blockState = world.getBlockState(new BlockPos(vec3d.add(vec3d2)));
                if (!(blockState.isAir() || blockState.getMaterial().isReplaceable() || blockState.getBlock() instanceof FluidBlock)) {
                    break;
                }
                user.fallDistance = 0;
                vec3d = vec3d.add(vec3d2);

                world.playSound(null, new BlockPos(user.getX(), user.getY(), user.getZ()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                world.playSound(null, new BlockPos(user.getX(), user.getY(), user.getZ()), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                world.getOtherEntities(user, new Box(vec3d.getX() - 4, vec3d.getY() - 4, vec3d.getZ() - 4, vec3d.getX() + 4, vec3d.getY() + 4, vec3d.getZ() + 4)).forEach(entity -> {
                    if (entity instanceof LivingEntity target && target.isPartOfGame() && target.damage(DamageSource.mob(user), 10.5f)) {
                        BionicUtils.sendEntityStatus((ServerWorld) user.getWorld(), user, HeartMoney.IDENTIFIER, 14);

                        for (int i = 0; i < world.getRandom().nextBetween(10, 30); i++) {
                            ItemEntity itemEntity = new ItemEntity(world, target.getX(), target.getBodyY(0.5), target.getZ(), ItemRegistry.HEART.getDefaultStack(),
                                    world.getRandom().nextGaussian() * 0.3, world.getRandom().nextDouble() * 0.4, world.getRandom().nextGaussian() * 0.3);
                            itemEntity.setPickupDelay(5);
                            world.spawnEntity(itemEntity);
                        }
                    }
                });
            }
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eShare enough to slice a heart in"));
        tooltip.add(Text.of("§ehalf, and not leave it damaged..."));
        tooltip.add(Text.of("§5§oEpic Tier"));

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
