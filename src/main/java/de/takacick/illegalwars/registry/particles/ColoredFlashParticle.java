package de.takacick.illegalwars.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class ColoredFlashParticle extends SpriteBillboardParticle {

    private final double size;

    ColoredFlashParticle(ClientWorld clientWorld, Vector3f color, double d, double e, double f, double g) {
        super(clientWorld, d, e, f);
        this.maxAge = 4;
        this.size = g;

        setColor(color.x(), color.y(), color.z());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getSize(float tickDelta) {
        return 7.1f * MathHelper.sin(((float) this.age + tickDelta - 1.0f) * 0.25f * (float) Math.PI) * (float) size;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.setAlpha(0.6f - ((float) this.age + tickDelta - 1.0f) * 0.25f * 0.5f);

        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<ColoredParticleEffect> {

        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColoredFlashParticle flash = new ColoredFlashParticle(clientWorld, particleEffect.color(), d, e, f, g);
            flash.setSprite(this.spriteProvider);
            return flash;
        }
    }
}

