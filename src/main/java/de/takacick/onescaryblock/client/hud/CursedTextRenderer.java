package de.takacick.onescaryblock.client.hud;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class CursedTextRenderer extends TextRenderer {
    private static final Vector3f FORWARD_SHIFT = new Vector3f(0.0f, 0.0f, 0.03f);

    private final Random random = Random.create();

    public CursedTextRenderer(TextRenderer textRenderer) {
        super(textRenderer.fontStorageAccessor, textRenderer.validateAdvance);
    }

    public int draw(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light) {
        return this.draw(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light, this.isRightToLeft());
    }

    /**
     * @param color the text color in the 0xAARRGGBB format
     */
    public int draw(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light, boolean rightToLeft) {
        return this.drawInternal(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light, rightToLeft);
    }

    /**
     * @param color the text color in the 0xAARRGGBB format
     */
    public int draw(Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light) {
        return this.draw(text.asOrderedText(), x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
    }

    /**
     * @param color the text color in the 0xAARRGGBB format
     */
    public int draw(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light) {
        return this.drawInternal(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
    }

    /**
     * @param outlineColor the outline color in 0xAARRGGBB
     * @param color the text color in 0xAARRGGBB
     */
    public void drawWithOutline(OrderedText text, float x, float y, int color, int outlineColor, Matrix4f matrix, VertexConsumerProvider vertexConsumers, int light) {
        int i = tweakTransparency(outlineColor);
        Drawer drawer = new Drawer(vertexConsumers, 0.0f, 0.0f, i, false, matrix, TextLayerType.NORMAL, light);
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0) continue;
                float[] fs = new float[]{x};
                int l = j;
                int m = k;
                text.accept((index, style, codePoint) -> {
                    boolean bl = style.isBold();
                    FontStorage fontStorage = this.getFontStorage(style.getFont());
                    Glyph glyph = fontStorage.getGlyph(codePoint, this.validateAdvance);
                    drawer.x = fs[0] + (float)l * glyph.getShadowOffset();
                    drawer.y = y + (float)m * glyph.getShadowOffset();
                    fs[0] = fs[0] + glyph.getAdvance(bl);
                    return drawer.accept(index, style.withColor(i), codePoint);
                });
            }
        }
        Drawer drawer2 = new Drawer(vertexConsumers, x, y, tweakTransparency(color), false, matrix, TextLayerType.POLYGON_OFFSET, light);
        text.accept(drawer2);
        drawer2.drawLayer(0, x);
    }

    private static int tweakTransparency(int argb) {
        if ((argb & 0xFC000000) == 0) {
            return argb | 0xFF000000;
        }
        return argb;
    }

    private int drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light, boolean mirror) {
        if (mirror) {
            text = this.mirror(text);
        }
        color = tweakTransparency(color);
        Matrix4f matrix4f = new Matrix4f(matrix);
        if (shadow) {
            this.drawLayer(text, x, y, color, true, matrix, vertexConsumers, layerType, backgroundColor, light);
            matrix4f.translate(FORWARD_SHIFT);
        }
        x = this.drawLayer(text, x, y, color, false, matrix4f, vertexConsumers, layerType, backgroundColor, light);
        return (int)x + (shadow ? 1 : 0);
    }

    private int drawInternal(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextLayerType layerType, int backgroundColor, int light) {
        color = tweakTransparency(color);
        Matrix4f matrix4f = new Matrix4f(matrix);
        if (shadow) {
            this.drawLayer(text, x, y, color, true, matrix, vertexConsumerProvider, layerType, backgroundColor, light);
            matrix4f.translate(FORWARD_SHIFT);
        }
        x = this.drawLayer(text, x, y, color, false, matrix4f, vertexConsumerProvider, layerType, backgroundColor, light);
        return (int)x + (shadow ? 1 : 0);
    }

    private float drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextLayerType layerType, int underlineColor, int light) {
        Drawer drawer = new Drawer(vertexConsumerProvider, x, y, color, shadow, matrix, layerType, light);
        TextVisitFactory.visitFormatted(text, Style.EMPTY, drawer);
        return drawer.drawLayer(underlineColor, x);
    }

    private float drawLayer(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextLayerType layerType, int underlineColor, int light) {
        Drawer drawer = new Drawer(vertexConsumerProvider, x, y, color, shadow, matrix, layerType, light);
        text.accept(drawer);
        return drawer.drawLayer(underlineColor, x);
    }

    void drawGlyphs(GlyphRenderer glyphRenderer, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
        glyphRenderer.draw(italic, x, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        if (bold) {
            glyphRenderer.draw(italic, x + weight, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        }
    }

    @Environment(value = EnvType.CLIENT)
    class Drawer
            implements CharacterVisitor {
        final VertexConsumerProvider vertexConsumers;
        private final boolean shadow;
        private final float brightnessMultiplier;
        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        private final Matrix4f matrix;
        private final TextLayerType layerType;
        private final int light;
        float x;
        float y;
        @Nullable
        private List<GlyphRenderer.Rectangle> rectangles;

        private void addRectangle(GlyphRenderer.Rectangle rectangle) {
            if (this.rectangles == null) {
                this.rectangles = Lists.newArrayList();
            }
            this.rectangles.add(rectangle);
        }

        public Drawer(VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, TextLayerType layerType, int light) {
            this.vertexConsumers = vertexConsumers;


            this.x = x;
            this.y = y;
            this.shadow = (random.nextDouble() < 0.3) == shadow;
            this.brightnessMultiplier = (shadow ? 0.25f : 1.0f) * (random.nextDouble() <= 0.1 ? 1f : random.nextFloat());
            this.red = (float) (color >> 16 & 0xFF) / 255.0f * this.brightnessMultiplier;
            this.green = (float) (color >> 8 & 0xFF) / 255.0f * this.brightnessMultiplier;
            this.blue = (float) (color & 0xFF) / 255.0f * this.brightnessMultiplier;
            this.alpha = (float) (color >> 24 & 0xFF) / 255.0f;
            this.matrix = matrix;
            this.layerType = layerType;
            this.light = light;
        }

        @Override
        public boolean accept(int i, Style style, int j) {
            float n;
            float l;
            float h;
            float g;
            FontStorage fontStorage = CursedTextRenderer.this.getFontStorage(style.getFont());
            Glyph glyph = fontStorage.getGlyph(j, CursedTextRenderer.this.validateAdvance);
            GlyphRenderer glyphRenderer = style.isObfuscated() && j != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(j);
            boolean bl = style.isBold();
            float f = this.alpha;
            TextColor textColor = style.getColor();
            if (textColor != null) {
                int k = textColor.getRgb();
                g = (float) (k >> 16 & 0xFF) / 255.0f * this.brightnessMultiplier;
                h = (float) (k >> 8 & 0xFF) / 255.0f * this.brightnessMultiplier;
                l = (float) (k & 0xFF) / 255.0f * this.brightnessMultiplier;
            } else {
                g = this.red;
                h = this.green;
                l = this.blue;
            }
            random.nextDouble();

            if (random.nextDouble() <= 0.1) {
                float jx = random.nextFloat();
                g = 0.9f + 0.1f * jx;
                h = 0.1f * jx;
                l = 0.1f * jx;
            }

            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                float m = bl ? glyph.getBoldOffset() : 0.0f;
                n = this.shadow ? glyph.getShadowOffset() : 0.0f;
                float xOffset =  (random.nextDouble() <= 0.01 ? random.nextDouble() <= 0.2  ? (float)random.nextGaussian() * 0.4f : (float)random.nextGaussian() * 0.1f : 0f);
                float yOffset =  (random.nextDouble() <= 0.01 ? random.nextDouble() <= 0.2  ? (float)random.nextGaussian() * 0.4f : (float)random.nextGaussian() * 0.1f : 0f);

                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                CursedTextRenderer.this.drawGlyphs(glyphRenderer, bl, style.isItalic(), m, this.x + n + xOffset, this.y + n +  yOffset, this.matrix, vertexConsumer, g, h, l, f, this.light);
            }
            float m = glyph.getAdvance(bl);
            float f2 = n = this.shadow ? 1.0f : 0.0f;
            if (style.isStrikethrough() || random.nextDouble() <= 0.1) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0f, this.y + n + 4.5f, this.x + n + m, this.y + n + 4.5f - 1.0f, 0.01f, g, h, l, f));
            }
            if (style.isUnderlined() ||  random.nextDouble() <= 0.001) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0f, this.y + n + 9.0f, this.x + n + m, this.y + n + 9.0f - 1.0f, 0.01f, g, h, l, f));
            }
            this.x += m;
            return true;
        }

        public float drawLayer(int underlineColor, float x) {
            if (underlineColor != 0) {
                float f = (float) (underlineColor >> 24 & 0xFF) / 255.0f;
                float g = (float) (underlineColor >> 16 & 0xFF) / 255.0f;
                float h = (float) (underlineColor >> 8 & 0xFF) / 255.0f;
                float i = (float) (underlineColor & 0xFF) / 255.0f;
                this.addRectangle(new GlyphRenderer.Rectangle(x - 1.0f, this.y + 9.0f, this.x + 1.0f, this.y - 1.0f, 0.01f, g, h, i, f));
            }
            if (this.rectangles != null) {
                GlyphRenderer glyphRenderer = CursedTextRenderer.this.getFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                for (GlyphRenderer.Rectangle rectangle : this.rectangles) {
                    glyphRenderer.drawRectangle(rectangle, this.matrix, vertexConsumer, this.light);
                }
            }
            return this.x;
        }
    }
}
