package de.takacick.raidbase.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Locale;

public record ShockwaveParticleEffect(ParticleType<ShockwaveParticleEffect> particleType, int maxAge, float distance,
                                      float width) implements ParticleEffect {

    public static final Factory<ShockwaveParticleEffect> FACTORY = new Factory<>() {
        @Override
        public ShockwaveParticleEffect read(ParticleType<ShockwaveParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            int f = stringReader.readInt();
            stringReader.expect(' ');
            float x = stringReader.readFloat();
            stringReader.expect(' ');
            float d = stringReader.readFloat();
            return new ShockwaveParticleEffect(particleType, f, x, d);
        }

        @Override
        public ShockwaveParticleEffect read(ParticleType<ShockwaveParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new ShockwaveParticleEffect(particleType, packetByteBuf.readInt(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };

    public ParticleType<ShockwaveParticleEffect> getType() {
        return particleType();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.maxAge);
        buf.writeFloat(this.distance);
        buf.writeFloat(this.width);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f  %2d %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.maxAge, Float.valueOf(this.distance), Float.valueOf(this.width));
    }
}
