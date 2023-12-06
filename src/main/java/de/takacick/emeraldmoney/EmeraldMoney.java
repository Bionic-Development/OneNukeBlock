package de.takacick.emeraldmoney;

import de.takacick.emeraldmoney.registry.EntityRegistry;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.item.VillagerRobe;
import de.takacick.emeraldmoney.server.commands.EmeraldMoneyCommand;
import de.takacick.emeraldmoney.server.commands.EmeraldShopCommand;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerType;

import java.util.ArrayList;
import java.util.List;

public class EmeraldMoney implements ModInitializer {

    public static final String MOD_ID = "emeraldmoney";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "illegalbase"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "emeraldmoney");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            EmeraldMoneyCommand.register(dispatcher);
            EmeraldShopCommand.register(dispatcher);
        });

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.emeraldmoney"))
                .icon(ItemRegistry.EMERALD_WALLET::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.EMERALD_SHOP_PORTAL);
                    entries.add(ItemRegistry.VILLAGER_NOSE);
                    entries.add(ItemRegistry.EMERALD_WALLET);
                    entries.add(ItemRegistry.EMERALD_GAUNTLET);
                    entries.add(ItemRegistry.VILLAGER_DRILLER);
                    entries.add(ItemRegistry.EMERALD_HEALTH_JAR);
                    entries.add(ItemRegistry.PILLAGER_CANNON);
                    entries.add(ItemRegistry.CREEPAGER_PET);
                    entries.add(ItemRegistry.EMERALD_TOTEM);
                    entries.add(ItemRegistry.CURSED_EMERALD_STAFF);

                    Registries.VILLAGER_PROFESSION.forEach(villagerProfession -> {
                        VillagerData villagerData = new VillagerData(VillagerType.PLAINS, villagerProfession, 0);
                        entries.add(VillagerRobe.setVillagerData(ItemRegistry.VILLAGER_ROBE.getDefaultStack(), villagerData));
                    });

                    Registries.VILLAGER_TYPE.forEach(villagerType -> {
                        if (!villagerType.equals(VillagerType.PLAINS)) {
                            Registries.VILLAGER_PROFESSION.forEach(villagerProfession -> {
                                VillagerData villagerData = new VillagerData(villagerType, villagerProfession, 0);
                                entries.add(VillagerRobe.setVillagerData(ItemRegistry.VILLAGER_ROBE.getDefaultStack(), villagerData));
                            });
                        }
                    });
                }).build());
    }

    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlocks = new ArrayList<BlockPos>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return circleBlocks;
    }

    public static void updateEntityHealth(LivingEntity livingEntity, double maxHealth, boolean heal) {
        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        livingEntity.setHealth(heal ? livingEntity.getMaxHealth() : Math.min(livingEntity.getMaxHealth(), livingEntity.getHealth()));

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeDouble(maxHealth);
        packetByteBuf.writeInt((int) Math.ceil(maxHealth / 2d));
        packetByteBuf.writeFloat(livingEntity.getHealth());

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(EmeraldMoney.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }

}
