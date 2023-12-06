package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.access.VillagerProperties;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InteractionObserver;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
@Implements({@Interface(iface = VillagerProperties.class, prefix = "emeraldmoney$")})
public abstract class VillagerEntityMixin extends MerchantEntity
        implements InteractionObserver,
        VillagerDataContainer {

    @Unique
    private static final TrackedData<Boolean> emeraldmoney$NOSE = BionicDataTracker.registerData(new Identifier(EmeraldMoney.MOD_ID, "villager_nose"), TrackedDataHandlerRegistry.BOOLEAN);

    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(emeraldmoney$NOSE, true);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (!emeraldmoney$hasNose()) {
            nbt.putBoolean("emeraldmoney$nose", getDataTracker().get(emeraldmoney$NOSE));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("emeraldmoney$nose", NbtElement.BYTE_TYPE)) {
            getDataTracker().set(emeraldmoney$NOSE, nbt.getBoolean("emeraldmoney$nose"));
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS)) {
            if (emeraldmoney$hasNose()) {
                if(!this.getWorld().isClient) {
                    this.emitGameEvent(GameEvent.SHEAR, player);
                    itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));

                    this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.AMBIENT, 1.0f, 1.0f);

                    Vec3d rotation = getRotationVector(MathHelper.clamp(getPitch(), -30, 30), getYaw()).multiply(0.15).add(0, 0.2, 0);

                    ItemEntity itemEntity = new ItemEntity(getWorld(), getX(), getBodyY(0.8), getZ(), ItemRegistry.VILLAGER_NOSE.getDefaultStack(), rotation.getX(), rotation.getY(), rotation.getZ());
                    itemEntity.setToDefaultPickupDelay();
                    getWorld().spawnEntity(itemEntity);

                    this.damage(getWorld().getDamageSources().playerAttack(player), 3);

                    emeraldmoney$setNose(false);
                }
                info.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }

    public void emeraldmoney$setNose(boolean nose) {
        getDataTracker().set(emeraldmoney$NOSE, nose);
    }

    public boolean emeraldmoney$hasNose() {
        return getDataTracker().get(emeraldmoney$NOSE);
    }
}
