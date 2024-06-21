package de.takacick.onescaryblock.registry.block.entity;

import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class Item303BlockEntity extends BlockEntity {

    private int maxAge = Random.create().nextBetween(3, 20);
    private int age = 0;

    public Item303BlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.ITEM_303, blockPos, blockState);
    }

    public static void tick(World world, BlockPos pos, BlockState state, Item303BlockEntity blockEntity) {
        blockEntity.age++;

        Random random = world.getRandom();
        if (!world.isClient) {
            if (blockEntity.age >= blockEntity.maxAge) {
                Registries.BLOCK.getRandom(random).ifPresent(blockReference -> {
                    Block block = blockReference.value();
                    BlockState blockState = block.getStateManager().getStates().get(random.nextInt(block.getStateManager().getStates().size()));
                    if (!blockState.isAir()) {
                        world.setBlockState(pos, blockState);
                    }
                });
            }
        } else {
            Random blockRandom = Random.create(blockEntity.getAge() + blockEntity.getPos().asLong());
            Registries.BLOCK.getRandom(blockRandom).ifPresent(blockReference -> {
                Block block = blockReference.value();
                BlockState blockState = block.getStateManager().getStates().get(blockRandom.nextInt(block.getStateManager().getStates().size()));

                world.playSoundAtBlockCenter(pos, blockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1f, 0.8f + world.getRandom().nextFloat() * 0.4f, true);
                world.addBlockBreakParticles(pos, blockState);
            });

            for (int i = 0; i < 5; i++) {
                Vec3d velocity = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize();
                Vec3d center = pos.toCenterPos().add(velocity.multiply(0.3));
                world.addParticle(ParticleRegistry.ITEM_303, center.getX(), center.getY(), center.getZ(),
                        velocity.getX() * 0.1, velocity.getY() * 0.1, velocity.getZ() * 0.1);
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("age", this.age);
        nbt.putInt("maxAge", this.maxAge);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("age", NbtElement.INT_TYPE)) {
            this.age = nbt.getInt("age");
        }
        if (nbt.contains("maxAge", NbtElement.INT_TYPE)) {
            this.maxAge = nbt.getInt("maxAge");
        }

        super.readNbt(nbt);
    }

    public int getAge() {
        return this.age;
    }
}

