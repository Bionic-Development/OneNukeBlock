package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.registry.entity.projectiles.GiantAxeMeteorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ImaginedGiantAxeMeteor extends Item {

    public ImaginedGiantAxeMeteor(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.getEntityWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.NEUTRAL, 1f, 3f);
            GiantAxeMeteorEntity giantAxeMeteorEntity = new GiantAxeMeteorEntity(user.getWorld(), user.getX(), user.getY() + 75, user.getZ(), user);
            giantAxeMeteorEntity.setVelocity(0, -1, 0);
            world.spawnEntity(giantAxeMeteorEntity);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return super.use(world, user, hand);
    }
}
