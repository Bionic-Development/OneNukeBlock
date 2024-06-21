package de.takacick.onescaryblock.registry.inventory;

import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.ScreenHandlerRegistry;
import de.takacick.onescaryblock.registry.block.entity.ScaryOneBlockBlockEntity;
import de.takacick.onescaryblock.utils.ConsumedItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.Box;

public class ScaryOneBlockScreenHandler
        extends ScreenHandler {

    private final Inventory inventory;
    private final ScreenHandlerContext context;

    public ScaryOneBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1), ScreenHandlerContext.EMPTY);
    }

    public ScaryOneBlockScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.SCARY_ONE_BLOCk, syncId);
        this.context = context;
        int j;

        int i;
        checkSize(inventory, 1);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 80, 35));

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return CraftingScreenHandler.canUse(this.context, player, ItemRegistry.SCARY_ONE_BLOCK);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 9 ? !this.insertItem(itemStack2, 9, 37, true) : !this.insertItem(itemStack2, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        ItemStack itemStack = getInventory().getStack(0);

        if (itemStack != null && !itemStack.isEmpty()) {
            this.context.get((world, blockPos) -> {

                if (world.getBlockState(blockPos).isOf(ItemRegistry.SCARY_ONE_BLOCK)) {
                    if (world.getBlockEntity(blockPos) instanceof ScaryOneBlockBlockEntity scaryOneBlockBlockEntity) {
                        scaryOneBlockBlockEntity.getConsumedItems().add(new ConsumedItem(itemStack));
                        world.updateListeners(scaryOneBlockBlockEntity.getPos(), scaryOneBlockBlockEntity.getCachedState(), scaryOneBlockBlockEntity.getCachedState(), Block.NOTIFY_ALL);
                    }

                    world.getOtherEntities(null, new Box(blockPos.add(0, 2, 0)).expand(0.3, 1, 0.3)).forEach(entity -> {
                        if (entity.getPos().getY() >= blockPos.getY()) {
                            entity.addVelocity(0, 1.3d, 0);
                            entity.velocityDirty = true;
                            entity.velocityModified = true;
                        }
                    });
                }

                return true;
            });
        }

        this.inventory.onClose(player);
    }
}

