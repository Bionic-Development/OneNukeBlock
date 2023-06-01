package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.commands.DeathShopCommand;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.entity.custom.DeathShopPortalEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeathShopPortal extends Item {

    public DeathShopPortal(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Vec3d vec3d = user.getRotationVector().multiply(2, 0, 2).add(user.getX(), user.getY(), user.getZ());
        boolean foundValidPos = false;

        for (int y = 2; y >= -3; y--) {
            Vec3d vec3d1 = vec3d.add(0, y, 0);
            BlockState blockState = world.getBlockState(new BlockPos(vec3d1));
            if (blockState.getMaterial().blocksMovement() && !world.getBlockState(new BlockPos(vec3d1).add(0, 1, 0)).getMaterial().blocksMovement()) {
                vec3d = new Vec3d(vec3d.getX(), (int) vec3d1.getY() + blockState.getCollisionShape(world, new BlockPos(vec3d1)).getMax(Direction.Axis.Y), vec3d.getZ());
                foundValidPos = true;
                break;
            }
        }

        if (!foundValidPos) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if (!world.isClient) {
            if (DeathShopCommand.DEATH_SHOP_PORTAL != null && user.distanceTo(DeathShopCommand.DEATH_SHOP_PORTAL) <= 70) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }

            DeathShopPortalEntity deathShopPortalEntity = new DeathShopPortalEntity(EntityRegistry.DEATH_SHOP_PORTAL, world);
            deathShopPortalEntity.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
            deathShopPortalEntity.setYaw(-user.getYaw());
            world.spawnEntity(deathShopPortalEntity);
            BionicUtils.sendEntityStatus((ServerWorld) world, deathShopPortalEntity, DeathMoney.IDENTIFIER, 10);

            ((PlayerProperties) user).setDeathShopPortal(deathShopPortalEntity);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Enter to spend your §cdeaths§7!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}