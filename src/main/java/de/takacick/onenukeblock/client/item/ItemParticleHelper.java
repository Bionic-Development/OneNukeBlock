package de.takacick.onenukeblock.client.item;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onenukeblock.registry.particles.ItemParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

import java.util.*;

public class ItemParticleHelper {

    private final ItemStack itemStack;
    private final Map<ParticleTextureSheet, Queue<Particle>> particles = Maps.newIdentityHashMap();
    private long lastRenderAge;
    private long lastRenderTime;
    private boolean ticking = false;

    public ItemParticleHelper(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void tick() {
        this.particles.forEach((particleTextureSheet, particleQueue) -> {
            Iterator<Particle> iterator = particleQueue.iterator();

            while (iterator.hasNext()) {
                Particle particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        });
    }

    public void addParticle(Particle particle) {
        Queue<Particle> particleQueue = this.particles.computeIfAbsent(particle.getType(), k -> new LinkedList<>());
        particleQueue.add(particle);
    }

    public boolean shouldTick(long time) {
        boolean bl = this.lastRenderAge < time;

        this.lastRenderAge = time;
        this.ticking = bl;

        return bl;
    }

    public boolean shouldRemove() {
        return System.currentTimeMillis() - this.lastRenderTime > 1000;
    }

    public boolean isTicking() {
        return this.ticking;
    }

    public void renderParticles(LightmapTextureManager lightmapTextureManager, Camera camera, MatrixStack.Entry entry, ModelTransformationMode modelTransformationMode, float tickDelta) {
        this.lastRenderTime = System.currentTimeMillis();

        lightmapTextureManager.enable();
        RenderSystem.enableDepthTest();
        Iterator var4 = MinecraftClient.getInstance().particleManager.PARTICLE_TEXTURE_SHEETS.iterator();

        while (true) {
            ParticleTextureSheet particleTextureSheet;
            Queue queue;
            BufferBuilder bufferBuilder;
            do {
                do {
                    do {
                        if (!var4.hasNext()) {
                            RenderSystem.depthMask(true);
                            RenderSystem.disableBlend();
                            lightmapTextureManager.disable();
                            return;
                        }

                        particleTextureSheet = (ParticleTextureSheet) var4.next();
                        queue = (Queue) this.particles.get(particleTextureSheet);
                    } while (queue == null);
                } while (queue.isEmpty());

                RenderSystem.setShader(GameRenderer::getParticleProgram);
                Tessellator tessellator = Tessellator.getInstance();
                bufferBuilder = particleTextureSheet.begin(tessellator, MinecraftClient.getInstance().particleManager.textureManager);
            } while (bufferBuilder == null);

            Iterator var9 = queue.iterator();

            while (var9.hasNext()) {
                Particle particle = (Particle) var9.next();

                try {
                    if (particle instanceof ItemParticle itemParticle) {

                        boolean force = false;
                        if (modelTransformationMode.equals(ModelTransformationMode.GUI)) {
                            Inventory inventory = MinecraftClient.getInstance().player.getInventory();
                            for (int i = 0; i < 9; i++) {
                                if (inventory.getStack(i).equals(itemStack)) {
                                    force = true;
                                }
                            }
                        }

                        itemParticle.setMatrixEntry(entry, modelTransformationMode, force);
                    }
                    particle.buildGeometry(bufferBuilder, camera, tickDelta);
                } catch (Throwable var14) {
                    Throwable throwable = var14;
                    CrashReport crashReport = CrashReport.create(throwable, "Rendering Particle");
                    CrashReportSection crashReportSection = crashReport.addElement("Particle being rendered");
                    Objects.requireNonNull(particle);
                    crashReportSection.add("Particle", particle::toString);
                    Objects.requireNonNull(particleTextureSheet);
                    crashReportSection.add("Particle Type", particleTextureSheet::toString);
                    throw new CrashException(crashReport);
                }
            }

            BuiltBuffer builtBuffer = bufferBuilder.endNullable();
            if (builtBuffer != null) {
                BufferRenderer.drawWithGlobalProgram(builtBuffer);
            }
        }
    }


}
