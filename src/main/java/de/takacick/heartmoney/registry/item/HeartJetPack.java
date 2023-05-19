package de.takacick.heartmoney.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.particles.ColoredParticleEffect;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HeartJetPack extends Item {

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public HeartJetPack(Settings settings) {
        super(settings);

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D");
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", 10d, EntityAttributeModifier.Operation.ADDITION));

        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
        if (itemStack2.isEmpty()) {
            user.equipStack(equipmentSlot, itemStack.copy());
            if (!world.isClient()) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            itemStack.setCount(0);
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    @Nullable
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).equals(stack)) {
                if (livingEntity.isSneaking()) {
                    livingEntity.fallDistance = 0;
                    Vec3d velocity = livingEntity.getRotationVector().add(0, 0.6, 0).multiply(2);
                    if (velocity.getY() <= 0) {
                        velocity = velocity.multiply(1, -0.2, 1);
                    }
                    livingEntity.setVelocity(velocity);
                    livingEntity.velocityModified = true;
                    livingEntity.velocityDirty = true;

                    for (int index = 1; index < 3; index++) {
                        Vec3d vec3d = Vec3d.fromPolar(new Vec2f(0, livingEntity.bodyYaw)).multiply(-0.45);
                        double x = getHeadX(livingEntity, index) + vec3d.getX();
                        double y = getHeadY(livingEntity, index);
                        double z = getHeadZ(livingEntity, index) + vec3d.getZ();

                        List<Vec3f> colors = Arrays.asList(new Vec3f(Vec3d.unpackRgb(0xB13F00)), new Vec3f(Vec3d.unpackRgb(0xDFA21B)));

                        if (world.getRandom().nextDouble() <= 0.5) {
                            livingEntity.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, colors.get(world.getRandom().nextInt(colors.size()))), x + livingEntity.getRandom().nextGaussian() * (double) 0.05f,
                                    y + livingEntity.getRandom().nextGaussian() * (double) 0.15f,
                                    z + livingEntity.getRandom().nextGaussian() * (double) 0.05f, vec3d.getX(), -1.7, vec3d.getZ());
                        } else {
                            livingEntity.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_HEART, colors.get(world.getRandom().nextInt(colors.size()))), x + livingEntity.getRandom().nextGaussian() * (double) 0.05f,
                                    y + livingEntity.getRandom().nextGaussian() * (double) 0.15f,
                                    z + livingEntity.getRandom().nextGaussian() * (double) 0.05f, vec3d.getX(), -1.7, vec3d.getZ());
                        }
                    }

                    if (livingEntity.age % 2 == 0) {
                        livingEntity.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.2f, 2f);
                    }
                    Vec3d vec3d = livingEntity.getVelocity();
                    vec3d = new Vec3d(vec3d.getX(), Math.min(vec3d.getY() + 0.13d, 0.34d), vec3d.getZ());
                    livingEntity.setVelocity(vec3d);
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private static double getHeadX(LivingEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getX();
        }
        float f = (entity.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.cos(f);
        return entity.getX() + (double) g * 0.21;
    }

    private static double getHeadY(LivingEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getY() + 3.0;
        }
        return entity.getY() + 0.9;
    }

    private static double getHeadZ(LivingEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getZ();
        }
        float f = (entity.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.sin(f);
        return entity.getZ() + (double) g * 0.21;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.CHEST) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eFueled with the power of love!"));
        tooltip.add(Text.of("§5§oEpic Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
