package stupidmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import stupidmod.SoundRegister;
import stupidmod.StupidMod;
import stupidmod.entity.tile.TileEntityCentrifuge;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CentrifugeSoundManager {

    static Map<BlockPos, SoundCentrifuge> playingSounds = new HashMap<>();

    @SubscribeEvent
    public static void worldUnLoaded(WorldEvent.Unload event) {
        playingSounds.clear();
    }

    //Adds and plays a new centrifuge sound or binds the existing one
    public static  void updateCentrifugeSound(TileEntityCentrifuge te) {
        BlockPos pos = te.getPos();

        SoundCentrifuge sound = playingSounds.get(pos);
        if (sound != null) {
            sound.entity = te;
        }
        else {
            sound = new SoundCentrifuge(SoundRegister.soundCentrifuge, te);
            Minecraft.getInstance().getSoundHandler().play(sound);
            playingSounds.put(pos, sound);
        }
    }

    //Removes the sound at given position
    public static void removeCentrifugeSound(BlockPos pos) {
        playingSounds.remove(pos);
    }

}
