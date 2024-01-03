package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.PigProperties;
import de.takacick.raidbase.access.SlimeProperties;
import de.takacick.raidbase.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private Vec3d pos;

    @Shadow
    private float standingEyeHeight;

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract double getBodyY(double heightScale);

    @Shadow
    @Final
    protected Random random;

    @Shadow
    private World world;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Inject(method = "calculateBoundingBox", at = @At("RETURN"), cancellable = true)
    public void calculateBoundingBox(CallbackInfoReturnable<Box> info) {
        if (this instanceof PigProperties pigProperties && pigProperties.isPigSoldier()) {
            Box box = info.getReturnValue();
            double height = 1f + (box.maxY - box.minY);
            info.setReturnValue(new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY + height, box.maxZ));
        }
    }

    @Inject(method = "calculateBoundsForPose", at = @At("RETURN"), cancellable = true)
    public void calculateBoundsForPose(CallbackInfoReturnable<Box> info) {
        if (this instanceof PigProperties pigProperties && pigProperties.isPigSoldier()) {
            Box box = info.getReturnValue();
            double height = 1f + (box.maxY - box.minY);

            info.setReturnValue(new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY + height, box.maxZ));
        }
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    public void getHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PigProperties pigProperties && pigProperties.isPigSoldier()) {
            info.setReturnValue(info.getReturnValue() + 1f);
        }
    }

    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    public void getStandingEyeHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PigProperties pigProperties && pigProperties.isPigSoldier()) {
            info.setReturnValue(info.getReturnValue() + 0.9f);
        }
    }

    @Inject(method = "getEyeY", at = @At("HEAD"), cancellable = true)
    public void getEyeY(CallbackInfoReturnable<Double> info) {
        if (this instanceof PigProperties pigProperties && pigProperties.isPigSoldier()) {
            info.setReturnValue(this.pos.getY() + this.standingEyeHeight + 0.9f);
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (this instanceof SlimeProperties slimeProperties && !slimeProperties.isSlimeSheared()) {
            if (player.getStackInHand(hand).getItem() instanceof ShovelItem) {
                slimeProperties.setSlimeSheared(true);

                if (!getWorld().isClient) {
                    for (int i = 0; i < 8; ++i) {
                        double h = getBodyY(0.3 + random.nextDouble() * 0.4);
                        double d = random.nextGaussian() * 0.4;
                        double e = random.nextDouble() * 1;
                        double f = random.nextGaussian() * 0.4;

                        ItemEntity itemEntity = new ItemEntity(this.world, getX() + d, h + e, getZ() + f, Items.SLIME_BLOCK.getDefaultStack(), d * 0.5, e * 0.3, f  * 0.5);
                        itemEntity.setToDefaultPickupDelay();
                        this.world.spawnEntity(itemEntity);
                    }

                    double h = getBodyY(0.3 + random.nextDouble() * 0.4);
                    double d = random.nextGaussian() * 0.4;
                    double e = random.nextDouble() * 1;
                    double f = random.nextGaussian() * 0.4;

                    ItemEntity itemEntity = new ItemEntity(this.world, getX() + d, h + e, getZ() + f, ItemRegistry.SLIME_SUIT.getDefaultStack(), d * 0.1, e * 0.2, f  * 0.1);
                    itemEntity.setToDefaultPickupDelay();
                    this.world.spawnEntity(itemEntity);

                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), (Entity) (Object) this, RaidBase.IDENTIFIER, 5);
                }

                info.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}