package de.takacick.illegalwars;

import de.takacick.illegalwars.registry.EffectRegistry;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.bossbar.BossBarUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.boss.BossBar;
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
    public static final BossBar.Color LIGHT_BLUE_BAR = BossBarUtils.register(new Identifier(MOD_ID, "light_blue")).texture(new Identifier(BionicUtils.MOD_ID, "textures/gui/custom_bar.png"), 256, 10).color(0x33D5E6).build();

    @Override
    public void onInitialize() {
        EffectRegistry.register();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.illegalwars"))
                .icon(ItemRegistry.BASE_WARS_MONEY_WHEEL_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.BASE_WARS_MONEY_WHEEL_ITEM);
                    entries.add(ItemRegistry.MONEY_BLOCK_ITEM);
                    entries.add(ItemRegistry.POOP_LAUNCHER_ITEM);
                    entries.add(ItemRegistry.SLUDGE_BUCKET);
                    entries.add(ItemRegistry.RAT);
                    entries.add(ItemRegistry.KING_RAT_TRIAL_SPAWNER_ITEM);
                    entries.add(ItemRegistry.DRIPSTONE_SPIKES_ITEM);
                    entries.add(ItemRegistry.PIGLIN_GOLD_TURRET_ITEM);
                    entries.add(ItemRegistry.SHARK);
                    entries.add(ItemRegistry.PROTO_PUPPY);
                    entries.add(ItemRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER_ITEM);
                    entries.add(ItemRegistry.COMMAND_BLOCK_PRESSURE_PLATES_ITEM);
                }).build());
    }
}
