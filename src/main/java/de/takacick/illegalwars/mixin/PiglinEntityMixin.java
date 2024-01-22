package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.access.PiglinProperties;
import de.takacick.illegalwars.registry.block.entity.PiglinGoldTurretBlockEntity;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntity.class)
@Implements({@Interface(iface = PiglinProperties.class, prefix = "illegalwars$")})
public abstract class PiglinEntityMixin extends AbstractPiglinEntity
        implements CrossbowUser,
        InventoryOwner {

    @Unique
    private static final TrackedData<BlockPos> illegalwars$BLOCK_POS = BionicDataTracker.registerData(new Identifier(IllegalWars.MOD_ID, "piglin_blockpos"), TrackedDataHandlerRegistry.BLOCK_POS);

    public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(illegalwars$BLOCK_POS, new BlockPos(0, -100, 0));
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    public void mobTick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (illegalwars$isUsingPiglinGoldTurret()) {
                if (!(getWorld().getBlockEntity(illegalwars$getPiglinGoldTurret()) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity
                        && equals(piglinGoldTurretBlockEntity.getShooter())
                        && piglinGoldTurretBlockEntity.getPos().isWithinDistance(getPos(), 2))) {
                    if (getWorld().getBlockEntity(illegalwars$getPiglinGoldTurret()) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity) {
                        if (equals(piglinGoldTurretBlockEntity.getShooter())) {
                            piglinGoldTurretBlockEntity.setShooter(null);
                            illegalwars$setPiglinGoldTurret(null);
                        } else if (piglinGoldTurretBlockEntity.getShooter() == null
                                && piglinGoldTurretBlockEntity.getPos().isWithinDistance(getPos(), 2)) {
                            piglinGoldTurretBlockEntity.setShooter((PiglinEntity) (Object) this);
                        }
                    } else {
                        illegalwars$setPiglinGoldTurret(null);
                    }
                }
            }
        }
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.illegalwars$isUsingPiglinGoldTurret()) {
            nbt.put("illegalwars$PiglinGoldTurretBlockPos", NbtHelper.fromBlockPos(illegalwars$getPiglinGoldTurret()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("illegalwars$PiglinGoldTurretBlockPos", NbtElement.COMPOUND_TYPE)) {
            illegalwars$setPiglinGoldTurret(NbtHelper.toBlockPos(nbt.getCompound("illegalwars$PiglinGoldTurretBlockPos")));
        }
    }

    public void illegalwars$setPiglinGoldTurret(BlockPos blockPos) {
        if (blockPos != null && getWorld().getBlockEntity(blockPos) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity) {
            piglinGoldTurretBlockEntity.setShooter((PiglinEntity) (Object) this);
        }

        getDataTracker().set(illegalwars$BLOCK_POS, blockPos == null ? new BlockPos(0, -100, 0) : new BlockPos(blockPos));
    }

    public BlockPos illegalwars$getPiglinGoldTurret() {
        return getDataTracker().get(illegalwars$BLOCK_POS);
    }

    public boolean illegalwars$isUsingPiglinGoldTurret() {
        return !getDataTracker().get(illegalwars$BLOCK_POS)
                .equals(new BlockPos(0, -100, 0));
    }
}
