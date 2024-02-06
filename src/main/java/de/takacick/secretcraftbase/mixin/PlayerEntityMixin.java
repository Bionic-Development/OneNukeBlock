package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "secretcraftbase$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract PlayerInventory getInventory();

    private static final TrackedData<Float> secretcraftbase$TONGUE_LENGTH = BionicDataTracker.registerData(new Identifier(SecretCraftBase.MOD_ID, "tongue_length"), TrackedDataHandlerRegistry.FLOAT);
    private final AnimationState secretcraftbase$heartRemovalAnimationState = new AnimationState();
    private final AnimationState secretcraftbase$frogTongueState = new AnimationState();
    private int secretcraftbase$heartRemovalTicks = 0;
    private ItemStack secretcraftbase$heartCarver;
    private int secretcraftbase$toungeTicks = 0;
    private Entity secretcraftbase$target = null;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(secretcraftbase$TONGUE_LENGTH, 1f);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (this.secretcraftbase$toungeTicks > 0) {
                this.secretcraftbase$toungeTicks--;

                if (this.secretcraftbase$toungeTicks <= 19) {
                    if (this.secretcraftbase$target != null) {
                        secretcraftbase$eatFrogTarget();
                        this.secretcraftbase$toungeTicks = 0;
                    }
                }
            }

            if (this.secretcraftbase$heartRemovalTicks > 0) {
                this.secretcraftbase$heartRemovalTicks--;
                damage(getDamageSources().generic(), 0.001f);
                BionicUtils.sendEntityStatus(getWorld(), this, SecretCraftBase.IDENTIFIER, 2);
                if (this.secretcraftbase$heartRemovalTicks <= 0) {
                    Vec3d right = getRotationVector(getPitch(), getYaw() + 90).multiply(0.05);
                    Vec3d vel = getRotationVector().multiply(0.35)
                            .add(right.getX() * getWorld().getRandom().nextGaussian(), 0.1, right.getZ() * getWorld().getRandom().nextGaussian()).multiply(0.35);
                    ItemEntity itemEntity = new ItemEntity(getWorld(), getX(), getBodyY(0.65), getZ(), ItemRegistry.HEART.getDefaultStack(), vel.getX(), vel.getY(), vel.getZ());
                    itemEntity.setPickupDelay(30);
                    itemEntity.setOwner(getUuid());
                    getWorld().spawnEntity(itemEntity);

                    SecretCraftBase.updateEntityHealth(this, getMaxHealth() - 2, false);

                    if (this.secretcraftbase$heartCarver != null) {
                        if (!isCreative()) {
                            this.secretcraftbase$heartCarver.damage(1, this, playerEntityMixin -> playerEntityMixin.sendToolBreakStatus(Hand.MAIN_HAND));
                        }
                        this.secretcraftbase$heartCarver = null;
                    }
                }
            }
        }
    }

    public AnimationState secretcraftbase$getHeartRemovalState() {
        return this.secretcraftbase$heartRemovalAnimationState;
    }

    public void secretcraftbase$setHeartCarverStack(ItemStack heartCarver) {
        this.secretcraftbase$heartCarver = heartCarver;
    }

    public void secretcraftbase$setHeartRemovalTicks(int heartRemovalTicks) {
        this.secretcraftbase$heartRemovalTicks = heartRemovalTicks;
    }

    public int secretcraftbase$getHeartRemovalTicks() {
        return this.secretcraftbase$heartRemovalTicks;
    }

    public AnimationState secretcraftbase$getFrogTongueState() {
        return this.secretcraftbase$frogTongueState;
    }

    public void secretcraftbase$setFrogTarget(Entity target) {
        this.secretcraftbase$toungeTicks = 25;
        this.secretcraftbase$target = target;
    }

    public void secretcraftbase$eatFrogTarget() {
        if (this.secretcraftbase$target instanceof LivingEntity) {
            return;
        }

        if (this.secretcraftbase$target instanceof ItemEntity itemEntity) {
            getInventory().offer(itemEntity.getStack(), true);
        } else if (this.secretcraftbase$target instanceof FallingBlockEntity fallingBlockEntity) {
            Item item = fallingBlockEntity.getBlockState().isOf(Blocks.NETHER_PORTAL) ? ItemRegistry.NETHER_PORTAL_BLOCK : fallingBlockEntity.getBlockState().getBlock().asItem();

            if (!item.equals(Items.AIR)) {
                getInventory().offer(item.getDefaultStack(), true);
            }
        }

        this.secretcraftbase$target.discard();
        this.secretcraftbase$target = null;
    }

    public boolean secretcraftbase$isUsingFrogTongue() {
        return this.secretcraftbase$toungeTicks > 18;
    }

    public void secretcraftbase$setFrogTongueLength(float frogTongueLength) {
        getDataTracker().set(secretcraftbase$TONGUE_LENGTH, frogTongueLength);
    }

    public float secretcraftbase$getFrogTongueLength() {
        return getDataTracker().get(secretcraftbase$TONGUE_LENGTH);
    }
}

