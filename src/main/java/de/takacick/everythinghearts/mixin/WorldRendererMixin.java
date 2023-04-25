package de.takacick.everythinghearts.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.EverythingHeartsClient;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.particles.ColoredParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Shadow
    private int ticks;

    @Shadow
    @Final
    private float[] field_20795;
    @Shadow
    @Final
    private float[] field_20794;
    @Shadow
    @Final
    private MinecraftClient client;
    private static final Identifier HEART_RAIN = new Identifier(EverythingHearts.MOD_ID, "textures/environment/heart_rain.png");

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo info) {
        if (eventId == 102391239) {
            if (data == 0) {
                for (int i = 0; i < 8; ++i) {
                    double g = pos.getX() + 0.5;
                    double h = pos.getY() + 0.5;
                    double j = pos.getZ() + 0.5;
                    world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.2, world.getRandom().nextGaussian() * 0.2, world.getRandom().nextGaussian() * 0.2);
                }

                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, SoundCategory.BLOCKS, 0.7f, 1f, true);
                info.cancel();
            } else if (data == 1) {
                for (int i = 0; i < 8; ++i) {
                    double f;
                    double e;
                    double d = world.getRandom().nextFloat() * 2.0f - 1.0f;
                    if (d * d + (e = world.getRandom().nextFloat() * 2.0f - 1.0f) * e + (f = world.getRandom().nextFloat() * 2.0f - 1.0f) * f > 1.0)
                        continue;
                    double g = pos.getX() + 0.5f + 0.8 * world.getRandom().nextGaussian();
                    double h = pos.getY() + 0.5f + 0.8 * world.getRandom().nextGaussian();
                    double j = pos.getZ() + 0.5f + 0.8 * world.getRandom().nextGaussian();
                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), false, g, h, j, d, e + 0.2, f);
                }

                for (int i = 0; i < 30; ++i) {
                    double f;
                    double e;
                    double d = world.getRandom().nextFloat() * 2.0f - 1.0f;
                    if (d * d + (e = world.getRandom().nextFloat() * 2.0f - 1.0f) * e + (f = world.getRandom().nextFloat() * 2.0f - 1.0f) * f > 1.0)
                        continue;
                    double g = pos.getX() + 0.5f + 0.6 * world.getRandom().nextGaussian();
                    double h = pos.getY() + 0.5f + 0.6 * world.getRandom().nextGaussian();
                    double j = pos.getZ() + 0.5f + 0.6 * world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleRegistry.HEART, false, g, h, j, d, e + 0.2, f);
                }
                this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 2.0f, 3.0f, false);
                this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.BLOCKS, 1.0f, 3.0f, false);
                this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_FIREWORK_ROCKET_SHOOT, SoundCategory.BLOCKS, 0.3f, 3.0f, false);
                this.world.addParticle(ParticleRegistry.HEART_EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
                this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0xFF1313))), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.25, 0, 0);
                info.cancel();
            } else if (data == 3) {
                Random random = Random.create();
                this.world.playSound(pos, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.BLOCKS, 0.2f, 5.0f, true);
                this.world.playSound(pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 0.2f, 2.0f, true);
                boolean bl = this.world.getBlockState(pos).isFullCube(this.world, pos);
                int k = bl ? 40 : 20;
                float ac = bl ? 0.45f : 0.25f;

                for (int t = 0; t < k; ++t) {
                    float ah = 2.0f * random.nextFloat() - 1.0f;
                    float ae = 2.0f * random.nextFloat() - 1.0f;
                    float ai = 2.0f * random.nextFloat() - 1.0f;
                    this.world.addParticle(ParticleRegistry.HEART, (double) pos.getX() + 0.5 + (double) (ah * ac), (double) pos.getY() + 0.5 + (double) (ae * ac), (double) pos.getZ() + 0.5 + (double) (ai * ac), ah * 0.07f, ae * 0.07f, ai * 0.07f);
                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vec3f(Vec3d.unpackRgb(0xFF1313))), (double) pos.getX() + 0.5 + (double) (ah * ac), (double) pos.getY() + 0.5 + (double) (ae * ac), (double) pos.getZ() + 0.5 + (double) (ai * ac), ah * 0.07f, ae * 0.07f, ai * 0.07f);
                }
            }
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"))
    public void renderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo info) {
        float f = EverythingHeartsClient.getHeartRainGradient(tickDelta);
        if (f <= 0.0f) {
            return;
        }

        manager.enable();
        ClientWorld world = this.client.world;
        int i = MathHelper.floor(cameraX);
        int j = MathHelper.floor(cameraY);
        int k = MathHelper.floor(cameraZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int l = 5;
        if (MinecraftClient.isFancyGraphicsOrBetter()) {
            l = 10;
        }
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        int m = -1;
        float g = (float) this.ticks + tickDelta;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int n = k - l; n <= k + l; ++n) {
            for (int o = i - l; o <= i + l; ++o) {
                float y;
                float h;
                int t;
                int p = (n - k + 16) * 32 + o - i + 16;
                double d = (double) this.field_20794[p] * 0.5;
                double e = (double) this.field_20795[p] * 0.5;
                mutable.set(o, cameraY, n);
                int q = world.getTopY(Heightmap.Type.MOTION_BLOCKING, o, n);
                int r = j - l;
                int s = j + l;
                if (r < q) {
                    r = q;
                }
                if (s < q) {
                    s = q;
                }
                if ((t = q) < j) {
                    t = j;
                }
                if (r == s) continue;
                Random random = Random.create(o * o * 3121 + o * 45238971 ^ n * n * 418711 + n * 13761);
                mutable.set(o, r, n);
                if (m != 0) {
                    if (m >= 0) {
                        tessellator.draw();
                    }
                    m = 0;
                    RenderSystem.setShaderTexture(0, HEART_RAIN);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                }
                int u = this.ticks + o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 0x1F;
                h = -((float) u + tickDelta) / 32.0f * (3.0f + random.nextFloat());
                double v = (double) o + 0.5 - cameraX;
                double w = (double) n + 0.5 - cameraZ;
                float x = (float) Math.sqrt(v * v + w * w) / (float) l;
                y = ((1.0f - x * x) * 0.5f + 0.5f) * f;
                mutable.set(o, t, n);
                int z = WorldRenderer.getLightmapCoordinates(world, mutable);
                bufferBuilder.vertex((double) o - cameraX - d + 0.5, (double) s - cameraY, (double) n - cameraZ - e + 0.5).texture(0.0f, (float) r * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                bufferBuilder.vertex((double) o - cameraX + d + 0.5, (double) s - cameraY, (double) n - cameraZ + e + 0.5).texture(1.0f, (float) r * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                bufferBuilder.vertex((double) o - cameraX + d + 0.5, (double) r - cameraY, (double) n - cameraZ + e + 0.5).texture(1.0f, (float) s * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
                bufferBuilder.vertex((double) o - cameraX - d + 0.5, (double) r - cameraY, (double) n - cameraZ - e + 0.5).texture(0.0f, (float) s * 0.25f + h).color(1.0f, 1.0f, 1.0f, y).light(z).next();
            }
        }
        if (m >= 0) {
            tessellator.draw();
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        manager.disable();
    }

    @Inject(method = "tickRainSplashing", at = @At("HEAD"))
    public void tickRainSplashing(Camera camera, CallbackInfo info) {
        float f = EverythingHeartsClient.getHeartRainGradient(MinecraftClient.getInstance().getTickDelta());
        if (f <= 0.0f) {
            return;
        }
        Random random = Random.create((long) this.ticks * 312987231L);
        ClientWorld worldView = this.client.world;
        BlockPos blockPos = new BlockPos(camera.getPos());
        Vec3i blockPos2 = null;
        int i = (int) (100.0f * f * f) / (this.client.options.getParticles().getValue() == ParticlesMode.DECREASED ? 2 : 1);
        for (int j = 0; j < i; ++j) {
            int k = random.nextInt(21) - 10;
            int l = random.nextInt(21) - 10;
            BlockPos blockPos3 = worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(k, 0, l));
            if (blockPos3.getY() <= worldView.getBottomY() || blockPos3.getY() > blockPos.getY() + 10 || blockPos3.getY() < blockPos.getY() - 10)
                continue;
            blockPos2 = blockPos3.down();
            if (this.client.options.getParticles().getValue() == ParticlesMode.MINIMAL) break;
            double d = random.nextDouble();
            double e = random.nextDouble();
            BlockState blockState = worldView.getBlockState((BlockPos) blockPos2);
            FluidState fluidState = worldView.getFluidState((BlockPos) blockPos2);
            VoxelShape voxelShape = blockState.getCollisionShape(worldView, (BlockPos) blockPos2);
            double g = voxelShape.getEndingCoord(Direction.Axis.Y, d, e);
            double h = fluidState.getHeight(worldView, (BlockPos) blockPos2);
            double m = Math.max(g, h);
            DefaultParticleType particleEffect = fluidState.isIn(FluidTags.LAVA) || blockState.isOf(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(blockState) ? ParticleTypes.SMOKE : ParticleRegistry.HEART_SPLASH;
            this.client.world.addParticle(particleEffect, (double) blockPos2.getX() + d, (double) blockPos2.getY() + m, (double) blockPos2.getZ() + e, 0.0, 0.0, 0.0);
        }
    }
}
