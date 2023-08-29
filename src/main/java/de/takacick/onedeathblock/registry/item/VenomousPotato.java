package de.takacick.onedeathblock.registry.item;

import de.takacick.onedeathblock.OneDeathBlockClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VenomousPotato extends Item {

    public VenomousPotato(Settings settings) {
        super(settings.food(new FoodComponent.Builder().hunger(8).saturationModifier(0.8f)
                .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 0), 1f)
                .alwaysEdible().build()));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity livingEntity) {
            if ((livingEntity.getMainHandStack().equals(stack)
                    || livingEntity.getOffHandStack().equals(stack))) {
                if (world.isClient) {
                    if (world.getRandom().nextDouble() <= 0.3) {
                        int index = livingEntity.getMainHandStack().equals(stack) ? 2 : 1;

                        Vec3d vec3d = Vec3d.fromPolar(new Vec2f(0, livingEntity.bodyYaw)).multiply(0.35);
                        double x = getHeadX(livingEntity, index) + vec3d.getX();
                        double y = getHeadY(livingEntity, index) - (livingEntity.isSneaking() ? 0.4 : 0.1);
                        double z = getHeadZ(livingEntity, index) + vec3d.getZ();

                        OneDeathBlockClient.addPotion(new Vec3d(x, y, z), StatusEffects.WITHER.getColor(), 1);
                    }
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static double getHeadX(PlayerEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getX();
        }
        float f = (entity.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.cos(f);
        return entity.getX() + (double) g * 0.45;
    }

    public static double getHeadY(PlayerEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getY() + 2.9;
        }
        return entity.getY() + 1.0;
    }

    public static double getHeadZ(PlayerEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getZ();
        }
        float f = (entity.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.sin(f);
        return entity.getZ() + (double) g * 0.45;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eWhy would anyone eat this?"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
