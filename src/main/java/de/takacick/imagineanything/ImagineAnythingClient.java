package de.takacick.imagineanything;

import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.client.ThoughtScreen;
import de.takacick.imagineanything.network.IronManExplosionHandler;
import de.takacick.imagineanything.network.TelekinesisExplosionHandler;
import de.takacick.imagineanything.network.ThanosExplosionHandler;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.ParticleRegistry;
import de.takacick.imagineanything.registry.block.entity.ImaginedGiantBedrockSpeakersBlockEntity;
import de.takacick.imagineanything.registry.block.entity.renderer.TrappedBedBlockEntityRenderer;
import de.takacick.imagineanything.registry.entity.custom.CaveConduitEntity;
import de.takacick.imagineanything.registry.entity.custom.ThoughtEntity;
import de.takacick.imagineanything.registry.entity.custom.renderer.CaveConduitEntityRenderer;
import de.takacick.imagineanything.registry.entity.custom.renderer.HologramItemEntityRenderer;
import de.takacick.imagineanything.registry.entity.custom.renderer.ThoughEntityRenderer;
import de.takacick.imagineanything.registry.entity.living.renderer.*;
import de.takacick.imagineanything.registry.entity.projectiles.renderer.*;
import de.takacick.imagineanything.registry.item.InfinityGauntletDisabled;
import de.takacick.imagineanything.registry.particles.*;
import de.takacick.imagineanything.registry.particles.goop.GoopDropParticle;
import de.takacick.imagineanything.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.imagineanything.registry.particles.goop.GoopParticle;
import de.takacick.imagineanything.registry.particles.goop.GoopStringParticle;
import de.takacick.imagineanything.registry.sound.BedrockSpeakerSoundInstance;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ImagineAnythingClient implements ClientModInitializer {

    private KeyBinding headRemoval;
    private boolean headRemovalBoolean;
    private KeyBinding ironManLaser;
    private boolean ironManLaserBoolean;
    private KeyBinding ironManLaserBullet;
    private boolean ironManLaserBulletBoolean;
    private KeyBinding ironManForcefield;
    private boolean ironManForcefieldBoolean;
    private KeyBinding telekinesisFlight;
    private boolean telekinesisFlightBoolean;
    private KeyBinding telekinesisControl;
    private boolean telekinesisControlBoolean;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.FALLING_BLOCK, CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.END_PORTAL, EndPortalEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HEAD, HeadEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GIANT_AXE_METEOR, GiantAxeMeteorEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GIANT_AXE_METEOR_SHOCKWAVE, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CAVE_CONDUIT, CaveConduitEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MYSTERIOUS_ENTITY, MysteriousEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALFRED_THE_PICKAXE, AlfredThePickaxeEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MALL_PILLAGER_GUARDS, MallPillagerGuardsEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.THANOS_CHAD, ThanosChadEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.THANOS_SHOT, ThanosShotEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HOLOGRAM_ITEM, HologramItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.THOUGHT, ThoughEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IRON_MAN_LASER_BULLET, IronManLaserBulletEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GIANT_NETHERITE_FEATHER, GiantNetheriteFeatherEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GIANT_VIBRATION, EmptyEntityRenderer::new);
        BlockEntityRendererRegistry.register(EntityRegistry.TRAPPED_BED_BLOCK, TrappedBedBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP, GoopParticle.GoopParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_DROP, GoopDropParticle.GoopDropParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GOOP_STRING, GoopStringParticle.GoopStringParticleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLOW, GlowParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLOW_TOTEM, GlowTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GLOW_SPARK, GlowSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THANOS_EXPLOSION, ThanosExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THANOS_EXPLOSION_EMITTER, new ThanosExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TELEKINESIS_EXPLOSION, TelekinesisExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.TELEKINESIS_EXPLOSION_EMITTER, new TelekinesisExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IRON_MAN_EXPLOSION, IronManExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.IRON_MAN_EXPLOSION_EMITTER, new IronManExplosionEmitterParticle.Factory());
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.FART, FartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.POOF, PoofParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.GIANT_VIBRATION, GiantVibration.Factory::new);

        try {
            headRemoval = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Head Removal",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImagineAnything Abilities")
            );
            ironManLaser = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Iron Man Laser",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImagineAnything Abilities")
            );
            ironManLaserBullet = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Iron Man Laser Bullet",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImagineAnything Abilities")
            );
            ironManForcefield = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Iron Man Forcefield",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImagineAnything Abilities")
            );
            telekinesisFlight = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Telekinesis Flight",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImagineAnything Abilities")
            );
            telekinesisControl = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding("Telekinesis Control",
                            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                            "ImagineAnything Abilities")
            );
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity == null) {
                    return;
                }

                if (headRemoval.isPressed() && !headRemovalBoolean) {
                    headRemovalBoolean = headRemoval.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "headremoval"), buf);
                } else {
                    headRemovalBoolean = headRemoval.isPressed();
                }

                if (ironManLaser.isPressed() && playerEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(1);
                    ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "ironmanlaser"), buf);
                    ironManLaserBoolean = true;
                } else {
                    if (ironManLaserBoolean) {
                        ironManLaserBoolean = false;
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(0);
                        ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "ironmanlaser"), buf);
                    }
                }

                if (ironManLaserBullet.isPressed() && !ironManLaserBulletBoolean) {
                    ironManLaserBulletBoolean = ironManLaserBullet.isPressed();
                    if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "ironmanlaserbullet"), buf);
                    }
                } else {
                    ironManLaserBulletBoolean = ironManLaserBullet.isPressed();
                }

                if (ironManForcefield.isPressed() && !ironManForcefieldBoolean) {
                    ironManForcefieldBoolean = ironManForcefield.isPressed();
                    if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "ironmanforcefield"), buf);
                    }
                } else {
                    ironManForcefieldBoolean = ironManForcefield.isPressed();
                }

                if (telekinesisFlight.isPressed() && !telekinesisFlightBoolean) {
                    telekinesisFlightBoolean = telekinesisFlight.isPressed();
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "telekinesisflight"), buf);
                } else {
                    telekinesisFlightBoolean = telekinesisFlight.isPressed();
                }

                if (telekinesisControl.isPressed()) {
                    if (!telekinesisControlBoolean) {
                        HitResult entityRaycast = entityRaycast(playerEntity, 9, 0, false);
                        if (entityRaycast instanceof EntityHitResult entityHitResult) {
                            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeInt(livingEntity.getId());
                                ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "telekinesiscontrol"), buf);
                                telekinesisControlBoolean = telekinesisControl.isPressed();
                            }
                        }
                    } else {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(-1);
                        ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "telekinesiscontrol"), buf);
                    }
                } else if (telekinesisControlBoolean) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(-2);
                    ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "telekinesiscontrol"), buf);

                    telekinesisControlBoolean = telekinesisControl.isPressed();
                }
            });
        } catch (RuntimeException ignored) {

        }

        ClientPlayNetworking.registerGlobalReceiver(ImagineAnything.IDENTIFIER, (client, handler, buf, responseSender) -> {
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
                            playerProperties.getHeadRemovalState().startIfNotRunning(entity.age);
                        }
                    } else if (status == 2) {
                        if (entity instanceof LivingEntity livingEntity) {
                            ItemStack stack = livingEntity.getMainHandStack().getItem() instanceof InfinityGauntletDisabled ? livingEntity.getMainHandStack() : playerEntity.getOffHandStack();
                            if (stack.getItem() instanceof InfinityGauntletDisabled) {
                                stack.getOrCreateNbt().putInt("destroyTicks", 50);
                                world.playSoundFromEntity(playerEntity, entity, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, entity.getSoundCategory(), 1.0f, 0.3f);

                                if (entity.equals(playerEntity)) {
                                    MinecraftClient.getInstance().inGameHud.setTitleTicks(0, 25, 0);
                                    MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§c[§e!§c] Error [§e!§c]"));
                                    MinecraftClient.getInstance().inGameHud.setSubtitle(Text.of("§eBroken beyond repair..."));
                                }
                            }
                        }
                    } else if (status == 3) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x710193));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        client.particleManager.addEmitter(entity, new ColoredGlowParticleEffect(ParticleRegistry.GLOW_TOTEM, color), 10);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight(), entity.getWidth() + 0.2f), entity.getX(), entity.getY(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.1f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.2f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.2f, entity.getZ(), 0, -1, 0);
                    } else if (status == 4) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0xFFFFFF));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), ParticleRegistry.POOF_EVENT, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        client.particleManager.addEmitter(entity, ParticleRegistry.POOF, 3);
                        client.particleManager.addEmitter(entity, ParticleRegistry.POOF, 3);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight(), entity.getWidth() + 0.2f), entity.getX(), entity.getY(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.1f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.2f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.2f, entity.getZ(), 0, -1, 0);
                    } else if (status == 5) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x7600BC));
                        world.addParticle(new GlowParticleEffect(color, 0, 0.4f), entity.getX(), entity.getY(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, -0.05f, 0.3f), entity.getX(), entity.getY() - 0.05f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, -0.1f, 0.2f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                    } else if (status == 6) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x7600BC));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        client.particleManager.addEmitter(entity, new ColoredGlowParticleEffect(ParticleRegistry.GLOW_TOTEM, color), 10);
                    } else if (status == 7) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x7600BC));
                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, entity.getSoundCategory(), 1.0f, 1f);

                        client.particleManager.addEmitter(entity, new ColoredGlowParticleEffect(ParticleRegistry.GLOW_TOTEM, color), 1);
                    } else if (status == 8) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x7600BC));
                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, entity.getSoundCategory(), 1.0f, 1f);

                        for (int i = 0; i < 25; i++) {
                            double d = 0.45000000596046448D * world.random.nextGaussian();
                            double e = 0.45000000596046448D * world.random.nextGaussian();
                            double f = 0.45000000596046448D * world.random.nextGaussian();
                            world.addParticle(new ColoredGlowParticleEffect(ParticleRegistry.GLOW_TOTEM, color), entity.getParticleX(entity.getWidth()),
                                    entity.getRandomBodyY(), entity.getParticleZ(entity.getWidth()), d, e, f);
                        }
                    } else if (status == 9) {
                        for (int i = 0; i < 8; i++)
                            world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                                    entity.getX() + 0.25 * world.getRandom().nextGaussian(), entity.getBodyY(0.5) + 0.25 * world.getRandom().nextGaussian(), entity.getZ() + 0.25 * world.getRandom().nextGaussian(),
                                    world.getRandom().nextGaussian() * 0.25,
                                    world.getRandom().nextDouble() * 0.10, world.getRandom().nextGaussian() * 0.25);
                    } else if (status == 10) {
                        for (int i = 0; i < 5; i++) {
                            world.addParticle(ParticleTypes.ENCHANT, entity.getX(), entity.getY() + 1.5, entity.getZ(),
                                    ((float) world.getRandom().nextGaussian() * 8), world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8);
                        }
                    } else if (status == 11) {
                        for (int i = 0; i < 8; i++)
                            world.addParticle(ParticleRegistry.POOF,
                                    entity.getX() + 0.25 * world.getRandom().nextGaussian(), entity.getBodyY(0.5) + 0.25 * world.getRandom().nextGaussian(), entity.getZ() + 0.25 * world.getRandom().nextGaussian(),
                                    world.getRandom().nextGaussian() * 0.1,
                                    world.getRandom().nextDouble() * 0.1, world.getRandom().nextGaussian() * 0.1);

                        world.playSoundFromEntity(playerEntity, entity, SoundEvents.ENTITY_PHANTOM_FLAP, entity.getSoundCategory(), 1.0f, 2f);
                    } else if (status == 12) {
                        for (int i = 0; i < 8; i++)
                            world.addParticle(ParticleRegistry.POOF,
                                    entity.getX() + 0.15 * world.getRandom().nextGaussian(), entity.getBodyY(0.5) + 0.15 * world.getRandom().nextGaussian(), entity.getZ() + 0.15 * world.getRandom().nextGaussian(),
                                    world.getRandom().nextGaussian() * 0.3,
                                    world.getRandom().nextDouble() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        world.playSound(entity.getX(), entity.getBodyY(0.5), entity.getZ(), ParticleRegistry.POOF_EVENT, entity.getSoundCategory(), 0.4f, 1.0f, false);
                    } else if (status == 13) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(2138367));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, entity.getSoundCategory(), 0.4f, 1.0f, false);

                        client.particleManager.addEmitter(entity, new ColoredGlowParticleEffect(ParticleRegistry.GLOW_TOTEM, color), 3);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight(), entity.getWidth() + 0.2f), entity.getX(), entity.getY(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.1f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.2f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.2f, entity.getZ(), 0, -1, 0);
                    } else if (status == 14) {
                        Vec3f color = new Vec3f(Vec3d.unpackRgb(2138367));
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, entity.getSoundCategory(), 0.4f, 1.0f, false);

                        client.particleManager.addEmitter(entity, new ColoredGlowParticleEffect(ParticleRegistry.GLOW_TOTEM, color), 3);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight(), entity.getWidth() + 0.2f), entity.getX(), entity.getY(), entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.1f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.1f, entity.getZ(), 0, -1, 0);
                        world.addParticle(new GlowParticleEffect(color, entity.getHeight() + 0.2f, entity.getWidth() + 0.2f), entity.getX(), entity.getY() - 0.2f, entity.getZ(), 0, -1, 0);
                    } else if (status == 15) {
                        for (int i = 0; i < 14; i++)
                            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x8a0303)), (float) (world.getRandom().nextDouble() * 3)),
                                    entity.getX(), entity.getBodyY(0.65), entity.getZ(), world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.20, world.getRandom().nextGaussian() * 0.25);
                    } else if (status == 16) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), ParticleRegistry.FART_EVENT, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        Vec3d vector = entity.getRotationVector().multiply(-0.2, 0, -0.2);
                        for (int i = 0; i < 15; i++) {
                            world.addImportantParticle(ParticleRegistry.FART, true,
                                    entity.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getBodyY(0.5f) + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    vector.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.15,
                                    vector.getY() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.15,
                                    vector.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.15);
                        }
                    } else if (status == 17) {
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                        world.playSound(entity.getX(), entity.getY(), entity.getZ(), ParticleRegistry.FART_EVENT, entity.getSoundCategory(), 1.0f, 1.0f, false);

                        for (int i = 0; i < 4; i++) {
                            world.addImportantParticle(ParticleTypes.EXPLOSION, true,
                                    entity.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getBodyY(0.5f) + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    0,
                                    0,
                                    0);
                        }

                        for (int i = 0; i < 15; i++) {
                            Vec3d vector = new Vec3d(world.getRandom().nextGaussian() * 0.25, world.getRandom().nextGaussian() * 0.25, world.getRandom().nextGaussian() * 0.25);
                            world.addImportantParticle(ParticleRegistry.FART, true,
                                    entity.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getBodyY(0.5f) + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    vector.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.15,
                                    vector.getY() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.15,
                                    vector.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.15);
                        }
                    } else if (status == 18) {
                        for (int i = 0; i < 14; i++)
                            world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x8a0303)), (float) (world.getRandom().nextDouble() * 3)),
                                    entity.getX(), entity.getEyeY(), entity.getZ(), world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.20, world.getRandom().nextGaussian() * 0.25);
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ThanosExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                ThanosExplosionHandler packet = new ThanosExplosionHandler(buf);
                client.execute(() -> {
                    ThanosExplosionHandler.Explosion explosion = new ThanosExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(TelekinesisExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                TelekinesisExplosionHandler packet = new TelekinesisExplosionHandler(buf);
                client.execute(() -> {
                    TelekinesisExplosionHandler.Explosion explosion = new TelekinesisExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ClientPlayNetworking.registerGlobalReceiver(IronManExplosionHandler.IDENTIFIER, (client, handler, buf, responseSender) -> {
            try {
                IronManExplosionHandler packet = new IronManExplosionHandler(buf);
                client.execute(() -> {
                    IronManExplosionHandler.Explosion explosion = new IronManExplosionHandler.Explosion(client.world, null, packet.getX(), packet.getY(), packet.getZ(), packet.getRadius(), packet.getAffectedBlocks());
                    explosion.affectWorld(true);
                    client.player.setVelocity(client.player.getVelocity().add(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ()));
                });
            } catch (Exception exception) {

            }
        });

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(ImagineAnything.MOD_ID + ":infinity_gauntlet_disabled_glow#inventory"));
        });

        AtomicReference<Float> oldTickDelta = new AtomicReference<>(0f);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.INFINITY_GAUNTLET_DISABLED, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            int destroyTicks = stack.getOrCreateNbt().getInt("destroyTicks");
            if (destroyTicks <= 0) {
                return;
            }

            float tickDelta = MinecraftClient.getInstance().getTickDelta();
            if (oldTickDelta.get() > tickDelta) {
                stack.getOrCreateNbt().putInt("destroyTicks", destroyTicks - 1);
            }
            oldTickDelta.set(tickDelta);

            BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModelManager().getModel(new ModelIdentifier(ImagineAnything.MOD_ID + ":infinity_gauntlet_disabled_glow#inventory"));
            renderBakedItemQuads(matrices,
                    vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel.getQuads(null, null, Random.create()),
                    stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);
            renderBakedItemQuads(matrices,
                    vertexConsumers.getBuffer(RenderLayer.getEyes(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), bakedModel.getQuads(null, null, Random.create()),
                    stack, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, overlay);

            matrices.translate(0.5, 0.15625, 0.5);

            float vertexConsumer = ((float) destroyTicks + tickDelta) / 6f;
            float vertexConsumer2 = 0f;
            Random random = Random.create();
            VertexConsumer vertexConsumer3 = vertexConsumers.getBuffer(LIGHTNING);
            matrices.push();
            int l = 0;
            while ((float) l < 4) {
                matrices.push();
                matrices.scale(0.45f, 0.45f, 0.45f);
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0f));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0f + vertexConsumer * 90.0f));
                float m = 1;
                float n = 1;
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                int o = (int) (255.0f * (1.0f - vertexConsumer2));

                float[] color = new float[]{1, 0, 0};

                method_23157(vertexConsumer3, matrix4f, color, o);
                method_23156(vertexConsumer3, matrix4f, color, m, n);
                method_23158(vertexConsumer3, matrix4f, color, m, n);
                method_23157(vertexConsumer3, matrix4f, color, o);
                method_23158(vertexConsumer3, matrix4f, color, m, n);
                method_23159(vertexConsumer3, matrix4f, m, n);
                method_23157(vertexConsumer3, matrix4f, color, o);
                method_23159(vertexConsumer3, matrix4f, m, n);
                method_23156(vertexConsumer3, matrix4f, color, m, n);
                ++l;
                matrices.pop();
            }
            matrices.pop();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                if (caveConduitEntity == null || caveConduitEntity.getWorld() != client.world) {
                    caveConduitEntity = new CaveConduitEntity(EntityRegistry.CAVE_CONDUIT, client.world);
                }
                caveConduitEntity.age++;
                if (caveConduitEntity.age % 3 == 0) {
                    caveConduitEntity.setItemStack(CaveConduitEntity.items.stream()
                            .filter(item -> !item.equals(caveConduitEntity.getItemStack().getItem()))
                            .toList().get(caveConduitEntity.getRandom().nextInt(CaveConduitEntity.items.size() - 1)).getDefaultStack());
                }
            }
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IMAGINED_CAVE_EXTRACTOR, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            if (caveConduitEntity == null) {
                return;
            }

            matrices.translate(0.5, 0, 0.5);
            if (mode.equals(ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND) || mode.equals(ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND)
                    || mode.equals(ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND) || mode.equals(ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND)) {
                matrices.translate(0, 0.35, 0);
            }
            matrices.scale(1.3f, 1.3f, 1.3f);
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(caveConduitEntity, BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            PlayerEntity user = MinecraftClient.getInstance().player;
            if (user == null) {
                return;
            }

            user.getEntityWorld().getPlayers().forEach(playerEntity -> {
                Entity entity = playerEntity.getEntityWorld().getEntityById(((PlayerProperties) playerEntity).getHolding());
                if (entity != null) {
                    entity.fallDistance = 0;
                    entity.setVelocity(0, 0, 0);

                    Vec3d vec3d = ImagineAnything.getRotationVector(playerEntity.getPitch(tickDelta), playerEntity.getYaw(tickDelta)).multiply(((PlayerProperties) playerEntity).getDistance());
                    entity.setPos(playerEntity.getX() + vec3d.getX(), playerEntity.getBodyY(0.55) + vec3d.getY(), playerEntity.getZ() + vec3d.getZ());
                }
            });
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.IRON_MAN_SUIT, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                    RenderLayer.getEntityTranslucent(new Identifier(ImagineAnything.MOD_ID, "textures/entity/iron_man_suit.png")));
            PlayerEntityModel.getTexturedModelData(Dilation.NONE, true).getRoot().createPart(64, 64)
                    .render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1.0f);
        });

        ModelPredicateProviderRegistry.register(ItemRegistry.THINK, new Identifier("thinking"), (stack, world, entity, seed) -> {
            return entity instanceof ThoughtEntity thoughtEntity ? ThoughtEntity.getThinkProgress(thoughtEntity.thinkingTime) : 0f;
        });
    }

    private CaveConduitEntity caveConduitEntity;

    public static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {
            float f = 0.9029412f;
            float g = 0.14705883f;
            float h = 0.3411765f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }

    private static final RenderLayer LIGHTNING = RenderLayer.of("lightning", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
            .shader(RenderLayer.LIGHTNING_SHADER).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING)
            .build(true));

    private static void method_23157(VertexConsumer vertices, Matrix4f matrix, float[] rgb, int alpha) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha / 255f).next();
    }

    private static void method_23156(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, -1f * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    private static void method_23158(VertexConsumer vertices, Matrix4f matrix, float[] rgb, float y, float x) {
        vertices.vertex(matrix, 1f * x, y, -0.5f * x).color(rgb[0], rgb[1], rgb[2], 0).next();
    }

    private static void method_23159(VertexConsumer vertices, Matrix4f matrix, float y, float z) {
        vertices.vertex(matrix, 0.0f, y, 1.0f * z).color(120, 120, 120, 0).next();
    }

    private HitResult entityRaycast(PlayerEntity playerEntity, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = playerEntity.isFallFlying() || playerEntity.isSwimming() ? playerEntity.getCameraPosVec(tickDelta).add(0, -1.3, 0) : playerEntity.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = playerEntity.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return this.getEntityCollision(playerEntity, vec3d, vec3d3);
    }

    protected EntityHitResult getEntityCollision(PlayerEntity playerEntity, Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(playerEntity.world, playerEntity, currentPosition, nextPosition, new Box(currentPosition, nextPosition).expand(1), entity -> entity instanceof LivingEntity);
    }

    public static void openThoughtScreen(PlayerEntity player) {
        if (player.equals(MinecraftClient.getInstance().player)) {
            MinecraftClient.getInstance().setScreen(new ThoughtScreen());
        }
    }

    public static void playSound(ImaginedGiantBedrockSpeakersBlockEntity blockEntity) {
        net.minecraft.client.MinecraftClient.getInstance().getSoundManager().play(new BedrockSpeakerSoundInstance(blockEntity));
    }
}
