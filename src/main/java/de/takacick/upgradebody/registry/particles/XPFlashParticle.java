package de.takacick.upgradebody.registry.particles;

import de.takacick.upgradebody.UpgradeBodyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class XPFlashParticle extends SpriteBillboardParticle {

    private final int ageOffset;
    private final double size;

    XPFlashParticle(ClientWorld clientWorld, double d, double e, double f, double g) {
        super(clientWorld, d, e, f);
        this.maxAge = 4;
        this.size = g;
        this.ageOffset = world.getRandom().nextBetween(0, 30);
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
        Vector3f vector3f = Vec3d.unpackRgb(UpgradeBodyClient.getColor((age + ageOffset) + tickDelta)).toVector3f();

        setColor(vector3f.x(), vector3f.y(), vector3f.z());
        this.setAlpha(0.6f - ((float) this.age + tickDelta - 1.0f) * 0.25f * 0.5f);

        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<DefaultParticleType> {

        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            XPFlashParticle flash = new XPFlashParticle(clientWorld, d, e, f, g);
            flash.setSprite(this.spriteProvider);
            return flash;
        }
    }
}

