package de.takacick.tinyhouse;

import de.takacick.tinyhouse.registry.EffectRegistry;
import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.ItemRegistry;
import de.takacick.tinyhouse.registry.ParticleRegistry;
import de.takacick.tinyhouse.server.TinyBaseCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TinyHouse implements ModInitializer {

    public static final String MOD_ID = "tinyhouse";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "tinyhouse"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "tinyhouse");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();
        EffectRegistry.register();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.tinyhouse"))
                .icon(ItemRegistry.TINY_BASE_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.TINY_BASE_ITEM);
                    entries.add(ItemRegistry.BLOCK_MAGNET);
                    entries.add(ItemRegistry.CHICKEN_ITEM);
                    entries.add(ItemRegistry.FIRE);
                    entries.add(ItemRegistry.FREEZING_PLATE_TRAP_ITEM);
                    entries.add(ItemRegistry.BURNING_PLATE_TRAP_ITEM);
                    entries.add(ItemRegistry.GIANT_CRUSHER_TRAP_ITEM);
                    entries.add(ItemRegistry.AERIAL_CHICKEN_CANNON_ITEM);
                    entries.add(ItemRegistry.SPINNING_PEEPEE_CHOPPA_ITEM);
                }).build());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TinyBaseCommand.register(dispatcher);
        });
    }
}
