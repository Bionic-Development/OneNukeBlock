package de.takacick.emeraldmoney.registry.entity.custom.renderer;

import de.takacick.emeraldmoney.registry.entity.custom.VillagerSpikeEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.VillagerResourceMetadata;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;

import java.io.IOException;
import java.util.Optional;

public class VillagerSpikeEntityRenderer extends EntityRenderer<VillagerSpikeEntity> {

    private static final Int2ObjectMap<Identifier> LEVEL_TO_ID = Util.make(new Int2ObjectOpenHashMap(), levelToId -> {
        levelToId.put(1, new Identifier("stone"));
        levelToId.put(2, new Identifier("iron"));
        levelToId.put(3, new Identifier("gold"));
        levelToId.put(4, new Identifier("emerald"));
        levelToId.put(5, new Identifier("diamond"));
    });
    private final Object2ObjectMap<VillagerType, VillagerResourceMetadata.HatType> villagerTypeToHat = new Object2ObjectOpenHashMap<VillagerType, VillagerResourceMetadata.HatType>();
    private final Object2ObjectMap<VillagerProfession, VillagerResourceMetadata.HatType> professionToHat = new Object2ObjectOpenHashMap<VillagerProfession, VillagerResourceMetadata.HatType>();

    private static final Identifier TEXTURE = new Identifier("textures/entity/villager/villager.png");
    private final VillagerResemblingModel<VillagerSpikeEntity> villagerResemblingModel;
    private final ResourceManager resourceManager;

    public VillagerSpikeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.villagerResemblingModel = new VillagerResemblingModel<>(context.getPart(EntityModelLayers.VILLAGER));
        this.resourceManager = context.getResourceManager();
    }

    @Override
    public void render(VillagerSpikeEntity villagerSpikeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float h = villagerSpikeEntity.getAnimationProgress(g);
        if (h == 0.0f) {
            return;
        }

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(villagerSpikeEntity.getYaw()));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(villagerSpikeEntity.getPitch()));
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(0.0, -h, 0.0);

        VillagerData villagerData = ((VillagerDataContainer) villagerSpikeEntity).getVillagerData();
        VillagerType villagerType = villagerData.getType();
        VillagerProfession villagerProfession = villagerData.getProfession();
        VillagerResourceMetadata.HatType hatType = this.getHatType(this.villagerTypeToHat, "type", Registries.VILLAGER_TYPE, villagerType);
        VillagerResourceMetadata.HatType hatType2 = this.getHatType(this.professionToHat, "profession", Registries.VILLAGER_PROFESSION, villagerProfession);

        this.villagerResemblingModel.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE)), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        this.villagerResemblingModel.setHatVisible(hatType2 == VillagerResourceMetadata.HatType.NONE || hatType2 == VillagerResourceMetadata.HatType.PARTIAL && hatType != VillagerResourceMetadata.HatType.FULL);
        Identifier identifier = this.findTexture("type", Registries.VILLAGER_TYPE.getId(villagerType));
        renderModel(this.villagerResemblingModel, identifier, matrixStack, vertexConsumerProvider, i, 1.0f, 1.0f, 1.0f);
        this.villagerResemblingModel.setHatVisible(true);
        if (villagerProfession != VillagerProfession.NONE) {
            Identifier identifier2 = this.findTexture("profession", Registries.VILLAGER_PROFESSION.getId(villagerProfession));
            renderModel(this.villagerResemblingModel, identifier2, matrixStack, vertexConsumerProvider, i, 1.0f, 1.0f, 1.0f);
            if (villagerProfession != VillagerProfession.NITWIT) {
                Identifier identifier3 = this.findTexture("profession_level", LEVEL_TO_ID.get(MathHelper.clamp(villagerData.getLevel(), 1, LEVEL_TO_ID.size())));
                renderModel(this.villagerResemblingModel, identifier3, matrixStack, vertexConsumerProvider, i, 1.0f, 1.0f, 1.0f);
            }
        }

        matrixStack.pop();
        super.render(villagerSpikeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    protected static <T extends VillagerSpikeEntity> void renderModel(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float red, float green, float blue) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0f);
    }

    @Override
    protected int getBlockLight(VillagerSpikeEntity entity, BlockPos pos) {
        return Math.max(Math.max(super.getBlockLight(entity, pos), super.getBlockLight(entity, pos.add(0, 1, 0))), super.getBlockLight(entity, pos.add(0, 2, 0)));
    }

    @Override
    public Identifier getTexture(VillagerSpikeEntity villagerSpikeEntity) {
        return TEXTURE;
    }

    private Identifier findTexture(String keyType, Identifier keyId) {
        return keyId.withPath(path -> "textures/entity/villager/" + keyType + "/" + path + ".png");
    }

    public <K> VillagerResourceMetadata.HatType getHatType(Object2ObjectMap<K, VillagerResourceMetadata.HatType> hatLookUp, String keyType, DefaultedRegistry<K> registry, K key) {
        return hatLookUp.computeIfAbsent(key, k -> this.resourceManager.getResource(this.findTexture(keyType, registry.getId(key))).flatMap(resource -> {
            try {
                return resource.getMetadata().decode(VillagerResourceMetadata.READER).map(VillagerResourceMetadata::getHatType);
            } catch (IOException iOException) {
                return Optional.empty();
            }
        }).orElse(VillagerResourceMetadata.HatType.NONE));
    }
}

