package de.takacick.upgradebody.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Locale;

public record TargetParticleEffect(ParticleType<TargetParticleEffect> particleType,
                                   int owner) implements ParticleEffect {

    public static final Factory<TargetParticleEffect> FACTORY = new Factory<>() {
        @Override
        public TargetParticleEffect read(ParticleType<TargetParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            int f = stringReader.readInt();
            stringReader.expect(' ');
            return new TargetParticleEffect(particleType, f);
        }

        @Override
        public TargetParticleEffect read(ParticleType<TargetParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new TargetParticleEffect(particleType, packetByteBuf.readInt());
        }
    };

    public ParticleType<TargetParticleEffect> getType() {
        return particleType();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.owner);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %2d", Registries.PARTICLE_TYPE.getId(this.getType()), this.owner);
    }
}
