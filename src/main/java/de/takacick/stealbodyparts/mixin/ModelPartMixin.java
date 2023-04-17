package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.access.ModelPartProperties;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
@Implements({@Interface(iface = ModelPartProperties.class, prefix = "stealbodyparts$")})
public abstract class ModelPartMixin {

    @Shadow
    public boolean visible;
    @Shadow
    @Final
    private Map<String, ModelPart> children;
    @Shadow
    @Final
    private List<ModelPart.Cuboid> cuboids;
    private Direction stealbodyparts$bodyModel = null;

    @Inject(method = "renderCuboids", at = @At("HEAD"), cancellable = true)
    public void renderCuboids(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        if (stealbodyparts$isBodyModel() && visible) {
            for (ModelPart.Cuboid cuboid : this.cuboids) {
                stealbodypartsrenderCuboid(cuboid, entry, vertexConsumer, light, overlay, red, green, blue, alpha);
            }
            info.cancel();
        }
    }

    private void stealbodypartsrenderCuboid(ModelPart.Cuboid cube, MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        Vec3f dir = stealbodyparts$bodyModel.getUnitVector();
        for (ModelPart.Quad quad : cube.sides) {

            if (!quad.direction.equals(dir)) {
                continue;
            }

            Vec3f vec3f = quad.direction.copy();
            vec3f.transform(matrix3f);
            float f = vec3f.getX();
            float g = vec3f.getY();
            float h = vec3f.getZ();
            for (ModelPart.Vertex vertex : quad.vertices) {
                float i = vertex.pos.getX() / 16.0f;
                float j = vertex.pos.getY() / 16.0f;
                float k = vertex.pos.getZ() / 16.0f;
                Vector4f vector4f = new Vector4f(i, j, k, 1.0f);
                vector4f.transform(matrix4f);
                vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, f, g, h);
            }
        }
    }

    public void stealbodyparts$setBodyModel(Direction direction) {
        this.stealbodyparts$bodyModel = direction;
        this.children.forEach((key, modelPart) -> {
            Direction dir = Direction.byName(key);
            ((ModelPartProperties) (Object) modelPart).setBodyModel(dir == null ? direction : dir);
        });
    }

    public boolean stealbodyparts$isBodyModel() {
        return this.stealbodyparts$bodyModel != null;
    }
}

