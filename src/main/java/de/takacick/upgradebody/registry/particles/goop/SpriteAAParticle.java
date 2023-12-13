package de.takacick.upgradebody.registry.particles.goop;


import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public abstract class SpriteAAParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;

    protected Vector3f scale;

    protected SpriteAAParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        float s = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.scale = new Vector3f(s, s, s);
        this.spriteProvider = spriteProvider;
        sprite = spriteProvider.getSprite(random);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d camPos = camera.getPos();
        Vec3d dir = new Vec3d(x, y, z).subtract(camPos).normalize();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - camPos.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - camPos.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - camPos.getZ());

        Vector3f[] vec3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};

        for (int k = 0; k < 4; ++k) {
            Vector3f vec3f = vec3fs[k];
            vec3f.rotateY((float) Math.toDegrees(Math.atan2(dir.x, dir.z)));
            vec3f.mul(scale.x(), scale.y(), scale.z());
            vec3f.add(f, g, h);
        }

        int n = this.getBrightness(tickDelta);

        vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(getMaxU(), getMaxV()).color(this.red, this.green, this.blue, this.alpha).light(n).next();
        vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(getMaxU(), getMinV()).color(this.red, this.green, this.blue, this.alpha).light(n).next();
        vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(getMinU(), getMinV()).color(this.red, this.green, this.blue, this.alpha).light(n).next();
        vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(getMinU(), getMaxV()).color(this.red, this.green, this.blue, this.alpha).light(n).next();
    }
}