package de.takacick.secretgirlbase.registry.entity.custom;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.access.LeadCuffProperties;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireworkTimeBombEntity extends Entity {

    protected static final TrackedData<Boolean> IGNITED = DataTracker.registerData(FireworkTimeBombEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> LAUNCHED = DataTracker.registerData(FireworkTimeBombEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EXPLODING = DataTracker.registerData(FireworkTimeBombEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Direction> RIDER_DIRECTION = DataTracker.registerData(FireworkTimeBombEntity.class, TrackedDataHandlerRegistry.FACING);
    private int ignite = 199;
    private int exploding = 201;
    private int explode = 0;

    public FireworkTimeBombEntity(EntityType<? extends FireworkTimeBombEntity> entityType, World world) {
        super(entityType, world);
    }

    public FireworkTimeBombEntity(World world, double x, double y, double z) {
        this(EntityRegistry.FIREWORK_TIME_BOMB, world);
        this.setPosition(x, y, z);
    }

    public static FireworkTimeBombEntity create(EntityType<FireworkTimeBombEntity> entityType, World world) {
        return new FireworkTimeBombEntity(entityType, world);
    }

    @Override
    public boolean isCollidable() {
        return !isExploding();
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(IGNITED, false);
        getDataTracker().startTracking(LAUNCHED, false);
        getDataTracker().startTracking(EXPLODING, false);
        getDataTracker().startTracking(RIDER_DIRECTION, Direction.NORTH);
    }

    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        Vec3d offset = Vec3d.of(getRiderDirection().getVector());

        Vec3d vec3d = this.getPos().add(offset.multiply(0.53));

        positionUpdater.accept(passenger, vec3d.x, vec3d.y + 0.125, vec3d.z);
    }

    public void tick() {
        super.tick();

        if (!getWorld().isClient) {
            if (!hasPassengers()) {
                getWorld().getOtherEntities(this, getBoundingBox().expand(0.2)).forEach(entity -> {
                    if (entity instanceof LeadCuffProperties leadCuffProperties && leadCuffProperties.isLeadCuffed()) {
                        leadCuffProperties.setLeadCuffed(null);
                        forceRiding(entity);
                    }
                });
            }

            if (isExploding()) {
                if (this.exploding > 0) {
                    this.exploding--;
                }
                BionicUtils.sendEntityStatus(getWorld(), this, SecretGirlBase.IDENTIFIER, 9);

                if (this.exploding <= 0) {
                    this.discard();
                }
            } else if (this.isIgnited()) {
                int seconds = (this.ignite / 20) + 1;

                if (this.explode > 0) {
                    this.explode--;

                    if (this.explode <= 0) {
                        getPassengerList().forEach(entity -> {
                            if (entity instanceof LivingEntity livingEntity) {
                                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 0, false, false, true));
                            }
                            entity.addVelocity(getVelocity().multiply(0, 0.5, 0));
                            entity.velocityModified = true;
                        });

                        BionicUtils.sendEntityStatus(getWorld(), this, SecretGirlBase.IDENTIFIER, 8);
                        setExploding(true);
                        setVelocity(0, 0, 0);
                        this.velocityModified = true;
                        removeAllPassengers();
                    }
                } else if (this.ignite > 0) {
                    this.ignite--;

                    TitleFadeS2CPacket titleFadeS2CPacket = new TitleFadeS2CPacket(0, 10, 0);
                    TitleS2CPacket titleS2CPacket = new TitleS2CPacket(Text.of("§4" + seconds));
                    SubtitleS2CPacket subtitleS2CPacket = new SubtitleS2CPacket(Text.of(""));

                    getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                        serverPlayerEntity.networkHandler.sendPacket(titleFadeS2CPacket);
                        serverPlayerEntity.networkHandler.sendPacket(titleS2CPacket);
                        serverPlayerEntity.networkHandler.sendPacket(subtitleS2CPacket);
                    });

                    if (getWorld().getRandom().nextDouble() <= 0.6) {
                        getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 5f, 1f + getWorld().getRandom().nextFloat() * 0.5f);
                    }

                    if (this.ignite <= 0) {
                        this.explode = 70;
                        setLaunched(true);
                        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 5.0f, 1.0f);
                    }
                }
            }
        } else {
            if (!isExploding()) {
                if (isLaunched()) {
                    for (int i = 0; i < 15; i++) {
                        double x = this.random.nextGaussian();
                        double z = this.random.nextGaussian();

                        this.getWorld().addImportantParticle(ParticleTypes.FIREWORK, true, this.getX() + x * 0.4, this.getLerpedPos(getWorld().getRandom().nextFloat()).getY(), this.getZ() + z * 0.4,
                                x * 0.05, -this.getVelocity().y * 0.5, z * 0.05);
                    }
                } else if (isIgnited()) {
                    for (int i = 0; i < 4; i++) {
                        double x = this.random.nextGaussian();
                        double z = this.random.nextGaussian();

                        this.getWorld().addImportantParticle(ParticleTypes.SMOKE, true,
                                this.getX() + x * 0.5, this.getLerpedPos(getWorld().getRandom().nextFloat()).getY(), this.getZ() + z * 0.5,
                                x * 0.05, -this.getVelocity().y * 0.5, z * 0.05);
                    }
                }
            }
        }

        if (isLaunched()) {
            addVelocity(0, 0.1, 0);
        }

        if (!isExploding()) {
            this.prevX = this.getX();
            this.prevY = this.getY();
            this.prevZ = this.getZ();
            Vec3d vec3d = this.getVelocity();
            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
            }
            if (this.getWorld().isClient) {
                this.noClip = false;
            } else {
                boolean bl = this.noClip = !this.getWorld().isSpaceEmpty(this, this.getBoundingBox().contract(1.0E-7));
                if (this.noClip) {
                    this.pushOutOfBlocks(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
                }
            }
            if (!this.isOnGround() || this.getVelocity().horizontalLengthSquared() > (double) 1.0E-5f || (this.age + this.getId()) % 4 == 0) {
                this.move(MovementType.SELF, this.getVelocity());
                float g = 0.98f;
                if (this.isOnGround()) {
                    g = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock().getSlipperiness() * 0.98f;
                }
                this.setVelocity(this.getVelocity().multiply(g, 0.98, g));
                if (this.isOnGround()) {
                    Vec3d vec3d2 = this.getVelocity();
                    if (vec3d2.y < 0.0) {
                        this.setVelocity(vec3d2.multiply(1.0, -0.5, 1.0));
                    }
                }
            }
            this.velocityDirty |= this.updateWaterState();
            if (!this.getWorld().isClient && this.getVelocity().subtract(vec3d).lengthSquared() > 0.01) {
                this.velocityDirty = true;
            }
        }
    }

    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0D;
        if (Double.isNaN(d)) {
            d = 4.0D;
        }

        d *= 64.0D;
        return distance < d * d;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("ignited", NbtElement.BYTE_TYPE)) {
            setIgnited(nbt.getBoolean("ignited"));
        }

        if (nbt.contains("launched", NbtElement.BYTE_TYPE)) {
            setLaunched(nbt.getBoolean("launched"));
        }

        if (nbt.contains("exploding", NbtElement.BYTE_TYPE)) {
            setExploding(nbt.getBoolean("exploding"));
        }
        if (nbt.contains("riderDirection", NbtElement.INT_TYPE)) {
            int id = nbt.getInt("riderDirection");
            if (id < Direction.values().length && id >= 0) {
                setRiderDirection(Direction.values()[id]);
            }
        }
        if (nbt.contains("ignite", NbtElement.INT_TYPE)) {
            this.ignite = nbt.getInt("ignite");
        }

        if (nbt.contains("exploding", NbtElement.INT_TYPE)) {
            this.exploding = nbt.getInt("exploding");
        }

        if (nbt.contains("explode", NbtElement.INT_TYPE)) {
            this.explode = nbt.getInt("explode");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("ignited", isIgnited());
        nbt.putBoolean("launched", isLaunched());
        nbt.putBoolean("exploding", isExploding());
        nbt.putInt("riderDirection", getRiderDirection().getId());
        nbt.putInt("ignite", this.ignite);
        nbt.putInt("exploding", this.exploding);
        nbt.putInt("explode", this.explode);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        ItemStack itemStack = player.getStackInHand(hand);

        if (itemStack.isIn(ItemTags.CREEPER_IGNITERS)) {
            if (!isIgnited()) {
                SoundEvent soundEvent = itemStack.isOf(Items.FIRE_CHARGE) ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.ITEM_FLINTANDSTEEL_USE;
                this.getWorld().playSound(player, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundCategory(), 1.0f, this.random.nextFloat() * 0.4f + 0.8f);
                if (!this.getWorld().isClient) {
                    this.setIgnited(true);
                    if (!itemStack.isDamageable()) {
                        itemStack.decrement(1);
                    } else {
                        itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
                    }
                }

                return ActionResult.success(this.getWorld().isClient);
            }
        }

        return super.interact(player, hand);
    }

    public void forceRiding(Entity entity) {
        double closest = 100;
        Direction closestDir = null;

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().equals(Direction.Axis.Y)) {
                continue;
            }

            Vec3d offset = Vec3d.of(direction.getVector());
            Vec3d vec3d = this.getPos().add(offset.multiply(0.53));

            double distance = vec3d.distanceTo(entity.getPos());

            if (closestDir == null || closest > distance) {
                closestDir = direction;
                closest = distance;
            }
        }

        setRiderDirection(closestDir);

        entity.startRiding(this, true);
        entity.setHeadYaw(getRiderDirection().asRotation());
        entity.setYaw(getRiderDirection().asRotation());
        entity.getWorld().playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.AMBIENT, 1f, 1f);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    public void setIgnited(boolean ignited) {
        getDataTracker().set(IGNITED, ignited);
    }

    public boolean isIgnited() {
        return getDataTracker().get(IGNITED);
    }

    public void setLaunched(boolean launched) {
        getDataTracker().set(LAUNCHED, launched);
    }

    public boolean isLaunched() {
        return getDataTracker().get(LAUNCHED);
    }

    public void setExploding(boolean exploding) {
        getDataTracker().set(EXPLODING, exploding);
    }

    public boolean isExploding() {
        return getDataTracker().get(EXPLODING);
    }

    public void setRiderDirection(Direction direction) {
        getDataTracker().set(RIDER_DIRECTION, direction == null ? Direction.NORTH : direction);
    }

    public Direction getRiderDirection() {
        return getDataTracker().get(RIDER_DIRECTION);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return !isExploding() && super.shouldRender(cameraX, cameraY, cameraZ);
    }
}