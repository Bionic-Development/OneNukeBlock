package de.takacick.imagineanything.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.takacick.imagineanything.registry.ParticleRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public record GlowParticleEffect(Vec3f color, float height, float width) implements ParticleEffect {

    public static final Factory<GlowParticleEffect> FACTORY = new Factory<>() {
        @Override
        public GlowParticleEffect read(ParticleType<GlowParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = stringReader.readFloat();
            stringReader.expect(' ');
            float g = stringReader.readFloat();
            stringReader.expect(' ');
            float b = stringReader.readFloat();
            stringReader.expect(' ');
            float f = stringReader.readFloat();
            stringReader.expect(' ');
            float x = stringReader.readFloat();
            return new GlowParticleEffect(new Vec3f(r, g, b), f, x);
        }

        @Override
        public GlowParticleEffect read(ParticleType<GlowParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new GlowParticleEffect(new Vec3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat()), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };

    public ParticleType<GlowParticleEffect> getType() {
        return ParticleRegistry.GLOW;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.color.getX());
        buf.writeFloat(this.color.getY());
        buf.writeFloat(this.color.getZ());
        buf.writeFloat(this.height);
        buf.writeFloat(this.width);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), Float.valueOf(this.height), Float.valueOf(this.width), this.color.getX(), this.color.getY(), this.color.getZ());
    }
}
