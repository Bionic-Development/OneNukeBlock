package de.takacick.everythinghearts.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public record HeartUpgradeParticleEffect(float height, float width, int owner) implements ParticleEffect {

    public static final Factory<HeartUpgradeParticleEffect> FACTORY = new Factory<>() {
        @Override
        public HeartUpgradeParticleEffect read(ParticleType<HeartUpgradeParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float f = stringReader.readFloat();
            float w = stringReader.readFloat();
            int i = stringReader.readInt();
            return new HeartUpgradeParticleEffect(f, w, i);
        }

        @Override
        public HeartUpgradeParticleEffect read(ParticleType<HeartUpgradeParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new HeartUpgradeParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readInt());
        }
    };

    public ParticleType<HeartUpgradeParticleEffect> getType() {
        return ParticleRegistry.HEART_UPGRADE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.height);
        buf.writeFloat(this.width);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %s", Registry.PARTICLE_TYPE.getId(this.getType()), Float.valueOf(this.height), Float.valueOf(this.width), owner);
    }
}
