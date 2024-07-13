package de.takacick.onegirlboyblock.utils.data.attachments;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import de.takacick.utils.common.event.EventHandler;
import de.takacick.utils.data.codec.NbtSerializable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TetrisDamageHelper implements NbtSerializable {

    public static final RegistryKey<DamageType> TETRIS = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(OneGirlBoyBlock.MOD_ID, "tetris"));

    private final List<TetrisDamage> tetrisDamages = new ArrayList<>();

    public TetrisDamageHelper() {
    }

    public void tick(LivingEntity livingEntity) {
        if (!livingEntity.getWorld().isClient) {
            World world = livingEntity.getWorld();

            this.tetrisDamages.forEach(damage -> {
                damage.setTick(damage.getTick() - 1);

                if (livingEntity.damage(world.getDamageSources().create(TETRIS), 0.1f)) {
                    EventHandler.sendEntityStatus(world, livingEntity, OneGirlBoyBlock.IDENTIFIER, 6, damage.getTetrisVariant().getId());
                }
            });

            this.tetrisDamages.removeIf(damage -> damage.getTick() <= 0);
        }
    }

    public void addTetrisDamage(TetrisEntity.Variant variant) {
        this.tetrisDamages.add(new TetrisDamage(variant, Random.create().nextBetween(50, 70)));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (!this.tetrisDamages.isEmpty()) {
            NbtList nbtList = new NbtList();

            this.tetrisDamages.forEach(tetrisDamage -> {
                NbtCompound nbtCompound = new NbtCompound();
                nbtList.add(tetrisDamage.write(nbtCompound));
            });

            nbt.put("tetrisDamages", nbtList);
        }

        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("tetrisDamages", NbtElement.LIST_TYPE)) {
            nbt.getList("tetrisDamages", NbtElement.COMPOUND_TYPE).forEach(nbtElement -> {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    this.tetrisDamages.add(TetrisDamage.of(nbtCompound));
                }
            });
        }
    }

    public boolean shouldRemove() {
        return this.tetrisDamages.isEmpty();
    }

    public static TetrisDamageHelper from(NbtCompound nbt) {
        TetrisDamageHelper animationHelper = new TetrisDamageHelper();
        animationHelper.readNbt(nbt);
        return animationHelper;
    }

    public static class TetrisDamage {

        private final TetrisEntity.Variant tetrisVariant;
        private int tick;

        public TetrisDamage(TetrisEntity.Variant tetrisVariant, int tick) {
            this.tetrisVariant = tetrisVariant;
            this.tick = tick;
        }

        public void setTick(int tick) {
            this.tick = tick;
        }

        public int getTick() {
            return tick;
        }

        public TetrisEntity.Variant getTetrisVariant() {
            return tetrisVariant;
        }

        public NbtCompound write(NbtCompound nbtCompound) {
            nbtCompound.putInt("variant", this.tetrisVariant.getId());
            nbtCompound.putInt("tick", this.tick);

            return nbtCompound;
        }

        public static TetrisDamage of(NbtCompound nbtCompound) {
            TetrisEntity.Variant variant = TetrisEntity.Variant.byId(nbtCompound.getInt("variant"));
            int tick = nbtCompound.getInt("tick");
            return new TetrisDamage(variant, tick);
        }
    }
}
