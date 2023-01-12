package de.takacick.immortalmobs.registry.item;

import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalFireworkEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ImmortalCannon extends RangedWeaponItem implements ImmortalItem {
    public static final Predicate<ItemStack> IMMORTAL_PROJECTILES = stack -> stack.isOf(ItemRegistry.IMMORTAL_FIREWORK);

    public ImmortalCannon(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return IMMORTAL_PROJECTILES;
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (hand.equals(Hand.MAIN_HAND)) {

            boolean bl = !user.getAbilities().creativeMode;
            ItemStack stack = user.getArrowType(itemStack);
            if (stack.isEmpty() && !bl) {
                return TypedActionResult.fail(itemStack);
            }

            shoot(world, user, hand, itemStack, stack, 1f, user.isCreative(), 1.6f, 1.0f, 0);

            if (!user.getAbilities().creativeMode) {
                stack.decrement(1);
                if (stack.isEmpty()) {
                    user.getInventory().removeOne(stack);
                }
            }
            return TypedActionResult.consume(itemStack);
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eConcenrated §dImmortality §eshot"));
        tooltip.add(Text.of("§ewith §cdangerous §epower!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        ProjectileEntity projectileEntity;
        if (world.isClient) {
            return;
        }

        projectileEntity = new ImmortalFireworkEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - (double) 0.15f, shooter.getZ(), true);

        if (shooter instanceof CrossbowUser) {
            CrossbowUser crossbowUser = (CrossbowUser) ((Object) shooter);
            crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternion quaternion = new Quaternion(new Vec3f(vec3d), simulated, true);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            Vec3f vec3f = new Vec3f(vec3d2);
            vec3f.rotate(quaternion);
            projectileEntity.setVelocity(vec3f.getX(), vec3f.getY(), vec3f.getZ(), speed, divergence);
        }
        world.spawnEntity(projectileEntity);
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0f, soundPitch);
    }
}
