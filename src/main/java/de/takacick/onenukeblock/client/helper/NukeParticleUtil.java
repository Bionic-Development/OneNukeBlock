package de.takacick.onenukeblock.client.helper;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class NukeParticleUtil {

    public static void spawnSmashAttackParticles(World world, BlockPos pos, int count, double distance) {
        double j;
        double h;
        double g;
        double f;
        double e;
        double d;

        Vec3d vec3d = pos.toCenterPos().add(0.0, 0.5, 0.0);
        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, world.getBlockState(pos));
        int i = 0;
        while ((float) i < (float) count) {
            d = vec3d.x + distance * Math.cos(i) + world.getRandom().nextGaussian() / 2.0;
            e = vec3d.y;
            f = vec3d.z + distance * Math.sin(i) + world.getRandom().nextGaussian() / 2.0;
            g = world.getRandom().nextGaussian() * (double) 0.2f;
            h = world.getRandom().nextGaussian() * (double) 0.2f;
            j = world.getRandom().nextGaussian() * (double) 0.2f;

            world.addImportantParticle(blockStateParticleEffect, d, e, f, g, h, j);
            ++i;
        }
    }

    public static void spawnSmashAttackParticles(World world, Vec3d pos, BlockState blockState, int count, double distance) {
        double j;
        double h;
        double g;
        double f;
        double e;
        double d;

        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, blockState);
        int i = 0;
        while ((float) i < (float) count) {
            d = pos.x + distance * Math.cos(i) + world.getRandom().nextGaussian() / 2.0;
            e = pos.y;
            f = pos.z + distance * Math.sin(i) + world.getRandom().nextGaussian() / 2.0;
            g = world.getRandom().nextGaussian() * (double) 0.2f;
            h = world.getRandom().nextGaussian() * (double) 0.2f;
            j = world.getRandom().nextGaussian() * (double) 0.2f;

            world.addImportantParticle(blockStateParticleEffect, d, e, f, g, h, j);
            ++i;
        }
    }

    public static void spawnSmashParticles(World world, Vec3d pos, int count, double distance, BlockState blockState) {
        double j;
        double h;
        double g;
        double f;
        double e;
        double d;

        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, blockState);
        int i = 0;
        while ((float) i < (float) count) {
            d = pos.x + distance * Math.cos(i) + world.getRandom().nextGaussian() / 2.0;
            e = pos.y;
            f = pos.z + distance * Math.sin(i) + world.getRandom().nextGaussian() / 2.0;
            g = world.getRandom().nextGaussian() * (double) 0.6f;
            h = world.getRandom().nextGaussian() * (double) 0.5f;
            j = world.getRandom().nextGaussian() * (double) 0.6f;
            if (world.getRandom().nextDouble() <= 0.1) {
                Particle particle = MinecraftClient.getInstance().particleManager.createParticle(ParticleTypes.HAPPY_VILLAGER, d, e, f, g, h, j);
                particle.setVelocity(g, h, j);
                MinecraftClient.getInstance().particleManager.addParticle(particle);
            } else {
                Particle particle = MinecraftClient.getInstance().particleManager.createParticle(blockStateParticleEffect, d, e, f, g, h, j);
                particle.setVelocity(g, h, j);
                MinecraftClient.getInstance().particleManager.addParticle(particle);
            }
            ++i;
        }
    }

    public static void addBlockBreakParticles(BlockPos pos, BlockState state) {
        if (state.isAir() || !state.hasBlockBreakParticles()) {
            return;
        }

        ClientWorld world = MinecraftClient.getInstance().world;
        VoxelShape voxelShape = state.getOutlineShape(world, pos);
        voxelShape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double d = Math.min(1.0, maxX - minX);
            double e = Math.min(1.0, maxY - minY);
            double f = Math.min(1.0, maxZ - minZ);
            int i = Math.max(2, MathHelper.ceil(d / 0.25)) * 2;
            int j = Math.max(2, MathHelper.ceil(e / 0.25)) * 2;
            int k = Math.max(2, MathHelper.ceil(f / 0.25)) * 2;
            for (int l = 0; l < i; ++l) {
                for (int m = 0; m < j; ++m) {
                    for (int n = 0; n < k; ++n) {
                        double g = ((double) l + 0.5) / (double) i;
                        double h = ((double) m + 0.5) / (double) j;
                        double o = ((double) n + 0.5) / (double) k;
                        double p = g * d + minX;
                        double q = h * e + minY;
                        double r = o * f + minZ;
                        MinecraftClient.getInstance().particleManager.addParticle(new BlockDustParticle(world,
                                (double) pos.getX() + p, (double) pos.getY() + q, (double) pos.getZ() + r,
                                (g - 0.5) * (world.getRandom().nextDouble() * 8 + 3),
                                (h - 0.5) * (world.getRandom().nextDouble() * 8 + 3),
                                (o - 0.5) * (world.getRandom().nextDouble() * 8 + 3), state, pos));
                    }
                }
            }
        });
    }

    public static void addPotion(Vec3d vec3d, int color, int count) {
        Random random = Random.create();
        float u = (float) (color >> 16 & 0xFF) / 255.0f;
        float v = (float) (color >> 8 & 0xFF) / 255.0f;
        float w = (float) (color >> 0 & 0xFF) / 255.0f;
        SimpleParticleType particleEffect = ParticleTypes.EFFECT;
        for (int x = 0; x < count; ++x) {
            double e = random.nextDouble() * 1.0;
            double f = random.nextDouble() * Math.PI * 2.0;
            double y = Math.cos(f) * e;
            double z = 0.01 + random.nextDouble() * 0.5;
            double aa = Math.sin(f) * e;
            Particle particle = MinecraftClient.getInstance().particleManager.addParticle(particleEffect, vec3d.x + y * 0.1, vec3d.y + 0.25 * random.nextGaussian(), vec3d.z + aa * 0.1, y * 0.6, z, aa * 0.6);
            if (particle == null) continue;
            float ab = 0.75f + random.nextFloat() * 0.25f;
            particle.setColor(u * ab, v * ab, w * ab);
            particle.move((float) e);
        }
    }
}
