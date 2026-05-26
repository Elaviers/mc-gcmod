package gcmod.block;

import gcmod.GCMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PooBlock extends Block
{
    public PooBlock( Properties settings )
    {
        super( settings );
    }

    @Override
    protected void randomTick( BlockState state, ServerLevel world, BlockPos pos, RandomSource random )
    {
        BlockPos abovePos = pos.above();
        BlockPos belowPos = pos.below();
        if ( world.getMaxLocalRawBrightness( pos ) < 10
                && world.getBlockState( abovePos ).getBlock() instanceof RopeBlock
                && world.getBlockState( abovePos ).getValue( RopeBlock.WET )
                && !world.getBlockState( belowPos ).isRedstoneConductor( world, belowPos )
        )
        {
            world.setBlockAndUpdate( pos, GCMod.FERMENTED_POO_BLOCK.defaultBlockState() );
            return;
        }

        for ( Direction direction : Direction.Plane.HORIZONTAL )
        {
            if ( world.getBlockState( belowPos.relative( direction ) ).getFluidState().is( FluidTags.WATER ) )
            {
                if ( world.getMaxLocalRawBrightness( pos ) < 5 )
                    if ( world.getRandom().nextInt( 5 ) == 0 )
                        world.setBlockAndUpdate( pos, GCMod.FERMENTED_POO_BLOCK.defaultBlockState() );

                break;
            }
        }
    }
}
