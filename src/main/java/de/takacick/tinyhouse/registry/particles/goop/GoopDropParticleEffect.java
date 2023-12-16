package de.takacick.tinyhouse.registry.particles.goop;


import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.takacick.tinyhouse.registry.ParticleRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.joml.Vector3f;

public class GoopDropParticleEffect extends AbstractGoopParticleEffect {
    public GoopDropParticleEffect(Vector3f color, float scale) {
        super(color, scale);
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GOOP_DROP;
    }

    public static class Factory implements ParticleEffect.Factory<GoopDropParticleEffect> {
        @Override
        public GoopDropParticleEffect read(ParticleType type, StringReader reader) throws CommandSyntaxException {
            Vector3f vec3f = AbstractGoopParticleEffect.readVec3(reader);
            reader.expect(' ');
            float f = reader.readFloat();
            return new GoopDropParticleEffect(vec3f, f);
        }

        @Override
        public GoopDropParticleEffect read(ParticleType type, PacketByteBuf buf) {
            return new GoopDropParticleEffect(readVec3(buf), buf.readFloat());
        }
    }
}