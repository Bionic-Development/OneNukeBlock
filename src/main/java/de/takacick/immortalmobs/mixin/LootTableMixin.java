package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalPickaxeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {

    @Inject(method = "generateLoot*", at = @At(value = "HEAD"), cancellable = true)
    public void generateLoot(LootContext context, Consumer<ItemStack> lootConsumer, CallbackInfo info) {
        LootTable lootTable = (LootTable) (Object) this;
        if (lootTable.getType().equals(LootContextTypes.BLOCK)) {
            if (context.hasParameter(LootContextParameters.THIS_ENTITY)) {
                Entity entity = context.hasParameter(LootContextParameters.DIRECT_KILLER_ENTITY) ? context.get(LootContextParameters.DIRECT_KILLER_ENTITY) : context.get(LootContextParameters.THIS_ENTITY);
                if (entity instanceof PlayerEntity playerEntity && playerEntity.getMainHandStack().isOf(ItemRegistry.IMMORTAL_PICKAXE) || entity instanceof ImmortalPickaxeEntity) {
                    World world = entity.getEntityWorld();
                    List<ItemStack> items = new ArrayList<>();
                    lootTable.generateUnprocessedLoot(context, LootTable.processStacks(items::add));

                    boolean smelt = false;

                    for (ItemStack itemStack : items) {
                        if (itemStack.isOf(Items.DIAMOND)) {
                            ItemStack output = ItemRegistry.IMMORTAL_DIAMOND.getDefaultStack();
                            output.setCount(itemStack.getCount());
                            lootConsumer.accept(output);
                            smelt = true;
                            continue;
                        }

                        Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(itemStack), world);
                        if (recipe.isPresent()) {
                            ItemStack output = recipe.get().getOutput().copy();
                            output.setCount(itemStack.getCount());
                            lootConsumer.accept(output);
                            smelt = true;
                        } else {
                            lootConsumer.accept(itemStack);
                        }
                    }

                    if (smelt) {
                        if (context.hasParameter(LootContextParameters.ORIGIN)) {

                            BlockPos pos = new BlockPos(context.get(LootContextParameters.ORIGIN));
                            for (int i = 0; i < 5; i++) {
                                double d = pos.getX() + world.getRandom().nextDouble();
                                double e = pos.getY() + world.getRandom().nextDouble();
                                double f = pos.getZ() + world.getRandom().nextDouble();
                                ((ServerWorld) world).spawnParticles(ParticleRegistry.IMMORTAL_GLOW_TOTEM, d, e, f, 1, 0.0D, 0.0D, 0, 0);
                            }
                        }
                    }

                    info.cancel();
                }
            }
        }
    }
}
