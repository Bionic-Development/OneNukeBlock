package de.takacick.secretcraftbase.registry.entity.custom;

import de.takacick.secretcraftbase.server.datatracker.BezierCurve;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BreakingBlockEntity extends AbstractBlockEntity {

    public BreakingBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    float getStartSize() {
        return 1f;
    }

    @Override
    public void tick() {
        this.prevSize = this.size;
        this.prevProgress = this.progress;

        this.size = MathHelper.clamp(this.size - 0.05f, 0.25f, 1f);

        if (Math.abs(getTrackedSize() - this.size) > 0.1f) {
            this.size = getTrackedSize();
        }

        if (this.size < 0.5f) {
            this.progress = MathHelper.clamp(this.progress + 0.05f, 0f, 1.2f);

            if (Math.abs(getTrackedProgress() - this.progress) > 0.2f) {
                this.progress = getTrackedProgress();
            }

            BezierCurve bezierCurve = getBezierCurve();
            Vec3d prevPos = bezierCurve.getPos(this.prevProgress);
            Vec3d pos = bezierCurve.getPos(this.progress);

            this.prevX = prevPos.getX();
            this.prevY = prevPos.getY();
            this.prevZ = prevPos.getZ();
            this.setPos(pos.getX(), pos.getY(), pos.getZ());

            if ((!getWorld().isClient && this.progress >= 1.2f) || (getWorld().isClient && this.progress >= 1f)) {

                if (getWorld().isClient) {
                    if (random.nextDouble() <= 0.3) {
                        BlockSoundGroup soundGroup = getBlockState().getSoundGroup();
                        getWorld().playSound(getX(), getY(), getZ(), soundGroup.getBreakSound(), SoundCategory.BLOCKS, (soundGroup.getVolume() + 1.0f) / 2.0f, soundGroup.getPitch() * 0.8f, false);
                    }

                    for (int j = 1; j < 5; j++) {
                        double x = random.nextGaussian();
                        double y = random.nextDouble();
                        double z = random.nextGaussian();

                        getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, getBlockState()),
                                this.getX() + x * 0.1, this.getY() + y * 0.1, this.getZ() + z * 0.1,
                                x * 0.05, y * 0.5, z * 0.05);
                    }
                }

                this.discard();
            }
        }

        if (!getWorld().isClient) {
            setTrackedSize(this.size);
            setTrackedProgress(this.progress);
        }

        super.tick();
    }
}
