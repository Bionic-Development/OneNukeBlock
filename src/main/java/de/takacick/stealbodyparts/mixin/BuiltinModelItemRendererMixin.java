package de.takacick.stealbodyparts.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import de.takacick.stealbodyparts.registry.entity.custom.model.BodyModel;
import de.takacick.stealbodyparts.registry.item.BodyPartItem;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.dynamic.DynamicSerializableUuid;
import net.minecraft.util.math.Vec3f;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin implements SynchronousResourceReloader {

    @Shadow
    public abstract void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);

    private final ModelPart model = BodyModel.getNoOffsetTexturedModelData(Dilation.NONE, false).getRoot().createPart(64, 64);
    private final ModelPart slimModel = BodyModel.getNoOffsetTexturedModelData(Dilation.NONE, true).getRoot().createPart(64, 64);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item instanceof BodyPartItem bodyPartItem) {
            GameProfile gameProfile = null;
            boolean slim = false;
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound != null) {
                if (nbtCompound.contains("PartOwner", NbtElement.COMPOUND_TYPE)) {
                    gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("PartOwner"));
                } else if (nbtCompound.contains("PartOwner", NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString("PartOwner"))) {
                    gameProfile = new GameProfile(null, nbtCompound.getString("PartOwner"));
                    nbtCompound.remove("PartOwner");

                    SkullBlockEntity.loadProperties(gameProfile, profile -> nbtCompound.put("PartOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile)));
                }
            }

            MinecraftClient client = MinecraftClient.getInstance();
            RenderLayer renderLayer;
            if (gameProfile == null) {
                renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(DefaultSkinHelper.getTexture());
            } else {
                Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = client
                        .getSkinProvider().getTextures(gameProfile);

                if (gameProfile.getProperties().containsKey("textures")) {
                    String decoded = new String(Base64.getDecoder().decode(gameProfile.getProperties()
                            .get("textures").iterator().next().getValue()));
                    JsonObject json = JsonParser.parseString(decoded).getAsJsonObject();
                    if(json.has("textures") && json.get("textures").isJsonObject()) {
                        json = json.getAsJsonObject("textures");
                    }

                    if (json.has("SKIN") && json.getAsJsonObject("SKIN").has("metadata")) {
                        JsonObject skin = json.getAsJsonObject("SKIN").getAsJsonObject("metadata");
                        if (skin.has("model")) {
                            slim = Objects.equals(skin.getAsJsonPrimitive("model").getAsString(), "slim");
                        }
                    }
                }

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

            ModelPart modelPart = (slim ? slimModel : model);

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

            matrices.translate(0.5, 0, 0.5);

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));

            bodyPartItem.getBodyPart().getParts().forEach(part -> {
                if (!modelPart.hasChild(part)) {
                    return;
                }

                ModelPart partChild = modelPart.getChild(part);
                partChild.traverse().forEach(child -> {
                    child.setTransform(ModelTransform.NONE);

                    child.forEachCuboid(matrices, (matrix, path, index, cuboid) -> {
                        cuboid.renderCuboid(matrix, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
                    });
                });
            });

            info.cancel();
        }
    }
}