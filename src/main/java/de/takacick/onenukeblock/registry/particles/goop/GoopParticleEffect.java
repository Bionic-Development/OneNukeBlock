package de.takacick.onenukeblock.registry.particles.goop;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class GoopParticleEffect extends AbstractGoopParticleEffect {

    public static final PacketCodec<RegistryByteBuf, GoopParticleEffect> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VECTOR3F, effect -> effect.color,
            PacketCodecs.FLOAT, AbstractGoopParticleEffect::getScale, Vec3dPacketCodec.CODEC, effect -> effect.dir, GoopParticleEffect::new);
    public static final MapCodec<GoopParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codecs.VECTOR_3F.fieldOf("color").forGetter(effect -> effect.color),
                    SCALE_CODEC.fieldOf("scale").forGetter(GoopParticleEffect::getScale),
                    Vec3d.CODEC.fieldOf("direction").forGetter(GoopParticleEffect::getDir)
            ).apply(instance, GoopParticleEffect::new)
    );

    protected final Vec3d dir;

    public GoopParticleEffect(Vector3f color, float scale, Vec3d dir) {
        super(color, scale);
        this.dir = dir;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GOOP;
    }

    public Vec3d getDir() {
        return dir;
    }

}