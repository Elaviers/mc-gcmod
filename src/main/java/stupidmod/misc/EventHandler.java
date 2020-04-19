package stupidmod.misc;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import stupidmod.StupidMod;
import stupidmod.StupidModSounds;
import stupidmod.block.ExplosiveBlock;
import stupidmod.entity.PooEntity;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    static void explosionDetonate(ExplosionEvent.Detonate event) {
        World world = event.getWorld();
        List<BlockPos> affectedBlocks = event.getAffectedBlocks();

        for (BlockPos p : affectedBlocks) {
            Block b = world.getBlockState(p).getBlock();

            if (b instanceof ExplosiveBlock) {
                ((ExplosiveBlock) b).explode(world, p, world.getBlockState(p));
            }
        }
    }

    static class PlayerState
    {
        PlayerEntity player;
        boolean isSneaking;

        long counterStartTime;
        int counter;

        long timeOfLastPooing;

        public PlayerState(PlayerEntity player, long time)
        {
            this.player = player;
            this.isSneaking = false;
            this.counterStartTime = 0;
            this.counter = 0;
            this.timeOfLastPooing = time;
        }

        public void onTick(long time)
        {
            if (player.isShiftKeyDown()) {
                if (!isSneaking) {
                    isSneaking = true;

                    if (counter == 0) {
                        counterStartTime = time;
                    }

                    counter++;

                    if (counter == 8) {
                        counter = 0;

                        if (time - timeOfLastPooing > 300 && time - counterStartTime <= 40) {
                            this.player.world.addEntity(new PooEntity(this.player.world, this.player.getPosX(), this.player.getPosY(), this.player.getPosZ()));
                            this.player.world.playSound(null, this.player.getPosX(), this.player.getPosY(), this.player.getPosZ(), StupidModSounds.FART, SoundCategory.MASTER, 1.f, 1.f);
                            timeOfLastPooing = time;
                        }
                    }
                }
            }
            else
                isSneaking = false;
        }
    }

    static ArrayList<PlayerState> states = new ArrayList<PlayerState>();

    @SubscribeEvent
    static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            PlayerState ps = null;

            for (PlayerState es : states)
            {
                if (es.player == event.player)
                {
                    ps = es;
                    break;
                }
            }

            World world = event.player.getEntityWorld();
            long t = world.getGameTime();

            if (ps == null)
            {
                states.add(new PlayerState(event.player, t));
                return;
            }

            ps.onTick(t);
        }
    }
}
