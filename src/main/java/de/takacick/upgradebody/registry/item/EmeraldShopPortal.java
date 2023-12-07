package de.takacick.upgradebody.registry.item;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.upgradebody.server.commands.EmeraldShopCommand;
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

public class EmeraldShopPortal extends Item {

    public EmeraldShopPortal(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Vec3d vec3d = user.getRotationVector().multiply(2, 0, 2).add(user.getX(), user.getY(), user.getZ());
        boolean foundValidPos = false;

        for (int y = 2; y >= -3; y--) {
            Vec3d vec3d1 = vec3d.add(0, y, 0);
            BlockState blockState = world.getBlockState(BlockPos.ofFloored(vec3d1));
            if (blockState.blocksMovement() && !world.getBlockState(BlockPos.ofFloored(vec3d1).add(0, 1, 0)).blocksMovement()) {
                vec3d = new Vec3d(vec3d.getX(), (int) vec3d1.getY() + blockState.getCollisionShape(world, BlockPos.ofFloored(vec3d1)).getMax(Direction.Axis.Y), vec3d.getZ());
                foundValidPos = true;
                break;
            }
        }

        if (!foundValidPos) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if (!world.isClient) {
            if (EmeraldShopCommand.EMERALD_SHOP_PORTAL != null && user.distanceTo(EmeraldShopCommand.EMERALD_SHOP_PORTAL) <= 70) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }

            EmeraldShopPortalEntity emeraldShopPortalEntity = new EmeraldShopPortalEntity(EntityRegistry.EMERALD_SHOP_PORTAL, world);
            emeraldShopPortalEntity.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
            emeraldShopPortalEntity.setYaw(-user.getYaw());
            world.spawnEntity(emeraldShopPortalEntity);
            BionicUtils.sendEntityStatus((ServerWorld) world, emeraldShopPortalEntity, UpgradeBody.IDENTIFIER, 8);

            ((PlayerProperties) user).setEmeraldShopPortal(emeraldShopPortalEntity);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Teleports you to the §aEmerald §9shop!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}