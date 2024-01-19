package de.takacick.illegalwars.registry.particles.goop;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.Locale;

public abstract class AbstractGoopParticleEffect implements ParticleEffect {
    protected final Vector3f color;
    protected final float scale;

    public AbstractGoopParticleEffect(Vector3f color, float scale) {
        this.color = color;
        this.scale = MathHelper.clamp(scale, 0.01f, 1f);
    }

    public static Vector3f readVec3(StringReader reader) throws CommandSyntaxException {
        reader.expect(' ');
        float f = reader.readFloat();
        reader.expect(' ');
        float g = reader.readFloat();
        reader.expect(' ');
        float h = reader.readFloat();
        return new Vector3f(f, g, h);
    }

    public static Vector3f readVec3(PacketByteBuf buf) {
        return new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.color.x());
        buf.writeFloat(this.color.y());
        buf.writeFloat(this.color.z());
        buf.writeFloat(this.scale);
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
                Registries.PARTICLE_TYPE.getId(this.getType()), this.color.x(), this.color.y(), this.color.z(), this.scale);
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }
}