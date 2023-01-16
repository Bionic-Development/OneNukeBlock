package de.takacick.imagineanything.registry.entity.living;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MysteriousEntity extends MobEntity {

    private final AnimationState animationState = new AnimationState();

    public MysteriousEntity(EntityType<? extends MysteriousEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {

        if (!world.isClient) {
            world.getOtherEntities(this, getBoundingBox().expand(10)).forEach(entity -> {
                if (entity instanceof PlayerEntity) {
                    entity.sendMessage(Text.of("§f<§dMysterious Entity§f> Thank you for saving me adventurers! I shall grant you each §eONE §fsuperpower of your choice to show my gratitude!"));
                }
            });
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void tick() {

        if (world.isClient) {
            animationState.startIfNotRunning(age);
        }

        super.tick();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (!world.isClient && source.getSource() instanceof PlayerEntity) {
            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, ImagineAnything.IDENTIFIER, 4);
            this.discard();
        }

        return false;
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void pushAway(Entity entity) {

    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }
}
