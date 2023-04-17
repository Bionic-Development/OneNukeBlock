package de.takacick.stealbodyparts;

import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.mixin.IngameHudAccessor;
import de.takacick.stealbodyparts.registry.ItemRegistry;
import de.takacick.stealbodyparts.registry.ParticleRegistry;
import de.takacick.stealbodyparts.registry.particles.goop.GoopDropParticle;
import de.takacick.stealbodyparts.registry.particles.goop.GoopParticle;
import de.takacick.stealbodyparts.registry.particles.goop.GoopStringParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class StealBodyPartsClient implements ClientModInitializer {

    private KeyBinding headRemoval;
    private boolean headRemovalBoolean;

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);

        try {
            headRemoval = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Head Removal",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "StealBodyParts Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (headRemoval.isPressed() && !headRemovalBoolean) {
                    headRemovalBoolean = headRemoval.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(StealBodyParts.MOD_ID, "headremoval"), buf);
                } else {
                    headRemovalBoolean = headRemoval.isPressed();
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(StealBodyParts.IDENTIFIER, (client, handler, buf, responseSender) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            World world = client.world;

            int entityId = buf.readInt();
            int status = buf.readInt();

            client.execute(() -> {
                Entity entity = world.getEntityById(entityId);
                if (entity != null) {
                    if (status == 1) {
                        if (entity instanceof PlayerProperties playerProperties) {
                            playerProperties.getHeartRemovalState().startIfNotRunning(entity.age);
                        }
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(StealBodyParts.MOD_ID, "healthupdate"), (client, handler, buf, responseSender) -> {
            try {
                double maxHealthValue = buf.readDouble();
                int healthValue = buf.readInt();
                float health = buf.readFloat();

                client.execute(() -> {
                    if (client.player != null) {
                        client.player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealthValue);
                        client.player.setHealth(health);
                    }
                    ((IngameHudAccessor) client.inGameHud).setLastHealthValue(healthValue);
                    ((IngameHudAccessor) client.inGameHud).setRenderHealthValue(healthValue);
                });
            } catch (Exception exception) {

            }
        });


        ModelPredicateProviderRegistry.register(ItemRegistry.IRON_HEART_CARVER, new Identifier("using"),
                (stack, world, entity, seed) -> entity instanceof PlayerProperties playerProperties
                        && playerProperties.getHeartRemovalState().isRunning() ? 1.0F : 0.0F);
    }
}
