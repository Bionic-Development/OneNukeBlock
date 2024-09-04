package de.takacick.onenukeblock.utils.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.takacick.onenukeblock.OneNukeBlock;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public record UseNukeBlockPacket(BlockPos blockPos) implements CustomPayload {

    public static final Codec<UseNukeBlockPacket> CODEC = codec(UseNukeBlockPacket::new);
    public static final Id<UseNukeBlockPacket> PACKET_ID = new Id<>(Identifier.of(OneNukeBlock.MOD_ID, "use_nuke_block"));
    public static final PacketCodec<RegistryByteBuf, UseNukeBlockPacket> PACKET_CODEC = PacketCodecs.unlimitedRegistryCodec(CODEC).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static <T extends UseNukeBlockPacket> Codec<T> codec(Function<BlockPos, T> create) {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockPos.CODEC.fieldOf("blockPos").forGetter(UseNukeBlockPacket::blockPos)
                ).apply(instance, create)
        );
    }
}
