package de.takacick.upgradebody;

import de.takacick.upgradebody.registry.EntityRegistry;
import de.takacick.upgradebody.registry.ItemRegistry;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.upgradebody.registry.entity.custom.renderer.EmeraldShopPortalEntityRenderer;
import de.takacick.upgradebody.registry.entity.custom.renderer.ShopItemEntityRenderer;
import de.takacick.upgradebody.registry.particles.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class UpgradeBodyClient implements ClientModInitializer {
    private final static List<Integer> EMERALD_COLORS = new ArrayList<>();

    static {
        EMERALD_COLORS.add(0x007B18);
        EMERALD_COLORS.add(0x17DD62);
        EMERALD_COLORS.add(0x82F6AD);
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.EMERALD_SHOP_PORTAL, EmeraldShopPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHOP_ITEM, ShopItemEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DUST, DustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TOTEM_DUST, TotemDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_POOF, EmeraldPoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_PORTAL, EmeraldPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_TOTEM, EmeraldTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_ITEMS, EmeraldItemsParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_EXPLOSION, EmeraldExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_EXPLOSION_EMITTER, new EmeraldExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.EMERALD_SPELL, EmeraldSpellParticle.Factory::new);

        ClientPlayNetworking.registerGlobalReceiver(UpgradeBody.IDENTIFIER, (client, handler, buf, responseSender) -> {
            if (client.player == null || client.world == null) {
                return;
            }

            ClientPlayerEntity playerEntity = client.player;
            World world = client.world;

            int entityId = buf.readInt();
            int status = buf.readInt();
            Random random = Random.create();

            client.execute(() -> {
                Entity entity = world.getEntityById(entityId);
                if (entity != null) {
                    if (status == 1) {
                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 0.8f, 1f);
                        client.particleManager.addEmitter(entity, ParticleRegistry.EMERALD_TOTEM, 2);

                        for (int i = 0; i < 15; ++i) {
                            double g = entity.getX();
                            double h = entity.getBodyY(0.45 + random.nextDouble() * 0.55);
                            double j = entity.getZ();

                            double d = random.nextGaussian() * 0.3;
                            double e = random.nextGaussian() * 0.3;
                            double f = random.nextGaussian() * 0.3;

                            world.addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, getEmeraldColor(random)),
                                    true, g + d, h + e, j + f, d, e, f);
                        }

                    }
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.EMERALD_SHOP_PORTAL, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.translate(0.5, 0, 0.5);
            matrices.scale(0.25f, 0.25f, 0.25f);

            EntityType<EmeraldShopPortalEntity> entityType = EntityRegistry.EMERALD_SHOP_PORTAL;
            EmeraldShopPortalEntity renderEntity = entityType.create(MinecraftClient.getInstance().world);
            if (renderEntity != null) {
                renderEntity.setAnimationProgress(renderEntity.getMaxAnimationProgress());
                renderEntity.age = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.age : 0;
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(renderEntity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
            }
        });
    }

    public static Vector3f getEmeraldColor(Random random) {
        return Vec3d.unpackRgb(EMERALD_COLORS.get(random.nextInt(EMERALD_COLORS.size())))
                .toVector3f();
    }
}
