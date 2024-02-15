package de.takacick.secretgirlbase.registry.item;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class LeadCuffs extends Item {

    public LeadCuffs(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {

        if (entity instanceof PlayerEntity playerEntity) {
            user.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1f, 1f);

            if (!user.getWorld().isClient) {
                LeadCuffProperties target = (LeadCuffProperties) entity;
                LeadCuffProperties player = (LeadCuffProperties) user;

                if (player.getLeadCuffedTarget() == null || !playerEntity.equals(player.getLeadCuffedTarget())) {
                    if (player.getLeadCuffedTarget() != null) {
                        ((LeadCuffProperties) player.getLeadCuffedTarget()).setLeadCuffed(null);
                        player.leadCuff(null);
                    }

                    player.leadCuff(playerEntity);
                    target.setLeadCuffed(user);
                } else {
                    target.setLeadCuffed(null);
                    player.leadCuff(null);
                }
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }
}