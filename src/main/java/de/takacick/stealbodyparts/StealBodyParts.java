package de.takacick.stealbodyparts;

import de.takacick.stealbodyparts.registry.EntityRegistry;
import de.takacick.stealbodyparts.registry.ItemRegistry;
import de.takacick.stealbodyparts.registry.ParticleRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class StealBodyParts implements ModInitializer {

    public static final String MOD_ID = "stealbodyparts";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "stealbodyparts"), () -> new ItemStack(ItemRegistry.HEAD));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "stealbodyparts");

    @Override
    public void onInitialize() {
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "headremoval"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {

            });
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
            ServerPlayNetworking.send(serverPlayerEntity, new Identifier(StealBodyParts.MOD_ID, "healthupdate"), packetByteBuf);
        }
    }
}
