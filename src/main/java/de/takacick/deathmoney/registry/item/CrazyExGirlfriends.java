package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.entity.living.CrazyExGirlfriendEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrazyExGirlfriends extends Item {

    public CrazyExGirlfriends(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            BlockPos pos = user.getBlockPos();
            for (int i = 0; i < world.getRandom().nextBetween(4, 10); i++) {
                CrazyExGirlfriendEntity crazyExGirlfriendEntity = EntityRegistry.CRAZY_EX_GIRLFRIEND.create((ServerWorld) world, null,
                        null, null, pos,
                        SpawnReason.EVENT, false, false);
                if (crazyExGirlfriendEntity != null) {

                    if (teleportTo(crazyExGirlfriendEntity, user, crazyExGirlfriendEntity.getRandom())) {
                        world.spawnEntity(crazyExGirlfriendEntity);
                        BionicUtils.sendEntityStatus((ServerWorld) world, crazyExGirlfriendEntity, DeathMoney.IDENTIFIER, 9);
                    }
                }
            }
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.damage(1, user, playerEntity -> {
            });
        }
        return TypedActionResult.success(itemStack, false);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§cRUN §7and DON'T look back!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public static boolean teleportTo(Entity entity, Entity target, Random random) {
        Vec3d vec3d = new Vec3d(entity.getX() - target.getX(), entity.getBodyY(0.5) - target.getEyeY(), entity.getZ() - target.getZ());
        vec3d = vec3d.normalize();
        for(int i = 0; i < 16; i++) {
            double e = entity.getX() + (random.nextDouble() - 0.5) * 8.0 - vec3d.x * 6.0;
            double f = entity.getY() + random.nextDouble() * 4;
            double g = entity.getZ() + (random.nextDouble() - 0.5) * 8.0 - vec3d.z * 6.0;
            boolean teleported = teleportTo(entity, target, e,f,g);

            if(teleported) {
                return true;
            }
        }

        return false;
    }

    private static boolean teleportTo(Entity entity, Entity target, double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > entity.world.getBottomY() && !entity.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = entity.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }

        entity.setPos(mutable.getX() + 0.5, mutable.getY() + 1.02, mutable.getZ() + 0.5);
        entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, target.getEyePos());
        entity.refreshPositionAndAngles(mutable.getX() + 0.5, mutable.getY() + 1.02, mutable.getZ() + 0.5,
                entity.getYaw(), entity.getPitch());

        return true;
    }

}
