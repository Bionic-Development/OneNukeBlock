package de.takacick.onenukeblock.utils.data;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.utils.data.item.BangMaceItemHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ItemDataComponents {

    public static final ComponentType<BangMaceItemHelper> BANG_MACE = register("bang_mace", builder -> builder.codec(BangMaceItemHelper.CODEC).packetCodec(BangMaceItemHelper.PACKET_CODEC).cache());

    public static void register() {

    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(OneNukeBlock.MOD_ID, id), builderOperator.apply(ComponentType.builder()).build());
    }
}
