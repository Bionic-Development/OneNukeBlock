package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.access.ItemProperties;
import de.takacick.onesuperblock.registry.item.SuperEnchanterBook;
import de.takacick.superitems.registry.ParticleRegistry;
import de.takacick.superitems.registry.item.SuperItem;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ItemEntity.class)
@Implements({@Interface(iface = ItemProperties.class, prefix = "onesuperblock$")})
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getStack();

    private static final TrackedData<Boolean> onesuperblock$RAINBOW = BionicDataTracker.registerData(new Identifier(OneSuperBlock.MOD_ID, "rainbow_item"), TrackedDataHandlerRegistry.BOOLEAN);

    private boolean onesuperblock$spawnParticles = false;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onesuperblock$RAINBOW, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("onesuperblock$rainbow", getDataTracker().get(onesuperblock$RAINBOW));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(onesuperblock$RAINBOW, nbt.getBoolean("onesuperblock$rainbow"));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (getStack().getItem() instanceof SuperItem) {
            if (this.age % 4 == 0) {
                double g = 0.1 * this.world.getRandom().nextGaussian();
                double h = 0.1 * this.world.getRandom().nextDouble();
                double j = 0.1 * this.world.getRandom().nextGaussian();

                this.world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_SPARK, this.world.getRandom().nextInt(24000),
                                false),
                        this.getX(), this.getBodyY(0.5) + this.random.nextDouble() * 0.25, this.getZ(),
                        g, h, j);
            }
        }

        if (!this.world.isClient) {
            if (getStack().getItem() instanceof SuperEnchanterBook && !onesuperblock$isRainbow()) {
                this.world.getOtherEntities(this, getBoundingBox().expand(3, 3, 3)).forEach(entity -> {
                    if (entity instanceof ItemEntity itemEntity
                            && itemEntity.age > 5
                            && this.world.getRandom().nextDouble() <= 0.1
                            && itemEntity.distanceTo(entity) <= 2.9) {
                        ItemStack itemStack = itemEntity.getStack();
                        AtomicBoolean enchanted = new AtomicBoolean(false);
                        Registry.ENCHANTMENT.getEntrySet().forEach(registryKeyEnchantmentEntry -> {
                            Enchantment enchantment = registryKeyEnchantmentEntry.getValue();
                            if (EnchantmentHelper.getLevel(enchantment, itemStack) < enchantment.getMaxLevel()
                                    && !enchantment.isCursed()) {
                                itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
                                enchanted.set(true);
                            }
                        });

                        itemEntity.setStack(Items.AIR.getDefaultStack());
                        itemEntity.setStack(itemStack);

                        if (enchanted.get()) {
                            BionicUtils.sendEntityStatus((ServerWorld) this.world, entity, OneSuperBlock.IDENTIFIER, 6);
                        }
                    }
                });
            }
        } else {
            if (getStack().getItem() instanceof SuperEnchanterBook && !onesuperblock$isRainbow() && (this.age % 20 == 0 || !this.onesuperblock$spawnParticles)) {
                this.onesuperblock$spawnParticles = true;
                double width = 3f;
                double count = Math.max(30 * width, 1f);
                for (int i = 0; i < count; i++) {
                    double increment = (2 * Math.PI) / count;
                    double angle = i * increment;
                    double x = (width * Math.cos(angle));
                    double z = (width * Math.sin(angle));

                    getWorld().addParticle(new RainbowParticleEffect(de.takacick.onesuperblock.registry.ParticleRegistry.RAINBOW_ITEM, 0, false),
                            true, x + getX(), getY(), z + getZ(), getId(), 0, 0);
                }
            }
        }
    }

    @Inject(method = "canMerge()Z", at = @At("HEAD"), cancellable = true)
    public void canMerge(CallbackInfoReturnable<Boolean> info) {
        if (onesuperblock$isRainbow()) {
            info.setReturnValue(false);
        }
    }

    public void onesuperblock$setRainbow(boolean rainbow) {
        getDataTracker().set(onesuperblock$RAINBOW, rainbow);
    }

    public boolean onesuperblock$isRainbow() {
        return getDataTracker().get(onesuperblock$RAINBOW);
    }
}

