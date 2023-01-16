package de.takacick.imagineanything.registry.item;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.fabric.FabricWorldEdit;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.projectiles.CustomBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ImaginedMallBartering extends Item {
    private final List<Direction> directionList = new ArrayList<>();

    public ImaginedMallBartering(Settings settings) {
        super(settings);
        directionList.add(Direction.NORTH);
        directionList.add(Direction.WEST);
        directionList.add(Direction.SOUTH);
        directionList.add(Direction.EAST);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.getEntityWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1f, 1f);

            spawnStructure(user, world, user.getBlockPos());
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    public void spawnStructure(PlayerEntity player, World world, BlockPos blockPos) {

        try {
            File file = File.createTempFile("src/main/resources/data/imagineanything/schematics/" + "imagined_mall_bartering", "file");
            FileUtils.copyInputStreamToFile(ImagineAnything.class.getClassLoader().getResourceAsStream("data/imagineanything/schematics/" + "imagined_mall_bartering" + ".schematic"), file);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));

            try {
                Clipboard clipboard = reader.read().transform(new AffineTransform().rotateY(directionList.indexOf(player.getMovementDirection()) * 90));
                BlockVector3 blockVector = BlockVector3.at(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(FabricWorldEdit.inst.getWorld(world)).maxBlocks(-1).build();

                try {
                    Operation operation = (new ClipboardHolder(clipboard)).createPaste(editSession).to(blockVector).ignoreAirBlocks(false).build();
                    Operations.complete(operation);
                } catch (Throwable var14) {
                    if (editSession != null) {
                        try {
                            editSession.close();
                        } catch (Throwable var13) {
                            var14.addSuppressed(var13);
                        }
                    }

                    throw var14;
                }

                if (editSession != null) {
                    editSession.close();
                }

                CuboidRegion region = new CuboidRegion(clipboard.getRegion().getMinimumPoint(), clipboard.getRegion().getMaximumPoint());
                region.setPos1(BlockVector3.at(clipboard.getRegion().getMinimumPoint().getX() - clipboard.getOrigin().getX(), clipboard.getRegion().getMinimumPoint().getY() - clipboard.getOrigin().getY(), clipboard.getRegion().getMinimumPoint().getZ() - clipboard.getOrigin().getZ()));
                region.setPos2(BlockVector3.at(clipboard.getRegion().getMaximumPoint().getX() - clipboard.getOrigin().getX(), clipboard.getRegion().getMaximumPoint().getY() - clipboard.getOrigin().getY(), clipboard.getRegion().getMaximumPoint().getZ() - clipboard.getOrigin().getZ()));
                region.setPos1(BlockVector3.at(blockVector.getX(), blockVector.getY(), blockVector.getZ()).add(region.getPos1()));
                region.setPos2(BlockVector3.at(blockVector.getX(), blockVector.getY(), blockVector.getZ()).add(region.getPos2()));
                defaultVersion(player, world, region);
            } catch (Throwable var15) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var12) {
                        var15.addSuppressed(var12);
                    }
                }

                throw var15;
            }

            if (reader != null) {
                reader.close();
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        }
    }

    public void defaultVersion(PlayerEntity player, final World world, final CuboidRegion region) {
        for (int x = region.getMinimumPoint().getX(); x <= region.getMaximumPoint().getX(); ++x) {
            for (int y = region.getMinimumPoint().getY(); y <= region.getMaximumPoint().getY(); ++y) {
                for (int z = region.getMinimumPoint().getZ(); z <= region.getMaximumPoint().getZ(); ++z) {
                    BlockState block = world.getBlockState(new BlockPos(x, y, z));
                    if (block.getBlock().equals(Blocks.BARRIER)) {
                        world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                    } else if (!block.isAir() && world.getRandom().nextDouble() <= 0.05) {

                        for (int i = 0; i < 1; i++) {
                            CustomBlockEntity customBlockEntity = new CustomBlockEntity(EntityRegistry.FALLING_BLOCK, player.getEntityWorld());
                            customBlockEntity.setOwner(player);
                            customBlockEntity.setPos(x + 0.5, y + 1, z + 0.5);

                            customBlockEntity.setItemStack(block,
                                    block.getBlock().asItem().getDefaultStack());

                            customBlockEntity.setVelocity(world.getRandom().nextGaussian(), world.getRandom().nextGaussian(),
                                    world.getRandom().nextGaussian());
                            world.spawnEntity(customBlockEntity);
                        }
                    }
                }
            }
        }
    }
}