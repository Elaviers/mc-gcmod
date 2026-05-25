package gcmod.block;

import gcmod.GCMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RopeBlock extends Block
{
    public static final BooleanProperty END = BooleanProperty.create( "end" );
    public static final BooleanProperty WET = BooleanProperty.create( "wet" );

    private static final VoxelShape SHAPE = Block.box( 7, 0, 7, 9, 16, 9 );

    public RopeBlock( Properties settings )
    {
        super( settings );

        this.registerDefaultState( this.defaultBlockState().setValue( END, false ).setValue( WET, false ) );
    }

    @Override
    protected void createBlockStateDefinition( StateDefinition.Builder<Block, BlockState> builder )
    {
        builder.add( END, WET );
    }

    @Override
    protected VoxelShape getCollisionShape( BlockState state, BlockGetter world, BlockPos pos, CollisionContext context )
    {
        return SHAPE;
    }

    @Override
    protected VoxelShape getShape( BlockState state, BlockGetter world, BlockPos pos, CollisionContext context )
    {
        return SHAPE;
    }

    @Override
    protected boolean isCollisionShapeFullBlock( BlockState state, BlockGetter world, BlockPos pos )
    {
        return false;
    }

    @Override
    protected boolean propagatesSkylightDown( BlockState state )
    {
        return true;
    }

    @Override
    protected boolean isPathfindable( BlockState state, PathComputationType type )
    {
        return false;
    }

    @Override
    protected boolean canSurvive( BlockState state, LevelReader world, BlockPos pos )
    {
        final BlockPos posAbove = pos.above();
        final BlockState stateAbove = world.getBlockState( posAbove );

        return stateAbove.getBlock() == GCMod.ROPE || stateAbove.isFaceSturdy( world, posAbove, Direction.DOWN, SupportType.CENTER );
    }

    @Override
    protected InteractionResult useWithoutItem( BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit )
    {
        return super.useWithoutItem( state, world, pos, player, hit );
    }

    @Override
    protected InteractionResult useItemOn( ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
    {
        if ( stack.is( GCMod.ROPE_ITEM ) )
        {
            BlockPos posBelow = pos.below();
            while ( world.getBlockState( posBelow ).getBlock() == GCMod.ROPE )
                posBelow = posBelow.below();

            if ( world.getBlockState( posBelow ).isAir() )
            {
                if ( !world.isClientSide() )
                {
                    if ( !player.isCreative() )
                        stack.shrink( 1 );

                    world.setBlockAndUpdate( posBelow, GCMod.ROPE.defaultBlockState() );
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.useItemOn( stack, state, world, pos, player, hand, hit );
    }

    private void updateState( Level world, BlockPos pos, BlockState state )
    {
        BlockPos testPos = pos.above();
        final boolean wet =
                (world.getBlockState( testPos ).getBlock() == this && world.getBlockState( testPos ).getValue( WET ))
                        || world.getBlockState( testPos.relative( Direction.UP ) ).getBlock() == Blocks.WATER;

        testPos = pos.below();
        final boolean end = world.getBlockState( testPos ).getBlock() != this && world.getBlockState( testPos ).isFaceSturdy( world, testPos, Direction.UP, SupportType.RIGID );

        if ( state.getValue( WET ) != wet || state.getValue( END ) != end )
            world.setBlockAndUpdate( pos, this.defaultBlockState().setValue( END, end ).setValue( WET, wet ) );
    }

    @Override
    protected void onPlace( BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify )
    {
        if ( !world.isClientSide() && stabilityTest( world, pos, state ) )
            updateState( world, pos, state );
    }

    @Override
    protected void randomTick( BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource )
    {
        updateState( serverLevel, blockPos, blockState );
    }

    @Override
    protected void neighborChanged( BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify )
    {
        if ( !world.isClientSide() && stabilityTest( world, pos, state ) )
            updateState( world, pos, state );
    }

    @Override
    protected BlockState updateShape( BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource )
    {
        if ( !levelReader.isClientSide() && !canSurvive( blockState, levelReader, blockPos ) )
            scheduledTickAccess.scheduleTick( blockPos, this, 0 );

        return super.updateShape( blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource );
    }

    @Override
    protected void tick( BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource )
    {
        stabilityTest( serverLevel, blockPos, serverLevel.getBlockState( blockPos ) );
    }

    @Override
    protected void affectNeighborsAfterRemoval( BlockState state, ServerLevel world, BlockPos pos, boolean moved )
    {
        if ( !world.isClientSide() )
        {
            final BlockPos posBelow = pos.below();
            final BlockState stateBelow = world.getBlockState( posBelow );
            final BlockPos posBelowHeld = posBelow.below();

            if ( stateBelow.isFaceSturdy( world, pos, Direction.UP, SupportType.RIGID ) && world.getBlockState( posBelowHeld ).isAir() )
                FallingBlockEntity.fall( world, posBelow, stateBelow );
        }

        super.affectNeighborsAfterRemoval( state, world, pos, moved );
    }

    private boolean stabilityTest( Level world, BlockPos pos, BlockState state )
    {
        if ( !canSurvive( state, world, pos ) )
        {
            fall( world, pos, state );
            return false;
        }

        return true;
    }

    private void fall( Level world, BlockPos pos, BlockState state )
    {
        final BlockPos posBelow = pos.below();
        final BlockState stateBelow = world.getBlockState( posBelow );

        FallingBlockEntity.fall( world, pos, state );

        if ( stateBelow.isFaceSturdy( world, posBelow, Direction.UP, SupportType.RIGID ) )
        {
            BlockPos belowHeldPos = posBelow.below();
            BlockState belowHeld = world.getBlockState( belowHeldPos );

            if ( belowHeld.isAir() && belowHeld.getBlock() != this )
            {
                world.removeBlock( pos, true );
                return;
            }

            FallingBlockEntity.fall( world, posBelow, stateBelow );
        }
    }
}
