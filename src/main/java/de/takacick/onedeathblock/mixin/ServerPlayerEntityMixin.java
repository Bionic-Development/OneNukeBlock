package de.takacick.onedeathblock.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.damage.DeathDamageSources;
import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import de.takacick.onedeathblock.server.oneblock.OneBlockHandler;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow
    private int joinInvulnerabilityTicks;

    @Shadow
    public abstract ServerWorld getWorld();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties && oldPlayer instanceof PlayerProperties oldProperties) {
            playerProperties.setDeaths(oldProperties.getDeaths());
            playerProperties.setDeathMultiplier(oldProperties.getDeathMultiplier());
            this.joinInvulnerabilityTicks = 0;
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (!world.isClient && this instanceof PlayerProperties playerProperties) {
            int deaths = damageSource instanceof DeathDamageSources.DeathDamageSource deathDamageSource
                    ? deathDamageSource.getDeaths(getRandom()) : 1;

            playerProperties.addDeaths(deaths, true);

            if (damageSource.equals(DeathDamageSources.DEATH_BLOCK)) {
                OneBlockHandler.INSTANCE.drop();
            }
        }
    }

    @Inject(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V", shift = At.Shift.AFTER), cancellable = true)
    public void trySleep(BlockPos pos, CallbackInfoReturnable<Either<SleepFailureReason, Unit>> info) {
        if (this.world.getBlockEntity(pos) instanceof SpikedBedBlockEntity) {
            Either<PlayerEntity.SleepFailureReason, Unit> either = super.trySleep(pos).ifRight(unit -> {
                this.incrementStat(Stats.SLEEP_IN_BED);
                Criteria.SLEPT_IN_BED.trigger((ServerPlayerEntity) (Object) this);
            });
            if (!this.getWorld().isSleepingEnabled()) {
                this.sendMessage(Text.translatable("sleep.not_possible"), true);
            }
            ((ServerWorld) this.world).updateSleepingPlayers();
            info.setReturnValue(either);
        }
    }
}

