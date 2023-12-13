package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.access.EntityProperties;
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
@Implements({@Interface(iface = EntityProperties.class, prefix = "upgradebody$")})
public abstract class PortalEntityMixin {

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

    private int upgradebody$portalCooldown = 0;
    private double upgradebody$portalX;
    private double upgradebody$portalY;
    private double upgradebody$portalZ;
    private float upgradebody$yaw;
    private RegistryKey<World> upgradebody$world;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (this.upgradebody$portalCooldown > 0) {
            this.upgradebody$portalCooldown--;
        }
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (this.upgradebody$world != null) {
            nbt.putDouble("upgradebody$emeraldShopPortalX", this.upgradebody$portalX);
            nbt.putDouble("upgradebody$emeraldShopPortalY", this.upgradebody$portalY);
            nbt.putDouble("upgradebody$emeraldShopPortalZ", this.upgradebody$portalZ);
            nbt.putFloat("upgradebody$yaw", this.upgradebody$yaw);
            nbt.putString("upgradebody$emeraldShopPortalWorld", this.upgradebody$world.getValue().toString());
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("upgradebody$emeraldShopPortalWorld", NbtCompound.STRING_TYPE)) {
            if (!getWorld().isClient) {
                Identifier id = new Identifier(nbt.getString("upgradebody$emeraldShopPortalWorld"));
                var optional = getServer().getWorldRegistryKeys().stream().filter(worldRegistryKey -> worldRegistryKey.getValue().equals(id)).findFirst();
                if (optional.isPresent()) {
                    this.upgradebody$world = optional.get();
                    this.upgradebody$portalX = nbt.getDouble("upgradebody$emeraldShopPortalX");
                    this.upgradebody$portalY = nbt.getDouble("upgradebody$emeraldShopPortalY");
                    this.upgradebody$portalZ = nbt.getDouble("upgradebody$emeraldShopPortalZ");
                    this.upgradebody$yaw = nbt.getFloat("upgradebody$yaw");
                }
            }
        }
    }

    public void upgradebody$setEmeraldShopPortal(World world, double x, double y, double z, float yaw, boolean shopPortal) {
        this.upgradebody$portalX = x;
        this.upgradebody$portalY = y;
        this.upgradebody$portalZ = z;
        this.upgradebody$yaw = yaw;
        this.upgradebody$world = world.getRegistryKey();

        if (shopPortal) {
            this.upgradebody$world = null;
        }
    }

    public void upgradebody$setEmeraldShopPortalCooldown(int portalCooldown) {
        this.upgradebody$portalCooldown = 10;
    }

    public boolean upgradebody$isOnEmeraldShopPortalCooldown() {
        return this.upgradebody$portalCooldown > 0;
    }

    public boolean upgradebody$teleportBackFromEmeraldShop() {
        if (this.upgradebody$world != null && !getEntityWorld().isClient) {
            World world = getServer().getWorld(this.upgradebody$world);

            if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.teleport((ServerWorld) world, this.upgradebody$portalX, this.upgradebody$portalY, this.upgradebody$portalZ, this.upgradebody$yaw, 0);
            } else {
                if (world != null && !getEntityWorld().equals(world)) {
                    moveToWorld((ServerWorld) world);
                }

                requestTeleport(this.upgradebody$portalX, this.upgradebody$portalY, this.upgradebody$portalZ);
            }
            upgradebody$setEmeraldShopPortal(world, this.upgradebody$portalX, this.upgradebody$portalY, this.upgradebody$portalZ, this.upgradebody$yaw, true);
            upgradebody$setEmeraldShopPortalCooldown(10);
            return true;
        }

        return false;
    }
}
