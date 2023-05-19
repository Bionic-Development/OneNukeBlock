package de.takacick.heartmoney.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.heartmoney.HeartMoney;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Optional;
import java.util.function.BiFunction;

public class CustomLayers {

    public static final Identifier OVERLAY = new Identifier(HeartMoney.MOD_ID, "textures/entity/maid_suit.png");
    public static final Identifier OVERLAY_SLIM = new Identifier(HeartMoney.MOD_ID, "textures/entity/maid_suit_slim.png");

    public static Identifier getOverlayTexture(boolean slim) {
        return slim ? OVERLAY_SLIM : OVERLAY;
    }

    public static Shader MAID_SHADER;
    public static final RenderPhase.Shader EMERALD_RENDER_SHADER = new RenderPhase.Shader(() -> MAID_SHADER);

    public static final BiFunction<Identifier, Identifier, RenderLayer> MAID_CUTOUT = Util.memoize((texture, texture2) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().shader(EMERALD_RENDER_SHADER)
                .texture(CustomTextures.create()
                        .add(texture, false, false)
                        .add(texture2, false, false)
                        .build())
                .transparency(RenderLayer.NO_TRANSPARENCY)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .cull(RenderPhase.DISABLE_CULLING)
                .overlay(RenderPhase.Overlay.ENABLE_OVERLAY_COLOR)
                .build(false);
        return RenderLayer.of("maid_coutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });

    @Environment(value = EnvType.CLIENT)
    public static class CustomTextures
            extends RenderPhase.TextureBase {
        private final Optional<Identifier> id;

        CustomTextures(ImmutableList<Triple<Identifier, Boolean, Boolean>> textures) {
            super(() -> {
                int i = 0;
                for (Triple triple : textures) {
                    if (i == 1) {
                        i += 2;
                    }

                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    textureManager.getTexture((Identifier) triple.getLeft()).setFilter((Boolean) triple.getMiddle(), (Boolean) triple.getRight());
                    RenderSystem.setShaderTexture(i++, (Identifier) triple.getLeft());
                }
            }, () -> {
            });
            this.id = textures.stream().findFirst().map(Triple::getLeft);
        }

        @Override
        protected Optional<Identifier> getId() {
            return this.id;
        }

        public static Builder create() {
            return new Builder();
        }

        @Environment(value = EnvType.CLIENT)
        public static final class Builder {
            private final ImmutableList.Builder<Triple<Identifier, Boolean, Boolean>> textures = new ImmutableList.Builder();

            public Builder add(Identifier id, boolean blur, boolean mipmap) {
                this.textures.add(Triple.of(id, blur, mipmap));
                return this;
            }

            public CustomTextures build() {
                return new CustomTextures(this.textures.build());
            }
        }
    }
}
