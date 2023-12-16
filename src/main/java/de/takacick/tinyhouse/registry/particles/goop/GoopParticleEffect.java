package de.takacick.tinyhouse.registry.particles.goop;


import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.takacick.tinyhouse.registry.ParticleRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class GoopParticleEffect extends AbstractGoopParticleEffect {
    protected final Vec3d dir;

    public GoopParticleEffect(Vector3f color, float scale, Vec3d dir) {
        super(color, scale);
        this.dir = dir;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GOOP;
    }

    public static class Factory implements ParticleEffect.Factory<GoopParticleEffect> {
        @Override
        public GoopParticleEffect read(ParticleType type, StringReader reader) throws CommandSyntaxException {
            Vector3f vec3f = AbstractGoopParticleEffect.readVec3(reader);
            reader.expect(' ');
            float f = reader.readFloat();
            reader.expect(' ');
            Vector3f dir = readVec3(reader);
            return new GoopParticleEffect(vec3f, f, new Vec3d(dir.x(), dir.y(), dir.z()));
        }

        @Override
        public GoopParticleEffect read(ParticleType type, PacketByteBuf buf) {
            return new GoopParticleEffect(readVec3(buf), buf.readFloat(), new Vec3d(readVec3(buf)));
        }
    }

    public Vec3d getDir() {
        return dir;
    }
}