package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.access.PlayerProperties;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.entity.dragon.ImmortalEnderDragonEntity;
import draylar.identity.impl.PlayerDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "immortalmobs$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    private final ServerBossBar immortalDragoBossBar = (ServerBossBar) new ServerBossBar(Text.of("§dImmortal Ender Dragon"), BossBar.Color.PURPLE, BossBar.Style.PROGRESS).setDarkenSky(false);
    private int immortalmobs$immortalCannon = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (!(((PlayerDataProvider) this).getIdentity() instanceof ImmortalEnderDragonEntity immortalEnderDragonEntity)) {
                immortalDragoBossBar.clearPlayers();
            } else {
                immortalEnderDragonEntity.tickWithEndCrystals((PlayerEntity) (Object) this);
                immortalDragoBossBar.addPlayer((ServerPlayerEntity) (Object) this);
                immortalDragoBossBar.setPercent(this.getHealth() / this.getMaxHealth());
            }
        }

        if (immortalmobs$immortalCannon > 0) {
            immortalmobs$immortalCannon--;
        }
    }

    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    public void spawnSweepAttackParticles(CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.IMMORTAL_SWORD)) {
            info.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (((PlayerDataProvider) this).getIdentity() instanceof ImmortalEnderDragonEntity) {
            cir.setReturnValue(super.damage(source, amount / 20f));
        }
    }

    public ServerBossBar immortalmobs$getImmortalDragonBar() {
        return immortalDragoBossBar;
    }

    public void immortalmobs$setItemUseTime(int itemUseItem) {
        this.itemUseTimeLeft = itemUseItem;
    }

    public void immortalmobs$setImmortalCannon(int immortalCannon) {
        this.immortalmobs$immortalCannon = immortalCannon;
    }

    public boolean immortalmobs$hasImmortalCannon() {
        return this.immortalmobs$immortalCannon > 0;
    }
}

