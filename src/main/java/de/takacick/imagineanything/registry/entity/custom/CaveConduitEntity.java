package de.takacick.imagineanything.registry.entity.custom;

import com.mojang.datafixers.util.Pair;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CaveConduitEntity extends MobEntity {

    public static final List<Item> items = new ArrayList<>();

    static {
        items.add(Items.DIAMOND);
        items.add(Items.EMERALD);
        items.add(Items.COAL);
        items.add(Items.REDSTONE);
        items.add(Items.COPPER_INGOT);
        items.add(Items.GOLD_INGOT);
        items.add(Items.IRON_INGOT);
    }

    protected static final TrackedData<BlockPos> FIRST_TARGET = DataTracker.registerData(CaveConduitEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected static final TrackedData<BlockPos> SECOND_TARGET = DataTracker.registerData(CaveConduitEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected static final TrackedData<BlockPos> THIRD_TARGET = DataTracker.registerData(CaveConduitEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected static final TrackedData<BlockPos> FOURTH_TARGET = DataTracker.registerData(CaveConduitEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private final List<BlockPos> oreBlocks = new ArrayList<>();
    private final HashMap<Pair<Integer, Integer>, Integer> highest = new HashMap<>();
    private boolean foundOres = false;
    private ItemStack itemStack = Items.DIAMOND.getDefaultStack();

    private boolean goUp = false;
    private double startY = -150;
    private int y = -63;

    public CaveConduitEntity(EntityType<? extends CaveConduitEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        this.ignoreCameraFrustum = true;
    }

    public CaveConduitEntity(World world, double x, double y, double z) {
        this(EntityRegistry.CAVE_CONDUIT, world);
        this.setPosition(x, y, z);
        this.noClip = true;
        this.ignoreCameraFrustum = true;
    }

    public static CaveConduitEntity create(EntityType<CaveConduitEntity> entityType, World world) {
        return new CaveConduitEntity(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(FIRST_TARGET, null);
        getDataTracker().startTracking(SECOND_TARGET, null);
        getDataTracker().startTracking(THIRD_TARGET, null);
        getDataTracker().startTracking(FOURTH_TARGET, null);
        super.initDataTracker();
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return !this.isRemoved();
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public void tick() {

        if (age % 5 == 0) {
            world.playSoundFromEntity(null, this, SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.AMBIENT, 1, 1);
        }

        if (!world.isClient) {
            if (!foundOres) {
                if (startY <= -100) {
                    startY = getY();
                }

                if (!goUp) {
                    y++;
                    List<BlockPos> blocks = generateCircle(getBlockPos(), 15, false);
                    blocks.forEach(origin -> {
                        BlockPos blockPos = new BlockPos(origin.getX(), y, origin.getZ());

                        BlockState blockState = world.getBlockState(blockPos);

                        if (blockState.getBlock() instanceof OreBlock) {
                            if (!oreBlocks.contains(blockPos)) {
                                oreBlocks.add(blockPos);
                            }
                        }

                        if (blockState.getMaterial().blocksMovement()) {
                            Pair<Integer, Integer> coords = new Pair<>(origin.getX(), origin.getZ());
                            highest.put(coords, blockPos.getY());
                        }
                    });

                    if (startY - 40 >= getY()) {
                        setGlowing(false);

                        if (y + 1 >= startY) {
                            setGlowing(true);
                            goUp = true;
                        }
                    } else {
                        teleport(getX(), age > 5 ? getY() - 1 : getY() - 0.5, getZ());
                    }
                } else {
                    if (getY() >= startY) {
                        getEntityWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 1f, 3f);
                        foundOres = true;
                        setGlowing(false);
                    } else {
                        teleport(getX(), getY() + 2.5, getZ());
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    BlockPos blockPos = getBlockPos(i);

                    if (blockPos == null) {
                        HitResult hitResult = raycast(20, -180 + (i * 90f) + (float) world.getRandom().nextGaussian() * 15f,
                                30f + (float) world.getRandom().nextGaussian() * 20f, false);
                        if (hitResult instanceof BlockHitResult blockHitResult && !hitResult.getType().equals(HitResult.Type.MISS)) {
                            setBlockPos(blockHitResult.getBlockPos(), i);
                        }
                    }
                }

                if (oreBlocks.isEmpty()) {
                    getEntityWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 1f, 3f);
                    ItemEntity itemEntity = dropItem(ItemRegistry.IMAGINED_CAVE_EXTRACTOR);
                    if (itemEntity != null) {
                        itemEntity.setGlowing(true);
                    }
                    this.discard();
                    return;
                }

                List<BlockPos> remove = new ArrayList<>();
                if (age % 5 == 0) {
                    Collections.shuffle(oreBlocks);
                }

                AtomicInteger replacedCount = new AtomicInteger(101);

                oreBlocks.replaceAll(blockPos -> {
                    if (replacedCount.addAndGet(-1) <= 0) {
                        return blockPos;
                    }

                    BlockState blockState = world.getBlockState(blockPos);

                    if (blockState.getBlock() instanceof OreBlock) {
                        BlockPos blockPosNew = new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                        BlockState swapState = world.getBlockState(blockPosNew);

                        if (swapState.getBlock() instanceof OreBlock) {
                            if (!oreBlocks.contains(blockPosNew)) {
                                remove.add(blockPos);
                            }
                            return blockPos;
                        }

                        if (!swapState.getBlock().equals(Blocks.AIR) || !canSee(blockPosNew)) {
                            world.setBlockState(blockPosNew, blockState);
                            world.setBlockState(blockPos, swapState);
                            playParticles(blockPos, blockState);
                            return blockPosNew;
                        } else {
                            remove.add(blockPos);
                        }
                    } else {
                        remove.add(blockPos);
                    }

                    return blockPos;
                });

                oreBlocks.removeAll(remove);
            }
        } else {
            if (age % 3 == 0) {
                itemStack = items.stream().filter(item -> !item.equals(itemStack.getItem())).toList()
                        .get(world.getRandom().nextInt(items.size() - 1)).getDefaultStack();
            }
        }

        super.tick();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    private void playParticles(BlockPos blockPos, BlockState blockState) {
        int y = highest.getOrDefault(Pair.of(blockPos.getX(), blockPos.getZ()), 0) + 1;
        BlockState blockState1 = world.getBlockState(new BlockPos(blockPos.getX(), y - 1, blockPos.getZ()));

        for (int x = 0; x < 10; x++) {
            double d = blockPos.getX() + 0.6 * world.getRandom().nextGaussian();
            double e = y + 0.05;
            double f = blockPos.getZ() + 0.6 * world.getRandom().nextGaussian();
            ((ServerWorld) world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK,
                            blockState1), d, e, f,
                    1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                    0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                    0);
        }

        world.playSound(null, blockPos.getX() + 0.5, y + 0.05, blockPos.getZ() + 0.5, blockState1.getSoundGroup().getHitSound(),
                SoundCategory.BLOCKS, 0.5f + 0.3f * (float) world.getRandom().nextInt(2),
                (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2f + 1.0f);
    }

    public boolean canSee(BlockPos blockPos) {
        Vec3d vec3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
        Vec3d vec3d2 = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 1.2, blockPos.getZ() + 0.5);
        if (vec3d2.distanceTo(vec3d) > 28.0) {
            return false;
        }
        return this.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.MISS;
    }

    public BlockPos getBlockPos(int id) {
        return switch (id) {
            case 0 -> getDataTracker().get(FIRST_TARGET);
            case 1 -> getDataTracker().get(SECOND_TARGET);
            case 2 -> getDataTracker().get(THIRD_TARGET);
            case 3 -> getDataTracker().get(FOURTH_TARGET);
            default -> null;
        };
    }

    public void setBlockPos(BlockPos blockPos, int id) {
        switch (id) {
            case 0 -> getDataTracker().set(FIRST_TARGET, blockPos);
            case 1 -> getDataTracker().set(SECOND_TARGET, blockPos);
            case 2 -> getDataTracker().set(THIRD_TARGET, blockPos);
            case 3 -> getDataTracker().set(FOURTH_TARGET, blockPos);
        }
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    public float getRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, (this.age - 1) * 6, this.age * 6) * -0.0375f;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    private Vec3d getCustomRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

    public HitResult raycast(double maxDistance, float yaw, float pitch, boolean includeFluids) {
        Vec3d vec3d = this.getCameraPosVec(1f);
        Vec3d vec3d2 = this.getCustomRotationVector(pitch, yaw);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return raycast(world, new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, this));
    }

    public BlockHitResult raycast(World world, RaycastContext context2) {
        return BlockView.raycast(context2.getStart(), context2.getEnd(), context2, (context, pos) -> {
            BlockState blockState = world.getBlockState(pos);
            FluidState fluidState = world.getFluidState(pos);
            Vec3d vec3d = context.getStart();
            Vec3d vec3d2 = context.getEnd();
            VoxelShape voxelShape = context.getBlockShape(blockState, world, pos);
            BlockHitResult blockHitResult = blockState.getMaterial().blocksMovement() ? world.raycastBlock(vec3d, vec3d2, pos, voxelShape, blockState) : null;
            VoxelShape voxelShape2 = context.getFluidShape(fluidState, world, pos);
            BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : context.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : context.getStart().squaredDistanceTo(blockHitResult2.getPos());
            return d <= e ? blockHitResult : blockHitResult2;
        }, context -> {
            Vec3d vec3d = context.getStart().subtract(context.getEnd());
            return BlockHitResult.createMissed(context.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), new BlockPos(context.getEnd()));
        });
    }

    public static List<BlockPos> generateCircle(BlockPos centerBlock, int radius, boolean hollow) {
        List<BlockPos> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getX();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int z = bz - radius; z <= bz + radius; z++) {
                double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)));
                if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                    circleBlocks.add(new BlockPos(x, centerBlock.getY(), z));
                }
            }
        }
        return circleBlocks;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public void setVelocity(Vec3d velocity) {

    }

    @Override
    public void setVelocity(double x, double y, double z) {

    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public int getTeamColorValue() {
        return 0xFF0000;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }
}

