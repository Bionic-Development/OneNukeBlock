package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.damage.DeathDamageSources;
import de.takacick.onedeathblock.registry.ItemRegistry;
import de.takacick.onedeathblock.server.oneblock.OneBlockHandler;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract Random getRandom();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if ((Object) this instanceof PlayerEntity playerEntity
                && playerEntity instanceof PlayerProperties playerProperties) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = playerEntity.getInventory().getStack(i);

                if (itemStack.isOf(ItemRegistry.SUPER_DEATH_TOTEM)) {
                    this.velocityModified = false;
                    this.setHealth(1.0f);
                    int deaths = source instanceof DeathDamageSources.DeathDamageSource deathDamageSource ? deathDamageSource.getDeaths(getRandom()) : 1;
                    playerProperties.addDeaths(deaths, true);
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, OneDeathBlock.IDENTIFIER, 1);
                    info.setReturnValue(true);

                    if (source.equals(DeathDamageSources.DEATH_BLOCK)) {
                        OneBlockHandler.INSTANCE.drop();
                    }
                    break;
                }
            }
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At("HEAD"), cancellable = true)
    private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (stack.isOf(ItemRegistry.SPIKY_IRON_ARMOR_SUIT)) {
            info.setReturnValue(EquipmentSlot.CHEST);
        } else if (stack.isOf(ItemRegistry.HEAD_SPAWNER)) {
            info.setReturnValue(EquipmentSlot.HEAD);
        }
    }
}