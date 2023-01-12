package de.takacick.immortalmobs.registry.entity.custom;

import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public class ImmortalItemEntity extends ItemEntity {

    public ImmortalItemEntity(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ImmortalItemEntity(World world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ) {
        super(EntityRegistry.IMMORTAL_ITEM, world);
        this.setPosition(x, y, z);
        this.setVelocity(velocityX, velocityY, velocityZ);
        this.setStack(stack);
        this.setGlowing(true);
    }

    public ImmortalItemEntity(World world, double x, double y, double z, ItemStack stack) {
        this(world, x, y, z, stack, 0, 0, 0);
    }

    @Override
    public void tick() {

        if (getVelocity().getY() > 0) {
            this.world.addParticle(ParticleRegistry.IMMORTAL_GLOW_TOTEM, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.25, this.random.nextGaussian() * 0.25, this.random.nextGaussian() * 0.25);
        }

        super.tick();
    }

    public static ImmortalItemEntity create(EntityType<ImmortalItemEntity> entityType, World world) {
        return new ImmortalItemEntity(entityType, world);
    }

    @Override
    public int getTeamColorValue() {
        return DyeColor.PURPLE.getSignColor();
    }
}
