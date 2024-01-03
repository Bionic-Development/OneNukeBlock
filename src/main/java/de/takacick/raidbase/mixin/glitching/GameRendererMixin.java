package de.takacick.raidbase.mixin.glitching;

import com.mojang.datafixers.util.Pair;
import de.takacick.raidbase.access.LivingProperties;
import de.takacick.raidbase.client.shaders.RaidBaseShaders;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract Camera getCamera();

    @Shadow
    @Final
    private Random random;

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Shadow @Final private Map<String, ShaderProgram> programs;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        if (getCamera().getFocusedEntity() instanceof LivingEntity livingEntity
                && ((LivingProperties) livingEntity).isGettingBanned() && !getCamera().isThirdPerson()) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, livingEntity.prevYaw, livingEntity.getYaw()) - 90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, livingEntity.prevPitch, livingEntity.getPitch()) - 45.0F));
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotation(MathHelper.lerp(tickDelta, (float) livingEntity.age - 1 + livingEntity.getId(), livingEntity.age + livingEntity.getId()) * 0.45f));
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(MathHelper.lerp(tickDelta, (float) livingEntity.age - 1 + livingEntity.getId(), livingEntity.age + livingEntity.getId()) * 0.45f));
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotation(MathHelper.lerp(tickDelta, (float) livingEntity.age - 1 + livingEntity.getId(), livingEntity.age + livingEntity.getId()) * 0.45f));
        }

        if (getCamera().getFocusedEntity() instanceof LivingProperties livingProperties
                && (livingProperties.isWaterElectroShocked() || livingProperties.isGlitchy())) {
            float g = (float) (Math.cos((double) getCamera().getFocusedEntity().age * 3.25) * Math.PI * (double) 0.4f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g + random.nextFloat() * 5));
        }
    }

    @Inject(method = "loadPrograms", at = @At("TAIL"))
    private void loadPrograms(ResourceFactory factory, CallbackInfo info) {
        ArrayList<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2 = new ArrayList<>();

        try {
            list2.add(Pair.of(new ShaderProgram(resourceManager, "raidbase/rendertype_entity_translucent",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> RaidBaseShaders.renderTypeEntityTranslucentCullProgram = shader));
            list2.add(Pair.of(new ShaderProgram(resourceManager, "raidbase/rendertype_item_entity_translucent_cull",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> RaidBaseShaders.renderTypeItemEntityTranslucentCullProgram = shader));
            list2.add(Pair.of(new ShaderProgram(resourceManager, "raidbase/rendertype_glitchy_item_entity_translucent_cull",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), shader -> RaidBaseShaders.renderTypeGlitchyItemEntityTranslucentCullProgram = shader));
            list2.add(Pair.of(new ShaderProgram(resourceManager, "raidbase/rendertype_solid",
                    VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), shader -> RaidBaseShaders.renderTypeSolidProgram = shader));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        list2.forEach(pair -> {
            ShaderProgram shader = pair.getFirst();
            this.programs.put(shader.getName(), shader);
            pair.getSecond().accept(shader);
        });
    }
}