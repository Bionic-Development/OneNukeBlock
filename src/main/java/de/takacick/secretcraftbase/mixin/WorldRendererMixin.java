package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.ParticleRegistry;
import de.takacick.secretcraftbase.registry.particles.ColoredParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 999)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Shadow
    protected abstract <T extends ParticleEffect> void addParticle(T parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "processWorldEvent", at = @At("HEAD"))
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo info) {
        if (eventId == 82139123) {
            if (data == 0) {
                BlockState blockState = this.world.getBlockState(pos);

                VoxelShape voxelShape = blockState.getOutlineShape(this.world, pos);

                ParticleEffect particleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM, ItemRegistry.REDSTONE_ORE_CHUNKS.getDefaultStack());

                voxelShape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    double d = Math.min(1.0, maxX - minX);
                    double e = Math.min(1.0, maxY - minY);
                    double f = Math.min(1.0, maxZ - minZ);
                    int i = Math.max(2, MathHelper.ceil(d / 0.25));
                    int j = Math.max(2, MathHelper.ceil(e / 0.25));
                    int k = Math.max(2, MathHelper.ceil(f / 0.25));
                    for (int l = 0; l < i; ++l) {
                        for (int m = 0; m < j; ++m) {
                            for (int n = 0; n < k; ++n) {
                                if (world.getRandom().nextDouble() > 0.7) {
                                    continue;
                                }
                                double g = ((double) l + 0.5) / (double) i;
                                double h = ((double) m + 0.5) / (double) j;
                                double o = ((double) n + 0.5) / (double) k;
                                double p = g * d + minX;
                                double q = h * e + minY;
                                double r = o * f + minZ;

                                this.addParticle(particleEffect, (double) pos.getX() + p, (double) pos.getY() + q, (double) pos.getZ() + r,
                                        (g - 0.5) * 0.15, (h - 0.5) * 0.55, (o - 0.5) * 0.15);
                            }
                        }
                    }
                });
            } else if (data == 1) {
                BlockState currentState = this.world.getBlockState(pos);

                BlockState blockState = (currentState.isOf(Blocks.REDSTONE_ORE) || currentState.isOf(Blocks.STONE) ? Blocks.STONE : Blocks.DEEPSLATE).getDefaultState();

                VoxelShape voxelShape = blockState.getOutlineShape(this.world, pos);

                voxelShape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    double d = Math.min(1.0, maxX - minX);
                    double e = Math.min(1.0, maxY - minY);
                    double f = Math.min(1.0, maxZ - minZ);
                    int i = Math.max(2, MathHelper.ceil(d / 0.25));
                    int j = Math.max(2, MathHelper.ceil(e / 0.25));
                    int k = Math.max(2, MathHelper.ceil(f / 0.25));
                    for (int l = 0; l < i; ++l) {
                        for (int m = 0; m < j; ++m) {
                            for (int n = 0; n < k; ++n) {
                                if (world.getRandom().nextDouble() > 0.5) {
                                    continue;
                                }
                                double g = ((double) l + 0.5) / (double) i;
                                double h = ((double) m + 0.5) / (double) j;
                                double o = ((double) n + 0.5) / (double) k;
                                double p = g * d + minX;
                                double q = h * e + minY;
                                double r = o * f + minZ;

                                this.client.particleManager.addParticle(new BlockDustParticle(this.world,
                                        (double) pos.getX() + p, (double) pos.getY() + q, (double) pos.getZ() + r,
                                        g - 0.5, h - 0.5, o - 0.5, blockState, pos));
                            }
                        }
                    }
                });
            } else if (data == 2) {
                ParticleEffect particleEffect = new ColoredParticleEffect(ParticleRegistry.LASER_DUST, new Vector3f(1f, 0f, 0f));
                for (int i = 0; i < 2; ++i) {
                    Vec3d vec3d = new Vec3d(
                            world.getRandom().nextGaussian(),
                            world.getRandom().nextDouble(),
                            world.getRandom().nextGaussian()
                    );

                    double g = pos.getX() + 0.5 + 0.4 * vec3d.getX();
                    double h = pos.getY() + 0.5 + 0.4 * vec3d.getY();
                    double j = pos.getZ() + 0.5 + 0.4 * vec3d.getZ();
                    this.world.addParticle(ParticleTypes.SMOKE, false, g, h, j, vec3d.getX() * 0.001, vec3d.getY() * 0.1, vec3d.getY() * 0.001);
                }

                for (int i = 0; i < 5; ++i) {
                    Vec3d vec3d = new Vec3d(
                            world.getRandom().nextGaussian(),
                            world.getRandom().nextGaussian(),
                            world.getRandom().nextGaussian()
                    );

                    double g = pos.getX() + 0.5 + 0.4 * vec3d.getX();
                    double h = pos.getY() + 0.5 + 0.4 * vec3d.getY();
                    double j = pos.getZ() + 0.5 + 0.4 * vec3d.getZ();
                    this.world.addParticle(particleEffect, false, g, h, j, vec3d.getX() * 0.05, vec3d.getY() * 0.05, vec3d.getY() * 0.05);
                }

                this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.25f, 1.0f, true);
            }
        }
    }
}