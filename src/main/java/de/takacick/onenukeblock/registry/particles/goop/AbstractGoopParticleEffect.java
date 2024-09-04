package de.takacick.onenukeblock.registry.particles.goop;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public abstract class AbstractGoopParticleEffect implements ParticleEffect {

    protected static final Codec<Float> SCALE_CODEC = Codec.FLOAT.validate(DataResult::success);

    protected final Vector3f color;
    protected final float scale;

    public AbstractGoopParticleEffect(Vector3f color, float scale) {
        this.color = color;
        this.scale = MathHelper.clamp(scale, 0.01f, 1f);
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }
}