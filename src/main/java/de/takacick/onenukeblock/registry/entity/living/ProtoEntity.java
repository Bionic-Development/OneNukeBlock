package de.takacick.onenukeblock.registry.entity.living;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.mixin.LivingEntityAccessor;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProtoEntity extends WolfEntity {

    private int poopTimer = 200 - getRandom().nextInt(100) + 1200;

    public ProtoEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    public void tick() {

        if (!getWorld().isClient) {
            this.poopTimer--;

            if (this.poopTimer <= 0) {
                poop();
            }
        }

        super.tick();
    }

    public void poop() {
        this.poopTimer = 200 - getRandom().nextInt(100) + 1200;
        Vec3d rotation = getRotationVector(0, getYaw());
        setVelocity(rotation.multiply(0.2).add(0, 0.2, 0));
        this.velocityDirty = true;

        EventHandler.sendEntityStatus(getWorld(), this, OneNukeBlock.IDENTIFIER, 7, 0);

        Vec3d pos = getPos().add(0, getHeight() * 0.5, 0).add(rotation.multiply(-getWidth()));

        ItemEntity itemEntity = new ItemEntity(getWorld(), pos.getX(), pos.getY(), pos.getZ(), Items.TNT.getDefaultStack(),
                -rotation.getX() * 0.5, -rotation.getY() * 0.5 - 0.1, -rotation.getZ() * 0.5);
        getWorld().spawnEntity(itemEntity);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!isTamed()) {
            setTamed(true, true);
            setOwner(player);
        }

        if (player.isSneaking()) {
            if (this.isOwner(player)) {
                this.setSitting(!this.isSitting());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return ActionResult.SUCCESS;
            }
        }

        if (hasPassengers() || !this.isOwner(player)) {
            return ActionResult.FAIL;
        }

        player.startRiding(this, true);
        this.setSitting(false);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean tryAttack(Entity target) {

        boolean bl = super.tryAttack(target);

        if (bl) {
            EventHandler.sendEntityStatus(getWorld(), target, OneNukeBlock.IDENTIFIER, 6, 0);
        }

        return bl;
    }

    @Override
    public WolfEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (!this.isAlive()) {
            return;
        }

        if (!this.hasPassengers() || !(this.getFirstPassenger() instanceof LivingEntity livingEntity)) {
            super.travel(movementInput);
            return;
        }

        setJumping(((LivingEntityAccessor) livingEntity).isJumping());
        this.setYaw(livingEntity.getYaw());
        this.prevYaw = this.getYaw();
        this.setPitch(livingEntity.getPitch() * 0.5f);
        this.setRotation(this.getYaw(), this.getPitch());
        this.headYaw = this.bodyYaw = this.getYaw();
        float f = livingEntity.sidewaysSpeed * 0.5f;
        float g = livingEntity.forwardSpeed;
        if (g <= 0.0f) {
            g *= 0.25f;
        }
        if (!this.isOnGround() && !this.jumping) {
            f = 0.0f;
            g = 0.0f;
        }
        if (this.isOnGround() && this.jumping) {
            double d = 0.42f * this.getJumpVelocityMultiplier();
            double e = d + this.getJumpBoostVelocityModifier();
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, e, vec3d.z);
            this.velocityDirty = true;
            if (g > 0.0f) {
                float h = MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
                float i = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
                this.setVelocity(this.getVelocity().add(-0.4f * h, 0.0, 0.4f * i));
            }
        }

        if (this.isLogicalSideForUpdatingMovement()) {
            this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            super.travel(new Vec3d(f, movementInput.y, g));
        } else if (livingEntity instanceof PlayerEntity) {
            this.setVelocity(Vec3d.ZERO);
        }

        this.updateLimbs(false);
        this.tryCheckBlockCollision();
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof MobEntity mob) {
            return mob;
        }
        if (this.getFirstPassenger() instanceof PlayerEntity playerEntity) {
            return playerEntity;
        }
        return null;
    }

    public float getTailAngle() {
        if (this.hasAngerTime()) {
            return 1.5393804f;
        }

        float f = this.getMaxHealth();
        float g = (f - this.getHealth()) / f;
        return (0.55f - g * 0.4f) * (float) Math.PI;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {

        nbt.putInt("poopTimer", this.poopTimer);

        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {

        if (nbt.contains("poopTimer", NbtElement.INT_TYPE)) {
            this.poopTimer = nbt.getInt("poopTimer");
        }

        super.readCustomDataFromNbt(nbt);
    }

    public void setOwner(PlayerEntity player) {
        this.setTamed(true, true);
        this.setOwnerUuid(player.getUuid());
    }
}