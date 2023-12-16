package de.takacick.tinyhouse.registry.particles.goop;


import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.takacick.tinyhouse.registry.ParticleRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.joml.Vector3f;

public class GoopStringParticleEffect extends AbstractGoopParticleEffect {
    public GoopStringParticleEffect(Vector3f color, float scale) {
        super(color, scale);
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GOOP_STRING;
    }

    public static class Factory implements ParticleEffect.Factory<GoopStringParticleEffect> {
        @Override
        public GoopStringParticleEffect read(ParticleType type, StringReader reader) throws CommandSyntaxException {
            Vector3f vec3f = AbstractGoopParticleEffect.readVec3(reader);
            reader.expect(' ');
            float f = reader.readFloat();
            return new GoopStringParticleEffect(vec3f, f);
        }

        @Override
        public GoopStringParticleEffect read(ParticleType type, PacketByteBuf buf) {
            return new GoopStringParticleEffect(readVec3(buf), buf.readFloat());
        }
    }
}
