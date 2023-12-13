package de.takacick.upgradebody.registry.bodypart.upgrades;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class CyberChainsaws extends BodyPart {

    public static final RegistryKey<DamageType> CYBER_CHAINSAW = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(UpgradeBody.MOD_ID, "cyber_chainsaw"));

    public CyberChainsaws() {
        super("Cyber Chainsaws", new Identifier(UpgradeBody.MOD_ID, "cyber_chainsaw"));
    }

    @Override
    public double getHeight() {
        return 0f;
    }

    @Override
    public double getWidth() {
        return 0.6f;
    }

    @Override
    public int getHeightIndex() {
        return 1;
    }

    @Override
    public float getHealth() {
        return 6f;
    }

    @Override
    public boolean allowsArms() {
        return true;
    }

    @Override
    public void onEquip(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, 0, false, false, true));
        super.onEquip(playerEntity);
    }

    @Override
    public void tick(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, 0, false, false, true));

        if (playerEntity.getWorld().isClient) {
            if (((PlayerProperties) playerEntity).hasCyberChainsawAnimation()) {
                playerEntity.getWorld().playSoundFromEntity(playerEntity, playerEntity, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                playerEntity.getWorld().playSoundFromEntity(playerEntity, playerEntity, SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        } else if (((PlayerProperties) playerEntity).isUsingCyberSlice()) {
            Vec3d vec3d = playerEntity.getPos().add(0, playerEntity.getHeight() / 2, 0);
            playerEntity.getWorld().getOtherEntities(playerEntity, new Box(vec3d, vec3d.add(playerEntity.getRotationVector().multiply(2.5)))).forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame()) {
                    BionicUtils.sendEntityStatus((ServerWorld) livingEntity.getWorld(), livingEntity, UpgradeBody.IDENTIFIER, 5);
                    livingEntity.damage(playerEntity.getWorld().getDamageSources().create(CYBER_CHAINSAW, playerEntity), 5);
                }
            });
        }

        super.tick(playerEntity);
    }

    @Override
    public void onDequip(PlayerEntity playerEntity) {
        playerEntity.removeStatusEffect(StatusEffects.STRENGTH);
        super.onDequip(playerEntity);
    }
}
