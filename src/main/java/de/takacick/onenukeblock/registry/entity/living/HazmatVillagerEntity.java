package de.takacick.onenukeblock.registry.entity.living;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HazmatVillagerEntity extends VillagerEntity {
    public HazmatVillagerEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }

    public HazmatVillagerEntity(EntityType<? extends VillagerEntity> entityType, World world, VillagerType type) {
        super(entityType, world, type);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {

        var data = super.initialize(world, difficulty, spawnReason, entityData);

        setVillagerData(getVillagerData().withProfession(VillagerProfession.NITWIT));

        return data;
    }

    @Override
    public void setVillagerData(VillagerData villagerData) {
        super.setVillagerData(villagerData.withProfession(VillagerProfession.NITWIT));
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable(this.getType().getTranslationKey());
    }

    @Override
    public boolean canRefreshTrades() {
        return false;
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        Brain<VillagerEntity> brain = this.createBrainProfile().deserialize(dynamic);
        this.initBrain(brain);
        return brain;
    }

    public void reinitializeBrain(ServerWorld world) {
        Brain<VillagerEntity> brain = this.getBrain();
        brain.stopAllTasks(world, this);
        this.brain = brain.copy();
        this.initBrain(this.getBrain());
    }

    private void initBrain(Brain<VillagerEntity> brain) {
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        brain.setSchedule(Schedule.VILLAGER_DEFAULT);

        brain.setTaskList(Activity.CORE, VillagerTaskListProvider.createCoreTasks(villagerProfession, 0.35f));
        brain.setTaskList(Activity.MEET, VillagerTaskListProvider.createMeetTasks(villagerProfession, 0.35f), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleState.VALUE_PRESENT)));
        brain.setTaskList(Activity.REST, VillagerTaskListProvider.createRestTasks(villagerProfession, 0.35f));
        brain.setTaskList(Activity.IDLE, VillagerTaskListProvider.createIdleTasks(villagerProfession, 0.35f));
        brain.setTaskList(Activity.PANIC, VillagerTaskListProvider.createPanicTasks(villagerProfession, 0.35f));
        brain.setTaskList(Activity.PRE_RAID, VillagerTaskListProvider.createPreRaidTasks(villagerProfession, 0.35f));
        brain.setTaskList(Activity.RAID, VillagerTaskListProvider.createRaidTasks(villagerProfession, 0.35f));
        brain.setTaskList(Activity.HIDE, VillagerTaskListProvider.createHideTasks(villagerProfession, 0.35f));
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.refreshActivities(this.getWorld().getTimeOfDay(), this.getWorld().getTime());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        answer();

        if(!getWorld().isClient) {
            MutableText name = Text.literal("<").append(getName()).append("> ");
            var messages = List.of(
                    Text.literal("We were given these comfy §esuits by... §f§l*cough*§f... But why?"),
                    Text.literal("What are we doing here...?"),
                    Text.literal("Should I be scared?")
            );

            player.sendMessage(name.append(messages.get(getRandom().nextInt(messages.size()))));
        }
        return ActionResult.SUCCESS;
    }

    private void answer() {
        this.setHeadRollingTimeLeft(40);
        if (!this.getWorld().isClient()) {
            if(getRandom().nextDouble() <= 0.5) {
                this.playSound(SoundEvents.ENTITY_VILLAGER_NO);
            } else {
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES);
            }
        }
    }

}