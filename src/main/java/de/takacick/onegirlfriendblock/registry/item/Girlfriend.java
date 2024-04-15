package de.takacick.onegirlfriendblock.registry.item;

import de.takacick.onegirlfriendblock.registry.EntityRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Girlfriend extends Item {

    public Girlfriend(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (((HitResult) hitResult).getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return TypedActionResult.success(itemStack);
        }

        Vec3d pos = Vec3d.ofBottomCenter(hitResult.getBlockPos()).add(0, 1.001f, 0);
        var optional = Optional.ofNullable(EntityRegistry.GIRLFRIEND.create(world));

        if (optional.isEmpty()) {
            return TypedActionResult.pass(itemStack);
        }

        float yaw = user.getYaw() - 180f;

        Entity entity = optional.get();
        entity.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, 0.0f);
        entity.setBodyYaw(yaw);
        entity.setHeadYaw(yaw);
        entity.prevYaw = yaw;
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.prevBodyYaw = yaw;
            livingEntity.prevHeadYaw = yaw;
        }
        serverWorld.spawnEntityAndPassengers(entity);

        world.playSoundFromEntity(null, entity, SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.PLAYERS, 1f, 1f);

        if (entity instanceof MobEntity mobEntity) {
            mobEntity.playAmbientSound();
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eCraft you §dGirlfriend §eto life!"));
        tooltip.add(Text.of("§9§oFreee meee pls! D:"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
