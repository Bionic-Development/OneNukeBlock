package de.takacick.stealbodyparts;

import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.mixin.IngameHudAccessor;
import de.takacick.stealbodyparts.registry.EntityRegistry;
import de.takacick.stealbodyparts.registry.ItemRegistry;
import de.takacick.stealbodyparts.registry.ParticleRegistry;
import de.takacick.stealbodyparts.registry.entity.living.renderer.AliveMoldedBossEntityRenderer;
import de.takacick.stealbodyparts.registry.entity.living.renderer.AliveMoldingBodyEntityRenderer;
import de.takacick.stealbodyparts.registry.particles.goop.GoopDropParticle;
import de.takacick.stealbodyparts.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.stealbodyparts.registry.particles.goop.GoopParticle;
import de.takacick.stealbodyparts.registry.particles.goop.GoopStringParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class StealBodyPartsClient implements ClientModInitializer {

    private KeyBinding lightningSummon;
    private boolean lightningSummonBoolean;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.ALIVE_MOLDED_BOSS, AliveMoldedBossEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALIVE_MOLDING_BODY, AliveMoldingBodyEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);

        try {
            lightningSummon = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Head Removal",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "StealBodyParts Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (lightningSummon.isPressed() && !lightningSummonBoolean) {
                    lightningSummonBoolean = lightningSummon.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(StealBodyParts.MOD_ID, "lightningsummon"), buf);
                } else {
                    lightningSummonBoolean = lightningSummon.isPressed();
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
                    } else if (status == 2) {
                        for (int i = 0; i < 60; i++)
                            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x8a0303)), (float) (world.getRandom().nextDouble() * 3)), true,
                                    entity.getX(), entity.getRandomBodyY(), entity.getZ(), world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.20, world.getRandom().nextGaussian() * 0.25);
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
