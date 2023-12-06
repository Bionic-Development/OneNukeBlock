package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.EmeraldMoneyClient;
import de.takacick.emeraldmoney.client.entity.model.EmeraldSpellCircleModel;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.item.EmeraldGauntlet;
import de.takacick.emeraldmoney.registry.particles.ColoredParticleEffect;
import de.takacick.emeraldmoney.server.utils.AppendedObjectIterator;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.SortedSet;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Shadow
    @Final
    private Long2ObjectMap<SortedSet<BlockBreakingInfo>> blockBreakingProgressions;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;
    @Unique
    private static final Identifier emeraldmoney$TEXTURE = new Identifier(EmeraldMoney.MOD_ID, "textures/entity/emerald_spell_circle.png");
    @Unique
    private static final EmeraldSpellCircleModel emeraldmoney$EMERALD_SPELL_CIRCLE = new EmeraldSpellCircleModel(EmeraldSpellCircleModel.getTexturedModelData().createModel());

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"))
    public void renderBloodMagic(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {

        if (!camera.isThirdPerson()
                && camera.getFocusedEntity() instanceof LivingEntity livingEntity
                && livingEntity.isUsingItem()
                && livingEntity.getActiveItem().isOf(ItemRegistry.CURSED_EMERALD_STAFF)) {
            matrices.push();
            Vec3d vec3d = camera.getPos();
            double d = vec3d.getX();
            double e = vec3d.getY();
            double f = vec3d.getZ();
            VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();

            d = MathHelper.lerp(tickDelta, livingEntity.lastRenderX, livingEntity.getX()) - d;
            e = MathHelper.lerp(tickDelta, livingEntity.lastRenderY, livingEntity.getY()) - e;
            f = MathHelper.lerp(tickDelta, livingEntity.lastRenderZ, livingEntity.getZ()) - f;

            float progress = MathHelper.clamp(MathHelper.lerp(tickDelta,
                    BowItem.getPullProgress(
                            livingEntity.getActiveItem().getMaxUseTime() - livingEntity.getItemUseTimeLeft() - 1),
                    BowItem.getPullProgress(
                            livingEntity.getActiveItem().getMaxUseTime() - livingEntity.getItemUseTimeLeft())) * 2, 0, 1f);

            matrices.translate(d, e + 1.501 * 2.5f * progress, f);
            matrices.scale(-2.5f * progress, -2.5f * progress, 2.5f * progress);
            matrices.translate(0, -0.01, 0);

            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, livingEntity.age, livingEntity.age + 1) * 15f));
            emeraldmoney$EMERALD_SPELL_CIRCLE.render(matrices, immediate.getBuffer(RenderLayer.getEntityCutoutNoCull(emeraldmoney$TEXTURE)),
                    15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            matrices.pop();
        }
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    public void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity
                && livingEntity.isUsingItem()
                && livingEntity.getActiveItem().isOf(ItemRegistry.CURSED_EMERALD_STAFF)) {
            matrices.push();
            double d = cameraX;
            double e = cameraY;
            double f = cameraZ;
            VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();

            d = MathHelper.lerp(tickDelta, livingEntity.lastRenderX, livingEntity.getX()) - d;
            e = MathHelper.lerp(tickDelta, livingEntity.lastRenderY, livingEntity.getY()) - e;
            f = MathHelper.lerp(tickDelta, livingEntity.lastRenderZ, livingEntity.getZ()) - f;

            float progress = MathHelper.clamp(MathHelper.lerp(tickDelta,
                    BowItem.getPullProgress(
                            livingEntity.getActiveItem().getMaxUseTime() - livingEntity.getItemUseTimeLeft() - 1),
                    BowItem.getPullProgress(
                            livingEntity.getActiveItem().getMaxUseTime() - livingEntity.getItemUseTimeLeft())) * 2, 0, 1f);

            matrices.translate(d, e + 1.501 * 2.5f * progress, f);
            matrices.scale(-2.5f * progress, -2.5f * progress, 2.5f * progress);
            matrices.translate(0, -0.01, 0);

            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, livingEntity.age, livingEntity.age + 1) * 15f));
            emeraldmoney$EMERALD_SPELL_CIRCLE.render(matrices, immediate.getBuffer(RenderLayer.getEntityCutoutNoCull(emeraldmoney$TEXTURE)),
                    15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            matrices.pop();
        }
    }

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 821482) {
            if (data == 0) {
                Vec3d pos = Vec3d.ofCenter(blockPos);

                for (int i = 0; i < 16; ++i) {
                    double g = 0.6 * this.world.getRandom().nextGaussian();
                    double h = 0.6 * this.world.getRandom().nextDouble();
                    double j = 0.6 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.TOTEM_DUST, EmeraldMoneyClient.getEmeraldColor(world.random)),
                            true,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }

                this.world.addParticle(ParticleRegistry.EMERALD_EXPLOSION,
                        true,
                        pos.getX(), pos.getY(), pos.getZ(),
                        0, 0, 0);
                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                info.cancel();
            }
        }
    }

    @ModifyVariable(method = "render",
            at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectSet;iterator()Lit/unimi/dsi/fastutil/objects/ObjectIterator;",
                    shift = At.Shift.BY, by = 2), ordinal = 0)
    private ObjectIterator<Long2ObjectMap.Entry<SortedSet<BlockBreakingInfo>>> appendBlockBreakingProgressions(ObjectIterator<Long2ObjectMap.Entry<SortedSet<BlockBreakingInfo>>> originalIterator) {
        return new AppendedObjectIterator<>(originalIterator, getCurrentExtraBreakingInfos());
    }

    @Unique
    private Long2ObjectMap<BlockBreakingInfo> getCurrentExtraBreakingInfos() {
        assert client.player != null;

        ItemStack itemStack = this.client.player.getInventory().getMainHandStack();

        if (itemStack.getItem() instanceof EmeraldGauntlet emeraldGauntlet) {
            if (client.crosshairTarget instanceof BlockHitResult breakingInfos) {
                BlockPos crosshairPos = breakingInfos.getBlockPos();
                SortedSet<BlockBreakingInfo> infos = this.blockBreakingProgressions.get(crosshairPos.asLong());

                if (infos != null && !infos.isEmpty()) {
                    BlockBreakingInfo breakingInfo = infos.last();
                    int stage = breakingInfo.getStage();

                    List<BlockPos> positions = emeraldGauntlet.findPositions(world, client.player, crosshairPos);
                    Long2ObjectMap<BlockBreakingInfo> map = new Long2ObjectLinkedOpenHashMap<>(positions.size());

                    for (BlockPos position : positions) {
                        BlockBreakingInfo info = new BlockBreakingInfo(breakingInfo.hashCode(), position);
                        info.setStage(stage);
                        map.put(position.asLong(), info);
                    }

                    return map;
                }
            }
        }

        return Long2ObjectMaps.emptyMap();
    }
}