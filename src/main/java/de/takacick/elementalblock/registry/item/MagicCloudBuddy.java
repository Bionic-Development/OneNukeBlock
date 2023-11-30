package de.takacick.elementalblock.registry.item;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.EntityRegistry;
import de.takacick.elementalblock.registry.entity.living.MagicCloudBuddyEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicCloudBuddy extends Item {

    public MagicCloudBuddy(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        Vec3d vec3d = user.getRotationVector().multiply(2, 1, 2).add(user.getX(), user.getEyeY(), user.getZ());
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            EntityType<MagicCloudBuddyEntity> entityType = EntityRegistry.MAGIC_CLOUD_BUDDY;
            MagicCloudBuddyEntity magicCloudBuddyEntity = entityType.spawnFromItemStack((ServerWorld) world, itemStack, user, BlockPos.ofFloored(vec3d), SpawnReason.SPAWN_EGG, true, false);

            if (magicCloudBuddyEntity != null) {
                if(user.isCreative()) {
                    itemStack.decrement(1);
                }

                BionicUtils.sendEntityStatus((ServerWorld) world, magicCloudBuddyEntity, OneElementalBlock.IDENTIFIER, 2);
                world.playSoundFromEntity(null, magicCloudBuddyEntity, SoundEvents.ENTITY_PHANTOM_FLAP,
                        SoundCategory.AMBIENT, 1f, 1f + world.getRandom().nextFloat() * 0.2f);
                user.swingHand(hand, true);
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("Fluffy §efriend you can ride!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
