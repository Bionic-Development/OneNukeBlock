package de.takacick.onenukeblock.registry.inventory;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.ItemRegistry;
import de.takacick.onenukeblock.registry.ScreenHandlerRegistry;
import de.takacick.onenukeblock.registry.block.entity.NukeOneBlockEntity;
import de.takacick.onenukeblock.registry.inventory.slot.NukeSlot;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class NukeOneBlockScreenHandler
        extends ScreenHandler {

    private final Inventory inventory;
    private final ScreenHandlerContext context;

    public NukeOneBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(2), ScreenHandlerContext.EMPTY);
    }

    public NukeOneBlockScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.NUKE_ONE_BLOCK, syncId);
        this.context = context;
        int j;

        int i;
        checkSize(inventory, 2);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        this.addSlot(new NukeSlot(inventory, 0, 44, 35));
        this.addSlot(new NukeSlot(inventory, 1, 116, 35));

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
        return CraftingScreenHandler.canUse(this.context, player, ItemRegistry.ONE_NUKE_BLOCK);
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
        if (!player.getWorld().isClient) {
            this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
        }
        super.onClosed(player);
    }

    protected void dropInventory(PlayerEntity player, Inventory inventory) {
        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).isDisconnected()) {
            for (int i = 0; i < inventory.size(); ++i) {
                player.dropItem(inventory.removeStack(i), false);
            }
            return;
        }

        for (int i = 0; i < inventory.size(); ++i) {
            PlayerInventory playerInventory = player.getInventory();
            if (!(playerInventory.player instanceof ServerPlayerEntity)) continue;
            playerInventory.offerOrDrop(inventory.removeStack(i));
        }
    }

    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        try {
            if (actionType.equals(SlotActionType.QUICK_MOVE)) {
                return;
            }
            Slot slot = getSlot(slotIndex);

            if (slot instanceof NukeSlot) {
                if (getCursorStack() != null && !getCursorStack().isEmpty()) {
                    if (player instanceof ServerPlayerEntity serverPlayerEntity) {

                        this.context.run((world, pos) -> {
                            if(world.getBlockEntity(pos) instanceof NukeOneBlockEntity nukeOneBlockEntity) {
                                nukeOneBlockEntity.ignite();
                            }
                        });

                        slot.setStack(this.getCursorStack());
                        EventHandler.sendEntityStatus(player.getWorld(), player, OneNukeBlock.IDENTIFIER, 5, 0);
                        player.sendMessage(Text.of("§eYou idiot..."));
                        serverPlayerEntity.closeHandledScreen();
                        return;
                    }
                }
            }
        } catch (Exception exception) {

        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

}

