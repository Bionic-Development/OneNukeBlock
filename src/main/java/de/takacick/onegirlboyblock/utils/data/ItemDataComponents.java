package de.takacick.onegirlboyblock.utils.data;

import com.mojang.serialization.Codec;
import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.utils.data.item.BitCannonItemHelper;
import de.takacick.utils.data.codec.NbtCodecs;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ItemDataComponents {

    private static final Codec<BitCannonItemHelper> BIT_CANNON_HELPER_CODEC = NbtCodecs.createCodec(BitCannonItemHelper::from);
    private static final PacketCodec<RegistryByteBuf, BitCannonItemHelper> BIT_CANNON_HELPER_PACKET_CODEC = PacketCodecs.registryCodec(BIT_CANNON_HELPER_CODEC);
    public static final ComponentType<BitCannonItemHelper> BIT_CANNON_HELPER = register("bit_cannon_helper", builder -> builder.codec(NbtCodecs.createCodec(BitCannonItemHelper::from)).packetCodec(BIT_CANNON_HELPER_PACKET_CODEC).cache());

    public static void register() {

    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, id), builderOperator.apply(ComponentType.builder()).build());
    }
}
