package de.takacick.upgradebody.registry.item;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.entity.custom.UpgradeShopPortalEntity;
import de.takacick.upgradebody.server.commands.UpgradeBodyShopCommand;
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

public class UpradeShopPortal extends Item {

    public UpradeShopPortal(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Vec3d vec3d = user.getRotationVector().multiply(2, 0, 2).add(user.getX(), user.getY(), user.getZ());

        if (!world.isClient) {
            if (UpgradeBodyShopCommand.UPGRADE_SHOP_PORTAL != null && user.distanceTo(UpgradeBodyShopCommand.UPGRADE_SHOP_PORTAL) <= 70) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }

            UpgradeShopPortalEntity upgradeShopPortalEntity = new UpgradeShopPortalEntity(EntityRegistry.UPGRADE_SHOP_PORTAL, world);
            upgradeShopPortalEntity.setPos(vec3d.getX(), vec3d.getY() + 0.2, vec3d.getZ());
            upgradeShopPortalEntity.setYaw(-user.getYaw());
            world.spawnEntity(upgradeShopPortalEntity);
            BionicUtils.sendEntityStatus((ServerWorld) world, upgradeShopPortalEntity, UpgradeBody.IDENTIFIER, 1);

            ((PlayerProperties) user).setUpgradeShopPortal(upgradeShopPortalEntity);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Teleports you to the §eUpgrade §9shop!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}