package stupidmod.misc;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import stupidmod.StupidMod;
import stupidmod.block.ExplosiveBlock;

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
}
