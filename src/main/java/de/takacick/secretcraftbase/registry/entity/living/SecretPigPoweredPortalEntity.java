package de.takacick.secretcraftbase.registry.entity.living;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.entity.living.brain.PigLookControl;
import de.takacick.secretcraftbase.registry.particles.ColoredParticleEffect;
import de.takacick.secretcraftbase.server.commands.SecretCraftBaseCommand;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SecretPigPoweredPortalEntity extends PigEntity {

    private static final TrackedData<Boolean> POWERED = DataTracker.registerData(SecretPigPoweredPortalEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> MOUTH_HEIGHT = DataTracker.registerData(SecretPigPoweredPortalEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private float mouthHeight = 0f;
    private float prevMouthHeight = 0f;

    public SecretPigPoweredPortalEntity(EntityType<? extends PigEntity> entityType, World world) {
        super(entityType, world);

        this.lookControl = new PigLookControl(this);
    }

    @Override
    public void setPitch(float pitch) {
        super.setPitch(pitch);
    }

    @Override
    protected void initDataTracker() {

        getDataTracker().startTracking(POWERED, false);
        getDataTracker().startTracking(MOUTH_HEIGHT, 0f);

        super.initDataTracker();
    }

    @Override
    public void tick() {
        this.prevMouthHeight = this.mouthHeight;
        if (isPowered()) {
            this.mouthHeight = MathHelper.clamp(this.mouthHeight + 0.1f, 0, 1);
        } else {
            this.mouthHeight = MathHelper.clamp(this.mouthHeight - 0.2f, 0, 1);
        }

        if (getWorld().isClient) {
            if ((getMouthHeight() - this.mouthHeight) > 0.1) {
                this.mouthHeight = getMouthHeight();
                this.prevMouthHeight = this.mouthHeight;
            }

            if (isPowered()) {
                float height = getMouthHeight(0.5f) / 4f;

                Vec3d rot = getHorizontalRotationVector().multiply(0.875);
                Vec3d sideways = getSidewaysRotationVector().multiply(0.525);
                Vec3d portalPos = getPos().add(0, 0.5625 * getHeight() / 0.9f, 0).add(rot);

                for (int j = 0; j < height; j++) {
                    if (j > 0) {
                        portalPos = portalPos.add(0, 0.25 * getHeight() / 0.9f, 0);
                    }

                    if (random.nextInt(600) == 0) {
                        getWorld().playSound(portalPos.getX(), portalPos.getY(), portalPos.getZ(), SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5f, random.nextFloat() * 0.4f + 0.8f, false);
                    }
                    if (getWorld().getRandom().nextDouble() <= 0.15) {
                        for (int i = 0; i < 1; ++i) {
                            double d = portalPos.getX() + sideways.getX() * getRandom().nextGaussian() * 0.5;
                            double e = portalPos.getY();
                            double f = portalPos.getZ() + sideways.getZ() * getRandom().nextGaussian() * 0.5;
                            double x = random.nextDouble() * 1 * rot.getX();
                            double y = random.nextGaussian() * 0.2;
                            double z = random.nextDouble() * 1 * rot.getZ();

                            getWorld().addParticle(ParticleTypes.PORTAL, d, e, f, x, y, z);
                        }
                    }
                }

                if (getWorld().getRandom().nextDouble() <= 0.25) {
                    Vec3d pos = getPos().add(0, 0.625 * getHeight() / 0.9f, 0).add(sideways);

                    getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.FOLLOW_DUST, DustParticleEffect.RED, 0.5f, getId()), pos.getX(), pos.getY(), pos.getZ(),
                            0.0, 0.0, 0.0);
                }
            }
        } else {
            if (isPowered()) {
                Vec3d rot = getHorizontalRotationVector().multiply(0.875);
                Vec3d portalPos = getPos().add(0, 0.5625 * getHeight() / 0.9f, 0).add(rot);
                float height = getMouthHeight(0.5f) / 4f;

                for (int j = 0; j < height; j++) {
                    if (j > 0) {
                        portalPos = portalPos.add(0, 0.25 * getHeight() / 0.9f, 0);
                    }

                    getWorld().getOtherEntities(this, new Box(portalPos, portalPos).expand(0.25)).forEach(SecretCraftBaseCommand::teleport);
                }
            }
        }

        super.tick();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        if (isPowered()) {
            return;
        }

        super.takeKnockback(strength, x, z);
    }

    @Override
    public boolean canMoveVoluntarily() {
        return super.canMoveVoluntarily();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (isPowered()) {
            movementInput = movementInput.multiply(0);
        }

        super.travel(movementInput);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!getWorld().isClient) {
            setPowered(!isPowered());

            BionicUtils.sendEntityStatus(getWorld(), this, SecretCraftBase.IDENTIFIER, isPowered() ? 3 : 4);
        }

        return ActionResult.SUCCESS;
    }

    public void setPowered(boolean powered) {
        getDataTracker().set(POWERED, powered);
    }

    public boolean isPowered() {
        return getDataTracker().get(POWERED);
    }

    public void setMouthHeight(float mouthHeight) {
        getDataTracker().set(MOUTH_HEIGHT, mouthHeight);
    }

    public float getMouthHeight() {
        return getDataTracker().get(MOUTH_HEIGHT);
    }

    public float getMouthHeight(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevMouthHeight, this.mouthHeight) * 16f * 6f;
    }

    public Vec3d getSidewaysRotationVector() {
        return this.getRotationVector(0, this.bodyYaw + 90);
    }

    public Vec3d getHorizontalRotationVector() {
        return this.getRotationVector(0, this.getHeadYaw());
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isPowered();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {

        nbt.putBoolean("powered", isPowered());
        nbt.putFloat("mouthHeight", getMouthHeight());

        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {

        setPowered(nbt.getBoolean("powered"));
        setMouthHeight(nbt.getFloat("mouthHeight"));

        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }
}
