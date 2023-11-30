package de.takacick.elementalblock;

import de.takacick.elementalblock.registry.EntityRegistry;
import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.ParticleRegistry;
import de.takacick.elementalblock.server.oneblock.OneBlockHandler;
import net.fabricmc.api.ModInitializer;
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

import java.util.ArrayList;
import java.util.List;

public class OneElementalBlock implements ModInitializer {

    public static final String MOD_ID = "elementalblock";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "illegalbase"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "elementalblock");

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.elementalblock"))
                .icon(ItemRegistry.EARTH_ELEMENTAL_BLOCK_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.EARTH_ELEMENTAL_BLOCK_ITEM);
                    entries.add(ItemRegistry.AIR_ELEMENTAL_BLOCK_ITEM);
                    entries.add(ItemRegistry.WATER_ELEMENTAL_BLOCK_ITEM);
                    entries.add(ItemRegistry.FIRE_ELEMENTAL_BLOCK_ITEM);
                    entries.add(ItemRegistry.TERRA_DRILL);
                    entries.add(ItemRegistry.WORM);
                    entries.add(ItemRegistry.DIRT_HEART_ITEM);
                    entries.add(ItemRegistry.TREE);
                    entries.add(ItemRegistry.COBBLE_GAUNTLET);
                    entries.add(ItemRegistry.CLOUD_BLOCK_ITEM);
                    entries.add(ItemRegistry.CLOUD_LOG_ITEM);
                    entries.add(ItemRegistry.CLOUD_PLANKS_ITEM);
                    entries.add(ItemRegistry.MAGIC_CLOUD_BUDDY);
                    entries.add(ItemRegistry.WHISPERWIND_BOW);
                    entries.add(ItemRegistry.WATER_BLOCK_ITEM);
                    entries.add(ItemRegistry.WATER_ARMOR_VESSEL);
                    entries.add(ItemRegistry.TSUNAMIC_TRIDENT);
                    entries.add(ItemRegistry.LAVA_BLOCK_ITEM);
                    entries.add(ItemRegistry.MAGMA_LOG_ITEM);
                    entries.add(ItemRegistry.MAGMA_PLANKS_ITEM);
                    entries.add(ItemRegistry.MAGMA_GRASS_BLOCK_ITEM);
                    entries.add(ItemRegistry.MAGIC_LAVA_SCRIPTURE);
                }).build());

        new OneBlockHandler();
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
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(OneElementalBlock.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }

}
