package de.takacick.secretcraftbase.registry.entity.custom;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.fabric.internal.NBTConverter;
import com.sk89q.worldedit.internal.block.BlockStateIdAccess;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.particles.ColoredParticleEffect;
import de.takacick.secretcraftbase.server.datatracker.SchematicBox;
import de.takacick.secretcraftbase.server.datatracker.SecretCraftBaseTracker;
import de.takacick.secretcraftbase.server.utils.WorldChunkUtils;
import de.takacick.secretcraftbase.server.worldedit.WorldEditUtils;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSchematicEntity extends Entity {

    private BlockPos center;
    private static final TrackedData<Float> ROTATION_SPEED = DataTracker.registerData(AbstractSchematicEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> TARGET_HEIGHT = DataTracker.registerData(AbstractSchematicEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<SchematicBox> SCHEMATIC_BOX = BionicDataTracker.registerData(new Identifier(BionicUtils.MOD_ID, "schematic_box"), SecretCraftBaseTracker.SCHEMATIC_BOX);
    private float rotation;
    private float prevRotation;
    private float size;
    private float prevSize;

    private final List<BlockPos> unmodifiedBlocks = new ArrayList<>();
    private int areaScale = -1;
    private int removing = -1;
    private int placing = -1;
    private boolean isPlacing = false;

    public AbstractSchematicEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
        this.intersectionChecked = true;
        this.ignoreCameraFrustum = true;
    }

    public abstract int getColor();

    public abstract String getSchematic();

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(ROTATION_SPEED, 0f);
        getDataTracker().startTracking(TARGET_HEIGHT, -100f);
        getDataTracker().startTracking(SCHEMATIC_BOX, new SchematicBox());
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient) {
            this.setRotationSpeed(Math.min(getRotationSpeed() + 1f, 15));
            Clipboard clipboard = WorldEditUtils.getClipboard(getSchematic()).orElse(null);
            if (clipboard == null) {
                this.discard();
                return;
            }
            BlockVector3 max = clipboard.getMaximumPoint();
            BlockVector3 min = clipboard.getMinimumPoint();

            if (getTargetHeight() <= -100f || center == null) {
                float maxHeight = (max.getY() - min.getY()) / 2f;
                setTargetHeight((float) (maxHeight + getY()));
                center = getBlockPos().add(0, (int) maxHeight, 0);

                BlockPos maxPos = getMaxBlockPos(max, min);
                BlockPos minPos = getMinBlockPos(max, min);
                setSchematicBox(new SchematicBox(minPos.add(center), maxPos.add(center)));
            }

            if (getTargetHeight() <= getY() && this.size >= 1f) {
                if (this.isPlacing) {
                    placingBlocks(clipboard);
                } else {
                    removeBlocks(clipboard);
                }
            }
        }

        this.prevSize = this.size;
        if (getTargetHeight() > getY()) {
            this.addVelocity(0, 0.01, 0);
        } else {
            SchematicBox schematicBox = getSchematicBox();

            if (!getWorld().isClient) {
                if (this.size == 0f) {
                    getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.BLOCKS, 12f, 0.9f);
                }

                this.size = MathHelper.clamp(this.size + 0.1f, 0f, 1f);
                schematicBox.setSizeProgress(this.size);
                if (this.prevSize != this.size) {
                    setSchematicBox(schematicBox);
                }
            } else {
                if (schematicBox.getSizeProgress() > 0f) {
                    this.size = MathHelper.clamp(this.size + 0.1f, 0f, 1f);
                }

                if (Math.abs(schematicBox.getSizeProgress() - this.size) > 0.2f) {
                    this.size = schematicBox.getSizeProgress();
                }
            }
        }

        this.prevRotation = this.rotation;
        this.rotation += getRotationSpeed();

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.85));

        if (getWorld().isClient) {

            for (int j = 1; j < 2; j++) {
                double x = random.nextGaussian();
                double y = random.nextGaussian();
                double z = random.nextGaussian();

                getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FIREWORK, Vec3d.unpackRgb(getColor()).toVector3f()),
                        this.getX() + x * 0.1, this.getY() + y * 0.1, this.getZ() + z * 0.1,
                        x * 0.05, -random.nextDouble() * 0.2, z * 0.05);
            }
        }
    }

    public void removeBlocks(Clipboard clipboard) {
        BlockVector3 max = clipboard.getMaximumPoint();
        BlockVector3 min = clipboard.getMinimumPoint();

        if (this.areaScale < getAxisLength(max.getX(), min.getX())
                || this.areaScale < getAxisLength(max.getY(), min.getY())
                || this.areaScale < getAxisLength(max.getZ(), min.getZ())) {
            BlockPos maxPos = getMaxBlockPos(max, min);
            BlockPos minPos = getMinBlockPos(max, min);

            int prevAreaScale = areaScale;
            this.areaScale += 5;

            int prevMinSize = -prevAreaScale;
            int minSize = -this.areaScale;

            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int x = Math.max(minSize, minPos.getX()); x <= Math.min(this.areaScale, maxPos.getX()); x++) {
                    for (int z = Math.max(minSize, minPos.getZ()); z <= Math.min(this.areaScale, maxPos.getZ()); z++) {
                        if (z > Math.max(prevMinSize, minPos.getZ()) && z <= Math.min(prevAreaScale, maxPos.getZ())
                                && x > Math.max(prevMinSize, minPos.getX()) && x <= Math.min(prevAreaScale, maxPos.getX())) {
                            continue;
                        }

                        this.unmodifiedBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        } else if (this.unmodifiedBlocks.isEmpty()) {
            if (this.removing < 0) {
                this.removing = 20;
            }

            if (this.removing > 0) {
                this.removing--;

                if (this.removing <= 0) {
                    this.areaScale = -1;
                    this.isPlacing = true;
                    return;
                }
            }
        } else {
            this.removing = -1;
        }

        int breaking = Math.min(this.unmodifiedBlocks.size() / 5, 90);
        boolean small = this.unmodifiedBlocks.size() < 10;
        List<BlockPos> modified = new ArrayList<>();

        for (int i = 0; i < Math.max(15, breaking); i++) {
            if (this.unmodifiedBlocks.isEmpty()) {
                break;
            }

            BlockPos offset = this.unmodifiedBlocks.get(random.nextInt(this.unmodifiedBlocks.size()));
            BlockPos blockPos = offset.add(center);
            BlockState blockState = getWorld().getBlockState(blockPos);
            WorldChunkUtils.setBlockState(getWorld(), blockPos, Blocks.AIR.getDefaultState(), false);

            modified.add(offset);

            if (blockState.isAir()) {
                continue;
            }

            Vec3d pos = Vec3d.ofBottomCenter(blockPos);

            BreakingBlockEntity breakingBlockEntity = new BreakingBlockEntity(EntityRegistry.BREAKING_BLOCK, getWorld());
            breakingBlockEntity.refreshPositionAndAngles(pos.getX(), pos.getY() + 0.375, pos.getZ(), 0f, 0f);
            breakingBlockEntity.setFromBlockPos(blockPos);
            breakingBlockEntity.setBlockState(blockState);
            breakingBlockEntity.initializeTarget(new Vec3d(getX(), getY() + 1, getZ()));
            getWorld().spawnEntity(breakingBlockEntity);

            if (getWorld().getRandom().nextDouble() <= 0.3 || small) {
                BionicUtils.sendEntityStatus(getWorld(), breakingBlockEntity, SecretCraftBase.IDENTIFIER, 10);
            }
        }

        this.unmodifiedBlocks.removeAll(modified);
    }

    public void placingBlocks(Clipboard clipboard) {
        BlockVector3 max = clipboard.getMaximumPoint();
        BlockVector3 min = clipboard.getMinimumPoint();

        if (this.areaScale < getAxisLength(max.getY(), min.getY())) {
            if (this.unmodifiedBlocks.isEmpty()) {
                BlockPos maxPos = getMaxBlockPos(max, min);
                BlockPos minPos = getMinBlockPos(max, min);

                this.areaScale += 1;

                for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
                    for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                        this.unmodifiedBlocks.add(new BlockPos(x, minPos.getY() + this.areaScale, z));
                    }
                }
            }
        } else if (this.unmodifiedBlocks.isEmpty()) {
            if (this.placing < 0) {
                this.placing = 40;
            }

            if (this.placing > 0) {
                this.placing--;

                if (this.placing <= 0) {
                    this.finish();
                    return;
                }
            }
        } else {
            this.placing = -1;
        }

        int placing = Math.min(this.unmodifiedBlocks.size() / 5, 90);

        BlockVector3 centerVector = getCenterBlockPos(max, min);

        boolean small = this.unmodifiedBlocks.size() < 10;

        List<BlockPos> modified = new ArrayList<>();

        for (int i = 0; i < Math.max(15, placing); i++) {
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

            NbtCompound nbtCompound = toNative(baseBlock.getNbtData());

            BlockPos blockPos = offset.add(center);
            final Vec3d pos = Vec3d.ofBottomCenter(blockPos).add(0, 0.375, 0);

            PlacingBlockEntity placingBlockEntity = new PlacingBlockEntity(EntityRegistry.PLACING_BLOCK, getWorld());
            placingBlockEntity.refreshPositionAndAngles(getX(), getY() + 1, getZ(), 0f, 0f);

            if (nbtCompound != null) {
                placingBlockEntity.setNbtCompound(nbtCompound);
            }

            placingBlockEntity.setFromBlockPos(blockPos);
            placingBlockEntity.setBlockState(blockState);
            placingBlockEntity.initializeTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));

            getWorld().spawnEntity(placingBlockEntity);

            if (getWorld().getRandom().nextDouble() <= 0.4 || small) {
                BionicUtils.sendEntityStatus(getWorld(), placingBlockEntity, SecretCraftBase.IDENTIFIER, 10);
            }
        }

        this.unmodifiedBlocks.removeAll(modified);
    }

    public void unorganizedPlacingBlocks(Clipboard clipboard) {
        BlockVector3 max = clipboard.getMaximumPoint();
        BlockVector3 min = clipboard.getMinimumPoint();

        if (this.areaScale < getAxisLength(max.getX(), min.getX())
                || this.areaScale < getAxisLength(max.getY(), min.getY())
                || this.areaScale < getAxisLength(max.getZ(), min.getZ())) {

            BlockPos maxPos = getMaxBlockPos(max, min);
            BlockPos minPos = getMinBlockPos(max, min);

            int prevAreaScale = areaScale;
            this.areaScale += 5;

            int prevMinSize = -prevAreaScale;
            int minSize = -this.areaScale;

            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int x = Math.max(minSize, minPos.getX()); x <= Math.min(this.areaScale, maxPos.getX()); x++) {
                    for (int z = Math.max(minSize, minPos.getZ()); z <= Math.min(this.areaScale, maxPos.getZ()); z++) {
                        if (z > Math.max(prevMinSize, minPos.getZ()) && z <= Math.min(prevAreaScale, maxPos.getZ())
                                && x > Math.max(prevMinSize, minPos.getX()) && x <= Math.min(prevAreaScale, maxPos.getX())) {
                            continue;
                        }

                        this.unmodifiedBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        } else if (this.unmodifiedBlocks.isEmpty()) {
            if (this.placing < 0) {
                this.placing = 20;
            }

            if (this.placing > 0) {
                this.placing--;

                if (this.placing <= 0) {
                    this.finish();
                    return;
                }
            }
        } else {
            this.placing = -1;
        }

        int placing = Math.min(this.unmodifiedBlocks.size() / 5, 60);

        BlockVector3 centerVector = getCenterBlockPos(max, min);
        boolean small = this.unmodifiedBlocks.size() < 10;

        for (int i = 0; i < Math.max(15, placing); i++) {
            if (this.unmodifiedBlocks.isEmpty()) {
                break;
            }

            BlockPos offset = this.unmodifiedBlocks.get(getWorld().getRandom().nextInt(this.unmodifiedBlocks.size()));

            BlockState blockState = toNative(clipboard.getBlock(centerVector.add(offset.getX(), offset.getY(), offset.getZ())));
            this.unmodifiedBlocks.remove(offset);

            if (blockState.isAir()) {
                continue;
            }

            BlockPos blockPos = offset.add(center);
            Vec3d pos = Vec3d.ofBottomCenter(blockPos);

            PlacingBlockEntity placingBlockEntity = new PlacingBlockEntity(EntityRegistry.PLACING_BLOCK, getWorld());
            placingBlockEntity.refreshPositionAndAngles(getX(), getY() + 1, getZ(), 0f, 0f);
            placingBlockEntity.setFromBlockPos(blockPos);
            placingBlockEntity.setBlockState(blockState);
            placingBlockEntity.initializeTarget(new Vec3d(pos.getX(), pos.getY() + 0.375, pos.getZ()));

            getWorld().spawnEntity(placingBlockEntity);

            if (getWorld().getRandom().nextDouble() <= 0.4 || small) {
                BionicUtils.sendEntityStatus(getWorld(), placingBlockEntity, SecretCraftBase.IDENTIFIER, 10);
            }
        }
    }

    public BlockState toNative(com.sk89q.worldedit.world.block.BlockState state) {
        int stateId = BlockStateIdAccess.getBlockStateId(state);
        return BlockStateIdAccess.isValidInternalId(stateId) ? Block.getStateFromRawId(stateId) : FabricAdapter.adapt(state);
    }

    public NbtCompound toNative(CompoundTag compoundTag) {
        return compoundTag != null ? NBTConverter.toNative(compoundTag) : null;
    }

    private BlockPos getMinBlockPos(BlockVector3 max, BlockVector3 min) {
        int x = max.getBlockX() - min.getX();
        int y = max.getBlockY() - min.getY();
        int z = max.getBlockZ() - min.getZ();

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

    private BlockVector3 getCenterBlockPos(BlockVector3 max, BlockVector3 min) {
        int x = max.getBlockX() - min.getX();
        int y = max.getBlockY() - min.getY();
        int z = max.getBlockZ() - min.getZ();

        return BlockVector3.at(
                max.getBlockX() - x / 2,
                max.getBlockY() - y / 2,
                max.getBlockZ() - z / 2
        );
    }

    private BlockPos getMaxBlockPos(BlockVector3 max, BlockVector3 min) {
        int minX = max.getBlockX() - min.getX();
        int minY = max.getBlockY() - min.getY();
        int minZ = max.getBlockZ() - min.getZ();

        return new BlockPos(
                minX / 2,
                minY / 2,
                minZ / 2
        );
    }

    private double getAxisLength(double x1, double x2) {
        return Math.abs(x1 - x2);
    }

    private void finish() {
        BionicUtils.sendEntityStatus(getWorld(), this, SecretCraftBase.IDENTIFIER, 11);
        this.discard();
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Age", (short) this.age);
        nbt.putFloat("RotationSpeed", this.getRotationSpeed());
        nbt.putFloat("TargetHeight", this.getTargetHeight());
        nbt.putFloat("PrevSize", this.prevSize);
        nbt.putFloat("Size", this.size);
        nbt.putInt("AreaScale", this.areaScale);
        nbt.putInt("Removing", this.removing);
        nbt.putInt("Placing", this.placing);
        nbt.putBoolean("isPlacing", this.isPlacing);
        nbt.put("SchematicBox", this.getSchematicBox().writeNbt(new NbtCompound()));
        if (this.center != null) {
            nbt.put("Center", NbtHelper.fromBlockPos(this.center));
        }
        if (this.unmodifiedBlocks != null && !this.unmodifiedBlocks.isEmpty()) {
            NbtList nbtElements = new NbtList();

            this.unmodifiedBlocks.forEach(blockPos -> {
                if (blockPos != null) {
                    nbtElements.add(NbtHelper.fromBlockPos(blockPos));
                }
            });

            nbt.put("unmodifiedBlocks", nbtElements);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.age = nbt.getInt("Age");
        if (nbt.contains("RotationSpeed", NbtCompound.FLOAT_TYPE)) {
            this.setRotationSpeed(nbt.getFloat("RotationSpeed"));
        }
        if (nbt.contains("TargetHeight", NbtCompound.FLOAT_TYPE)) {
            this.setTargetHeight(nbt.getFloat("TargetHeight"));
        }
        if (nbt.contains("PrevSize", NbtCompound.FLOAT_TYPE)) {
            this.prevSize = nbt.getFloat("PrevSize");
        }
        if (nbt.contains("Size", NbtCompound.FLOAT_TYPE)) {
            this.size = nbt.getFloat("Size");
        }
        if (nbt.contains("AreaScale", NbtCompound.INT_TYPE)) {
            this.areaScale = nbt.getInt("AreaScale");
        }
        if (nbt.contains("Removing", NbtCompound.INT_TYPE)) {
            this.removing = nbt.getInt("Removing");
        }
        if (nbt.contains("Placing", NbtCompound.INT_TYPE)) {
            this.placing = nbt.getInt("Placing");
        }
        if (nbt.contains("isPlacing", NbtCompound.BYTE_TYPE)) {
            this.isPlacing = nbt.getBoolean("isPlacing");
        }
        if (nbt.contains("SchematicBox", NbtCompound.COMPOUND_TYPE)) {
            this.getSchematicBox().readNbt(nbt.getCompound("SchematicBox"));
        }
        if (nbt.contains("Center", NbtCompound.COMPOUND_TYPE)) {
            this.center = NbtHelper.toBlockPos(nbt.getCompound("Center"));
        }
        if (nbt.contains("unmodifiedBlocks", NbtCompound.LIST_TYPE)) {
            nbt.getList("unmodifiedBlocks", NbtCompound.COMPOUND_TYPE).forEach(nbtElement -> {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    this.unmodifiedBlocks.add(NbtHelper.toBlockPos(nbtCompound));
                }
            });
        }
    }

    public void setTargetHeight(float targetHeight) {
        getDataTracker().set(TARGET_HEIGHT, targetHeight);
    }

    public float getTargetHeight() {
        return getDataTracker().get(TARGET_HEIGHT);
    }

    public void setRotationSpeed(float speed) {
        getDataTracker().set(ROTATION_SPEED, speed);
    }

    public float getRotationSpeed() {
        return getDataTracker().get(ROTATION_SPEED);
    }

    public void setSchematicBox(SchematicBox schematicBox) {
        getDataTracker().set(SCHEMATIC_BOX, schematicBox, true);
    }

    public SchematicBox getSchematicBox() {
        return getDataTracker().get(SCHEMATIC_BOX);
    }

    public float getRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevRotation, this.rotation);
    }

    public float getSize(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevSize, this.size);
    }

    @Override
    public boolean isImmuneToExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
