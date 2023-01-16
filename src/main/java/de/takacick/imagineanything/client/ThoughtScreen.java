package de.takacick.imagineanything.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.imagineanything.ImagineAnything;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.glfw.GLFW;

public class ThoughtScreen extends Screen {

    private static final Identifier TEXTURE = new Identifier(ImagineAnything.MOD_ID, "textures/container/thought.png");
    private ModelPart model = getTexturedModelData().createModel();
    private int ticksSinceOpened;

    private int currentRow;
    private SelectionManager selectionManager;
    private final String[] text = new String[]{"", "", "", ""};

    protected int backgroundWidth = 176;
    protected int backgroundHeight = 166;

    public ThoughtScreen() {
        super(Text.translatable(""));
        this.backgroundHeight = 176;
        this.backgroundWidth = 90;
    }

    @Override
    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120, 200, 20, ScreenTexts.DONE, button -> this.finishEditing()));

        this.selectionManager = new SelectionManager(() -> this.text[this.currentRow], text -> {
            this.text[this.currentRow] = text;
        }, SelectionManager.makeClipboardGetter(this.client),
                SelectionManager.makeClipboardSetter(this.client), text -> this.client.textRenderer.getWidth(text) <= 90);

    }

    @Override
    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
    }

    @Override
    public void tick() {
        ++this.ticksSinceOpened;

    }

    private void finishEditing() {
        ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
        if (clientPlayNetworkHandler != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            ClientPlayNetworking.send(new Identifier(ImagineAnything.MOD_ID, "finishedthought"), buf);
        }
        this.client.setScreen(null);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.selectionManager.insert(chr);
        return true;
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.currentRow = this.currentRow - 1 & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.currentRow = this.currentRow + 1 & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        }
        if (this.selectionManager.handleSpecialKey(keyCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int p;
        int o;
        String string;
        int m;
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrices);
        SignEditScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
        matrices.push();
        matrices.translate(this.width / 2, 0.0, 50.0);
        float f = 93.75f;
        matrices.scale(93.75f, -93.75f, 93.75f);
        matrices.translate(0.0, -1.3125, 0.0);

        boolean bl2 = this.ticksSinceOpened / 6 % 2 == 0;
        float g = 0.6666667f;
        matrices.push();
        matrices.translate(0.0, 0.5, 0.0);
        matrices.scale(0.6666667f, -0.6666667f, -0.6666667f);
        VertexConsumerProvider.Immediate immediate = this.client.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.model.getChild("bone").render(matrices, vertexConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        float h = 0.010416667f;
        matrices.translate(0.0, 0.3333333432674408, 0.046666666865348816);
        matrices.scale(0.010416667f, -0.010416667f, 0.010416667f);
        int i = 0x000000;
        int j = this.selectionManager.getSelectionStart();
        int k = this.selectionManager.getSelectionEnd();
        int l = this.currentRow * 10 - this.text.length * 5;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        for (m = 0; m < this.text.length; ++m) {
            string = this.text[m];
            if (string == null) continue;
            if (this.textRenderer.isRightToLeft()) {
                string = this.textRenderer.mirror(string);
            }
            float n = -this.client.textRenderer.getWidth(string) / 2;
            this.client.textRenderer.draw(string, n, m * 10 - this.text.length * 5, i, false, matrix4f, immediate, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE, false);
            if (m != this.currentRow || j < 0 || !bl2) continue;
            o = this.client.textRenderer.getWidth(string.substring(0, Math.max(Math.min(j, string.length()), 0)));
            p = o - this.client.textRenderer.getWidth(string) / 2;
            if (j < string.length()) continue;
            this.client.textRenderer.draw("_", p, l, i, false, matrix4f, immediate, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE, false);
        }
        immediate.draw();
        for (m = 0; m < this.text.length; ++m) {
            string = this.text[m];
            if (string == null || m != this.currentRow || j < 0) continue;
            int q = this.client.textRenderer.getWidth(string.substring(0, Math.max(Math.min(j, string.length()), 0)));
            o = q - this.client.textRenderer.getWidth(string) / 2;
            if (bl2 && j < string.length()) {
                SignEditScreen.fill(matrices, o, l - 1, o + 1, l + this.client.textRenderer.fontHeight, 0xFF000000 | i);
            }
            if (k == j) continue;
            p = Math.min(j, k);
            int r = Math.max(j, k);
            int s = this.client.textRenderer.getWidth(string.substring(0, p)) - this.client.textRenderer.getWidth(string) / 2;
            int t = this.client.textRenderer.getWidth(string.substring(0, r)) - this.client.textRenderer.getWidth(string) / 2;
            int u = Math.min(s, t);
            int v = Math.max(s, t);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableTexture();
            RenderSystem.enableColorLogicOp();
            RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f, u, l + this.client.textRenderer.fontHeight, 0.0f).color(0, 0, 255, 255).next();
            bufferBuilder.vertex(matrix4f, v, l + this.client.textRenderer.fontHeight, 0.0f).color(0, 0, 255, 255).next();
            bufferBuilder.vertex(matrix4f, v, l, 0.0f).color(0, 0, 255, 255).next();
            bufferBuilder.vertex(matrix4f, u, l, 0.0f).color(0, 0, 255, 255).next();
            BufferRenderer.drawWithShader(bufferBuilder.end());
            RenderSystem.disableColorLogicOp();
            RenderSystem.enableTexture();
        }
        matrices.pop();

        DiffuseLighting.enableGuiDepthLighting();
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        super.renderBackground(matrices);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -37.0F, 0.0F, 32.0F, 32.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }
}

