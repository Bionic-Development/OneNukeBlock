package de.takacick.upgradebody;

import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.ItemRegistry;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.server.commands.EmeraldShopCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UpgradeBody implements ModInitializer {

    public static final String MOD_ID = "upgradebody";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "illegalbase"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "upgradebody");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            EmeraldShopCommand.register(dispatcher);
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.upgradebody"))
                .icon(ItemRegistry.EMERALD_SHOP_PORTAL::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.EMERALD_SHOP_PORTAL);
                }).build());
    }

    public static void updateEntityHealth(LivingEntity livingEntity, double maxHealth, boolean heal) {
        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        livingEntity.setHealth(heal ? livingEntity.getMaxHealth() : Math.min(livingEntity.getMaxHealth(), livingEntity.getHealth()));

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeDouble(maxHealth);
        packetByteBuf.writeInt((int) Math.ceil(maxHealth / 2d));
        packetByteBuf.writeFloat(livingEntity.getHealth());

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(UpgradeBody.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }

}
