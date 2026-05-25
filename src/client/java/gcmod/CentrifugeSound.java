package gcmod;

import gcmod.entity.CentrifugeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundSource;

public class CentrifugeSound extends AbstractSoundInstance implements TickableSoundInstance
{
    final CentrifugeEntity ent;
    boolean done;

    public CentrifugeSound( CentrifugeEntity ent )
    {
        super( GCMod.CENTRIFUGE_SOUND, SoundSource.BLOCKS, SoundInstance.createUnseededRandom() );
        this.ent = ent;

        this.x = ent.getBlockPos().getX() + .5f;
        this.y = ent.getBlockPos().getY() + .5f;
        this.z = ent.getBlockPos().getZ() + .5f;
        this.volume = 0.01f;

        this.done = false;
        this.looping = true;
    }

    @Override
    public boolean isStopped()
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
            Minecraft.getInstance().getSoundManager().play( new CentrifugeSound( ent ) );
            ent.startedAudio = true;
        }
    }
}
