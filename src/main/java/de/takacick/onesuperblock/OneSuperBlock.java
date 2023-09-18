package de.takacick.onesuperblock;

import de.takacick.onesuperblock.registry.EntityRegistry;
import de.takacick.onesuperblock.registry.ItemRegistry;
import de.takacick.onesuperblock.registry.ParticleRegistry;
import de.takacick.onesuperblock.server.oneblock.OneBlockHandler;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.bossbar.BossBarUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class OneSuperBlock implements ModInitializer {

    public static final String MOD_ID = "onesuperblock";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "onesuperblock"), () -> new ItemStack(ItemRegistry.SUPER_BLOCK));
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "onesuperblock");
    public static final BossBar.Color RAINBOW_BAR = BossBarUtils.register(new Identifier(MOD_ID, "rainbow_bar")).texture(new Identifier(BionicUtils.MOD_ID, "textures/gui/custom_bar.png"), 256, 10).rainbow().build();

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();

        new OneBlockHandler();
    }

    public static List<BlockPos> generateSphere(BlockPos centerBlock, int radius, boolean hollow) {

        List<BlockPos> circleBlocks = new ArrayList<BlockPos>();

        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return circleBlocks;
    }

}
