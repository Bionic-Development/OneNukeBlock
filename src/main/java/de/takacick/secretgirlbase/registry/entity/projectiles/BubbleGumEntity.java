package de.takacick.secretgirlbase.registry.entity.projectiles;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.access.LivingProperties;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BubbleGumEntity extends PersistentProjectileEntity {

    public BubbleGumEntity(EntityType<? extends BubbleGumEntity> entityType, World world) {
        super(entityType, world, ItemStack.EMPTY);
    }

    public BubbleGumEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.BUBBLE_GUM, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static BubbleGumEntity create(EntityType<BubbleGumEntity> entityType, World world) {
        return new BubbleGumEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            if (isOnGround() || isInsideWall()) {
                this.discard();
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient) {

            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                float strength = 0.35f;
                if (livingEntity instanceof LivingProperties livingProperties) {
                    livingProperties.setBubbleGumStrength(livingProperties.getBubbleGumStrength(1f) + 0.35f);
                    strength = livingProperties.getBubbleGumStrength(1f);
                }
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (int) (160 * strength), 2, false, false, true));
            }

            entityHitResult.getEntity().damage(getWorld().getDamageSources().thrown(this, getOwner() instanceof LivingEntity livingEntity ? livingEntity : null), 0.5f);
            BionicUtils.sendEntityStatus(getWorld(), entityHitResult.getEntity(), SecretGirlBase.IDENTIFIER, 5);
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        onHit();
    }


    @Override
    public boolean collidesWithStateAtPos(BlockPos pos, BlockState state) {
        return super.collidesWithStateAtPos(pos, state) && !(state.isOf(ItemRegistry.BUBBLE_GUM_LAUNCHER) && this.age <= 3);
    }

    private void onHit() {
        if (!getWorld().isClient) {
            BionicUtils.sendEntityStatus(getWorld(), this, SecretGirlBase.IDENTIFIER, 5);
            this.discard();
        }
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15f;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public byte getPierceLevel() {
        return (byte) 0;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !getType().equals(entity.getType()) && super.canHit(entity);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}