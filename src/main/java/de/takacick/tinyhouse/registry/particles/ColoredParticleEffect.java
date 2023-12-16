package de.takacick.tinyhouse.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import org.joml.Vector3f;

import java.util.Locale;

public record ColoredParticleEffect(ParticleType<ColoredParticleEffect> particleType,
                                    Vector3f color) implements ParticleEffect {

    public static final Factory<ColoredParticleEffect> FACTORY = new Factory<>() {
        @Override
        public ColoredParticleEffect read(ParticleType<ColoredParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = stringReader.readFloat();
            stringReader.expect(' ');
            float g = stringReader.readFloat();
            stringReader.expect(' ');
            float b = stringReader.readFloat();
            return new ColoredParticleEffect(particleType, new Vector3f(r, g, b));
        }

        @Override
        public ColoredParticleEffect read(ParticleType<ColoredParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new ColoredParticleEffect(particleType, new Vector3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat()));
        }
    };

    public ParticleType<ColoredParticleEffect> getType() {
        return particleType();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.color.x());
        buf.writeFloat(this.color.y());
        buf.writeFloat(this.color.z());
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.color.x(), this.color.y(), this.color.z());
    }
}
