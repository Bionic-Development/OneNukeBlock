package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class InfinityGauntletDisabled extends Item {
    public InfinityGauntletDisabled(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if(!world.isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) world, user, ImagineAnything.IDENTIFIER, 2);
        }

        return super.use(world, user, hand);
    }
}
