package de.takacick.everythinghearts.registry.item;

import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.particles.ColoredParticleEffect;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Heartmond extends Item {

    public Heartmond(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity livingEntity && world.isClient) {
            if ((livingEntity.getMainHandStack().equals(stack)
                    || livingEntity.getOffHandStack().equals(stack))) {
                int index = livingEntity.getMainHandStack().equals(stack) ? 2 : 1;

                Vec3d vec3d = Vec3d.fromPolar(new Vec2f(0, livingEntity.bodyYaw)).multiply(0.35);
                double x = getHeadX(livingEntity, index) + vec3d.getX();
                double y = getHeadY(livingEntity, index) - (livingEntity.isSneaking() ? 0.4 : 0.1);
                double z = getHeadZ(livingEntity, index) + vec3d.getZ();

                float j = world.random.nextFloat() * 0.6f + 0.4f;

                double vx = livingEntity.getRandom().nextGaussian() * (double) 0.15f;
                double vy = livingEntity.getRandom().nextGaussian() * (double) 0.15f;
                double vz = livingEntity.getRandom().nextGaussian() * (double) 0.15f;

                livingEntity.world.addParticle(ParticleRegistry.HEART, x + vx * 0.05d,
                        y + vy * 0.15d,
                        z + vz * 0.05d, vx, vy, vz);
                livingEntity.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), x + livingEntity.getRandom().nextGaussian() * 0.05d,
                        y + livingEntity.getRandom().nextGaussian() * 0.15d,
                        z + livingEntity.getRandom().nextGaussian() * 0.05d, 0.0, 0.0, 0.0);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eDiamond + §cHeart §e= Heartmond!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    private double getHeadX(PlayerEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getX();
        }
        float f = (entity.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.cos(f);
        return entity.getX() + (double) g * 0.48;
    }

    private double getHeadY(PlayerEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getY() + 3.0;
        }
        return entity.getY() + 1.1;
    }

    private double getHeadZ(PlayerEntity entity, int headIndex) {
        if (headIndex <= 0) {
            return entity.getZ();
        }
        float f = (entity.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.sin(f);
        return entity.getZ() + (double) g * 0.48;
    }
}
