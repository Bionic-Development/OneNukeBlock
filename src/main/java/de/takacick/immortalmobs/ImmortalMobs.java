package de.takacick.immortalmobs;

import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.dragon.ImmortalEnderDragonEntity;
import de.takacick.immortalmobs.registry.entity.living.*;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalDragonBallEntity;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalDragonBreathEntity;
import draylar.identity.fabric.config.IdentityFabricConfig;
import draylar.identity.impl.PlayerDataProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ImmortalMobs implements ModInitializer {

    public static final String MOD_ID = "immortalmobs";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "immortalmobs"), () -> new ItemStack(ItemRegistry.IMMORTAL_WOOL_ITEM));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "immortalmobs");

    @Override
    public void onInitialize() {
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();

        ((IdentityFabricConfig) IdentityFabricConfig.getInstance()).overlayIdentityUnlocks = false;
        ((IdentityFabricConfig) IdentityFabricConfig.getInstance()).overlayIdentityRevokes = false;
        ((IdentityFabricConfig) IdentityFabricConfig.getInstance()).maxHealth = 500;

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "immortaldragonball"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (((PlayerDataProvider) player).getIdentity() instanceof ImmortalEnderDragonEntity) {
                    AtomicReference<Vec3d> vec3d = new AtomicReference<>(player.getCameraPosVec(0));

                    vec3d.set(vec3d.get().add(getRotation(0, player.getYaw()).multiply(1).multiply(5)).subtract(0, 3, 0));
                    player.getWorld().playSound(null, new BlockPos(vec3d.get()), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0f, 1);
                    Vec3d direction = player.getRotationVecClient().normalize();

                    World world = player.getEntityWorld();
                    ImmortalDragonBallEntity fireballEntity = new ImmortalDragonBallEntity(EntityRegistry.IMMORTAL_DRAGON_BALL, player, direction.getX(), direction.getY(), direction.getZ(), player.getEntityWorld());
                    fireballEntity.setPos(vec3d.get().getX(), vec3d.get().getY(), vec3d.get().getZ());
                    world.spawnEntity(fireballEntity);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "immortaldragonbreath"), (server, player, handler, buf, responseSender) -> {
            int sound = buf.readInt();

            server.execute(() -> {
                if (((PlayerDataProvider) player).getIdentity() instanceof ImmortalEnderDragonEntity) {
                    AtomicReference<Vec3d> vec3d = new AtomicReference<>(player.getCameraPosVec(0));

                    vec3d.set(vec3d.get().add(getRotation(0, player.getYaw()).multiply(1).multiply(5)).subtract(0, 3, 0));

                    if (sound == 1) {
                        player.getWorld().playSound(null, new BlockPos(vec3d.get()), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.0f, 1);
                    }

                    Vec3d direction = player.getRotationVecClient().multiply(2.5);

                    World world = player.getEntityWorld();
                    ImmortalDragonBreathEntity immortalDragonBreathEntity = new ImmortalDragonBreathEntity(EntityRegistry.IMMORTAL_DRAGON_BREATH, player, direction.getX(), direction.getY(), direction.getZ(), player.getEntityWorld());
                    immortalDragonBreathEntity.setPos(vec3d.get().getX(), vec3d.get().getY(), vec3d.get().getZ());
                    immortalDragonBreathEntity.setVelocity(direction);

                    world.spawnEntity(immortalDragonBreathEntity);
                }
            });
        });
    }

    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlockRegistry = new ArrayList<BlockPos>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlockRegistry.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return circleBlockRegistry;
    }

    public static Vec3d getRotation(float pitch, float yaw) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double) (i * j), (double) (-k), (double) (h * j));
    }
}
