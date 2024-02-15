package de.takacick.secretgirlbase.registry.particles.goop;


import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SurfaceAlignedParticle extends SpriteBillboardParticle {
    private final List<Boolean> faceShouldRender = new ArrayList<>();
    private final List<Vector3f> verts = new ArrayList<>();
    private final List<Vec2f> uvs = new ArrayList<>();
    private final List<Float> maxDeform = new ArrayList<>();
    protected final SpriteProvider spriteProvider;
    protected final Vector3f dir;

    protected float deformation;
    float targetSize;
    boolean isFancy;

    protected SurfaceAlignedParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider,
                                     Vector3f color, float scale, Vec3d dir) {
        super(world, x, y, z);
        this.targetSize = scale;
        this.scale = this.random.nextFloat() * scale * 0.5f + scale * 0.25f;
        this.spriteProvider = spriteProvider;
        sprite = spriteProvider.getSprite(random);
        gravityStrength = 0;
        angle = random.nextFloat() * 360;
        setColor(color.x(), color.y(), color.z());

        isFancy = true;

        this.dir = new Vector3f((float) Math.round(dir.x), (float) Math.round(dir.y), (float) Math.round(dir.z));
        boolean b = dir.x != 0;
        if (dir.y != 0) {
            if (b)
                markDead();
            b = true;
        }
        if (dir.z != 0 && b)
            markDead();

        if (dead) {
            this.scale = 0;
            return;
        }

        Vector3f modDir = new Vector3f(dir.getX() == 0 ? 1 : 0, dir.getY() == 0 ? 1 : 0, dir.getZ() == 0 ? 1 : 0);
        float s = isFancy ? Math.max(targetSize, 1) : 1;
        for (int vy = 0; vy <= s; vy++)
            for (int vx = 0; vx <= s; vx++) {
                Vector3f vert;
                if (dir.y != 0)
                    vert = new Vector3f(modDir.x() * vx / s, modDir.y(), modDir.z() * vy / s);
                else
                    vert = new Vector3f(modDir.x() * vx / s, modDir.y() * vy / s, modDir.z() * vx / s);
                vert.add(dir.multiply(random.nextFloat() / 100).toVector3f()); //fight Z-Fighting);
                verts.add(vert);
                faceShouldRender.add(true);
                uvs.add(new Vec2f(MathHelper.lerp(vx / s, getMinU(), getMaxU()), MathHelper.lerp(vy / s, getMinV(), getMaxV())));
                if (dir.y == 0)
                    maxDeform.add(random.nextFloat());
                else
                    maxDeform.add(random.nextBoolean() ? random.nextFloat() * 0.25f * targetSize : 0);
            }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (verts.size() == 0)
            return;

        Vec3d camPos = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - camPos.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - camPos.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - camPos.getZ());

        Vector3f dir = this.dir;

        List<Vector3f> verts = new ArrayList<>();
        Vector3f modDir = new Vector3f(dir.x() == 0 ? 1 : 0, dir.y() == 0 ? 1 : 0, dir.z() == 0 ? 1 : 0);
        this.verts.forEach(i ->
        {
            Vector3f v = new Vector3f(i);
            v.sub(new Vector3f((float) (modDir.x() * 0.5), (float) (modDir.y() * 0.5), (float) (modDir.z() * 0.5)));
            verts.add(v);
        });

        AtomicInteger atomicInt = new AtomicInteger();
        verts.forEach(i ->
        {
            //random rotation
            i.rotate(fromEulerXyzDegrees(new Vector3f(dir.x() * angle, dir.y() * angle, dir.z() * angle)));
            //deformation
            if (!(this.dir.y() > 0) && isFancy)
                i.sub(new Vector3f(0, deformation * maxDeform.get(atomicInt.get()), 0));
            i.mul(scale);
            i.add(f, g, h);
            atomicInt.getAndIncrement();
        });

        int n = this.getBrightness(tickDelta);
        float ts = isFancy ? Math.max(targetSize, 1) : 1;

        for (int y = 1, vi = 0; y < (int) ts + 1; y++, vi++) {
            for (int x = 1; x < (int) ts + 1; x++, vi++) {
                Vector3f[] modVerts = new Vector3f[]{new Vector3f(verts.get(vi)), new Vector3f(verts.get((int) (vi + ts + 1))),
                        new Vector3f(verts.get((int) (vi + ts + 2))), new Vector3f(verts.get(vi + 1))};

                boolean render = !isFancy || faceShouldRender.get(vi);

                if (isFancy && dir.y() > 0 && faceShouldRender.get(vi)) {
                    Vector3f faceCenter = new Vector3f(modVerts[0]);
                    faceCenter.add(modVerts[1]);
                    faceCenter.add(modVerts[2]);
                    faceCenter.add(modVerts[3]);
                    faceCenter.mul(0.25f);

                    BlockPos pos = BlockPos.ofFloored(camPos.add(new Vec3d(faceCenter)));
                    render = !world.isAir(pos.down()) && !world.getBlockState(pos).isSolidBlock(world, pos);
                    if (!render)
                        faceShouldRender.set(vi, false); //so faces don't reappear after being removed

                    for (Vector3f mv : modVerts) {
                        Vector3f camPosF = camPos.toVector3f();
                        mv.add(camPosF);
                        BlockPos vpos = BlockPos.ofFloored(new Vec3d(mv));
                        if ((world.isAir(vpos.down()) || world.getBlockState(pos).isSolidBlock(world, pos)) && targetSize >= 2)
                            moveToBlockEdge(mv);
                        mv.sub(camPosF);
                    }
                }

                if (render) {
                    //top
                    vertexConsumer.vertex(modVerts[0].x(), modVerts[0].y(), modVerts[0].z()).texture(uvs.get(vi).x, uvs.get(vi).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    vertexConsumer.vertex(modVerts[1].x(), modVerts[1].y(), modVerts[1].z()).texture(uvs.get((int) (vi + ts + 1)).x, uvs.get((int) (vi + ts + 1)).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    vertexConsumer.vertex(modVerts[2].x(), modVerts[2].y(), modVerts[2].z()).texture(uvs.get((int) (vi + ts + 2)).x, uvs.get((int) (vi + ts + 2)).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    vertexConsumer.vertex(modVerts[3].x(), modVerts[3].y(), modVerts[3].z()).texture(uvs.get(vi + 1).x, uvs.get(vi + 1).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    //bottom
                    vertexConsumer.vertex(modVerts[3].x(), modVerts[3].y(), modVerts[3].z()).texture(uvs.get(vi + 1).x, uvs.get(vi + 1).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    vertexConsumer.vertex(modVerts[2].x(), modVerts[2].y(), modVerts[2].z()).texture(uvs.get((int) (vi + ts + 2)).x, uvs.get((int) (vi + ts + 2)).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    vertexConsumer.vertex(modVerts[1].x(), modVerts[1].y(), modVerts[1].z()).texture(uvs.get((int) (vi + ts + 1)).x, uvs.get((int) (vi + ts + 1)).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                    vertexConsumer.vertex(modVerts[0].x(), modVerts[0].y(), modVerts[0].z()).texture(uvs.get(vi).x, uvs.get(vi).y).color(this.red, this.green, this.blue, this.alpha).light(n).next();
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (world.getBlockState(new BlockPos((int) (x - dir.x()), (int) (y - dir.y()), (int) (z - dir.z()))).isAir() ||
                !world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).isAir())
            markDead();
        deformation = (float) age / maxAge;
    }

    private void moveToBlockEdge(Vector3f vert) {
        Vec3d dir = new Vec3d(vert).subtract(x, y, z).normalize().multiply(0.33);
        vert.set(Math.round(vert.x() - dir.x), vert.y(), Math.round(vert.z() - dir.z));
    }

    public static Quaternionf fromEulerXyzDegrees(Vector3f vector) {
        return fromEulerXyz((float) Math.toRadians(vector.x()), (float) Math.toRadians(vector.y()), (float) Math.toRadians(vector.z()));
    }

    public static Quaternionf fromEulerXyz(float x, float y, float z) {
        Quaternionf quaternion = new Quaternionf(0, 0, 0, 1);
        quaternion.nlerp(new Quaternionf((float) Math.sin(x / 2.0f), 0.0f, 0.0f, (float) Math.cos(x / 2.0f)), 1f);
        quaternion.nlerp(new Quaternionf(0.0f, (float) Math.sin(y / 2.0f), 0.0f, (float) Math.cos(y / 2.0f)), 1f);
        quaternion.nlerp(new Quaternionf(0.0f, 0.0f, (float) Math.sin(z / 2.0f), (float) Math.cos(z / 2.0f)), 1f);
        return quaternion;
    }

}