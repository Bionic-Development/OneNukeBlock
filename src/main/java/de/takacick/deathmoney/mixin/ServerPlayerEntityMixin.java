package de.takacick.deathmoney.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.entity.living.CrazyExGirlfriendEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties && oldPlayer instanceof PlayerProperties oldProperties) {
            playerProperties.setDeaths(oldProperties.getDeaths());
            playerProperties.setDeathMultiplier(oldProperties.getDeathMultiplier());
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (!world.isClient && this instanceof PlayerProperties playerProperties) {
            int deaths = damageSource instanceof DeathDamageSources.DeathDamageSource deathDamageSource ? deathDamageSource.getDeaths() : 1;

            if (damageSource.getSource() instanceof CrazyExGirlfriendEntity) {
                deaths = 125;
            }

            playerProperties.addDeaths(deaths, true);
        }
    }

    @Inject(method = "useBook", at = @At("HEAD"), cancellable = true)
    public void useBook(ItemStack book, Hand hand, CallbackInfo info) {
        if (book.isOf(ItemRegistry.DEATH_NOTE)) {
            //this.networkHandler.sendPacket(new OpenWrittenBookS2CPacket(hand));
            info.cancel();
        }
    }
}

