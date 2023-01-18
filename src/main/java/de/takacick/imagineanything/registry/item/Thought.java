package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.ImagineAnythingClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Thought extends Item implements Imagined {
    public Thought(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (world.isClient) {
            ImagineAnythingClient.openThoughtScreen(user);
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eHelps that head of yours turn it's"));
        tooltip.add(Text.of("§dimagination §einto reality!"));
        tooltip.add(Text.of(" "));
        tooltip.add(Text.of("§9§oTip: The more items fed, the less"));
        tooltip.add(Text.of("§9§ochance of §c§odangerous §9§oimaginiation!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt() || super.hasGlint(stack);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(ImagineAnything.MOD_ID, "textures/item/thought.png");
    }
}
