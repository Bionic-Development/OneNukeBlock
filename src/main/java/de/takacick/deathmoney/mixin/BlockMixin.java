package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "onSteppedOn", at = @At(value = "HEAD"))
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo info) {
        if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.GRASS) || state.isOf(Blocks.TALL_GRASS)) {
            if (entity instanceof PlayerProperties playerProperties
                    && !entity.getWorld().isClient) {
                if (playerProperties.getGamerAllergyTicks() > 0) {
                    entity.damage(DeathDamageSources.GRASS_TOUCH, 5f);
                }
            }
        }
    }
}
