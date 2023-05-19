package de.takacick.heartmoney.access;

public interface LivingProperties {

    void setMaidExplosion(boolean maidExplosion);

    boolean isMaidExploding();

    float getMaidExplosionProgress(float tickDelta);
}
