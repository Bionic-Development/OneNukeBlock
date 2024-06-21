package de.takacick.onescaryblock.mixin;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.PlayerProperties;
import de.takacick.onescaryblock.utils.datatracker.OneScaryBlockDataTracker;
import de.takacick.onescaryblock.utils.datatracker.SoulFragmentHelper;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "onescaryblock$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique
    private static final TrackedData<SoulFragmentHelper> onescaryblock$SOUL_FRAGMENT = BionicDataTracker.registerData(new Identifier(OneScaryBlock.MOD_ID, "soul_fragment"), OneScaryBlockDataTracker.SOUL_FRAGMENT);
    @Unique
    private SoulFragmentHelper onescaryblock$soulFragment = new SoulFragmentHelper();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onescaryblock$SOUL_FRAGMENT, new SoulFragmentHelper());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        SoulFragmentHelper soulFragmentHelper = onescaryblock$getSoulFragmentHelper();
        soulFragmentHelper.tick((PlayerEntity) (Object) this);
        if (getWorld().isClient) {
            soulFragmentHelper.sync(getDataTracker().get(onescaryblock$SOUL_FRAGMENT));
        } else if (soulFragmentHelper.isDirty()) {
            onescaryblock$setSoulFragmentHelper(soulFragmentHelper);
            soulFragmentHelper.setDirty(false);
        }
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        SoulFragmentHelper soulFragmentHelper = onescaryblock$getSoulFragmentHelper();

        if (soulFragmentHelper != null) {
            nbt.put("onescaryblock$soulFragment", soulFragmentHelper.writeNbt(new NbtCompound()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("onescaryblock$soulFragment", NbtElement.COMPOUND_TYPE)) {
            SoulFragmentHelper soulFragmentHelper = onescaryblock$getSoulFragmentHelper();
            soulFragmentHelper.readNbt(nbt.getCompound("onescaryblock$soulFragment"));
            onescaryblock$setSoulFragmentHelper(soulFragmentHelper);
        }
    }

    public void onescaryblock$setSoulFragmentHelper(SoulFragmentHelper soulFragmentHelper) {
        getDataTracker().set(onescaryblock$SOUL_FRAGMENT, SoulFragmentHelper.copy(soulFragmentHelper));
        this.onescaryblock$soulFragment = soulFragmentHelper;
    }

    public SoulFragmentHelper onescaryblock$getSoulFragmentHelper() {
        return this.onescaryblock$soulFragment;
    }
}

