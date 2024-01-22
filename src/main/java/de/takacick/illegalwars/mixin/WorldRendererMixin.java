package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.access.PiglinProperties;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.block.BaseWarsMoneyWheelBlock;
import de.takacick.illegalwars.registry.block.entity.BaseWarsMoneyWheelBlockEntity;
import de.takacick.illegalwars.registry.block.entity.PiglinGoldTurretBlockEntity;
import de.takacick.illegalwars.registry.block.entity.PoopLauncherBlockEntity;
import de.takacick.illegalwars.registry.particles.ColoredParticleEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 6435123) {

            if (data == 0) {
                if (this.world.getBlockEntity(blockPos) instanceof PoopLauncherBlockEntity poopLauncherBlockEntity) {
                    poopLauncherBlockEntity.getShootState().start(poopLauncherBlockEntity.getAge());
                }
            } else if (data == 1) {
                if (this.world.getBlockEntity(blockPos) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity) {
                    Vec3d rot = piglinGoldTurretBlockEntity.getHorizontalRotationVec().multiply(-0.875);
                    Vec3d center = Vec3d.ofBottomCenter(piglinGoldTurretBlockEntity.getPos()).add(rot).add(0, 0.875, 0);

                    List<Vector3f> colors = Arrays.asList(0xF5CC27, 0xFFD83E, 0xFFFD90, 0xFEE048).stream().map(hex -> Vec3d.unpackRgb(hex).toVector3f()).toList();

                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_EXPLOSION, new Vector3f(colors.get(world.getRandom().nextInt(colors.size())))),
                            center.getX(), center.getY(), center.getZ(),
                            0.95, 0.01, 0.01);

                    for (int i = 0; i < 25; i++) {
                        ParticleEffect particleEffect = new ColoredParticleEffect(ParticleRegistry.FLYING_DUST, new Vector3f(colors.get(world.getRandom().nextInt(colors.size()))));
                        double x = this.world.getRandom().nextGaussian();
                        double y = this.world.getRandom().nextGaussian();
                        double z = this.world.getRandom().nextGaussian();

                        this.world.addParticle(particleEffect,
                                center.getX() + x * 0.10, center.getY() + y * 0.10, center.getZ() + z * 0.10,
                                rot.getX() * 1 + x * 0.35, rot.getY() * 1 + y * 0.35, rot.getZ() * 1 + z * 0.35);
                    }
                }
            } else if (data == 2 || data == 3) {
                if (this.world.getBlockEntity(blockPos) instanceof BaseWarsMoneyWheelBlockEntity baseWarsMoneyWheelBlockEntity) {
                    Vec3d rot = Vec3d.of(baseWarsMoneyWheelBlockEntity.getCachedState().get(BaseWarsMoneyWheelBlock.FACING).rotateYClockwise().getVector());
                    Vec3d center = Vec3d.ofCenter(baseWarsMoneyWheelBlockEntity.getPos()).add(0, 1, 0);

                    List<Vector3f> colors;

                    if (data == 2) {
                        colors = Arrays.asList(0x966C4A, 0xB9855C, 0x79553A).stream().map(hex -> Vec3d.unpackRgb(hex).toVector3f()).toList();
                        this.world.playSound(center.getX(), center.getY(), center.getZ(), ParticleRegistry.FART, SoundCategory.BLOCKS, 4f, 1f, false);

                        this.client.inGameHud.setTitle(Text.of("§a$1 §6poop §4illegal §pBase"));
                    } else {
                        colors = Arrays.asList(0x65F5E3, 0x9EFEEB, 0x4BEDE6, 0x3DE0E5).stream().map(hex -> Vec3d.unpackRgb(hex).toVector3f()).toList();
                        this.world.playSound(center.getX(), center.getY(), center.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 4f, 1f, false);
                        this.client.inGameHud.setTitle(Text.of("§a$1,000,000 §4Illegal §bBase"));
                    }
                    this.client.inGameHud.setSubtitle(Text.of("§7§l[§e§lBase §c§lWars§7§l]"));
                    this.client.inGameHud.setTitleTicks(0, 30, 1);

                    this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FLASH, new Vector3f(colors.get(world.getRandom().nextInt(colors.size())))),
                            center.getX(), center.getY(), center.getZ(),
                            1.5f, 0f, 0f);

                    for (int i = 0; i < 64; i++) {
                        ParticleEffect particleEffect = new ColoredParticleEffect(ParticleRegistry.FLYING_DUST, new Vector3f(colors.get(world.getRandom().nextInt(colors.size()))));
                        double x = this.world.getRandom().nextGaussian();
                        double y = this.world.getRandom().nextGaussian();
                        double z = this.world.getRandom().nextGaussian();

                        this.world.addParticle(particleEffect, true,
                                center.getX() + rot.getX() * 1 * x, center.getY() + y * 1, center.getZ() + rot.getZ() * 1 * z,
                                rot.getX() * x * 1, Math.abs(y) * 1, rot.getZ() * z * 1);
                    }
                }
            }

            info.cancel();
        }
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    public void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (entity instanceof PiglinProperties piglinProperties
                && entity instanceof PiglinEntity piglinEntity
                && piglinProperties.isUsingPiglinGoldTurret()
                && this.world.getBlockEntity(piglinProperties.getPiglinGoldTurret()) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity) {

            piglinEntity.setYaw(piglinGoldTurretBlockEntity.getYaw());
            piglinEntity.prevYaw = piglinGoldTurretBlockEntity.getPrevYaw();

            piglinEntity.bodyYaw = piglinEntity.getYaw();
            piglinEntity.prevBodyYaw = piglinEntity.prevYaw;

            piglinEntity.setHeadYaw(piglinGoldTurretBlockEntity.getYaw());
            piglinEntity.prevHeadYaw = piglinGoldTurretBlockEntity.getPrevYaw();

            piglinEntity.setPitch(MathHelper.clamp(piglinGoldTurretBlockEntity.getPitch(), -30, 30) + 3.5f);
            piglinEntity.prevPitch = MathHelper.clamp(piglinGoldTurretBlockEntity.getPrevPitch(), -30, 30) + 3.5f;

            Vec3d vec3d = Vec3d.ofBottomCenter(piglinGoldTurretBlockEntity.getPos())
                    .add(piglinGoldTurretBlockEntity.getHorizontalRotationVec().multiply(0.85));

            piglinEntity.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
        }
    }
}
