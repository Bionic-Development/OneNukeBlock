package de.takacick.onegirlboyblock.client.shader;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class AdvancedTextures
        extends RenderPhase.TextureBase {
    private final Optional<Identifier> id;

    AdvancedTextures(ImmutableList<Triple<Identifier, Boolean, Boolean>> textures) {
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

        public AdvancedTextures build() {
            return new AdvancedTextures(this.textures.build());
        }
    }
}
