package de.takacick.immortalmobs.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public record ImmortalWolfParticleEffect(Vec3f color, float height) implements ParticleEffect {

    public static final Factory<ImmortalWolfParticleEffect> FACTORY = new Factory<>() {
        @Override
        public ImmortalWolfParticleEffect read(ParticleType<ImmortalWolfParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = stringReader.readFloat();
            stringReader.expect(' ');
            float g = stringReader.readFloat();
            stringReader.expect(' ');
            float b = stringReader.readFloat();
            stringReader.expect(' ');
            float f = stringReader.readFloat();
            return new ImmortalWolfParticleEffect(new Vec3f(r, g, b), f);
        }

        @Override
        public ImmortalWolfParticleEffect read(ParticleType<ImmortalWolfParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new ImmortalWolfParticleEffect(new Vec3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat()), packetByteBuf.readFloat());
        }
    };

    public ParticleType<ImmortalWolfParticleEffect> getType() {
        return ParticleRegistry.IMMORTAL_WOLF;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.color.getX());
        buf.writeFloat(this.color.getY());
        buf.writeFloat(this.color.getZ());
        buf.writeFloat(this.height);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), Float.valueOf(this.height), this.color.getX(), this.color.getY(), this.color.getZ());
    }
}
