package de.takacick.secretcraftbase.registry.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.secretcraftbase.client.shaders.vertex.TranslucentVertexConsumer;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.SecretMagicWellWaterBlock;
import de.takacick.secretcraftbase.registry.block.entity.SecretMagicWellBlockEntity;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.joml.Matrix4f;

import java.util.SortedSet;

public class SecretMagicWellBlockEntityRenderer implements BlockEntityRenderer<SecretMagicWellBlockEntity> {

    private final BlockRenderManager blockRenderManager;
    private final FluidRenderer fluidRenderer;

    public SecretMagicWellBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
        this.fluidRenderer = this.blockRenderManager.fluidRenderer;
    }

    public void render(SecretMagicWellBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        if (!blockEntity.isOwner()) {
            return;
        }

        World world = blockEntity.getWorld();

        if (world == null) {
            return;
        }

        float alpha = blockEntity.getAlpha(tickDelta);

        if (alpha <= 0) {
            return;
        }

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        VertexConsumerProvider.Immediate consumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());

        SecretMagicWellBlockEntity.BLOCKS.forEach((vec3d, blockState) -> {
            if (world.getBlockEntity(blockEntity.getPos().add(BlockPos.ofFloored(vec3d)))
                    instanceof SecretMagicWellBlockEntity secretMagicWellBlockEntity) {

                MatrixStack.Entry entry = matrixStack.peek();

                BlockState state = secretMagicWellBlockEntity.getBlockState();
                if (secretMagicWellBlockEntity.getWorld() == null || state.isAir()) {
                    return;
                }

                matrixStack.push();
                matrixStack.translate(vec3d.getX(), vec3d.getY(), vec3d.getZ());

                boolean fluid = secretMagicWellBlockEntity.getBlockState().isLiquid();

                RenderLayer renderLayer = RenderLayers.getBlockLayer(state);

                if (!fluid) {
                    VertexConsumerProvider vertexConsumerProvider2;

                    SortedSet<BlockBreakingInfo> set = MinecraftClient.getInstance().worldRenderer.blockBreakingProgressions
                            .get(secretMagicWellBlockEntity.getPos().asLong());

                    if (set != null && set.last().getStage() >= 0) {
                        OverlayVertexConsumer vertexConsumer = new OverlayVertexConsumer(MinecraftClient.getInstance()
                                .getBufferBuilders().getEffectVertexConsumers().getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(set.last().getStage())),
                                entry.getPositionMatrix(), entry.getNormalMatrix(), 1.0f);

                        vertexConsumerProvider2 = layer -> {
                            VertexConsumer vertexConsumer2 = consumerProvider.getBuffer(layer);
                            if (layer.hasCrumbling()) {
                                return VertexConsumers.union(vertexConsumer, vertexConsumer2);
                            }
                            return vertexConsumer2;
                        };
                    } else {
                        vertexConsumerProvider2 = consumerProvider;
                    }

                    VertexConsumer vertexConsumer;

                    if (alpha > 0f) {
                        TranslucentVertexConsumer translucentVertexConsumer = new TranslucentVertexConsumer(vertexConsumerProvider2.getBuffer(RenderLayer.getTranslucent()));
                        translucentVertexConsumer.setAlpha(alpha);

                        vertexConsumer = translucentVertexConsumer;
                    } else {
                        vertexConsumer = vertexConsumerProvider2.getBuffer(renderLayer);
                    }

                    renderBlock(state, secretMagicWellBlockEntity.getPos(), secretMagicWellBlockEntity.getWorld(),
                            matrixStack, vertexConsumer, true, secretMagicWellBlockEntity.getWorld().getRandom());
                }
                matrixStack.pop();
            }
        });

        consumerProvider.draw();
        tessellator.getBuffer().clear();

        SecretMagicWellBlockEntity.FLUIDS.forEach((vec3d, blockState) -> {
            if (world.getBlockEntity(blockEntity.getPos().add(BlockPos.ofFloored(vec3d)))
                    instanceof SecretMagicWellBlockEntity secretMagicWellBlockEntity) {
                BlockState state = secretMagicWellBlockEntity.getBlockState();
                if (secretMagicWellBlockEntity.getWorld() == null || state.isAir()) {
                    return;
                }

                matrixStack.push();
                matrixStack.translate(vec3d.getX(), vec3d.getY(), vec3d.getZ());

                FluidState fluidState = getFluidState(secretMagicWellBlockEntity.getBlockState());
                boolean fluid = secretMagicWellBlockEntity.getBlockState().isLiquid();

                if (fluid) {
                    RenderLayer renderLayer = RenderLayer.getTranslucent();

                    renderFluid(secretMagicWellBlockEntity.getWorld(), matrixStack.peek().getPositionMatrix(), secretMagicWellBlockEntity.getPos(),
                            consumerProvider.getBuffer(renderLayer), secretMagicWellBlockEntity.getBlockState(), fluidState, alpha);
                }

                matrixStack.pop();
            }
        });

        consumerProvider.draw();
        tessellator.getBuffer().clear();
    }

    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random) {
        try {
            BlockRenderType blockRenderType = state.getRenderType();
            if (blockRenderType == BlockRenderType.MODEL || state.isOf(ItemRegistry.SECRET_MAGIC_WELL_TORCH)) {
                this.blockRenderManager.getModelRenderer().render(world, this.blockRenderManager.getModel(state), state, pos, matrices, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
            }
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tesselating block in world");
            CrashReportSection crashReportSection = crashReport.addElement("Block being tesselated");
            CrashReportSection.addBlockInfo(crashReportSection, world, pos, state);
            throw new CrashException(crashReport);
        }
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    private static boolean isSameFluid(FluidState a, FluidState b) {
        return b.getFluid().matchesType(a.getFluid());
    }

    public void renderFluid(BlockRenderView world, Matrix4f matrix4f, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, float alpha) {
        float ag;
        float af;
        float ae;
        float ad;
        float ac;
        float ab;
        float z;
        float y;
        float r;
        float q;
        float p;
        float o;
        boolean bl = fluidState.isIn(FluidTags.LAVA);

        Sprite[] sprites = fluidRenderer.waterSprites;
        int i = bl ? 0xFFFFFF : BiomeColors.getWaterColor(world, pos);
        float f = (float) (i >> 16 & 0xFF) / 255.0f;
        float g = (float) (i >> 8 & 0xFF) / 255.0f;
        float h = (float) (i & 0xFF) / 255.0f;
        BlockState blockState2 = world.getBlockState(pos.offset(Direction.DOWN));
        FluidState fluidState2 = getFluidState(blockState2);
        BlockState blockState3 = world.getBlockState(pos.offset(Direction.UP));
        FluidState fluidState3 = getFluidState(blockState3);
        BlockState blockState4 = world.getBlockState(pos.offset(Direction.NORTH));
        FluidState fluidState4 = getFluidState(blockState4);
        BlockState blockState5 = world.getBlockState(pos.offset(Direction.SOUTH));
        FluidState fluidState5 = getFluidState(blockState5);
        BlockState blockState6 = world.getBlockState(pos.offset(Direction.WEST));
        FluidState fluidState6 = getFluidState(blockState6);
        BlockState blockState7 = world.getBlockState(pos.offset(Direction.EAST));
        FluidState fluidState7 = getFluidState(blockState7);
        boolean bl2 = !isSameFluid(fluidState, fluidState3);
        boolean bl3 = FluidRenderer.shouldRenderSide(world, pos, fluidState, blockState, Direction.DOWN, fluidState2) && !FluidRenderer.isSideCovered(world, pos, Direction.DOWN, 0.8888889f, blockState2);
        boolean bl4 = FluidRenderer.shouldRenderSide(world, pos, fluidState, blockState, Direction.NORTH, fluidState4);
        boolean bl5 = FluidRenderer.shouldRenderSide(world, pos, fluidState, blockState, Direction.SOUTH, fluidState5);
        boolean bl6 = FluidRenderer.shouldRenderSide(world, pos, fluidState, blockState, Direction.WEST, fluidState6);
        boolean bl7 = FluidRenderer.shouldRenderSide(world, pos, fluidState, blockState, Direction.EAST, fluidState7);
        if (!(bl2 || bl3 || bl7 || bl6 || bl4 || bl5)) {
            return;
        }
        float j = world.getBrightness(Direction.DOWN, true);
        float k = world.getBrightness(Direction.UP, true);
        float l = world.getBrightness(Direction.NORTH, true);
        float m = world.getBrightness(Direction.WEST, true);
        Fluid fluid = fluidState.getFluid();
        float n = this.fluidRenderer.getFluidHeight(world, fluid, pos, blockState, fluidState);
        if (n >= 1.0f) {
            o = 1.0f;
            p = 1.0f;
            q = 1.0f;
            r = 1.0f;
        } else {
            float s = this.fluidRenderer.getFluidHeight(world, fluid, pos.north(), blockState4, fluidState4);
            float t = this.fluidRenderer.getFluidHeight(world, fluid, pos.south(), blockState5, fluidState5);
            float u = this.fluidRenderer.getFluidHeight(world, fluid, pos.east(), blockState7, fluidState7);
            float v = this.fluidRenderer.getFluidHeight(world, fluid, pos.west(), blockState6, fluidState6);
            o = this.fluidRenderer.calculateFluidHeight(world, fluid, n, s, u, pos.offset(Direction.NORTH).offset(Direction.EAST));
            p = this.fluidRenderer.calculateFluidHeight(world, fluid, n, s, v, pos.offset(Direction.NORTH).offset(Direction.WEST));
            q = this.fluidRenderer.calculateFluidHeight(world, fluid, n, t, u, pos.offset(Direction.SOUTH).offset(Direction.EAST));
            r = this.fluidRenderer.calculateFluidHeight(world, fluid, n, t, v, pos.offset(Direction.SOUTH).offset(Direction.WEST));
        }
        Sprite sprite;
        final double d = 0;
        final double e = 0;
        final double w = 0;

        float x = 0.001f;
        float f2 = y = bl3 ? 0.001f : 0.0f;
        if (bl2 && !this.fluidRenderer.isSideCovered(world, pos, Direction.UP, Math.min(Math.min(p, r), Math.min(q, o)), blockState3)) {
            float ak;
            float ai;
            float ah;
            float aa;
            p -= 0.001f;
            r -= 0.001f;
            q -= 0.001f;
            o -= 0.001f;
            Vec3d vec3d = fluidState.getVelocity(world, pos);
            if (vec3d.x == 0.0 && vec3d.z == 0.0) {
                sprite = sprites[0];
                z = sprite.getFrameU(0.0f);
                aa = sprite.getFrameV(0.0f);
                ab = z;
                ac = sprite.getFrameV(1.0f);
                ad = sprite.getFrameU(1.0f);
                ae = ac;
                af = ad;
                ag = aa;
            } else {
                sprite = sprites[1];
                ah = (float) MathHelper.atan2(vec3d.z, vec3d.x) - 1.5707964f;
                ai = MathHelper.sin(ah) * 0.25f;
                float aj = MathHelper.cos(ah) * 0.25f;
                ak = 0.5f;
                z = sprite.getFrameU(0.5f + (-aj - ai));
                aa = sprite.getFrameV(0.5f + (-aj + ai));
                ab = sprite.getFrameU(0.5f + (-aj + ai));
                ac = sprite.getFrameV(0.5f + (aj + ai));
                ad = sprite.getFrameU(0.5f + (aj + ai));
                ae = sprite.getFrameV(0.5f + (aj - ai));
                af = sprite.getFrameU(0.5f + (aj - ai));
                ag = sprite.getFrameV(0.5f + (-aj - ai));
            }
            float al = (z + ab + ad + af) / 4.0f;
            ah = (aa + ac + ae + ag) / 4.0f;
            ai = sprites[0].getAnimationFrameDelta();
            z = MathHelper.lerp(ai, z, al);
            ab = MathHelper.lerp(ai, ab, al);
            ad = MathHelper.lerp(ai, ad, al);
            af = MathHelper.lerp(ai, af, al);
            aa = MathHelper.lerp(ai, aa, ah);
            ac = MathHelper.lerp(ai, ac, ah);
            ae = MathHelper.lerp(ai, ae, ah);
            ag = MathHelper.lerp(ai, ag, ah);
            int am = this.fluidRenderer.getLight(world, pos);
            ak = k * f;
            float an = k * g;
            float ao = k * h;
            vertex(vertexConsumer, matrix4f, d + 0.0, e + (double) p, w + 0.0, ak, an, ao, z, aa, am, alpha);
            vertex(vertexConsumer, matrix4f, d + 0.0, e + (double) r, w + 1.0, ak, an, ao, ab, ac, am, alpha);
            vertex(vertexConsumer, matrix4f, d + 1.0, e + (double) q, w + 1.0, ak, an, ao, ad, ae, am, alpha);
            vertex(vertexConsumer, matrix4f, d + 1.0, e + (double) o, w + 0.0, ak, an, ao, af, ag, am, alpha);
            if (fluidState.canFlowTo(world, pos.up())) {
                vertex(vertexConsumer, matrix4f, d + 0.0, e + (double) p, w + 0.0, ak, an, ao, z, aa, am, alpha);
                vertex(vertexConsumer, matrix4f, d + 1.0, e + (double) o, w + 0.0, ak, an, ao, af, ag, am, alpha);
                vertex(vertexConsumer, matrix4f, d + 1.0, e + (double) q, w + 1.0, ak, an, ao, ad, ae, am, alpha);
                vertex(vertexConsumer, matrix4f, d + 0.0, e + (double) r, w + 1.0, ak, an, ao, ab, ac, am, alpha);
            }
        }
        if (bl3) {
            z = sprites[0].getMinU();
            ab = sprites[0].getMaxU();
            ad = sprites[0].getMinV();
            af = sprites[0].getMaxV();
            int ap = this.fluidRenderer.getLight(world, pos.down());
            ac = j * f;
            ae = j * g;
            ag = j * h;
            vertex(vertexConsumer, matrix4f, d, e + (double) y, w + 1.0, ac, ae, ag, z, af, ap, alpha);
            vertex(vertexConsumer, matrix4f, d, e + (double) y, w, ac, ae, ag, z, ad, ap, alpha);
            vertex(vertexConsumer, matrix4f, d + 1.0, e + (double) y, w, ac, ae, ag, ab, ad, ap, alpha);
            vertex(vertexConsumer, matrix4f, d + 1.0, e + (double) y, w + 1.0, ac, ae, ag, ab, af, ap, alpha);
        }
        int aq = this.fluidRenderer.getLight(world, pos);
        for (Direction direction : Direction.Type.HORIZONTAL) {
            Block block;
            double au;
            double at;
            double as;
            double ar;
            float aa;
            if (!(switch (direction) {
                case NORTH -> {
                    af = p;
                    aa = o;
                    ar = d;
                    as = d + 1.0;
                    at = w + (double) 0.001f;
                    au = w + (double) 0.001f;
                    yield bl4;
                }
                case SOUTH -> {
                    af = q;
                    aa = r;
                    ar = d + 1.0;
                    as = d;
                    at = w + 1.0 - (double) 0.001f;
                    au = w + 1.0 - (double) 0.001f;
                    yield bl5;
                }
                case WEST -> {
                    af = r;
                    aa = p;
                    ar = d + (double) 0.001f;
                    as = d + (double) 0.001f;
                    at = w + 1.0;
                    au = w;
                    yield bl6;
                }
                default -> {
                    af = o;
                    aa = q;
                    ar = d + 1.0 - (double) 0.001f;
                    as = d + 1.0 - (double) 0.001f;
                    at = w;
                    au = w + 1.0;
                    yield bl7;
                }
            }) || this.fluidRenderer.isSideCovered(world, pos, direction, Math.max(af, aa), world.getBlockState(pos.offset(direction))))
                continue;
            BlockPos blockPos = pos.offset(direction);
            Sprite sprite2 = sprites[1];
            if (!bl && ((block = world.getBlockState(blockPos).getBlock()) instanceof TranslucentBlock || block instanceof LeavesBlock)) {
                sprite2 = this.fluidRenderer.waterOverlaySprite;
            }
            float av = sprite2.getFrameU(0.0f);
            float aw = sprite2.getFrameU(0.5f);
            float ax = sprite2.getFrameV((1.0f - af) * 0.5f);
            float ay = sprite2.getFrameV((1.0f - aa) * 0.5f);
            float az = sprite2.getFrameV(0.5f);
            float ba = direction.getAxis() == Direction.Axis.Z ? l : m;
            float bb = k * ba * f;
            float bc = k * ba * g;
            float bd = k * ba * h;
            vertex(vertexConsumer, matrix4f, ar, e + (double) af, at, bb, bc, bd, av, ax, aq, alpha);
            vertex(vertexConsumer, matrix4f, as, e + (double) aa, au, bb, bc, bd, aw, ay, aq, alpha);
            vertex(vertexConsumer, matrix4f, as, e + (double) y, au, bb, bc, bd, aw, az, aq, alpha);
            vertex(vertexConsumer, matrix4f, ar, e + (double) y, at, bb, bc, bd, av, az, aq, alpha);
            if (sprite2 == this.fluidRenderer.waterOverlaySprite) continue;
            vertex(vertexConsumer, matrix4f, ar, e + (double) y, at, bb, bc, bd, av, az, aq, alpha);
            vertex(vertexConsumer, matrix4f, as, e + (double) y, au, bb, bc, bd, aw, az, aq, alpha);
            vertex(vertexConsumer, matrix4f, as, e + (double) aa, au, bb, bc, bd, aw, ay, aq, alpha);
            vertex(vertexConsumer, matrix4f, ar, e + (double) af, at, bb, bc, bd, av, ax, aq, alpha);
        }
    }

    public static FluidState getFluidState(BlockState blockState) {
        if (blockState.getBlock() instanceof SecretMagicWellWaterBlock secretMagicWellWaterBlock) {
            return secretMagicWellWaterBlock.getRealFluidState(blockState);
        }

        return blockState.getFluidState();
    }

    public final void vertex(VertexConsumer vertexConsumer, Matrix4f model, double x, double y, double z, float red, float green, float blue, float u, float v, int light, float alpha) {
        vertexConsumer.vertex(model, (float) x, (float) y, (float) z).color(red, green, blue, alpha).texture(u, v).light(light).normal(1.0F, 1.0F, 1.0F).next();
    }

    @Override
    public boolean rendersOutsideBoundingBox(SecretMagicWellBlockEntity secretMagicWellBlockEntity) {
        return true;
    }
}
