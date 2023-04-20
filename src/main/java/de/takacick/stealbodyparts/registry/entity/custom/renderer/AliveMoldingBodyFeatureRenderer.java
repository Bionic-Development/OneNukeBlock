package de.takacick.stealbodyparts.registry.entity.custom.renderer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import de.takacick.stealbodyparts.registry.entity.living.AliveMoldingBodyEntity;
import de.takacick.stealbodyparts.registry.entity.living.MoldingPart;
import de.takacick.stealbodyparts.registry.entity.living.model.AliveMoldingBodyEntityModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.DynamicSerializableUuid;

import java.util.Map;

public class AliveMoldingBodyFeatureRenderer<T extends AliveMoldingBodyEntity, M extends AliveMoldingBodyEntityModel<T>>
        extends FeatureRenderer<T, M> {

    public AliveMoldingBodyFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        getContextModel().getPart().rotate(matrixStack);

        MinecraftClient client = MinecraftClient.getInstance();
        boolean bl = this.isVisible(livingEntity);
        boolean bl2 = !bl && !livingEntity.isInvisibleTo(client.player);
        int p = LivingEntityRenderer.getOverlay(livingEntity, this.getAnimationCounter());

        if(!bl && !bl2) {
            return;
        }

        for (MoldingPart bodyPart : MoldingPart.values()) {
            if (AliveMoldingBodyEntityModel.bodyParts.containsKey(bodyPart) && livingEntity.hasBodyPart(bodyPart)) {
                GameProfile gameProfile = livingEntity.getBodyPart(bodyPart);

                matrixStack.push();
                if (bodyPart.getName().contains("leg")) {
                    matrixStack.translate(0f, 21.0f / 16.0f, 0f);
                } else {
                    matrixStack.translate(0f, 21.0f / 16.0f, 0f);
                    getContextModel().body.rotate(matrixStack);
                }

                boolean slim = livingEntity.isSlim(bodyPart);

                RenderLayer renderLayer;
                if (gameProfile == null) {
                    renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(DefaultSkinHelper.getTexture());
                } else {
                    Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = client
                            .getSkinProvider().getTextures(gameProfile);

                    if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                        renderLayer = RenderLayer.getEntityTranslucent(client.getSkinProvider()
                                .loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN));
                    } else {
                        if ((DynamicSerializableUuid.getUuidFromProfile(gameProfile).hashCode() & 1) == 1) {
                            slim = true;
                        }
                        renderLayer = RenderLayer.getEntityCutoutNoCull(DefaultSkinHelper
                                .getTexture(DynamicSerializableUuid.getUuidFromProfile(gameProfile)));
                    }
                }

                ModelPart child = (slim
                        ? AliveMoldingBodyEntityModel.slimBodyParts : AliveMoldingBodyEntityModel.bodyParts).get(bodyPart);
                child.render(matrixStack, vertexConsumerProvider.getBuffer(renderLayer), i, p, 1f, 1f, 1f, bl2 ? 0.15f : 1.0f);
                matrixStack.pop();
            }
        }
    }

    protected float getAnimationCounter() {
        return 0.0f;
    }

    protected boolean isVisible(T entity) {
        return !entity.isInvisible();
    }
}

