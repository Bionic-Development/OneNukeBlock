package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.upgradebody.registry.entity.custom.ShopItemEntity;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "upgradebody$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract boolean isCreative();

    private static final TrackedData<Integer> upgradebody$EMERALDS = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "emeralds"), TrackedDataHandlerRegistry.INTEGER);
    private EmeraldShopPortalEntity upgradebody$emeraldShopPortalEntity;

    protected PlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(upgradebody$EMERALDS, 0);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {

        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("upgradebody$emeralds", getDataTracker().get(upgradebody$EMERALDS));

        if (this.upgradebody$emeraldShopPortalEntity != null && !getWorld().isClient) {
            this.upgradebody$emeraldShopPortalEntity.setDead(true);
            this.upgradebody$emeraldShopPortalEntity = null;
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(upgradebody$EMERALDS, nbt.getInt("upgradebody$emeralds"));
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        if (target instanceof ShopItemEntity && isCreative()) {
            target.discard();
            info.cancel();
        }
    }

    public void upgradebody$setEmeraldShopPortal(EmeraldShopPortalEntity emeraldShopPortal) {
        if (this.upgradebody$emeraldShopPortalEntity != null && this.upgradebody$emeraldShopPortalEntity.isAlive()) {
            this.upgradebody$emeraldShopPortalEntity.setDead(true);
        }

        this.upgradebody$emeraldShopPortalEntity = emeraldShopPortal;
    }
}
