package gcmod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WallWirelessTorchBlock extends WirelessTorchBlock
{
    public static final MapCodec<WallWirelessTorchBlock> CODEC = simpleCodec( WallWirelessTorchBlock::new );
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public WallWirelessTorchBlock( Properties settings )
    {
        super( settings );
        this.registerDefaultState( this.defaultBlockState().setValue( FACING, Direction.NORTH ).setValue( WirelessTorchBlock.LIT, Boolean.FALSE ) );
    }

    @Override
    protected void createBlockStateDefinition( StateDefinition.Builder<Block, BlockState> builder )
    {
        super.createBlockStateDefinition( builder );
        builder.add( FACING );
    }

    @Override
    protected MapCodec<? extends BaseTorchBlock> codec()
    {
        return CODEC;
    }

    @Override
    Direction getFacing( BlockState state )
    {
        return state.getValue( FACING );
    }

    @Override
    protected VoxelShape getShape( BlockState state, BlockGetter world, BlockPos pos, CollisionContext context )
    {
        return WallTorchBlock.getShape( state );
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockPlaceContext ctx )
    {
        BlockState blockState = Blocks.WALL_TORCH.getStateForPlacement( ctx );
        return blockState == null ? null : this.defaultBlockState().setValue( FACING, blockState.getValue( FACING ) );
    }

    //
    // abstract torch stuff


    @Override
    protected BlockState updateShape( BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random )
    {
        return direction == getFacing( state ).getOpposite() && !this.canSurvive( state, world, pos )
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape( state, world, tickView, pos, direction, neighborPos, neighborState, random );
    }

    @Override
    protected boolean canSurvive( BlockState state, LevelReader world, BlockPos pos )
    {
        final Direction facing = getFacing( state );
        return world.getBlockState( pos.relative( facing.getOpposite() ) ).isFaceSturdy( world, pos, facing );
    }
}
