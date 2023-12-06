package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.access.PlayerProperties;
import de.takacick.emeraldmoney.client.gui.EmeraldToast;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.emeraldmoney.registry.entity.custom.ShopItemEntity;
import de.takacick.emeraldmoney.registry.item.VillagerDriller;
import de.takacick.utils.data.BionicDataTracker;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.*;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
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

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "emeraldmoney$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract PlayerInventory getInventory();

    private static final TrackedData<Integer> emeraldmoney$EMERALDS = BionicDataTracker.registerData(new Identifier(EmeraldMoney.MOD_ID, "emeralds"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> emeraldmoney$EMERALD_WALLET = BionicDataTracker.registerData(new Identifier(EmeraldMoney.MOD_ID, "emerald_wallet"), TrackedDataHandlerRegistry.BOOLEAN);
    private double emeraldmoney$emeraldMultiplier = 1d;
    private EmeraldShopPortalEntity emeraldmoney$emeraldShopPortalEntity;
    private boolean emeraldmoney$emeraldGauntlet = false;
    private List<EmeraldToast> emeraldmoney$emeraldToast;

    protected PlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(emeraldmoney$EMERALDS, 0);
        getDataTracker().startTracking(emeraldmoney$EMERALD_WALLET, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (emeraldmoney$hasEmeraldWallet() && age % 4 == 0 && !isCreative()) {
                int emeralds = 0;
                for (int i = 0; i < getInventory().size(); i++) {
                    ItemStack stack = getInventory().getStack(i);
                    int count = (stack.isOf(Items.EMERALD) ? 1 : stack.isOf(Items.EMERALD_BLOCK) ? 9 : 0) * stack.getCount();
                    if (count > 0) {
                        stack.setCount(0);
                        emeralds += count;
                    }
                }

                emeraldmoney$addEmeralds(emeralds, true);
            }

            if (getMainHandStack().isOf(ItemRegistry.EMERALD_GAUNTLET)
                    || getOffHandStack().isOf(ItemRegistry.EMERALD_GAUNTLET)) {
                this.emeraldmoney$emeraldGauntlet = true;
                addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, -1, 0, false, false, true));
            } else if (this.emeraldmoney$emeraldGauntlet) {
                this.emeraldmoney$emeraldGauntlet = false;
                removeStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
            }
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> info) {
        if (stack.isOf(ItemRegistry.VILLAGER_DRILLER)) {
            VillagerDriller.setRotation(stack, VillagerDriller.getRotation(stack, 1f));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("emeraldmoney$emeralds", getDataTracker().get(emeraldmoney$EMERALDS));
        nbt.putBoolean("emeraldmoney$emeraldWallet", getDataTracker().get(emeraldmoney$EMERALD_WALLET));
        nbt.putDouble("emeraldmoney$emeraldMultiplier", this.emeraldmoney$emeraldMultiplier);
        if (this.emeraldmoney$emeraldGauntlet) {
            nbt.putBoolean("emeraldmoney$emeraldGauntlet", true);
        }

        if (this.emeraldmoney$emeraldShopPortalEntity != null && !getWorld().isClient) {
            this.emeraldmoney$emeraldShopPortalEntity.setDead(true);
            this.emeraldmoney$emeraldShopPortalEntity = null;
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(emeraldmoney$EMERALDS, nbt.getInt("emeraldmoney$emeralds"));
        getDataTracker().set(emeraldmoney$EMERALD_WALLET, nbt.getBoolean("emeraldmoney$emeraldWallet"));
        this.emeraldmoney$emeraldGauntlet = nbt.getBoolean("emeraldmoney$emeraldGauntlet");
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        if (target instanceof ShopItemEntity && isCreative()) {
            target.discard();
            info.cancel();
        }
    }

    public void emeraldmoney$setEmeralds(int emeralds) {
        getDataTracker().set(emeraldmoney$EMERALDS, emeralds);
    }

    public int emeraldmoney$addEmeralds(int emeralds, boolean multiplier) {
        if (multiplier) {
            emeralds *= (int) emeraldmoney$getEmeraldMultiplier();
        }

        if (emeralds == 0) {
            return emeraldmoney$getEmeralds();
        }

        if (!this.getWorld().isClient) {
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(emeralds);
            ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new Identifier(EmeraldMoney.MOD_ID, "addemeralds"), packetByteBuf);
        }

        if (emeralds > 0 && emeraldmoney$getEmeralds() > emeralds + emeraldmoney$getEmeralds()) {
            emeralds = Integer.MAX_VALUE;
        } else {
            emeralds += emeraldmoney$getEmeralds();
        }

        getDataTracker().set(emeraldmoney$EMERALDS, emeralds);

        return emeralds;
    }

    public void emeraldmoney$setEmeraldShopPortal(EmeraldShopPortalEntity emeraldShopPortal) {
        if (this.emeraldmoney$emeraldShopPortalEntity != null && this.emeraldmoney$emeraldShopPortalEntity.isAlive()) {
            this.emeraldmoney$emeraldShopPortalEntity.setDead(true);
        }

        this.emeraldmoney$emeraldShopPortalEntity = emeraldShopPortal;
    }

    public int emeraldmoney$getEmeralds() {
        return getDataTracker().get(emeraldmoney$EMERALDS);
    }

    public void emeraldmoney$setEmeraldMultiplier(double emeraldMultiplier) {
        this.emeraldmoney$emeraldMultiplier = emeraldMultiplier;
    }

    public double emeraldmoney$getEmeraldMultiplier() {
        return this.emeraldmoney$emeraldMultiplier + (getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.VILLAGER_ROBE) ? 1 : 0);
    }

    public void emeraldmoney$setEmeraldWallet(boolean emeraldWallet) {
        getDataTracker().set(emeraldmoney$EMERALD_WALLET, emeraldWallet);
    }

    public boolean emeraldmoney$hasEmeraldWallet() {
        return getDataTracker().get(emeraldmoney$EMERALD_WALLET);
    }

    public List<EmeraldToast> emeraldmoney$getEmeraldToasts() {
        if (this.emeraldmoney$emeraldToast == null) {
            this.emeraldmoney$emeraldToast = new ArrayList<>();
        }

        return this.emeraldmoney$emeraldToast;
    }
}
