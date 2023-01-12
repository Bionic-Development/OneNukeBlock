package de.takacick.immortalmobs.registry.entity.dragon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.feature.EndPortalFeature;

public class CustomDragonEntity extends MobEntity implements Monster {
    public static final TrackedData<Integer> PHASE_TYPE = DataTracker.registerData(CustomDragonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final double[][] segmentCircularBuffer = new double[64][3];
    public final CustomDragonPart head = new CustomDragonPart(this, "head", 1.0F, 1.0F);
    private final CustomDragonPart[] parts;
    private final CustomDragonPart neck = new CustomDragonPart(this, "neck", 3.0F, 3.0F);
    private final CustomDragonPart body = new CustomDragonPart(this, "body", 5.0F, 3.0F);
    private final CustomDragonPart tail1 = new CustomDragonPart(this, "tail", 2.0F, 2.0F);
    private final CustomDragonPart tail2 = new CustomDragonPart(this, "tail", 2.0F, 2.0F);
    private final CustomDragonPart tail3 = new CustomDragonPart(this, "tail", 2.0F, 2.0F);
    private final CustomDragonPart rightWing = new CustomDragonPart(this, "wing", 4.0F, 2.0F);
    private final CustomDragonPart leftWing = new CustomDragonPart(this, "wing", 4.0F, 2.0F);

    public int latestSegment = -1;
    public float prevWingPosition;
    public float wingPosition;
    public boolean slowedDownByBlock;
    public float yawAcceleration;
    public int ticksSinceDeath;

    public CustomDragonEntity(EntityType<? extends CustomDragonEntity> entityType, World world) {
        super(entityType, world);
        this.parts = new CustomDragonPart[]{this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.rightWing, this.leftWing};
        this.setHealth(this.getMaxHealth());
        this.noClip = true;
        this.ignoreCameraFrustum = true;
    }

    public static DefaultAttributeContainer.Builder createEnderDragonAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D);
    }

    public boolean hasWings() {
        float f = MathHelper.cos(this.wingPosition * 6.2831855F);
        float g = MathHelper.cos(this.prevWingPosition * 6.2831855F);
        return g <= -0.3F && f >= -0.3F;
    }

    public void addFlapEffects() {
        if (this.world.isClient && !this.isSilent()) {
            this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, this.getSoundCategory(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
        }

    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(PHASE_TYPE, PhaseType.HOVER.getTypeId());
    }

    public double[] getSegmentProperties(int segmentNumber, float tickDelta) {
        if (this.isDead()) {
            tickDelta = 0.0F;
        }

        tickDelta = 1.0F - tickDelta;
        int i = this.latestSegment - segmentNumber & 63;
        int j = this.latestSegment - segmentNumber - 1 & 63;
        double[] ds = new double[3];
        double d = this.segmentCircularBuffer[i][0];
        double e = MathHelper.wrapDegrees(this.segmentCircularBuffer[j][0] - d);
        ds[0] = d + e * (double) tickDelta;
        d = this.segmentCircularBuffer[i][1];
        e = this.segmentCircularBuffer[j][1] - d;
        ds[1] = d + e * (double) tickDelta;
        ds[2] = MathHelper.lerp((double) tickDelta, this.segmentCircularBuffer[i][2], this.segmentCircularBuffer[j][2]);
        return ds;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    public void tickMovement() {
        this.addAirTravelEffects();
        if (this.world.isClient) {
            this.setHealth(this.getHealth());
        }

        this.prevWingPosition = this.wingPosition;
        float i;
        if (this.isDead()) {
            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            i = (this.random.nextFloat() - 0.5F) * 4.0F;
            float h = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle(ParticleTypes.EXPLOSION, this.getX() + (double) f, this.getY() + 2.0D + (double) i, this.getZ() + (double) h, 0.0D, 0.0D, 0.0D);
        } else {
            Vec3d vec3d = this.getVelocity();
            i = 0.2F / ((float) vec3d.horizontalLength() * 10.0F + 1.0F);
            i *= (float) Math.pow(2.0D, vec3d.y);
            if (this.slowedDownByBlock) {
                this.wingPosition += i * 0.5F;
            } else {
                this.wingPosition += i;
            }

            this.setYaw(MathHelper.wrapDegrees(this.getYaw()));
            if (this.isAiDisabled()) {
                this.wingPosition = 0.5F;
            } else {
                if (this.latestSegment < 0) {
                    for (int j = 0; j < this.segmentCircularBuffer.length; ++j) {
                        this.segmentCircularBuffer[j][0] = (double) this.getYaw();
                        this.segmentCircularBuffer[j][1] = this.getY();
                    }
                }

                if (++this.latestSegment == this.segmentCircularBuffer.length) {
                    this.latestSegment = 0;
                }

                this.segmentCircularBuffer[this.latestSegment][0] = (double) this.getYaw();
                this.segmentCircularBuffer[this.latestSegment][1] = this.getY();
                double m;
                double n;
                double o;
                float aj;
                float al;
                float ak;
                if (this.world.isClient) {
                    if (this.bodyTrackingIncrements > 0) {
                        double d = this.getX() + (this.serverX - this.getX()) / (double) this.bodyTrackingIncrements;
                        m = this.getY() + (this.serverY - this.getY()) / (double) this.bodyTrackingIncrements;
                        n = this.getZ() + (this.serverZ - this.getZ()) / (double) this.bodyTrackingIncrements;
                        o = MathHelper.wrapDegrees(this.serverYaw - (double) this.getYaw());
                        this.setYaw(this.getYaw() + (float) o / (float) this.bodyTrackingIncrements);
                        this.setPitch(this.getPitch() + (float) (this.serverPitch - (double) this.getPitch()) / (float) this.bodyTrackingIncrements);
                        --this.bodyTrackingIncrements;
                        this.setPosition(d, m, n);
                        this.setRotation(this.getYaw(), this.getPitch());
                    }
                } else {

                }

                this.bodyYaw = this.getYaw();
                Vec3d[] vec3ds = new Vec3d[this.parts.length];

                for (int x = 0; x < this.parts.length; ++x) {
                    vec3ds[x] = new Vec3d(this.parts[x].getX(), this.parts[x].getY(), this.parts[x].getZ());
                }

                float y = (float) (this.getSegmentProperties(5, 1.0F)[1] - this.getSegmentProperties(10, 1.0F)[1]) * 10.0F * 0.017453292F;
                float z = MathHelper.cos(y);
                float aa = MathHelper.sin(y);
                float ab = this.getYaw() * 0.017453292F;
                float ac = MathHelper.sin(ab);
                float ad = MathHelper.cos(ab);
                this.movePart(this.body, (double) (ac * 0.5F), 0.0D, (double) (-ad * 0.5F));
                this.movePart(this.rightWing, (double) (ad * 4.5F), 2.0D, (double) (ac * 4.5F));
                this.movePart(this.leftWing, (double) (ad * -4.5F), 2.0D, (double) (ac * -4.5F));

                float ae = MathHelper.sin(this.getYaw() * 0.017453292F - this.yawAcceleration * 0.01F);
                float af = MathHelper.cos(this.getYaw() * 0.017453292F - this.yawAcceleration * 0.01F);
                float ag = this.getHeadVerticalMovement();
                this.movePart(this.head, (double) (ae * 6.5F * z), (double) (ag + aa * 6.5F), (double) (-af * 6.5F * z));
                this.movePart(this.neck, (double) (ae * 5.5F * z), (double) (ag + aa * 5.5F), (double) (-af * 5.5F * z));
                double[] ds = this.getSegmentProperties(5, 1.0F);

                int ah;
                for (ah = 0; ah < 3; ++ah) {
                    CustomDragonPart enderDragonPart = null;
                    if (ah == 0) {
                        enderDragonPart = this.tail1;
                    }

                    if (ah == 1) {
                        enderDragonPart = this.tail2;
                    }

                    if (ah == 2) {
                        enderDragonPart = this.tail3;
                    }

                    double[] es = this.getSegmentProperties(12 + ah * 2, 1.0F);
                    float ai = this.getYaw() * 0.017453292F + this.wrapYawChange(es[0] - ds[0]) * 0.017453292F;
                    aj = MathHelper.sin(ai);
                    ak = MathHelper.cos(ai);
                    al = 1.5F;
                    float am = (float) (ah + 1) * 2.0F;
                    this.movePart(enderDragonPart, (double) (-(ac * 1.5F + aj * am) * z), es[1] - ds[1] - (double) ((am + 1.5F) * aa) + 1.5D, (double) ((ad * 1.5F + ak * am) * z));
                }

                if (!this.world.isClient) {

                }

                for (ah = 0; ah < this.parts.length; ++ah) {
                    this.parts[ah].prevX = vec3ds[ah].x;
                    this.parts[ah].prevY = vec3ds[ah].y;
                    this.parts[ah].prevZ = vec3ds[ah].z;
                    this.parts[ah].lastRenderX = vec3ds[ah].x;
                    this.parts[ah].lastRenderY = vec3ds[ah].y;
                    this.parts[ah].lastRenderZ = vec3ds[ah].z;
                }

            }
        }
    }


    private void movePart(CustomDragonPart enderDragonPart, double dx, double dy, double dz) {
        enderDragonPart.setPosition(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
    }

    private float getHeadVerticalMovement() {
        double[] ds = this.getSegmentProperties(5, 1.0F);
        double[] es = this.getSegmentProperties(0, 1.0F);
        return (float) (ds[1] - es[1]);
    }

    public float getChangeInNeckPitch(int segmentOffset, double[] segment1, double[] segment2) {
        double h;
        BlockPos blockPos = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.ORIGIN);
        double d = Math.max(Math.sqrt(blockPos.getSquaredDistance(this.getPos())) / 4.0, 1.0);
        h = (double) segmentOffset / d;


        return (float) h;
    }

    @Override
    protected void updatePostDeath() {
        ++this.ticksSinceDeath;
        if (this.ticksSinceDeath >= 180 && this.ticksSinceDeath <= 200) {
            float f = (this.random.nextFloat() - 0.5f) * 8.0f;
            float g = (this.random.nextFloat() - 0.5f) * 4.0f;
            float h = (this.random.nextFloat() - 0.5f) * 8.0f;
            this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double) f, this.getY() + 2.0 + (double) g, this.getZ() + (double) h, 0.0, 0.0, 0.0);
        }

        boolean bl = this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT);

        if (this.world instanceof ServerWorld) {
            if (this.ticksSinceDeath == 1 && !this.isSilent()) {
                this.world.syncGlobalEvent(WorldEvents.ENDER_DRAGON_DIES, this.getBlockPos(), 0);
            }
        }
        this.move(MovementType.SELF, new Vec3d(0.0, 0.1f, 0.0));
        this.setYaw(this.getYaw() + 20.0f);
        this.bodyYaw = this.getYaw();
        if (this.ticksSinceDeath == 200 && this.world instanceof ServerWorld) {
            this.remove(Entity.RemovalReason.KILLED);
            this.emitGameEvent(GameEvent.ENTITY_DIE);
        }
    }


    private float wrapYawChange(double yawDegrees) {
        return (float) MathHelper.wrapDegrees(yawDegrees);
    }

    protected boolean parentDamage(DamageSource source, float amount) {
        return super.damage(source, amount);
    }

    public void kill() {
        this.remove(RemovalReason.KILLED);
    }

    public CustomDragonPart[] getBodyParts() {
        return this.parts;
    }

    public boolean collides() {
        return false;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDER_DRAGON_HURT;
    }

    protected float getSoundVolume() {
        return 5.0F;
    }

    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    public boolean canUsePortals() {
        return false;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    public boolean canTarget(LivingEntity target) {
        return target.canTakeDamage();
    }
}

