package de.takacick.deathmoney.registry.entity.custom;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ItemRegistry;
import de.takacick.deathmoney.registry.ParticleRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class BlackMatterShockwaveEntity extends Entity {

    private LivingEntity owner;
    private Vec3d vec3d;

    public BlackMatterShockwaveEntity(EntityType<? extends BlackMatterShockwaveEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlackMatterShockwaveEntity(World world, Vec3d vec3d, double x, double y, double z) {
        this(EntityRegistry.BLACK_MATTER_SHOCKWAVE, world);
        this.setPosition(x, y, z);
        this.vec3d = vec3d;
        this.noClip = true;
    }

    public static BlackMatterShockwaveEntity create(EntityType<BlackMatterShockwaveEntity> entityType, World world) {
        return new BlackMatterShockwaveEntity(entityType, world);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
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

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    public void setVelocityClient(double x, double y, double z) {
        this.setVelocity(x, y, z);
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            double d = Math.sqrt(x * x + z * z);
            this.setYaw((float) (MathHelper.atan2(x, z) * 57.2957763671875D));
            this.setPitch((float) (MathHelper.atan2(y, d) * 57.2957763671875D));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
    }

    public void tick() {
        super.tick();

        if (!world.isClient) {
            if (age >= 80) {
                remove(RemovalReason.DISCARDED);
                return;
            }

             playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 0.5f, 1f + (float) world.getRandom().nextDouble() * 0.2f);
            ((ServerWorld) getEntityWorld()).spawnParticles(ParticleRegistry.BLACK_MATTER_EXPLOSION, getX(), getY(), getZ(), 1, 0.5, 0.5, 0.5, 0);

            world.getOtherEntities(owner, new Box(getPos(), getPos()).expand(6.3, 15, 6.3)).forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame()) {

                    if (livingEntity instanceof ServerPlayerEntity playerEntity) {
                        if (!playerEntity.isSpectator()) {
                            playerEntity.changeGameMode(GameMode.SPECTATOR);

                            Text text = Text.literal("§e" + playerEntity.getName().getString() + " §fhas been §celiminated §ffrom the §bdatabase");

                            playerEntity.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> serverPlayerEntity.sendMessage(text));
                        }
                    } else {
                        livingEntity.discard();
                    }
                }
            });

            DeathMoney.generateCircle(getBlockPos(), 8, false).forEach(blockPos -> {
                for (int y = -10; y < 10; y++) {
                    BlockState blockState = world.getBlockState(blockPos.add(0, y, 0));
                    if (!blockState.isAir() && !blockState.isOf(ItemRegistry.BLACK_MATTER)) {
                        world.setBlockState(blockPos.add(0, y, 0), ItemRegistry.BLACK_MATTER.getDefaultState(), ~Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS
                                | Block.FORCE_STATE | Block.REDRAW_ON_MAIN_THREAD | ~Block.NO_REDRAW | ~Block.SKIP_LIGHTING_UPDATES);
                    }
                }
            });


            move(MovementType.SELF, vec3d);
            velocityModified = true;
            velocityDirty = true;
        }
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    public boolean isAttackable() {
        return false;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}