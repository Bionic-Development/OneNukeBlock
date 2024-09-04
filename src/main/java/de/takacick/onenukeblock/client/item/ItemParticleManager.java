package de.takacick.onenukeblock.client.item;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class ItemParticleManager {

    private static final HashMap<ItemStack, ItemParticleHelper> ITEM_PARTICLE_HELPER_LIST = new HashMap<>();

    public static ItemParticleHelper getParticleHelper(ItemStack itemStack) {
        return ITEM_PARTICLE_HELPER_LIST.get(itemStack);
    }

    public static HashMap<ItemStack, ItemParticleHelper> getItemParticleHelperList() {
        return ITEM_PARTICLE_HELPER_LIST;
    }

    public static ItemParticleHelper tick(ItemStack itemStack, long time) {
        ItemParticleHelper itemParticleHelper = ITEM_PARTICLE_HELPER_LIST.get(itemStack);
        if (itemParticleHelper == null) {
            itemParticleHelper = new ItemParticleHelper(itemStack);
            ITEM_PARTICLE_HELPER_LIST.put(itemStack, itemParticleHelper);
        }

        if (itemParticleHelper.shouldTick(time)) {
            itemParticleHelper.tick();
        }

        return itemParticleHelper;
    }
}
