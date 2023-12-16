package de.takacick.tinyhouse.registry.block.entity;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.registry.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GiantCrusherTrapBlockEntity extends BlockEntity {
    public static final RegistryKey<DamageType> GIANT_CRUSHER_TRAP = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(TinyHouse.MOD_ID, "giant_crusher_trap"));

    public List<BlockPos> poweredBlocks = new ArrayList<>();
    private BlockPos ownerBlockPos;
    private boolean isOwner;
    private float height = 0f;
    private float prevHeight = 0f;
    private static final float maxHeight = 3f;

    public GiantCrusherTrapBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.GIANT_CRUSHER_TRAP, pos, state);
        ownerBlockPos = pos;
        isOwner = false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, GiantCrusherTrapBlockEntity blockEntity) {
        if (!blockEntity.isOwner()) {
            GiantCrusherTrapBlockEntity crusherTrapBlockEntity = null;
            if (blockEntity.ownerBlockPos != null
                    && world.getBlockEntity(blockEntity.ownerBlockPos) instanceof GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity) {
                crusherTrapBlockEntity = giantCrusherTrapBlockEntity;
            }

            if (crusherTrapBlockEntity == null) {
                if (!world.isClient) {
                    world.breakBlock(pos, true);
                    blockEntity.markRemoved();
                }
            }
        } else {
            blockEntity.prevHeight = blockEntity.height;
            blockEntity.height += blockEntity.isPowered() ? 1f : -1f;

            blockEntity.height = MathHelper.clamp(blockEntity.height, 0, maxHeight);

            if (!world.isClient) {
                if (blockEntity.isPowered()) {
                    if (blockEntity.prevHeight != blockEntity.height) {
                        int range = 3;
                        for (int x = -1; x < range - 1; x++) {
                            for (int z = -1; z < range - 1; z++) {
                                for (int y = 0; y > -range; y--) {
                                    BlockPos blockPos = new BlockPos(pos.getX() + x, pos.getY() - 2 + y, pos.getZ() + z);

                                    BlockState blockState = world.getBlockState(blockPos);
                                    if (!blockState.isOf(blockEntity.getCachedState().getBlock())) {
                                        world.breakBlock(blockPos, true);
                                        world.setBlockState(blockPos, state);
                                    }

                                    if (world.getBlockEntity(blockPos)
                                            instanceof GiantCrusherTrapBlockEntity giantCrusherTrapBlockEntity) {
                                        giantCrusherTrapBlockEntity.setOwnerBlockPos(pos);
                                        world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (blockEntity.prevHeight != blockEntity.height) {
                        int range = 3;
                        for (int x = -1; x < range - 1; x++) {
                            for (int z = -1; z < range - 1; z++) {
                                for (int y = 0; y > -range; y--) {
                                    BlockPos blockPos = new BlockPos(pos.getX() + x, pos.getY() - 2 + y, pos.getZ() + z);

                                    if (-y >= blockEntity.getExtensionLevel(1f)) {
                                        BlockState blockState = world.getBlockState(blockPos);
                                        if (blockState.isOf(blockEntity.getCachedState().getBlock())) {
                                            world.removeBlock(blockPos, true);
                                        } else {
                                            world.breakBlock(blockPos, true);
                                            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                boolean crushed = blockEntity.height != blockEntity.prevHeight;

                if (blockEntity.height >= 3) {
                    Vec3d centerPos = Vec3d.of(pos);

                    int range = 3;
                    for (int x = 1; x > -range + 1; x--) {
                        for (int z = 1; z > -range + 1; z--) {
                            BlockPos blockPos = pos.add(x, -4, z);
                            AtomicInteger entities = new AtomicInteger(0);
                            world.getOtherEntities(null, new Box(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                            blockPos.getX() + 1, blockPos.getY() + 0.6, blockPos.getZ() + 1))
                                    .forEach(entity -> {
                                        if (entity instanceof EntityProperties entityProperties && !entity.isSpectator()) {
                                            if (!(entity instanceof ItemEntity itemEntity && itemEntity.getStack().isOf(Items.BONE))) {
                                                if (entity.isLiving()) {
                                                    entities.addAndGet(1);
                                                }

                                                if (crushed) {
                                                    entity.damage(world.getDamageSources().create(GIANT_CRUSHER_TRAP), 7.5f);
                                                }

                                                entityProperties.setCrushed(140);
                                                entityProperties.setStuckInsidePiston(4);
                                            }
                                        }
                                    });

                            if (crushed && entities.get() > 0) {
                                state.getOutlineShape(world, blockPos).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ())
                                        .forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                                            Box box = new Box(minX, minY, minZ, maxX, maxY, maxZ);
                                            Vec3d center = box.getCenter();

                                            for (int j = 0; j < 8; j++) {
                                                Vec3d random = new Vec3d(world.getRandom().nextGaussian(), world.getRandom().nextDouble(), world.getRandom().nextGaussian());

                                                double d = center.getX() + (box.getXLength() / 2d * random.getX());
                                                double e = blockPos.getY() + 0.01d + 0.01d * random.getY();
                                                double f = center.getZ() + box.getYLength() / 2d * random.getZ();
                                                ItemEntity itemEntity = new ItemEntity(world, d + (centerPos.getX() - d) * -0.9, e, f + -(centerPos.getZ() - f) * -0.9, Items.BONE.getDefaultStack(),
                                                        random.getX() * 0.2, random.getY() * 0.1, random.getZ() * 0.2);
                                                world.spawnEntity(itemEntity);
                                            }
                                        });
                            }
                        }
                    }
                }
            } else if (blockEntity.height >= 3 && blockEntity.height != blockEntity.prevHeight) {
                int range = 3;
                if (world.isClient) {
                    world.playSound(pos.getX() + 0.5, pos.getY() - 4, pos.getZ() + 0.5,
                            SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.BLOCKS, 1f, 1f, true);
                    for (int x = 1; x > -range + 1; x--) {
                        for (int z = 1; z > -range + 1; z--) {
                            BlockPos blockPos = pos.add(x, -4, z);

                            state.getOutlineShape(world, blockPos).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ())
                                    .forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                                        BlockState blockState = world.getBlockState(blockPos.add(0, -1, 0));
                                        blockState = blockState.hasBlockBreakParticles() ? blockState : state;

                                        Box box = new Box(minX, minY, minZ, maxX, maxY, maxZ);
                                        Vec3d center = box.getCenter();

                                        for (int j = 0; j < 30; j++) {
                                            Vec3d random = new Vec3d(world.getRandom().nextGaussian(), world.getRandom().nextDouble(), world.getRandom().nextGaussian());

                                            double d = box.getXLength() / 2d * random.getX();
                                            double e = 0.01d + 0.01d * random.getY();
                                            double f = box.getYLength() / 2d * random.getZ();

                                            world.addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), true,
                                                    center.getX() + d, blockPos.getY() + e, center.getZ() + f,
                                                    random.getX() * 0.2, random.getY() * 0.1, random.getZ() * 0.2);

                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    public void setOwnerBlockPos(BlockPos ownerBlockPos) {
        this.ownerBlockPos = ownerBlockPos;

        if (ownerBlockPos.equals(getPos())) {
            setOwner(true);
        } else {
            setOwner(false);
        }
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public BlockPos getOwnerBlockPos() {
        return ownerBlockPos;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("ownerBlockPos", NbtHelper.fromBlockPos(this.ownerBlockPos));
        nbt.putBoolean("isOwner", this.isOwner);
        nbt.putFloat("height", this.height);
        nbt.putFloat("prevHeight", this.prevHeight);

        if (!this.poweredBlocks.isEmpty()) {
            NbtCompound powered = new NbtCompound();

            for (int i = 0; i < this.poweredBlocks.size(); i++) {
                powered.put(String.valueOf(i), NbtHelper.fromBlockPos(this.poweredBlocks.get(i)));
            }

            nbt.put("poweredBlocks", powered);
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.ownerBlockPos = NbtHelper.toBlockPos(nbt.getCompound("ownerBlockPos"));
        this.isOwner = nbt.getBoolean("isOwner");
        this.height = nbt.getFloat("height");
        if (Math.abs(this.height - this.prevHeight) > 0.5) {
            this.prevHeight = nbt.getFloat("prevHeight");
        }

        if (nbt.contains("poweredBlocks", NbtElement.COMPOUND_TYPE)) {
            NbtCompound powered = nbt.getCompound("poweredBlocks");

            int i = 0;
            while (powered.contains(String.valueOf(i), NbtElement.COMPOUND_TYPE)) {
                this.poweredBlocks.add(NbtHelper.toBlockPos(powered.getCompound(String.valueOf(i))));
                i++;
            }
        } else {
            this.poweredBlocks.clear();
        }
        super.readNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public boolean isOwner() {
        return isOwner;
    }

    public float getExtensionLevel(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevHeight, this.height);
    }

    public boolean isPowered() {
        return !this.poweredBlocks.isEmpty();
    }
}
