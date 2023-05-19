package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.access.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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
@Implements({@Interface(iface = EntityProperties.class, prefix = "heartmoney$")})
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

    private int heartmoney$portalCooldown = 0;
    private double heartmoney$portalX;
    private double heartmoney$portalY;
    private double heartmoney$portalZ;
    private RegistryKey<World> heartmoney$world;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (heartmoney$portalCooldown > 0) {
            heartmoney$portalCooldown--;
        }
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (heartmoney$world != null) {
            nbt.putDouble("heartmoney$heartShopPortalX", heartmoney$portalX);
            nbt.putDouble("heartmoney$heartShopPortalY", heartmoney$portalY);
            nbt.putDouble("heartmoney$heartShopPortalZ", heartmoney$portalZ);
            nbt.putString("heartmoney$heartShopPortalWorld", heartmoney$world.getValue().toString());
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("xpshopportalworld", NbtCompound.STRING_TYPE)) {
            if (!getWorld().isClient) {
                Identifier id = new Identifier(nbt.getString("heartmoney$heartShopPortalWorld"));
                var optional = getServer().getWorldRegistryKeys().stream().filter(worldRegistryKey -> worldRegistryKey.getValue().equals(id)).findFirst();
                if (optional.isPresent()) {
                    heartmoney$world = optional.get();
                    heartmoney$portalX = nbt.getDouble("heartmoney$heartShopPortalX");
                    heartmoney$portalY = nbt.getDouble("heartmoney$heartShopPortalY");
                    heartmoney$portalZ = nbt.getDouble("heartmoney$heartShopPortalZ");
                }
            }
        }
    }

    public void heartmoney$setHeartShopPortal(World world, double x, double y, double z, boolean shopPortal) {
        this.heartmoney$portalX = x;
        this.heartmoney$portalY = y;
        this.heartmoney$portalZ = z;
        this.heartmoney$world = world.getRegistryKey();

        if (shopPortal) {
            this.heartmoney$world = null;
        }
    }

    public void heartmoney$setHeartShopPortalCooldown(int portalCooldown) {
        this.heartmoney$portalCooldown = 10;
    }

    public boolean heartmoney$isOnHeartShopPortalCooldown() {
        return this.heartmoney$portalCooldown > 0;
    }

    public void heartmoney$teleportBackFromHeartShop() {
        if (this.heartmoney$world != null && !getEntityWorld().isClient) {
            World world = getServer().getWorld(this.heartmoney$world);

            if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.teleport((ServerWorld) world, heartmoney$portalX, heartmoney$portalY, heartmoney$portalZ, getYaw(), 0);
            } else {
                if (world != null && !getEntityWorld().equals(world)) {
                    moveToWorld((ServerWorld) world);
                }

                requestTeleport(heartmoney$portalX, heartmoney$portalY, heartmoney$portalZ);
            }
            heartmoney$setHeartShopPortal(world, heartmoney$portalX, heartmoney$portalY, heartmoney$portalZ, true);
            heartmoney$setHeartShopPortalCooldown(10);
        }
    }
}
