package de.takacick.raidbase;

import de.takacick.raidbase.access.PlayerProperties;
import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.raidbase.registry.ItemRegistry;
import de.takacick.raidbase.registry.ParticleRegistry;
import de.takacick.raidbase.registry.inventory.CopperHopperScreenHandler;
import de.takacick.utils.BionicUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class RaidBase implements ModInitializer {

    public static final String MOD_ID = "raidbase";
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "raidbase"));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "raidbase");

    public static final ScreenHandlerType<CopperHopperScreenHandler> COPPER_HOPPER = ScreenHandlerRegistry.registerSimple(new Identifier(RaidBase.MOD_ID, "copper_hopper"), CopperHopperScreenHandler::new);

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.raidbase"))
                .icon(ItemRegistry.PIE_LAUNCHER_ITEM::getDefaultStack)
                .entries((context, entries) -> {
                    entries.add(ItemRegistry.PIE_LAUNCHER_ITEM);
                    entries.add(ItemRegistry.PIE);
                    entries.add(ItemRegistry.GLITCHY_QUICKSAND_ITEM);
                    entries.add(ItemRegistry.BEACON_DEATH_LASER_ITEM);
                    entries.add(ItemRegistry.BUCKET_OF_ELECTRO_WATER);
                    entries.add(ItemRegistry.SLIME_SUIT);
                    entries.add(ItemRegistry.LIGHTNING);
                    entries.add(ItemRegistry.SELF_BAN_HAMMER);
                    entries.add(ItemRegistry.COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.EXPOSED_COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.WEATHERED_COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.OXIDIZED_COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.WAXED_COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.WAXED_EXPOSED_COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.WAXED_WEATHERED_COPPER_HOPPER_ITEM);
                    entries.add(ItemRegistry.WAXED_OXIDIZED_COPPER_HOPPER_ITEM);
                }).build());

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(RaidBase.MOD_ID, "exitslimesuit"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
                    playerProperties.setSlimeSuit(false);
                    ItemStack itemStack = player.getMainHandStack();
                    player.setStackInHand(Hand.MAIN_HAND, ItemRegistry.SLIME_SUIT.getDefaultStack());
                    if (!itemStack.isEmpty()) {
                        player.getInventory().offerOrDrop(itemStack);
                    }
                    BionicUtils.sendEntityStatus(player.getServerWorld(), player, RaidBase.IDENTIFIER, 6);
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_ARMOR_EQUIP_IRON, player.getSoundCategory(), 1f, 1f);
                }
            });
        });
    }


    public static void sendProjectile(PlayerEntity playerEntity, ProjectileEntity projectileEntity) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeUuid(playerEntity.getUuid());
        packetByteBuf.writeInt(projectileEntity.getId());
        packetByteBuf.writeFloat(projectileEntity.getYaw());

        if (playerEntity instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.getServerWorld().getPlayers().forEach(serverPlayerEntity -> {
                ServerPlayNetworking.send(serverPlayerEntity, new Identifier(RaidBase.MOD_ID, "slimesuit"), packetByteBuf);
            });
        }
    }
}
