package de.takacick.everythinghearts.registry.item;

import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.entity.projectiles.HeartmondEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartmondBlaster extends Item {

    public HeartmondBlaster(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient) {
            Vec3d vec3d = user.getRotationVector();
            Vec3d pos = new Vec3d(user.getX(), user.getBodyY(0.71f), user.getZ()).add(vec3d);
            HeartmondEntity heartmondEntity = new HeartmondEntity(world, pos.getX(), pos.getY(), pos.getZ(), user);
            heartmondEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 2.6F, 1.0F);
            world.spawnEntity(heartmondEntity);

            world.playSound(null, heartmondEntity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.25F, 1.0F);
            double d = -MathHelper.sin(user.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(user.getYaw() * ((float) Math.PI / 180));
            ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART_EXPLOSION, user.getX() + d, user.getBodyY(0.5), user.getZ() + e, 0, d, 0.0, e, 0.0);
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eUses the power §clove §eas ammo to"));
        tooltip.add(Text.of("§edestroy everything!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
