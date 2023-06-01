package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.EntityProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.item.CactusOnesie;
import de.takacick.deathmoney.registry.item.FireSuit;
import de.takacick.deathmoney.registry.item.SweetBerrySuit;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@Implements({@Interface(iface = EntityProperties.class, prefix = "deathmoney$")})
public abstract class EntityMixin {

    @Shadow
    @Nullable
    public abstract MinecraftServer getServer();

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract World getEntityWorld();

    @Shadow
    public abstract @Nullable Entity moveToWorld(ServerWorld destination);

    @Shadow
    public abstract void requestTeleport(double destX, double destY, double destZ);

    @Shadow
    public abstract float getYaw();

    @Shadow
    public World world;
    private int deathmoney$portalCooldown = 0;
    private double deathmoney$portalX;
    private double deathmoney$portalY;
    private double deathmoney$portalZ;
    private RegistryKey<World> deathmoney$world;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (deathmoney$portalCooldown > 0) {
            deathmoney$portalCooldown--;
        }
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (deathmoney$world != null) {
            nbt.putDouble("deathmoney$deathShopPortalX", deathmoney$portalX);
            nbt.putDouble("deathmoney$deathShopPortalY", deathmoney$portalY);
            nbt.putDouble("deathmoney$deathShopPortalZ", deathmoney$portalZ);
            nbt.putString("deathmoney$deathShopPortalWorld", deathmoney$world.getValue().toString());
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("deathhopportalworld", NbtCompound.STRING_TYPE)) {
            if (!getWorld().isClient) {
                Identifier id = new Identifier(nbt.getString("deathmoney$deathShopPortalWorld"));
                var optional = getServer().getWorldRegistryKeys().stream().filter(worldRegistryKey -> worldRegistryKey.getValue().equals(id)).findFirst();
                if (optional.isPresent()) {
                    deathmoney$world = optional.get();
                    deathmoney$portalX = nbt.getDouble("deathmoney$deathShopPortalX");
                    deathmoney$portalY = nbt.getDouble("deathmoney$deathShopPortalY");
                    deathmoney$portalZ = nbt.getDouble("deathmoney$deathShopPortalZ");
                }
            }
        }
    }

    @Inject(method = "move", at = @At("HEAD"))
    public void move(MovementType movementType, Vec3d movement, CallbackInfo info) {
        if (!world.isClient
                && (movement.length() > 0.05 && movementType.equals(MovementType.PLAYER))
                && (Object) this instanceof LivingEntity livingEntity) {
            if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SweetBerrySuit) {
                if (livingEntity.damage(DeathDamageSources.SWEET_BERRY_SUIT, 2)) {
                    BionicUtils.sendEntityStatus((ServerWorld) world, livingEntity, DeathMoney.IDENTIFIER, 4);
                }
            } else if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof FireSuit) {
                if (livingEntity.damage(DeathDamageSources.FIRE_SUIT, 5)) {
                    BionicUtils.sendEntityStatus((ServerWorld) world, livingEntity, DeathMoney.IDENTIFIER, 5);
                }
            } else if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof CactusOnesie) {
                if (livingEntity.damage(DeathDamageSources.CACTUS_ONESIE, 3)) {
                    BionicUtils.sendEntityStatus((ServerWorld) world, livingEntity, DeathMoney.IDENTIFIER, 6);
                }
            }
        }
    }

    @Inject(method = "isOnFire", at = @At("HEAD"), cancellable = true)
    public void isOnFire(CallbackInfoReturnable<Boolean> info) {

        if (world.isClient
                && (Object) this instanceof LivingEntity livingEntity) {
            if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.FIRE_SUIT)) {
                info.setReturnValue(true);
            }
        }
    }

    public void deathmoney$setDeathShopPortal(World world, double x, double y, double z, boolean shopPortal) {
        this.deathmoney$portalX = x;
        this.deathmoney$portalY = y;
        this.deathmoney$portalZ = z;
        this.deathmoney$world = world.getRegistryKey();

        if (shopPortal) {
            this.deathmoney$world = null;
        }
    }

    public void deathmoney$setDeathShopPortalCooldown(int portalCooldown) {
        this.deathmoney$portalCooldown = 10;
    }

    public boolean deathmoney$isOnDeathShopPortalCooldown() {
        return this.deathmoney$portalCooldown > 0;
    }

    public void deathmoney$teleportBackFromDeathShop() {
        if (this.deathmoney$world != null && !getEntityWorld().isClient) {
            World world = getServer().getWorld(this.deathmoney$world);

            if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.teleport((ServerWorld) world, deathmoney$portalX, deathmoney$portalY, deathmoney$portalZ, getYaw(), 0);
            } else {
                if (world != null && !getEntityWorld().equals(world)) {
                    moveToWorld((ServerWorld) world);
                }

                requestTeleport(deathmoney$portalX, deathmoney$portalY, deathmoney$portalZ);
            }
            deathmoney$setDeathShopPortal(world, deathmoney$portalX, deathmoney$portalY, deathmoney$portalZ, true);
            deathmoney$setDeathShopPortalCooldown(10);
        }
    }
}
