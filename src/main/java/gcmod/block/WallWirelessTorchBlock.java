package gcmod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class WallWirelessTorchBlock extends WirelessTorchBlock
{
    public static final MapCodec<WallWirelessTorchBlock> CODEC = createCodec( WallWirelessTorchBlock::new );
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public WallWirelessTorchBlock( Settings settings )
    {
        super( settings );
        this.setDefaultState( this.getDefaultState().with( FACING, Direction.NORTH ).with( WirelessTorchBlock.LIT, Boolean.FALSE ) );
    }

    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder )
    {
        super.appendProperties( builder );
        builder.add( FACING );
    }

    @Override
    protected MapCodec<? extends AbstractTorchBlock> getCodec()
    {
        return CODEC;
    }

    @Override
    Direction getFacing( BlockState state )
    {
        return state.get( FACING );
    }

    @Override
    protected VoxelShape getOutlineShape( BlockState state, BlockView world, BlockPos pos, ShapeContext context )
    {
        return WallTorchBlock.getBoundingShape( state );
    }


    @Nullable
    @Override
    public BlockState getPlacementState( ItemPlacementContext ctx )
    {
        BlockState blockState = Blocks.WALL_TORCH.getPlacementState( ctx );
        return blockState == null ? null : this.getDefaultState().with( FACING, blockState.get( FACING ) );
    }

    //
    // abstract torch stuff


    @Override
    protected BlockState getStateForNeighborUpdate( BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random )
    {
        return direction == getFacing( state ).getOpposite() && !this.canPlaceAt( state, world, pos )
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate( state, world, tickView, pos, direction, neighborPos, neighborState, random );
    }

    @Override
    protected boolean canPlaceAt( BlockState state, WorldView world, BlockPos pos )
    {
        final Direction facing = getFacing( state );
        return world.getBlockState( pos.offset( facing.getOpposite() ) ).isSideSolidFullSquare( world, pos, facing );
    }
}
