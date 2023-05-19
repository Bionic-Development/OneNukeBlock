package de.takacick.heartmoney.registry.item;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.access.PlayerProperties;
import de.takacick.heartmoney.commands.HeartShopCommand;
import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.entity.custom.HeartShopPortalEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartShopPortal extends Item {

    public HeartShopPortal(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (HeartShopCommand.HEART_SHOP_PORTAL != null && user.distanceTo(HeartShopCommand.HEART_SHOP_PORTAL) <= 70) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }

            Vec3d vec3d = user.getRotationVector().multiply(2, 0, 2).add(user.getX(), user.getY(), user.getZ());

            HeartShopPortalEntity heartShopPortalEntity = new HeartShopPortalEntity(EntityRegistry.HEART_SHOP_PORTAL, world);
            heartShopPortalEntity.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
            heartShopPortalEntity.setYaw(-user.getYaw());
            world.spawnEntity(heartShopPortalEntity);
            BionicUtils.sendEntityStatus((ServerWorld) world, heartShopPortalEntity, HeartMoney.IDENTIFIER, 4);

            ((PlayerProperties) user).setHeartShopPortal(heartShopPortalEntity);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eLeads you to the Heart Shop!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}