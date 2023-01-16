package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.projectiles.GiantNetheriteFeatherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ImaginedGiantNetheriteFeather extends Item {

    public ImaginedGiantNetheriteFeather(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Vec3d pos = user.getPos().add(0, user.getHeight() / 2, 0).add(user.getRotationVector());
            user.getEntityWorld().playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 1f, 1f);

            Vec3d vec3d = getRotationVector(user.getPitch(), user.getYaw() + 90);

            if (user.getPitch() > 80) {
                vec3d = getRotationVector(40, user.getYaw() + 90);
            } else if (user.getPitch() < -80) {
                vec3d = getRotationVector(-40, user.getYaw() + 90);
            }

            for (int i = 0; i < 13; i++) {
                Vec3d vec3d1 = vec3d.multiply(world.getRandom().nextInt(4) * (world.getRandom().nextDouble() >= 0.5 ? -1 : 1));

                GiantNetheriteFeatherEntity giantNetheriteFeatherEntity = new GiantNetheriteFeatherEntity(EntityRegistry.GIANT_NETHERITE_FEATHER, world, user);
                giantNetheriteFeatherEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F,
                        0.75F, 0.5F);
                giantNetheriteFeatherEntity.setPos(pos.getX() + vec3d1.getX(), pos.getY() + ((world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 2), pos.getZ() + vec3d1.getZ());
                world.spawnEntity(giantNetheriteFeatherEntity);
            }
        }
        return super.use(world, user, hand);
    }

    protected final Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
}
