package de.takacick.onenukeblock.registry.entity.projectiles;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.EffectRegistry;
import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class DiamondSwordEntity extends PersistentProjectileEntity {

    public DiamondSwordEntity(EntityType<? extends DiamondSwordEntity> entityType, World world) {
        super(entityType, world);
    }

    public DiamondSwordEntity(World world, LivingEntity owner) {
        super(EntityRegistry.DIAMOND_SWORD, owner, world, Items.DIAMOND_SWORD.getDefaultStack(), null);
    }

    public DiamondSwordEntity(World world, double x, double y, double z) {
        super(EntityRegistry.DIAMOND_SWORD, x, y, z, world, Items.DIAMOND_SWORD.getDefaultStack(), null);
    }

    @Override
    public void tick() {


        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity.damage(this.getDamageSources().thrown(this, this.getOwner()), 7)) {
            EventHandler.sendEntityStatus(getWorld(), entity, OneNukeBlock.IDENTIFIER, 3, 0);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
            }
            this.discard();
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    protected double getGravity() {
        return 0.01;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.DIAMOND_SWORD.getDefaultStack();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setSound(getHitSound());
    }
}