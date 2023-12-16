package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.LivingProperties;
import de.takacick.tinyhouse.access.PlayerProperties;
import de.takacick.tinyhouse.registry.ItemRegistry;
import de.takacick.tinyhouse.registry.item.BlockMagnet;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "tinyhouse$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract PlayerInventory getInventory();

    private static final TrackedData<Integer> tinyhouse$BLOCK_MAGNET = BionicDataTracker.registerData(new Identifier(TinyHouse.MOD_ID, "block_magnet"), TrackedDataHandlerRegistry.INTEGER);
    private int tinyhouse$blockMagnetTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (tinyhouse$getBlockMagnetHolding() > 0) {
                boolean holding = getMainHandStack().isOf(ItemRegistry.BLOCK_MAGNET) || getOffHandStack().isOf(ItemRegistry.BLOCK_MAGNET);
                Entity entity = getWorld().getEntityById(tinyhouse$getBlockMagnetHolding());

                if (entity == null || entity.isRemoved() || !holding) {
                    if (entity instanceof EntityProperties entityProperties) {
                        entityProperties.setBlockMagnetOwner(null);
                    }

                    tinyhouse$setBlockMagnetHolding(null);
                } else {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.damage(getWorld().getDamageSources().create(BlockMagnet.BLOCK_MAGNET), 0.2f);
                    }

                    this.tinyhouse$blockMagnetTicks--;
                    if (this.tinyhouse$blockMagnetTicks <= 0) {
                        boolean hasRedstone = isCreative() || getInventory().count(Items.REDSTONE) > 0;
                        if (hasRedstone) {
                            if (isCreative()) {
                                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, TinyHouse.IDENTIFIER, 5);
                                this.tinyhouse$blockMagnetTicks = 60;
                            } else {
                                for (int i = 0; i < getInventory().size(); i++) {
                                    ItemStack itemStack = getInventory().getStack(i);

                                    if (itemStack.isOf(Items.REDSTONE)) {
                                        itemStack.decrement(1);
                                        BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, TinyHouse.IDENTIFIER, 5);
                                        this.tinyhouse$blockMagnetTicks = 60;
                                        break;
                                    }
                                }
                            }
                        } else {
                            tinyhouse$setBlockMagnetHolding(null);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(tinyhouse$BLOCK_MAGNET, -1);
    }

    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    public void getMovementSpeed(CallbackInfoReturnable<Float> info) {
        if (this instanceof EntityProperties entityProperties
                && entityProperties.isStuckInsidePiston()) {
            info.setReturnValue(0f);
        } else if (this instanceof LivingProperties livingProperties) {
            if (livingProperties.isBurning()) {
                info.setReturnValue(info.getReturnValue() * 2f);
            } else if (livingProperties.hasFrozenBody()) {
                info.setReturnValue(info.getReturnValue() * 2f);
            }
        }
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void jump(CallbackInfo info) {
        if (this instanceof EntityProperties entityProperties
                && entityProperties.isStuckInsidePiston()) {
            info.cancel();
        }
    }

    public void tinyhouse$setBlockMagnetHolding(Entity blockMagnetHolding) {
        getDataTracker().set(tinyhouse$BLOCK_MAGNET, blockMagnetHolding != null ? blockMagnetHolding.getId() : -1);
    }

    public int tinyhouse$getBlockMagnetHolding() {
        return getDataTracker().get(tinyhouse$BLOCK_MAGNET);
    }

}
