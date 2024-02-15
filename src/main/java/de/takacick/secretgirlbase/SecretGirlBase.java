package de.takacick.secretgirlbase;

import de.takacick.secretgirlbase.access.PlayerProperties;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.ItemRegistry;
import de.takacick.secretgirlbase.registry.ParticleRegistry;
import de.takacick.secretgirlbase.server.commands.SecretGirlBaseCommand;
import de.takacick.utils.BionicUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
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
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class SecretGirlBase implements ModInitializer {

    public static final String MOD_ID = "secretgirlbase";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "secretgirlbase"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "secretgirlbase");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SecretGirlBaseCommand.register(dispatcher);
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.secretgirlbase"))
                .icon(ItemRegistry.ZUKO::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.ZUKO);
                    entries.add(ItemRegistry.PHONE);
                    entries.add(ItemRegistry.MAGIC_FLOWER_DOOR);
                    entries.add(ItemRegistry.MAGIC_DISAPPEARING_PLATFORM_ITEM);
                    entries.add(ItemRegistry.BUBBLE_GUM_LAUNCHER_ITEM);
                    entries.add(ItemRegistry.TEENY_BERRIES);
                    entries.add(ItemRegistry.GREAT_BERRIES);
                    entries.add(ItemRegistry.RED_BUTTON_ITEM);
                    entries.add(ItemRegistry.LEAD_CUFFS);
                    entries.add(ItemRegistry.FIREWORK_TIME_BOMB);
                }).build());

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "smokedrop"), (server, player, handler, buf, responseSender) -> {
            if (player instanceof PlayerProperties playerProperties) {
                server.execute(() -> {
                    World world = player.getWorld();
                    player.changeGameMode(GameMode.SPECTATOR);
                    playerProperties.getSmokeDrop().ifPresent(itemStack -> {
                        ItemEntity itemEntity = new ItemEntity(player.getWorld(), player.getX(), player.getBodyY(0.1f), player.getZ(), itemStack, 0, 0, 0);
                        itemEntity.setToDefaultPickupDelay();
                        world.spawnEntity(itemEntity);
                    });

                    BionicUtils.sendEntityStatus(world, player, SecretGirlBase.IDENTIFIER, 2);
                });
            }
        });
    }

    public static void updateEntityHealth(LivingEntity livingEntity, double maxHealth, boolean heal) {
        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        livingEntity.setHealth(heal ? livingEntity.getMaxHealth() : Math.min(livingEntity.getMaxHealth(), livingEntity.getHealth()));

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeDouble(maxHealth);
        packetByteBuf.writeInt((int) Math.ceil(maxHealth / 2d));
        packetByteBuf.writeFloat(livingEntity.getHealth());

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(SecretGirlBase.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }
}
