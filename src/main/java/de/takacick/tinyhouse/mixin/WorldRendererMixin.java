package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.registry.ParticleRegistry;
import de.takacick.tinyhouse.registry.block.entity.AerialChickenCannonBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 813923) {
            if (data == 0) {
                Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);

                for (int x = 0; x <= 20; x++) {
                    Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextDouble(), this.world.getRandom().nextGaussian());
                    this.world.addParticle(ParticleTypes.FLAME, true, vec3d.getX() + vel.getX() * 0.3, vec3d.getY() + vel.getY() * 0.01, vec3d.getZ() + vel.getZ() * 0.3,
                            vel.getX() * 0.01, vel.getY() * 0.2, vel.getZ() * 0.01);
                    this.world.addParticle(ParticleTypes.SMOKE, true, vec3d.getX() + vel.getX() * 0.3, vec3d.getY() + vel.getY() * 0.01, vec3d.getZ() + vel.getZ() * 0.3,
                            vel.getX() * 0.01, vel.getY() * 0.2, vel.getZ() * 0.01);
                }

                this.world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1f, 1f, true);
                this.world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1f, 1f, true);
            } else if (data == 1) {
                Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);

                for (int x = 0; x <= 20; x++) {
                    Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextDouble(), this.world.getRandom().nextGaussian());
                    this.world.addParticle(ParticleTypes.SMOKE, true, vec3d.getX() + vel.getX() * 0.3, vec3d.getY() + vel.getY() * 0.01, vec3d.getZ() + vel.getZ() * 0.3,
                            vel.getX() * 0.01, vel.getY() * 0.01, vel.getZ() * 0.01);
                }

                this.world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f, true);
            } else if (data == 2) {
                Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);

                for (int x = 0; x <= 5; x++) {
                    Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextDouble(), this.world.getRandom().nextGaussian());
                    this.world.addParticle(ParticleTypes.SMOKE, true, vec3d.getX() + vel.getX() * 0.3, vec3d.getY() + vel.getY() * 0.01, vec3d.getZ() + vel.getZ() * 0.3,
                            vel.getX() * 0.001, vel.getY() * 0.01, vel.getZ() * 0.001);
                }
            } else if (data == 3) {
                Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);

                for (int x = 0; x <= 25; x++) {
                    Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextDouble(), this.world.getRandom().nextGaussian());
                    this.world.addParticle(ParticleTypes.SNOWFLAKE, true, vec3d.getX() + vel.getX() * 0.3, vec3d.getY() + vel.getY() * 0.01, vec3d.getZ() + vel.getZ() * 0.3,
                            vel.getX() * 0.01, vel.getY() * 0.2, vel.getZ() * 0.01);
                }

                BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState());

                for (int x = 0; x <= 10; x++) {
                    Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextDouble(), this.world.getRandom().nextGaussian());
                    this.world.addParticle(particleEffect, true, vec3d.getX() + vel.getX() * 0.3, vec3d.getY() + vel.getY() * 0.01, vec3d.getZ() + vel.getZ() * 0.3,
                            vel.getX() * 0.01, vel.getY() * 0.2, vel.getZ() * 0.01);
                }
                this.world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(), SoundEvents.BLOCK_POWDER_SNOW_PLACE, SoundCategory.BLOCKS, 1f, 1f, true);
            } else if (data == 4) {
                Vec3d pos = Vec3d.ofCenter(blockPos);

                for (int i = 0; i < 16; ++i) {
                    double g = 0.4 * this.world.getRandom().nextGaussian();
                    double h = 0.4 * this.world.getRandom().nextGaussian();
                    double j = 0.4 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }
                for (int i = 0; i < 5; ++i) {
                    double g = 0.4 * this.world.getRandom().nextGaussian();
                    double h = 0.4 * this.world.getRandom().nextGaussian();
                    double j = 0.4 * this.world.getRandom().nextGaussian();
                    this.world.addParticle(ParticleTypes.HEART,
                            i % 2 == 0,
                            pos.getX() + g, pos.getY() + h, pos.getZ() + j,
                            g * 0.3, h * 0.3, j * 0.3);
                }

                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.BLOCKS, 1f, 1f, true);
                this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.BLOCKS, 0.8f, 1f, true);
            } else if (data == 5) {

                if (this.world.getBlockEntity(blockPos) instanceof AerialChickenCannonBlockEntity aerialChickenCannonBlockEntity) {
                    Vec3d vec3d = aerialChickenCannonBlockEntity.getRotationVector();
                    Vec3d pos = Vec3d.ofCenter(blockPos);
                    Vec3d finalPos = pos.add(vec3d);

                    for (int x = 0; x <= 20; x++) {
                        Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextGaussian(), this.world.getRandom().nextGaussian());
                        this.world.addParticle(ParticleTypes.SMOKE, true, finalPos.getX() + vel.getX() * 0.1, finalPos.getY() + vel.getY() * 0.1, finalPos.getZ() + vel.getZ() * 0.1,
                                vec3d.getX() * 0.23 + vel.getX() * 0.005, vec3d.getY() * 0.2 + vel.getY() * 0.005, vec3d.getZ() * 0.2 + vel.getZ() * 0.005);
                    }

                    for (int x = 0; x <= 10; x++) {
                        Vec3d vel = new Vec3d(this.world.getRandom().nextGaussian(), this.world.getRandom().nextGaussian(), this.world.getRandom().nextGaussian());
                        this.world.addParticle(ParticleRegistry.FEATHER, true, finalPos.getX() + vel.getX() * 0.15, finalPos.getY() + vel.getY() * 0.15, finalPos.getZ() + vel.getZ() * 0.15,
                                vec3d.getX() * 0.3 + vel.getX() * 0.05, vec3d.getY() * 0.3 + vel.getY() * 0.005, vec3d.getZ() * 0.3 + vel.getZ() * 0.05);
                    }

                    this.world.playSound(finalPos.getX(), finalPos.getY(), finalPos.getZ(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.BLOCKS, 1f, 1f, true);
                    this.world.playSound(finalPos.getX(), finalPos.getY(), finalPos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.25f, 1f, true);
                }
            }
            info.cancel();
        }
    }

}
