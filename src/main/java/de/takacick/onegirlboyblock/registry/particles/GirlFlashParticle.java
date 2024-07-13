package de.takacick.onegirlboyblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class GirlFlashParticle extends SpriteBillboardParticle {

    private final double size;

    GirlFlashParticle(ClientWorld clientWorld, double d, double e, double f, double g) {
        super(clientWorld, d, e, f);
        this.maxAge = 4;
        this.size = g;

        float offset = this.random.nextFloat() * 0.1f + 0.9f;
        Vector3f color = GlitterParticle.COLORS.get(this.random.nextInt(GlitterParticle.COLORS.size())).mul(offset, new Vector3f(1f, 1f, 1f));
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
            implements ParticleFactory<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            GirlFlashParticle flash = new GirlFlashParticle(clientWorld, d, e, f, g);
            flash.setSprite(this.spriteProvider);
            return flash;
        }
    }
}

