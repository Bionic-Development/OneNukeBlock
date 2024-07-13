package de.takacick.onegirlboyblock.registry.particles;

import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class TetrisFlashParticle extends SpriteBillboardParticle {

    private final double size;

    TetrisFlashParticle(ClientWorld clientWorld, double d, double e, double f, double g) {
        super(clientWorld, d, e, f);
        this.maxAge = 4;
        this.size = g;
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
            implements ParticleFactory<TetrisParticleEffect> {

        @Override
        public Particle createParticle(TetrisParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            TetrisFlashParticle flash = new TetrisFlashParticle(clientWorld, d, e, f, g);
            flash.setSprite(this.spriteProvider);

            int variant = particleEffect.tetrisVariant();
            int length = TetrisEntity.Variant.values().length;
            TetrisEntity.Variant tetris = variant < length && variant >= 0 ? TetrisEntity.Variant.byId(variant) : TetrisEntity.Variant.byId(flash.random.nextInt(length));

            float offset = flash.random.nextFloat() * 0.1f + 0.9f;
            Vector3f color = Vec3d.unpackRgb(tetris.getColor()).toVector3f().mul(offset, new Vector3f(1f, 1f, 1f));
            flash.setColor(color.x(), color.y(), color.z());

            return flash;
        }
    }
}

