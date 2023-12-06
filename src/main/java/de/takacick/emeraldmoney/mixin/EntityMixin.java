package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.access.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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
@Implements({@Interface(iface = EntityProperties.class, prefix = "emeraldmoney$")})
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

    private int emeraldmoney$portalCooldown = 0;
    private double emeraldmoney$portalX;
    private double emeraldmoney$portalY;
    private double emeraldmoney$portalZ;
    private float emeraldmoney$yaw;
    private RegistryKey<World> emeraldmoney$world;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (this.emeraldmoney$portalCooldown > 0) {
            this.emeraldmoney$portalCooldown--;
        }
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (this.emeraldmoney$world != null) {
            nbt.putDouble("emeraldmoney$emeraldShopPortalX", this.emeraldmoney$portalX);
            nbt.putDouble("emeraldmoney$emeraldShopPortalY", this.emeraldmoney$portalY);
            nbt.putDouble("emeraldmoney$emeraldShopPortalZ", this.emeraldmoney$portalZ);
            nbt.putFloat("emeraldmoney$yaw", this.emeraldmoney$yaw);
            nbt.putString("emeraldmoney$emeraldShopPortalWorld", this.emeraldmoney$world.getValue().toString());
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("emeraldmoney$emeraldShopPortalWorld", NbtCompound.STRING_TYPE)) {
            if (!getWorld().isClient) {
                Identifier id = new Identifier(nbt.getString("emeraldmoney$emeraldShopPortalWorld"));
                var optional = getServer().getWorldRegistryKeys().stream().filter(worldRegistryKey -> worldRegistryKey.getValue().equals(id)).findFirst();
                if (optional.isPresent()) {
                    this.emeraldmoney$world = optional.get();
                    this.emeraldmoney$portalX = nbt.getDouble("emeraldmoney$emeraldShopPortalX");
                    this.emeraldmoney$portalY = nbt.getDouble("emeraldmoney$emeraldShopPortalY");
                    this.emeraldmoney$portalZ = nbt.getDouble("emeraldmoney$emeraldShopPortalZ");
                    this.emeraldmoney$yaw = nbt.getFloat("emeraldmoney$yaw");
                }
            }
        }
    }

    public void emeraldmoney$setEmeraldShopPortal(World world, double x, double y, double z, float yaw, boolean shopPortal) {
        this.emeraldmoney$portalX = x;
        this.emeraldmoney$portalY = y;
        this.emeraldmoney$portalZ = z;
        this.emeraldmoney$yaw = yaw;
        this.emeraldmoney$world = world.getRegistryKey();

        if (shopPortal) {
            this.emeraldmoney$world = null;
        }
    }

    public void emeraldmoney$setEmeraldShopPortalCooldown(int portalCooldown) {
        this.emeraldmoney$portalCooldown = 10;
    }

    public boolean emeraldmoney$isOnEmeraldShopPortalCooldown() {
        return this.emeraldmoney$portalCooldown > 0;
    }

    public boolean emeraldmoney$teleportBackFromEmeraldShop() {
        if (this.emeraldmoney$world != null && !getEntityWorld().isClient) {
            World world = getServer().getWorld(this.emeraldmoney$world);

            if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.teleport((ServerWorld) world, this.emeraldmoney$portalX, this.emeraldmoney$portalY, this.emeraldmoney$portalZ, this.emeraldmoney$yaw, 0);
            } else {
                if (world != null && !getEntityWorld().equals(world)) {
                    moveToWorld((ServerWorld) world);
                }

                requestTeleport(this.emeraldmoney$portalX, this.emeraldmoney$portalY, this.emeraldmoney$portalZ);
            }
            emeraldmoney$setEmeraldShopPortal(world, this.emeraldmoney$portalX, this.emeraldmoney$portalY, this.emeraldmoney$portalZ, this.emeraldmoney$yaw, true);
            emeraldmoney$setEmeraldShopPortalCooldown(10);
            return true;
        }

        return false;
    }
}
