package gcmod;

import gcmod.entity.CentrifugeEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;

public class CentrifugeSound extends AbstractSoundInstance implements TickableSoundInstance
{
    final CentrifugeEntity ent;
    boolean done;

    public CentrifugeSound( CentrifugeEntity ent )
    {
        super( GCMod.CENTRIFUGE_SOUND, SoundCategory.BLOCKS, SoundInstance.createRandom() );
        this.ent = ent;

        this.x = ent.getPos().getX() + .5f;
        this.y = ent.getPos().getY() + .5f;
        this.z = ent.getPos().getZ() + .5f;
        this.volume = 0.01f;

        this.done = false;
        this.repeat = true;
    }

    @Override
    public boolean isDone()
    {
        return this.done;
    }

    @Override
    public void tick()
    {
        if ( ent == null || ent.isRemoved() )
        {
            this.done = true;
            return;
        }

        this.volume = ent.spinRate;
        this.pitch = ent.spinRate;
    }

    public static void updateSoundForEntity( CentrifugeEntity ent )
    {
        if ( !ent.startedAudio && ent.isPowered )
        {
            MinecraftClient.getInstance().getSoundManager().play( new CentrifugeSound( ent ) );
            ent.startedAudio = true;
        }
    }
}
