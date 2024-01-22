package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.block.CyberWardenSecurityTrialSpawner;
import de.takacick.illegalwars.registry.block.KingRatTrialSpawner;
import de.takacick.illegalwars.registry.block.entity.*;
import de.takacick.illegalwars.registry.entity.living.SharkEntity;
import de.takacick.illegalwars.registry.item.EntityItem;
import de.takacick.illegalwars.registry.item.TrialSpawnerItem;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.MobSpawnerBlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private PoopLauncherBlockEntity illegalwars$renderPieLauncher;
    @Unique
    private KingRatTrialSpawnerBlockEntity illegalwars$renderKingRatSpawner;
    @Unique
    private CyberWardenSecurityTrialSpawnerBlockEntity illegalwars$renderCyberWardenSpawner;
    @Unique
    private PiglinGoldTurretBlockEntity illegalwars$piglinGoldTurret;
    @Unique
    private BaseWarsMoneyWheelBlockEntity illegalwars$baseWarsMoneyWheel;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V", shift = At.Shift.BEFORE))
    public void render(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.POOP_LAUNCHER_ITEM)) {
            if (this.illegalwars$renderPieLauncher == null) {
                this.illegalwars$renderPieLauncher = new PoopLauncherBlockEntity(BlockPos.ORIGIN, ItemRegistry.POOP_LAUNCHER.getDefaultState());
            }
            this.client.getBlockEntityRenderDispatcher().renderEntity(this.illegalwars$renderPieLauncher, matrices, vertexConsumers, light, overlay);
        } else if (stack.isOf(ItemRegistry.PIGLIN_GOLD_TURRET_ITEM)) {
            if (this.illegalwars$piglinGoldTurret == null) {
                this.illegalwars$piglinGoldTurret = new PiglinGoldTurretBlockEntity(BlockPos.ORIGIN, ItemRegistry.PIGLIN_GOLD_TURRET.getDefaultState());
            }
            this.client.getBlockEntityRenderDispatcher().renderEntity(this.illegalwars$piglinGoldTurret, matrices, vertexConsumers, light, overlay);
        } else if (stack.isOf(ItemRegistry.BASE_WARS_MONEY_WHEEl_ITEM)) {
            if (this.illegalwars$baseWarsMoneyWheel == null) {
                this.illegalwars$baseWarsMoneyWheel = new BaseWarsMoneyWheelBlockEntity(BlockPos.ORIGIN, ItemRegistry.BASE_WARS_MONEY_WHEEl.getDefaultState());
                this.illegalwars$baseWarsMoneyWheel.setOwner(true);
                this.illegalwars$baseWarsMoneyWheel.gui = true;
            }

            this.client.getBlockEntityRenderDispatcher().renderEntity(this.illegalwars$baseWarsMoneyWheel, matrices, vertexConsumers, light, overlay);
        } else if (stack.getItem() instanceof TrialSpawnerItem) {
            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
            World world = this.client.world;
            if (world != null) {
                TrialSpawnerLogic trialSpawnerLogic = null;

                if (stack.isOf(ItemRegistry.KING_RAT_TRIAL_SPAWNER_ITEM)) {
                    if (this.illegalwars$renderKingRatSpawner == null || this.illegalwars$renderKingRatSpawner.getWorld() != world) {
                        this.illegalwars$renderKingRatSpawner = new KingRatTrialSpawnerBlockEntity(BlockPos.ORIGIN, ItemRegistry.KING_RAT_TRIAL_SPAWNER.getDefaultState());
                        this.illegalwars$renderKingRatSpawner.setWorld(world);
                        this.illegalwars$renderKingRatSpawner.setEntityType(KingRatTrialSpawner.ENTITY_TYPE, this.client.world.getRandom());
                    }
                    trialSpawnerLogic = this.illegalwars$renderKingRatSpawner.getSpawner();
                    if (trialSpawnerLogic == null) {
                        this.illegalwars$renderKingRatSpawner = null;
                    }
                } else if (stack.isOf(ItemRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER_ITEM)) {
                    if (this.illegalwars$renderCyberWardenSpawner == null || this.illegalwars$renderCyberWardenSpawner.getWorld() != world) {
                        this.illegalwars$renderCyberWardenSpawner = new CyberWardenSecurityTrialSpawnerBlockEntity(BlockPos.ORIGIN, ItemRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER.getDefaultState());
                        this.illegalwars$renderCyberWardenSpawner.setWorld(world);
                        this.illegalwars$renderCyberWardenSpawner.setEntityType(CyberWardenSecurityTrialSpawner.ENTITY_TYPE, this.client.world.getRandom());
                    }
                    trialSpawnerLogic = this.illegalwars$renderCyberWardenSpawner.getSpawner();
                    if (trialSpawnerLogic == null) {
                        this.illegalwars$renderCyberWardenSpawner = null;
                    }
                }

                if (trialSpawnerLogic != null) {
                    TrialSpawnerData trialSpawnerData = trialSpawnerLogic.getData();
                    Entity entity = trialSpawnerData.setDisplayEntity(trialSpawnerLogic, world, TrialSpawnerState.ACTIVE);
                    if (entity != null) {
                        MobSpawnerBlockEntityRenderer.render(tickDelta, matrices, vertexConsumers, light, entity,
                                this.client.getEntityRenderDispatcher(), world.getTime(), world.getTime() + tickDelta);
                    }
                }
            }
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/BuiltinModelItemRenderer;render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", shift = At.Shift.BEFORE))
    public void renderBuiltIn(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.getItem() instanceof EntityItem entityItem) {
            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
            matrices.push();
            matrices.translate(0.5, 0.5, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));

            Entity entity = entityItem.getEntityType().create(this.client.world);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setYaw(0);
                livingEntity.setHeadYaw(0);
                livingEntity.setBodyYaw(0);
                livingEntity.prevHeadYaw = livingEntity.getHeadYaw();
                livingEntity.prevYaw = livingEntity.getYaw();
                livingEntity.prevBodyYaw = livingEntity.getBodyYaw();
                World world = this.client.world;
                if (world != null) {
                    entity.age = (int) world.getTime();
                }

                if (livingEntity instanceof SharkEntity sharkEntity) {
                    sharkEntity.guiRendering = true;
                }
            }
            this.client.getEntityRenderDispatcher().getRenderer(entity).render(entity,
                    0, tickDelta, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}