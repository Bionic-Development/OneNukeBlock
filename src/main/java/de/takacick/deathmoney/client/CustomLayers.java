package de.takacick.deathmoney.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomLayers {

    public static Shader BLACK_MATTER_SHADER;
    private static final RenderPhase.Shader BLACK_MATTER_RENDER_PHASE = new RenderPhase.Shader(() -> BLACK_MATTER_SHADER);

    public static final Function<Identifier, RenderLayer> BLACK_MATTER_CUTOUT = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().shader(BLACK_MATTER_RENDER_PHASE)
                .texture(CustomTextures.create()
                        .add(texture, false, false)
                        .add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
                        .add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false)
                        .add(texture, false, false)
                        .build())
                .transparency(RenderLayer.NO_TRANSPARENCY)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .cull(RenderPhase.DISABLE_CULLING)
                .overlay(RenderPhase.Overlay.ENABLE_OVERLAY_COLOR).build(false);
        return RenderLayer.of("immortal_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });

    public static final BiFunction<Identifier, Identifier, RenderLayer> BLACK_MATTER_CUTOUT_MIPPED = Util.memoize((texture, texture2) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().shader(BLACK_MATTER_RENDER_PHASE)
                .texture(CustomTextures.create()
                        .add(texture, false, false)
                        .add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
                        .add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false)
                        .add(texture2, false, false)
                        .build())
                .transparency(RenderLayer.NO_TRANSPARENCY)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .cull(RenderPhase.DISABLE_CULLING)
                .overlay(RenderPhase.Overlay.ENABLE_OVERLAY_COLOR)
                .build(false);
        return RenderLayer.of("immortal_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });

    @Environment(value = EnvType.CLIENT)
    public static class CustomTextures
            extends RenderPhase.TextureBase {
        private final Optional<Identifier> id;

        CustomTextures(ImmutableList<Triple<Identifier, Boolean, Boolean>> textures) {
            super(() -> {
                int i = 0;
                for (Triple<Identifier, Boolean, Boolean> triple : textures) {
                    if (i == 1) {
                        i += 2;
                    }

                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    textureManager.getTexture(triple.getLeft()).setFilter(triple.getMiddle(), triple.getRight());
                    RenderSystem.setShaderTexture(i++, triple.getLeft());
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
            private final ImmutableList.Builder<Triple<Identifier, Boolean, Boolean>> textures = new ImmutableList.Builder<>();

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