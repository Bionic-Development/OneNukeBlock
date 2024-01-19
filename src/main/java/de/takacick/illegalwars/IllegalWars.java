package de.takacick.illegalwars;

import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IllegalWars implements ModInitializer {

    public static final String MOD_ID = "illegalwars";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "illegalwars"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "illegalwars");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.illegalwars"))
                .icon(ItemRegistry.PIE_LAUNCHER_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.PIE_LAUNCHER_ITEM);
                    entries.add(ItemRegistry.PIE);
                }).build());
    }
}
