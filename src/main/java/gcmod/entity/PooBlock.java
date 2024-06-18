package gcmod.entity;

import gcmod.GCMod;
import gcmod.block.RopeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class PooBlock extends Block
{
    public PooBlock( Settings settings )
    {
        super( settings );
    }

    @Override
    protected void randomTick( BlockState state, ServerWorld world, BlockPos pos, Random random )
    {
        BlockPos abovePos = pos.up();
        BlockPos belowPos = pos.down();
        if ( world.getLightLevel( pos ) < 10
                && world.getBlockState( abovePos ).getBlock() instanceof RopeBlock
                && world.getBlockState( abovePos ).get( RopeBlock.WET )
                && !world.getBlockState( belowPos ).isSolidBlock( world, belowPos )
        )
        {
            world.setBlockState( pos, GCMod.FERMENTED_POO_BLOCK.getDefaultState() );
            return;
        }

        for ( Direction direction : Direction.Type.HORIZONTAL )
        {
            if ( world.getBlockState( belowPos.offset( direction ) ).getFluidState().isIn( FluidTags.WATER ) )
            {
                if ( world.getLightLevel( pos ) < 5 )
                    if ( world.random.nextInt( 5 ) == 0 )
                        world.setBlockState( pos, GCMod.FERMENTED_POO_BLOCK.getDefaultState() );

                break;
            }
        }
    }
}
