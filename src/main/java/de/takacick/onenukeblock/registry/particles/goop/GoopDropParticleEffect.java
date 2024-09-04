package de.takacick.onenukeblock.registry.particles.goop;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;

public class GoopDropParticleEffect extends AbstractGoopParticleEffect {

    public static final PacketCodec<RegistryByteBuf, GoopDropParticleEffect> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VECTOR3F, effect -> effect.color, PacketCodecs.FLOAT, AbstractGoopParticleEffect::getScale, GoopDropParticleEffect::new);
    public static final MapCodec<GoopDropParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codecs.VECTOR_3F.fieldOf("color").forGetter(effect -> effect.color),
                    SCALE_CODEC.fieldOf("scale").forGetter(GoopDropParticleEffect::getScale)
            ).apply(instance, GoopDropParticleEffect::new)
    );

    public GoopDropParticleEffect(Vector3f color, float scale) {
        super(color, scale);
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GOOP_DROP;
    }
}