package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.SecretGirlBaseClient;
import de.takacick.secretgirlbase.registry.ParticleRegistry;
import de.takacick.secretgirlbase.registry.block.entity.BubbleGumLauncherBlockEntity;
import de.takacick.secretgirlbase.registry.particles.ColoredParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
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
    @Final
    private MinecraftClient client;

    @Inject(method = "processWorldEvent", at = @At("HEAD"))
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo info) {
        if (eventId == 9412934) {
            if (data == 0) {
                this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1f, 1f, true);
                this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.BLOCKS, 3f, 1f, false);
                if (this.client.inGameHud != null) {
                    this.client.inGameHud.setTitle(Text.of("§e§lStand back!"));
                    this.client.inGameHud.setSubtitle(Text.of(""));
                    this.client.inGameHud.setTitleTicks(0, 20, 5);
                }
            } else if (data >= 1 && data <= 4) {
                Direction direction = Direction.NORTH;
                BlockState blockState = this.world.getBlockState(pos);
                if (blockState.contains(HorizontalFacingBlock.FACING)) {
                    direction = blockState.get(HorizontalFacingBlock.FACING);
                }
                if (blockState.contains(WallMountedBlock.FACE)) {
                    BlockFace blockFace = blockState.get(WallMountedBlock.FACE);
                    if (blockFace.equals(BlockFace.CEILING)) {
                        direction = Direction.DOWN;
                    } else if (blockFace.equals(BlockFace.FLOOR)) {
                        direction = Direction.UP;
                    }
                }

                double g = pos.getX() + (direction.getOffsetX() != 0 ? 0.25 * direction.getOffsetX() : 0.5);
                double h = pos.getY() + (direction.getOffsetY() != 0 ? 0.25 * direction.getOffsetY() : 0.5);
                double j = pos.getZ() + (direction.getOffsetZ() != 0 ? 0.25 * direction.getOffsetZ() : 0.5);
                if (data == 1) {
                    this.world.playSound(g, h, j, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.BLOCKS, 0.6f, 1f, true);

                    for (int i = 0; i < 6; ++i) {
                        double d = this.world.getRandom().nextGaussian() * 0.4;
                        double e = this.world.getRandom().nextGaussian() * 0.15;
                        double f = this.world.getRandom().nextGaussian() * 0.4;

                        this.world.addParticle(ParticleTypes.SMOKE,
                                true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                    }
                } else if (data == 2) {
                    this.world.playSound(g, h, j, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.6f, 1f, true);

                    for (int i = 0; i < 6; ++i) {
                        double d = this.world.getRandom().nextGaussian() * 0.4;
                        double e = this.world.getRandom().nextGaussian() * 0.15;
                        double f = this.world.getRandom().nextGaussian() * 0.4;

                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                                true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                    }
                } else if (data == 3) {
                    for (int i = 0; i < 6; ++i) {
                        double d = this.world.getRandom().nextGaussian() * 0.4;
                        double e = this.world.getRandom().nextGaussian() * 0.15;
                        double f = this.world.getRandom().nextGaussian() * 0.4;

                        this.world.addParticle(ParticleTypes.SMOKE,
                                true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                    }
                } else if (data == 4) {
                    this.world.playSound(g, h, j, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.6f, 1f, true);
                    world.addFireworkParticle(g, h, j, 0, 0, 0, SecretGirlBaseClient.getRocketNbt());
                }
            } else if (data == 5) {
                if (this.world.getBlockEntity(pos) instanceof BubbleGumLauncherBlockEntity bubbleGumLauncherBlockEntity) {
                    bubbleGumLauncherBlockEntity.getShootState().start(bubbleGumLauncherBlockEntity.getAge());

                    Vec3d vec3d = bubbleGumLauncherBlockEntity.getHorizontalRotationVec().multiply(0.5);
                    Vec3d rotated = bubbleGumLauncherBlockEntity.getHorizontalRotationVec(bubbleGumLauncherBlockEntity.getYaw() + 90).multiply(0.5);

                    for (int i = 0; i < 6; ++i) {
                        Vec3d center = Vec3d.ofBottomCenter(pos).add(vec3d)
                                .add(rotated.getX() * world.getRandom().nextGaussian(),
                                        0.96875,
                                        rotated.getZ() * world.getRandom().nextGaussian());

                        double d = this.world.getRandom().nextGaussian() * 0.15;
                        double e = this.world.getRandom().nextGaussian() * 0.15;
                        double f = this.world.getRandom().nextGaussian() * 0.15;

                        this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_BUBBLE_DUST, Vec3d.unpackRgb(0xEA1CD0).toVector3f()),
                                true, center.getX() + d, center.getY() + e, center.getZ() + f,
                                vec3d.getX() * 0.5 + d * 0.1,  e * 0.3, vec3d.getZ() *  0.5  + f * 0.1);
                    }
                }
            }
        }
    }
}