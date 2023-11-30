package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.OneElementalBlockClient;
import de.takacick.elementalblock.registry.ParticleRegistry;
import de.takacick.elementalblock.registry.particles.ColoredParticleEffect;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 821482) {
            if (data == 0) {
                Vec3d pos = Vec3d.ofCenter(blockPos);

                for (int i = 0; i < 16; ++i) {
                    double g = 0.6 * this.world.getRandom().nextGaussian();
                    double h = 0.6 * this.world.getRandom().nextDouble();
                    double j = 0.6 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }

                for (int i = 0; i < 12; ++i) {
                    double g = 1 * this.world.getRandom().nextGaussian();
                    double h = 1 * this.world.getRandom().nextDouble();
                    double j = 1 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.2, -h * 0.3, j * 0.2);
                }
                for (int i = 0; i < 3; ++i) {
                    double g = 1 * this.world.getRandom().nextGaussian();
                    double h = 1 * this.world.getRandom().nextDouble();
                    double j = 1 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleTypes.CHERRY_LEAVES,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.1, -h * 0.3, j * 0.1);
                }

                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                info.cancel();
            } else if (data == 1) {
                Vec3d pos = Vec3d.ofCenter(blockPos);
                Vector3f color = Vec3d.unpackRgb(4159204).toVector3f();

                for (int i = 0; i < 7; ++i) {
                    double g = 0.6 * this.world.getRandom().nextGaussian();
                    double h = 0.6 * this.world.getRandom().nextDouble();
                    double j = 0.6 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.FALLING_WATER, color),
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }
                info.cancel();
            } else if (data == 2) {
                Vec3d pos = Vec3d.ofCenter(blockPos);
                Vector3f color = Vec3d.unpackRgb(4159204).toVector3f();

                for (int i = 0; i < 7; ++i) {
                    double g = 0.6 * this.world.getRandom().nextGaussian();
                    double h = 0.6 * this.world.getRandom().nextDouble();
                    double j = 0.6 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.FALLING_WATER, color),
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }
                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_AXOLOTL_SPLASH, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                info.cancel();
            } else if (data == 3) {
                Vec3d pos = Vec3d.ofCenter(blockPos);

                for (int i = 0; i < 7; ++i) {
                    double g = 0.6 * this.world.getRandom().nextGaussian();
                    double h = 0.6 * this.world.getRandom().nextDouble();
                    double j = 0.6 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleRegistry.CLOUD,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }


                info.cancel();
            } else if (data >= 4 && data <= 7) {
                Vec3d pos = Vec3d.ofCenter(blockPos);

                ItemStack stack = OneElementalBlockClient.getRocketItem(data - 4);
                world.addFireworkParticle(pos.getX(), pos.getY() + 2, pos.getZ(), 0, 1, 0, stack.getOrCreateSubNbt("Fireworks"));

                world.playSound(pos.getX(), pos.getY() + 2, pos.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                world.addParticle(ParticleTypes.FLASH, pos.getX(), pos.getY() + 2, pos.getZ(), 0, -1, 0);
                if (data == 4) {
                    List<Integer> colors = Arrays.asList(0x564429, 0x886142, 0x70922D, 0x886142);

                    for (int i = 0; i < 50; i++) {

                        Vector3f vector3f = Vec3d.unpackRgb(colors.get(world.getRandom().nextInt(colors.size()))).toVector3f();
                        double d = world.getRandom().nextGaussian();
                        double e = world.getRandom().nextGaussian();
                        double f = world.getRandom().nextGaussian();

                        this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, vector3f), true, pos.getX() + d, pos.getY() + 0.5 + e, pos.getZ() + f, d * 0.5, e * 0.5, f * 0.5);
                    }
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ROOTED_DIRT_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f, false);

                } else if (data == 5) {
                    for (int i = 0; i < 15; ++i) {
                        double g = 0.6 * this.world.getRandom().nextGaussian();
                        double h = 0.6 * this.world.getRandom().nextDouble();
                        double j = 0.6 * this.world.getRandom().nextGaussian();
                        this.world.addParticle(ParticleRegistry.CLOUD,
                                i % 2 == 0,
                                pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                                g * 0.3, h * 0.3, j * 0.3);
                    }

                    Vector3f color = Vec3d.unpackRgb(0xFFFFFF).toVector3f();

                    for (int i = 0; i < 50; i++) {
                        double d = world.getRandom().nextGaussian();
                        double e = world.getRandom().nextDouble();
                        double f = world.getRandom().nextGaussian();

                        this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, color), true, pos.getX() + d, pos.getY() + 0.5 + e, pos.getZ() + f, d * 0.5, e * 0.5, f * 0.5);
                    }
                } else if (data == 6) {
                    Vector3f color = Vec3d.unpackRgb(4159204).toVector3f();

                    for (int i = 0; i < 25; i++) {
                        double g = 0.6 * this.world.getRandom().nextGaussian();
                        double h = 0.6 * this.world.getRandom().nextDouble();
                        double j = 0.6 * this.world.getRandom().nextGaussian();
                        this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.FALLING_WATER, color),
                                i % 2 == 0,
                                pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                                g * 0.3, h * 0.3, j * 0.3);
                    }

                    for (int i = 0; i < 50; i++) {
                        double d = world.getRandom().nextGaussian();
                        double e = world.getRandom().nextDouble();
                        double f = world.getRandom().nextGaussian();

                        this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, color), true, pos.getX() + d, pos.getY() + 0.5 + e, pos.getZ() + f, d * 0.5, e * 0.5, f * 0.5);
                    }
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_AXOLOTL_SPLASH, SoundCategory.BLOCKS, 2f, 1.0f, false);
                } else if (data == 7) {
                    List<Integer> colors = Arrays.asList(0xCA4E06, 0x652828, 0xF89E44, 0x723232);

                    for (int i = 0; i < 25; i++) {

                        Vector3f vector3f = Vec3d.unpackRgb(colors.get(world.getRandom().nextInt(colors.size()))).toVector3f();
                        double d = world.getRandom().nextGaussian();
                        double e = world.getRandom().nextDouble();
                        double f = world.getRandom().nextGaussian();

                        this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, vector3f), true, pos.getX() + d, pos.getY() + 0.5 + e, pos.getZ() + f, d * 1, e * 1, f * 1);
                    }
                    for (int i = 0; i < 25; i++) {
                        double d = world.getRandom().nextGaussian();
                        double e = world.getRandom().nextDouble();
                        double f = world.getRandom().nextGaussian();

                        this.world.addParticle(ParticleTypes.FLAME, true, pos.getX() + d, pos.getY() + e, pos.getZ() + f, d * 0.5, e * 0.5, f * 0.5);
                        this.world.addParticle(ParticleTypes.SMOKE, true, pos.getX() + d, pos.getY() + e, pos.getZ() + f, d * 0.5, e * 0.5, f * 0.5);
                    }
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.7f, 1.0f, false);
                }

                info.cancel();
            }
        }
    }
}