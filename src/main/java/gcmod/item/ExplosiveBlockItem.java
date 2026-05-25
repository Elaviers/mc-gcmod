package gcmod.item;

import gcmod.ExplosiveBlockComponent;
import gcmod.GCMod;
import gcmod.block.ExplosiveBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ExplosiveBlockItem extends BlockItem
{
    public ExplosiveBlockItem( Block block, Properties settings )
    {
        super( block, settings );
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        ItemStack stack = super.getDefaultInstance();

        if ( getBlock() == GCMod.AIRSTRIKE_TNT )
        {
            stack.set( GCMod.DATA_EXPLOSIVE_INFO, new ExplosiveBlockComponent( 0, 2, Blocks.AIR.defaultBlockState(), 4, 8, 50 ) );
        }
        else if ( getBlock() == GCMod.BLAST_TNT )
        {
            stack.set( GCMod.DATA_EXPLOSIVE_INFO, new ExplosiveBlockComponent( 0, 4, Blocks.AIR.defaultBlockState(), 0, 0, 0 ) );
        }
        else if ( getBlock() == GCMod.CONSTRUCTIVE_TNT )
        {
            stack.set( GCMod.DATA_EXPLOSIVE_INFO, new ExplosiveBlockComponent( 0, 4, Blocks.STONE.defaultBlockState(), 0, 0, 0 ) );
        }
        else if ( getBlock() == GCMod.DIG_TNT )
        {
            stack.set( GCMod.DATA_EXPLOSIVE_INFO, new ExplosiveBlockComponent( 0, 3, Blocks.AIR.defaultBlockState(), 0, 0, 0 ) );
        }

        return stack;
    }
}
