package de.takacick.onegirlfriendblock.registry.entity.living;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.server.oneblock.OneBlockServerState;
import de.takacick.utils.BionicUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Optional;

public class GirlfriendEntity extends PathAwareEntity {

    private static final TrackedData<Integer> FUSE = DataTracker.registerData(GirlfriendEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final int DEFAULT_FUSE = 160;
    private int lastFuseTime = DEFAULT_FUSE;
    private int currentFuseTime = DEFAULT_FUSE;

    public GirlfriendEntity(EntityType<? extends GirlfriendEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(FUSE, DEFAULT_FUSE);
    }

    @Override
    public void tick() {
        this.addVelocity(0, 0.001, 0);

        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.4, 0.0));
        }

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.85));

        int i = this.getFuse() - 1;
        this.setFuse(i);

        if (!getWorld().isClient) {
            if (i == 160 - 128) {
                getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.PLAYERS, 1f, 1.2f);
            }
        }

        this.lastFuseTime = this.currentFuseTime;
        this.currentFuseTime--;

        if(Math.abs(this.currentFuseTime - i) > 2) {
            this.currentFuseTime = i;
        }

        if (i <= 0) {
            if (!this.getWorld().isClient) {
                this.explode();
            }
        }
    }

    public void explode() {
        if (!(getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        OneBlockServerState.getServerState(serverWorld.getServer()).ifPresent(serverState -> {
            Optional.ofNullable(serverState.getGirlfriendTeleport()).ifPresent(uuid -> {
                ServerPlayerEntity target = serverWorld.getServer().getPlayerManager().getPlayer(uuid);
                if (target != null) {
                    target.teleport(serverWorld, getX(), getY(), getZ(), getYaw(), getPitch());
                    target.changeGameMode(GameMode.SURVIVAL);
                    sendTeleportGirlfriend(serverWorld, target, getPos());
                }
            });
        });

        BionicUtils.sendEntityStatus(serverWorld, this, OneGirlfriendBlock.IDENTIFIER, 21);
        this.discard();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (amount > 10000) {
            discard();
        }

        return false;
    }

    public float getClientFuseTime(float timeDelta) {
        return MathHelper.lerp(timeDelta, (float) this.lastFuseTime, (float) this.currentFuseTime);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Age", (short) this.age);
        nbt.putShort("Fuse", (short) this.getFuse());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.age = nbt.getInt("Age");
        if (nbt.contains("Fuse", NbtCompound.SHORT_TYPE)) {
            this.setFuse(nbt.getShort("Fuse"));
        }
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public static void sendTeleportGirlfriend(World world, LivingEntity livingEntity, Vec3d pos) {
        if (world.getServer() == null) {
            return;
        }

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeVec3d(pos);
        packetByteBuf.writeInt(livingEntity.getId());

        world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(OneGirlfriendBlock.MOD_ID, "girlfriendteleport"), packetByteBuf);

        });
    }
}