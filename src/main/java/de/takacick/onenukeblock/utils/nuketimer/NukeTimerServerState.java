package de.takacick.onenukeblock.utils.nuketimer;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NukeTimerServerState extends PersistentState {

    private static final Type<NukeTimerServerState> TYPE = new Type<>(
            NukeTimerServerState::new,
            NukeTimerServerState::createFromNbt,
            null
    );
    private static final ServerBossBar PROGRESS_BAR = new ServerBossBar(Text.of("§4§lDetonation §e⌚"), ParticleRegistry.NUKE_BOSSBAR, BossBar.Style.PROGRESS);

    private int maxTicks = 100;
    private int ticks = 0;
    private boolean running = false;
    private boolean visible = false;

    public void tick(MinecraftServer server) {
        if (isRunning()) {
            this.ticks = MathHelper.clamp(this.ticks + 1, 0, this.maxTicks);

            markDirty();
        }

        updateProgressBar();

        server.getPlayerManager().getPlayerList().forEach(getProgressBar()::addPlayer);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.maxTicks != 100) {
            nbt.putInt("maxTicks", this.maxTicks);
        }
        if (this.ticks != 0) {
            nbt.putInt("ticks", this.ticks);
        }
        if (this.running) {
            nbt.putBoolean("running", this.running);
        }
        nbt.putBoolean("visible", this.visible);

        return nbt;
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("maxTicks", NbtElement.INT_TYPE)) {
            this.maxTicks = Math.max(nbt.getInt("maxTicks"), 1);
        }

        if (nbt.contains("ticks", NbtElement.INT_TYPE)) {
            this.ticks = Math.max(nbt.getInt("ticks"), 0);
        }

        if (nbt.contains("running", NbtElement.BYTE_TYPE)) {
            this.running = nbt.getBoolean("running");
        }

        if (nbt.contains("visible", NbtElement.BYTE_TYPE)) {
            this.visible = nbt.getBoolean("visible");
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
        this.markDirty();
    }

    public boolean isRunning() {
        return running;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
        this.markDirty();
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
        this.markDirty();
    }

    public ServerBossBar getProgressBar() {
        return PROGRESS_BAR;
    }

    public void updateProgressBar() {
        this.ticks = MathHelper.clamp(this.ticks, 0, this.maxTicks);

        ServerBossBar bossBar = getProgressBar();
        bossBar.setVisible(this.visible);
        bossBar.setPercent(MathHelper.clamp(1f - (float) this.ticks / (float) this.maxTicks, 0f, 1f));
    }

    public void startTimer(MinecraftServer server, boolean forceStart) {
        if (isRunning() && !forceStart) {
            return;
        }

        setRunning(true);
        setTicks(0);
        setVisible(true);
        updateProgressBar();
    }

    public static Optional<NukeTimerServerState> getServerState(@Nullable MinecraftServer server) {
        if (server == null) {
            return Optional.empty();
        }
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        if (serverWorld == null) {
            return Optional.empty();
        }
        PersistentStateManager persistentStateManager = serverWorld.getPersistentStateManager();

        return Optional.of(persistentStateManager.getOrCreate(TYPE, OneNukeBlock.MOD_ID + "nuketimer"));
    }

    public static NukeTimerServerState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NukeTimerServerState state = new NukeTimerServerState();
        state.readNbt(tag, registryLookup);

        state.markDirty();
        return state;
    }
}