package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(method = "onEntityCollision", at = @At(value = "HEAD"))
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info) {
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
