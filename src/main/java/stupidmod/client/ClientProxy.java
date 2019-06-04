package stupidmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import stupidmod.Proxy;
import stupidmod.SoundRegister;
import stupidmod.StupidMod;
import stupidmod.entity.tile.TileEntityCentrifuge;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientProxy extends Proxy {
    @OnlyIn(Dist.CLIENT)
    private Map<BlockPos, SoundCentrifuge> playingSounds = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    static void worldUnloaded(WorldEvent.Unload event) {
        ((ClientProxy)StupidMod.proxy).playingSounds.clear();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clUpdateCentrifugeSound(TileEntityCentrifuge te) {
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clRemoveCentrifugeSound(BlockPos pos) {
        playingSounds.remove(pos);
    }

}
