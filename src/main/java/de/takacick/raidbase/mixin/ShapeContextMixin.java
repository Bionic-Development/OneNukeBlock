package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.LivingProperties;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapeContext.class)
public interface ShapeContextMixin  {

    @Inject(method = "of", at = @At("HEAD"), cancellable = true)
    private static void of(Entity entity, CallbackInfoReturnable<ShapeContext> info) {
        if(entity == null) {
            info.setReturnValue(new EntityShapeContext(false, -1.7976931348623157E308, ItemStack.EMPTY, fluidState -> false, null){

                @Override
                public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) {
                    return defaultValue;
                }
            });
        }
    }
}