package de.takacick.tinyhouse.registry.item;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.PlayerProperties;
import de.takacick.tinyhouse.server.BlockmagnetHitUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockMagnet extends Item {

    public static final RegistryKey<DamageType> BLOCK_MAGNET = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(TinyHouse.MOD_ID, "block_magnet"));

    public BlockMagnet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user instanceof PlayerProperties playerProperties && playerProperties.getBlockMagnetHolding() > 0) {
            Entity entity = world.getEntityById(playerProperties.getBlockMagnetHolding());

            if (entity instanceof EntityProperties entityProperties && entityProperties.getBlockMagnetOwner() == user.getId()) {
                entityProperties.setBlockMagnetOwner(null);
            }
            playerProperties.setBlockMagnetHolding(null);
        } else {

            if(user.getInventory().count(Items.REDSTONE) <= 0 && !user.isCreative()) {
                return TypedActionResult.fail(itemStack);
            }

            BlockHitResult blockHitResult = null;
            EntityHitResult entityHitResult = null;
            block9:
            {
                block8:
                {
                    HitResult hitResult = this.getHitResult(user);

                    if (hitResult instanceof EntityHitResult entityResult
                            && entityResult.getEntity() instanceof LivingEntity) {
                        entityHitResult = entityResult;
                        break block9;
                    }

                    if (!(hitResult instanceof BlockHitResult)) break block8;
                    blockHitResult = (BlockHitResult) hitResult;
                    if (hitResult.getType() == HitResult.Type.BLOCK) break block9;
                }
                return TypedActionResult.fail(itemStack);
            }

            if (entityHitResult != null) {

                if (entityHitResult.getEntity() != null) {
                    if (!world.isClient) {
                        ((PlayerProperties) user).setBlockMagnetHolding(entityHitResult.getEntity());
                        ((EntityProperties) entityHitResult.getEntity()).setBlockMagnetOwner(user);
                    }
                    return TypedActionResult.success(itemStack);
                }
            } else if (blockHitResult != null) {

                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                if (!blockState.isAir() && !blockState.hasBlockEntity()) {
                    if (!world.isClient) {
                        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, blockPos, blockState);
                        ((PlayerProperties) user).setBlockMagnetHolding(fallingBlockEntity);
                        ((EntityProperties) fallingBlockEntity).setBlockMagnetOwner(user);
                    }
                    return TypedActionResult.success(itemStack);
                }
            }
        }

        return TypedActionResult.pass(itemStack);
    }

    private HitResult getHitResult(PlayerEntity user) {
        return BlockmagnetHitUtil.getCollision(user, user.isCreative() ? 7 : 4.2);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eUses §9magnetization §eto pick up"));
        tooltip.add(Text.of("§eanything!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
