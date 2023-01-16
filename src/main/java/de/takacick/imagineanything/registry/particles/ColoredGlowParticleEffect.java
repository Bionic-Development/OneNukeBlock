package de.takacick.imagineanything.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public record ColoredGlowParticleEffect(ParticleType<ColoredGlowParticleEffect> particleType,
                                        Vec3f color) implements ParticleEffect {

    public static final Factory<ColoredGlowParticleEffect> FACTORY = new Factory<>() {
        @Override
        public ColoredGlowParticleEffect read(ParticleType<ColoredGlowParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = stringReader.readFloat();
            stringReader.expect(' ');
            float g = stringReader.readFloat();
            stringReader.expect(' ');
            float b = stringReader.readFloat();
            return new ColoredGlowParticleEffect(particleType, new Vec3f(r, g, b));
        }

        @Override
        public ColoredGlowParticleEffect read(ParticleType<ColoredGlowParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new ColoredGlowParticleEffect(particleType, new Vec3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat()));
        }
    };

    public ParticleType<ColoredGlowParticleEffect> getType() {
        return particleType();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.color.getX());
        buf.writeFloat(this.color.getY());
        buf.writeFloat(this.color.getZ());
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), this.color.getX(), this.color.getY(), this.color.getZ());
    }
}
