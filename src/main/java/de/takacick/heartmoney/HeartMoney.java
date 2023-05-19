package de.takacick.heartmoney;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.heartmoney.access.PlayerProperties;
import de.takacick.heartmoney.commands.HeartShopCommand;
import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HeartMoney implements ModInitializer {

    public static final String MOD_ID = "heartmoney";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "heartmoney"), () -> new ItemStack(ItemRegistry.HEART_SHOP_PORTAL));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "heartmoney");

    @Override
    public void onInitialize() {
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("setheartmultiplier").requires((source) -> {
                return source.hasPermissionLevel(2);
            }).then((CommandManager.argument("target", EntityArgumentType.players())
                    .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg()).executes((context) -> {
                        double multiplier = DoubleArgumentType.getDouble(context, "multiplier");
                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "target");

                        if (playerEntities != null) {
                            playerEntities.forEach(serverPlayerEntity -> {
                                context.getSource().sendFeedback(Text.of("Set " + serverPlayerEntity.getName().getString() + "'s Heart multiplier to: §e" + multiplier), false);
                                ((PlayerProperties) serverPlayerEntity).setHeartMultiplier(multiplier);
                            });
                        } else {
                            context.getSource().sendFeedback(Text.of("§cUnknown players!"), false);
                        }
                        return 1;
                    })))).build();
            dispatcher.getRoot().addChild(rootNode);

            HeartShopCommand.register(dispatcher);
        });

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, HEART_CATALYST_ORE_UPPER.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, HEART_CATALYST_ORE_MIDDLE.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, HEART_CATALYST_ORE_SMALL.getKey().get());

    }

    public static final List<OreFeatureConfig.Target> HEART_ORES = List.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    ItemRegistry.HEART_CATALYST_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ItemRegistry.DEEPSLATE_HEART_CATALYST_ORE.getDefaultState()));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> HEART_ORE = ConfiguredFeatures.register("heartmoney_heart_catalyst_ore", Feature.ORE, new OreFeatureConfig(HEART_ORES, 9));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> HEART_ORE_SMALL = ConfiguredFeatures.register("heartmoney_heart_catalyst_ore_small", Feature.ORE, new OreFeatureConfig(HEART_ORES, 4));

    public static final RegistryEntry<PlacedFeature> HEART_CATALYST_ORE_UPPER = PlacedFeatures.register("heart_catalyst_ore_upper", HEART_ORE, OrePlacedFeatures.modifiersWithCount(90, HeightRangePlacementModifier.trapezoid(YOffset.fixed(80), YOffset.fixed(384))));
    public static final RegistryEntry<PlacedFeature> HEART_CATALYST_ORE_MIDDLE = PlacedFeatures.register("heart_catalyst_ore_middle", HEART_ORE, OrePlacedFeatures.modifiersWithCount(10, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-24), YOffset.fixed(56))));
    public static final RegistryEntry<PlacedFeature> HEART_CATALYST_ORE_SMALL = PlacedFeatures.register("heart_catalyst_ore_small", HEART_ORE_SMALL, OrePlacedFeatures.modifiersWithCount(10, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(72))));

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
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(HeartMoney.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }
}
