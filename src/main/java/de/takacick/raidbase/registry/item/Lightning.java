package de.takacick.raidbase.registry.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class Lightning extends Item {

    public Lightning(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult hitResult = user.raycast(200, 0, true);
        if (!hitResult.getType().equals(HitResult.Type.MISS)) {
            world.playSoundFromEntity(null, user, SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.PLAYERS, 0.8f, 1f);
            LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightningEntity.setPos(hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ());
            world.spawnEntity(lightningEntity);
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }

        return super.use(world, user, hand);
    }
}
