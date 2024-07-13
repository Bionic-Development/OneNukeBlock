package de.takacick.onegirlboyblock.registry.entity.living;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TurboBoardEntity extends PathAwareEntity {

    private int flyTime;
    public int itemDamage;

    private Vec3d prevPos;

    public TurboBoardEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    @Override
    public void tick() {
        if (!getWorld().isClient) {
            if (this.prevPos != null && this.prevPos.distanceTo(this.getPos()) > 0.01 && hasPassengers()) {
                this.flyTime++;

                if (this.flyTime > 0 && this.flyTime % 20 == 0) {
                    this.flyTime = 0;
                    this.itemDamage++;

                    if (this.itemDamage > ItemRegistry.TURBO_BOARD.getDefaultStack().getMaxDamage()) {
                        EventHandler.sendEntityStatus(getWorld(), this, OneGirlBoyBlock.IDENTIFIER, 7, 0);
                        this.discard();
                    }
                }
            }
        }

        this.prevPos = getPos();

        super.tick();
    }

    @Override
    protected void updatePostDeath() {

    }

    @Override
    public void onDeath(DamageSource damageSource) {

        EventHandler.sendEntityStatus(getWorld(), this, OneGirlBoyBlock.IDENTIFIER, 7, 0);
        this.discard();

        super.onDeath(damageSource);
    }

    @Override
    public void kill() {
        this.discard();
    }

    public boolean isLogicalSideForUpdatingMovement() {
        LivingEntity livingEntity = this.getControllingPassenger();
        if (livingEntity instanceof PlayerEntity playerEntity && !isDead()) {
            return playerEntity.isMainPlayer();
        }
        return this.canMoveVoluntarily();
    }

    @Override
    protected int getNextAirUnderwater(int air) {
        return air;
    }

    public HitResult raycast(double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = this.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = this.getRotationVector(90f, getYaw());
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return this.getWorld().raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, this));
    }

    @Override
    public void travel(Vec3d movementInput) {
        LivingEntity entity = this;

        LivingEntity livingEntity = this.hasPassengers() ? (LivingEntity) this.getPassengerList().get(0) : null;
        if (livingEntity == null || isDead()) {

            this.setYaw(getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(getPitch() * 0.5F);
            this.setRotation(this.getYaw(), this.getPitch());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;
            float f = sidewaysSpeed * 0.1F;
            float g = forwardSpeed * 0.1F;

            if (this.isOnGround() && !this.jumping) {
                f = 0.0F;
                g = 0.0F;
            }

            double d = isDead() ? -1f : -0.1;
            double h;
            if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                h = d + (double) ((float) (this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
            } else {
                h = d;
            }

            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, h, vec3d.z);
            this.velocityDirty = true;

            if (g > 0.0F) {
                float i = MathHelper.sin(this.getYaw() * 0.017453292F);
                float j = MathHelper.cos(this.getYaw() * 0.017453292F);
                this.setVelocity(this.getVelocity().add(-0.4F * i, 0.0D, 0.4F * j));
            }

            if (this.isLogicalSideForUpdatingMovement()) {
                this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * (entity.isOnGround() ? 1 : 0.25f));
                super.travel(new Vec3d(f, movementInput.y, g));
            } else {
                super.travel(new Vec3d(f, movementInput.y, g));
            }
        } else {
            this.setYaw(livingEntity.getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(livingEntity.getPitch() * 0.5F);
            this.setRotation(this.getYaw(), this.getPitch());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;
            float f = livingEntity.sidewaysSpeed * 0.15F;
            float g = livingEntity.forwardSpeed * 0.15f;

            double y = livingEntity.getRotationVector().getY();

            double d = g > 0f ? y * 0.65 : 0;
            double h;
            if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                h = d + (double) ((float) (this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
            } else {
                h = d;
            }

            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, h, vec3d.z);

            updateVelocity(1f, new Vec3d(f, movementInput.y, g));
            this.velocityDirty = true;

            if (this.isLogicalSideForUpdatingMovement()) {
                this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                super.travel(new Vec3d(f, movementInput.y, g));
            } else if (livingEntity instanceof PlayerEntity) {
                this.setVelocity(Vec3d.ZERO);
            }
            this.tryCheckBlockCollision();
        }
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {

        if (player.isSneaking()) {
            if (hasControllingPassenger()) {
                return ActionResult.FAIL;
            }

            if (!getWorld().isClient) {
                ItemStack itemStack = ItemRegistry.TURBO_BOARD.getDefaultStack();
                itemStack.setDamage(this.itemDamage);
                player.getInventory().offerOrDrop(itemStack);
                this.discard();
                getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.AMBIENT, 1f, 1f);
            }

            return ActionResult.SUCCESS;
        }

        if (!hasPassengers()) {
            player.startRiding(this, true);

            return ActionResult.SUCCESS;
        }

        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public boolean shouldDismountUnderwater() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }
}