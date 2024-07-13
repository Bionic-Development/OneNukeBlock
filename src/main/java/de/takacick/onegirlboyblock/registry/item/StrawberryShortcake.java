package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.utils.data.AttachmentTypes;
import de.takacick.onegirlboyblock.utils.data.attachments.SizeHelper;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class StrawberryShortcake extends Item {

    private final Identifier attribute = Identifier.of(OneGirlBoyBlock.MOD_ID, "health_attribute");

    public StrawberryShortcake(Settings settings) {
        super(settings.food(new FoodComponent.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible()
                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 600), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 1), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1), 1f)
                .build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            EntityAttributeInstance entityAttributeInstance = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (entityAttributeInstance != null) {
                EntityAttributeModifier entityAttributeModifier = entityAttributeInstance.getModifier(attribute);
                if (entityAttributeModifier == null) {
                    entityAttributeModifier = new EntityAttributeModifier(attribute, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE);
                    entityAttributeInstance.addPersistentModifier(entityAttributeModifier);
                } else {
                    entityAttributeModifier = new EntityAttributeModifier(attribute, entityAttributeModifier.value() + 2.0d, EntityAttributeModifier.Operation.ADD_VALUE);
                    entityAttributeInstance.updateModifier(entityAttributeModifier);
                }
            }

            user.setHealth(user.getHealth() + 2.0f);

            SizeHelper sizeHelper = user.getAttachedOrCreate(AttachmentTypes.SIZE_HELPER, SizeHelper::new);
            sizeHelper.setBoyEffect(false);
            sizeHelper.setTick(600);
            sizeHelper.setPrevTick(600);
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public SoundEvent getEatSound() {
        return super.getEatSound();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Who says cant §cpower §rbe as").withColor(0xFFCCFF));
        tooltip.add(Text.literal("§esweet §ras §cstrawberries§r?").withColor(0xFFCCFF));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
