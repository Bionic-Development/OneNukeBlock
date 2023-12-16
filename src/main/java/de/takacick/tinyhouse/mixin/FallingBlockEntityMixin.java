package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.PlayerProperties;
import de.takacick.tinyhouse.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow
    public abstract BlockState getBlockState();

    private boolean tinyhouse$hadOwner = false;

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo info) {
        if (this instanceof EntityProperties entityProperties
                && entityProperties.getBlockMagnetOwner() > -1) {
            Entity entity = getWorld().getEntityById(entityProperties.getBlockMagnetOwner());
            if (entity == null) {
                entityProperties.setBlockMagnetOwner(null);
            } else {
                if (entity instanceof PlayerProperties playerProperties) {
                    if (playerProperties.getBlockMagnetHolding() != getId()) {
                        entityProperties.setBlockMagnetOwner(null);
                        return;
                    }
                }

                this.tinyhouse$hadOwner = true;
                info.cancel();
            }
        } else if (!getWorld().isClient) {
            if (this.tinyhouse$hadOwner && getBlockState().isOf(Blocks.FIRE)) {
                dropItem(ItemRegistry.FIRE);
                this.discard();
            }
        }
    }
}