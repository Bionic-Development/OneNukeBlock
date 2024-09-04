package de.takacick.onenukeblock.registry.entity.custom;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.fabric.internal.NBTConverter;
import com.sk89q.worldedit.internal.block.BlockStateIdAccess;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.onenukeblock.registry.entity.projectiles.PlacingBlockEntity;
import de.takacick.onenukeblock.utils.worldedit.WorldEditUtils;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.enginehub.linbus.tree.LinCompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkylandTntEntity extends Entity implements Ownable {

    private BlockPos center;
    private static final TrackedData<Boolean> EXPLODING = DataTracker.registerData(SkylandTntEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private String schematic = "";
    private final List<BlockPos> unmodifiedBlocks = new ArrayList<>();
    private int areaScale = -1;
    private int removing = -1;
    private int placing = -1;
    private int explodingDelay = 0;
    private boolean isPlacing = false;

    private static final TrackedData<Integer> FUSE = DataTracker.registerData(SkylandTntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final String FUSE_NBT_KEY = "fuse";
    private static final ExplosionBehavior TELEPORTED_EXPLOSION_BEHAVIOR = new ExplosionBehavior() {

        @Override
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            if (state.isOf(Blocks.NETHER_PORTAL)) {
                return false;
            }
            return super.canDestroyBlock(explosion, world, pos, state, power);
        }

        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                return Optional.empty();
            }
            return super.getBlastResistance(explosion, world, pos, blockState, fluidState);
        }
    };
    @Nullable
    private LivingEntity causingEntity;
    private boolean teleported;

    public SkylandTntEntity(EntityType<? extends SkylandTntEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public SkylandTntEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityRegistry.SKYLAND_TNT, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.setFuse(80);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FUSE, 80);
        builder.add(EXPLODING, false);
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    protected double getGravity() {
        return isExploding() ? 0.0 : 0.04;
    }

    @Override
    public void tick() {
        this.tickPortalTeleportation();
        this.applyGravity();
        this.move(MovementType.SELF, this.getVelocity());
        if (isExploding()) {
            setVelocity(getVelocity().getX(), getVelocity().getY() * 0.8, getVelocity().getZ());
        } else {
            this.setVelocity(isExploding() ? this.getVelocity().add(0, -1, 0) : this.getVelocity().multiply(0.98));
        }
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
        }
        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            if (!this.getWorld().isClient) {
                this.explode();
            }
        } else {
            this.updateWaterState();
            if (this.getWorld().isClient) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void explode() {
        Clipboard clipboard = WorldEditUtils.getClipboard(getSchematic()).orElse(null);
        if (clipboard == null) {
            this.discard();
            return;
        }

        if (!isExploding()) {
            this.explodingDelay = 10;


            this.getWorld().createExplosion(this, Explosion.createDamageSource(this.getWorld(), this), this.teleported ? TELEPORTED_EXPLOSION_BEHAVIOR : null, this.getX(), this.getBodyY(0.0625), this.getZ(), 4.0f, false, World.ExplosionSourceType.TNT);
            setExploding(true);

            BlockVector3 max = clipboard.getMaximumPoint();
            BlockVector3 min = clipboard.getMinimumPoint();
            this.center = getBlockPos().add(0, (int) (getAxisLength(max.y(), min.y()) * 0.5), 0);
            this.setVelocity(0, (int) (getAxisLength(max.y(), min.y()) * 0.15), 0);
            this.velocityDirty = true;

            if (this.areaScale < getAxisLength(max.y(), min.y())) {
                if (this.unmodifiedBlocks.isEmpty()) {
                    BlockPos maxPos = getMaxBlockPos(max, min);
                    BlockPos minPos = getMinBlockPos(max, min);

                    this.areaScale += 1;

                    for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
                        for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                            for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                                this.unmodifiedBlocks.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            }

            return;
        }

        if (this.explodingDelay > 0) {
            this.explodingDelay--;
            return;
        }

        EventHandler.sendEntityStatus(getWorld(), this, OneNukeBlock.IDENTIFIER, 4, 0);

        placingBlocks(clipboard);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putShort(FUSE_NBT_KEY, (short) this.getFuse());

        nbt.putInt("Age", (short) this.age);
        nbt.putBoolean("Exploding", this.isExploding());
        nbt.putInt("AreaScale", this.areaScale);
        nbt.putInt("Removing", this.removing);
        nbt.putInt("Placing", this.placing);
        nbt.putBoolean("isPlacing", this.isPlacing);
        if (getSchematic() != null && !getSchematic().isEmpty()) {
            nbt.putString("schematic", this.getSchematic());
        }
        if (this.center != null) {
            nbt.put("Center", NbtHelper.fromBlockPos(this.center));
        }
        if (this.unmodifiedBlocks != null && !this.unmodifiedBlocks.isEmpty()) {
            NbtList nbtElements = new NbtList();

            this.unmodifiedBlocks.forEach(blockPos -> {
                if (blockPos != null) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.put("blockPos", NbtHelper.fromBlockPos(blockPos));
                }
            });

            nbt.put("unmodifiedBlocks", nbtElements);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.setFuse(nbt.getShort(FUSE_NBT_KEY));

        this.age = nbt.getInt("Age");
        if (nbt.contains("AreaScale", NbtCompound.INT_TYPE)) {
            this.areaScale = nbt.getInt("AreaScale");
        }

        if (nbt.contains("schematic", NbtCompound.STRING_TYPE)) {
            this.setSchematic(nbt.getString("schematic"));
        }

        if (nbt.contains("Removing", NbtCompound.INT_TYPE)) {
            this.removing = nbt.getInt("Removing");
        }
        setExploding(nbt.getBoolean("Exploding"));
        if (nbt.contains("Placing", NbtCompound.INT_TYPE)) {
            this.placing = nbt.getInt("Placing");
        }
        if (nbt.contains("isPlacing", NbtCompound.BYTE_TYPE)) {
            this.isPlacing = nbt.getBoolean("isPlacing");
        }
        if (nbt.contains("Center", NbtCompound.COMPOUND_TYPE)) {
            this.center = NbtHelper.toBlockPos(nbt, "Center").orElse(BlockPos.ORIGIN);
        }
        if (nbt.contains("unmodifiedBlocks", NbtCompound.LIST_TYPE)) {
            nbt.getList("unmodifiedBlocks", NbtCompound.COMPOUND_TYPE).forEach(nbtElement -> {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    NbtHelper.toBlockPos(nbtCompound, "blockPos").ifPresent(blockPos -> {
                        this.unmodifiedBlocks.add(blockPos);
                    });
                }
            });
        }
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        return this.causingEntity;
    }

    @Override
    public void copyFrom(Entity original) {
        super.copyFrom(original);
        if (original instanceof SkylandTntEntity tntEntity) {
            this.causingEntity = tntEntity.causingEntity;
        }
    }

    public void setExploding(boolean exploding) {
        this.dataTracker.set(EXPLODING, exploding);
    }

    public boolean isExploding() {
        return this.dataTracker.get(EXPLODING);
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    private void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    @Override
    @Nullable
    public Entity teleportTo(TeleportTarget teleportTarget) {
        Entity entity = super.teleportTo(teleportTarget);
        if (entity instanceof SkylandTntEntity tntEntity) {
            tntEntity.setTeleported(true);
        }
        return entity;
    }

    public void setSchematic(String schematic) {
        if (this.getFuse() > 0) {
            this.schematic = schematic;
        }
    }

    public String getSchematic() {
        return this.schematic;
    }

    public void placingBlocks(Clipboard clipboard) {
        BlockVector3 max = clipboard.getMaximumPoint();
        BlockVector3 min = clipboard.getMinimumPoint();

        if (this.unmodifiedBlocks.isEmpty()) {
            if (this.placing < 0) {
                this.placing = 2;
            }

            if (this.placing > 0) {
                this.placing--;

                if (this.placing <= 0) {
                    this.discard();
                    return;
                }
            }
        } else {
            this.placing = -1;
        }

        BlockVector3 centerVector = getCenterBlockPos(max, min);

        boolean small = this.unmodifiedBlocks.size() < 10;

        List<BlockPos> modified = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            if (this.unmodifiedBlocks.isEmpty()) {
                break;
            }

            BlockPos offset = this.unmodifiedBlocks.get(getWorld().getRandom().nextInt(this.unmodifiedBlocks.size()));

            BaseBlock baseBlock = clipboard.getFullBlock(centerVector.add(offset.getX(), offset.getY(), offset.getZ()));

            BlockState blockState = toNative(baseBlock.toImmutableState());
            modified.add(offset);

            if (blockState.isAir()) {
                continue;
            }

            NbtCompound nbtCompound = toNative(baseBlock.getNbt());

            BlockPos blockPos = offset.add(center);
            final Vec3d pos = Vec3d.ofBottomCenter(blockPos).add(0, 0.375, 0);

            PlacingBlockEntity placingBlockEntity = new PlacingBlockEntity(EntityRegistry.PLACING_BLOCK, getWorld());
            placingBlockEntity.refreshPositionAndAngles(getX(), getBodyY(0.45), getZ(), 0f, 0f);

            if (nbtCompound != null) {
                placingBlockEntity.setNbtCompound(nbtCompound);
            }

            placingBlockEntity.setFromBlockPos(blockPos);
            placingBlockEntity.setBlockState(blockState);
            placingBlockEntity.initializeTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));

            getWorld().spawnEntity(placingBlockEntity);

            if (getWorld().getRandom().nextDouble() <= 0.4 || small) {
                //      BionicUtils.sendEntityStatus(getWorld(), placingBlockEntity, SecretCraftBase.IDENTIFIER, 10);
            }
        }

        this.unmodifiedBlocks.removeAll(modified);
    }


    public BlockState toNative(com.sk89q.worldedit.world.block.BlockState state) {
        int stateId = BlockStateIdAccess.getBlockStateId(state);
        return BlockStateIdAccess.isValidInternalId(stateId) ? Block.getStateFromRawId(stateId) : FabricAdapter.adapt(state);
    }

    public NbtCompound toNative(LinCompoundTag compoundTag) {
        return compoundTag != null ? NBTConverter.toNative(compoundTag) : null;
    }

    private BlockVector3 getCenterBlockPos(BlockVector3 max, BlockVector3 min) {
        int x = max.x() - min.x();
        int y = max.y() - min.y();
        int z = max.z() - min.z();

        return BlockVector3.at(
                max.x() - x / 2,
                max.y() - y / 2,
                max.z() - z / 2
        );
    }

    private BlockPos getMaxBlockPos(BlockVector3 max, BlockVector3 min) {
        int minX = max.x() - min.x();
        int minY = max.y() - min.y();
        int minZ = max.z() - min.z();

        return new BlockPos(
                minX / 2,
                minY / 2,
                minZ / 2
        );
    }

    private BlockPos getMinBlockPos(BlockVector3 max, BlockVector3 min) {
        int x = max.x() - min.x();
        int y = max.y() - min.y();
        int z = max.z() - min.z();

        if (x % 2 != 0) {
            x += 1;
        }
        if (y % 2 != 0) {
            y += 1;
        }
        if (z % 2 != 0) {
            z += 1;
        }
        return BlockPos.ofFloored(
                Math.ceil(-x / 2d),
                Math.ceil(-y / 2d),
                Math.ceil(-z / 2d)
        );
    }

    private double getAxisLength(double x1, double x2) {
        return Math.abs(x1 - x2);
    }

}

