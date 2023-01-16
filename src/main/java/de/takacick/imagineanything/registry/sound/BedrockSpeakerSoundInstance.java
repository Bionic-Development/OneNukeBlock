package de.takacick.imagineanything.registry.sound;

import de.takacick.imagineanything.registry.ParticleRegistry;
import de.takacick.imagineanything.registry.block.entity.ImaginedGiantBedrockSpeakersBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(value = EnvType.CLIENT)
public class BedrockSpeakerSoundInstance
        extends MovingSoundInstance {

    protected final ImaginedGiantBedrockSpeakersBlockEntity blockEntity;

    public BedrockSpeakerSoundInstance(ImaginedGiantBedrockSpeakersBlockEntity entity) {
        super(ParticleRegistry.BEDROCK_SPEAKER_EVENT, SoundCategory.BLOCKS, SoundInstance.createRandom());
        this.blockEntity = entity;
        this.x = (float) entity.getPos().getX() + 0.5;
        this.y = (float) entity.getPos().getY() + 0.5;
        this.z = (float) entity.getPos().getZ() + 0.5;
        this.repeat = true;
        this.repeatDelay = -2;
        this.volume = 3.0f;
    }

    @Override
    public float getVolume() {
        return 20;
    }

    @Override
    public void tick() {
        if (this.blockEntity.isRemoved() || !this.blockEntity.isActivated()) {
            this.blockEntity.playedSound = false;
            this.setDone();
            return;
        }
        this.x = (float) blockEntity.getPos().getX() + 0.5;
        this.y = (float) blockEntity.getPos().getY() + 0.5;
        this.z = (float) blockEntity.getPos().getZ() + 0.5;

        this.pitch = 1f;
        this.volume = 1f;
    }

    private float getMinPitch() {
        return 1f;
    }

    private float getMaxPitch() {
        return 1f;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public boolean canPlay() {
        return this.blockEntity.isActivated();
    }
}