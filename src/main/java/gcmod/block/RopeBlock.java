package gcmod.block;

import gcmod.GCMod;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class RopeBlock extends Block
{
    public static final BooleanProperty END = BooleanProperty.of( "end" );
    public static final BooleanProperty WET = BooleanProperty.of( "wet" );

    private static final VoxelShape SHAPE = Block.createCuboidShape( 7, 0, 7, 9, 16, 9 );

    public RopeBlock( Settings settings )
    {
        super( settings );

        this.setDefaultState( this.getDefaultState().with( END, false ).with( WET, false ) );
    }

    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder )
    {
        builder.add( END, WET );
    }

    @Override
    protected VoxelShape getCollisionShape( BlockState state, BlockView world, BlockPos pos, ShapeContext context )
    {
        return SHAPE;
    }

    @Override
    protected VoxelShape getOutlineShape( BlockState state, BlockView world, BlockPos pos, ShapeContext context )
    {
        return SHAPE;
    }

    @Override
    protected boolean isShapeFullCube( BlockState state, BlockView world, BlockPos pos )
    {
        return false;
    }

    @Override
    protected boolean isTransparent( BlockState state, BlockView world, BlockPos pos )
    {
        return true;
    }

    @Override
    protected boolean canPathfindThrough( BlockState state, NavigationType type )
    {
        return false;
    }

    @Override
    protected boolean canPlaceAt( BlockState state, WorldView world, BlockPos pos )
    {
        final BlockPos posAbove = pos.up();
        final BlockState stateAbove = world.getBlockState( posAbove );

        return stateAbove.getBlock() == GCMod.ROPE || stateAbove.isSideSolid( world, posAbove, Direction.DOWN, SideShapeType.CENTER );
    }

    @Override
    protected ItemActionResult onUseWithItem( ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit )
    {
        if ( stack.isOf( GCMod.ROPE_ITEM ) )
        {
            if ( !world.isClient )
            {
                BlockPos posBelow = pos.down();
                while ( world.getBlockState( posBelow ).getBlock() == GCMod.ROPE )
                    posBelow = posBelow.down();

                if ( world.getBlockState( posBelow ).isAir() )
                {
                    if ( !player.isCreative() )
                        stack.decrement( 1 );

                    world.setBlockState( posBelow, GCMod.ROPE.getDefaultState() );

                    return ItemActionResult.SUCCESS;
                }
            }
        }

        return super.onUseWithItem( stack, state, world, pos, player, hand, hit );
    }

    private void updateState( World world, BlockPos pos, BlockState state )
    {
        BlockPos testPos = pos.up();
        final boolean wet =
                (world.getBlockState( testPos ).getBlock() == this && world.getBlockState( testPos ).get( WET ))
                        || world.getBlockState( testPos.offset( Direction.UP ) ).getBlock() == Blocks.WATER;

        testPos = pos.down();
        final boolean end = world.getBlockState( testPos ).getBlock() != this && world.getBlockState( testPos ).isSideSolid( world, testPos, Direction.UP, SideShapeType.RIGID );

        if ( state.get( WET ) != wet || state.get( END ) != end )
            world.setBlockState( pos, this.getDefaultState().with( END, end ).with( WET, wet ) );
    }

    @Override
    protected void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify )
    {
        if ( !world.isClient && stabilityTest( world, pos, state ) )
            updateState( world, pos, state );
    }

    @Override
    protected void neighborUpdate( BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify )
    {
        if ( !world.isClient && stabilityTest( world, pos, state ) )
            updateState( world, pos, state );
    }

    @Override
    protected void onStateReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean moved )
    {
        if ( !world.isClient && newState.getBlock() != this )
        {
            final BlockPos posBelow = pos.down();
            final BlockState stateBelow = world.getBlockState( posBelow );
            final BlockPos posBelowHeld = posBelow.down();

            if ( stateBelow.isSideSolid( world, pos, Direction.UP, SideShapeType.RIGID ) && world.getBlockState( posBelowHeld ).isAir() )
                FallingBlockEntity.spawnFromBlock( world, posBelow, stateBelow );
        }

        super.onStateReplaced( state, world, pos, newState, moved );
    }

    private boolean stabilityTest( World world, BlockPos pos, BlockState state )
    {
        if ( !canPlaceAt( state, world, pos ) )
        {
            fall( world, pos, state );
            return false;
        }

        return true;
    }

    private void fall( World world, BlockPos pos, BlockState state )
    {
        final BlockPos posBelow = pos.down();
        final BlockState stateBelow = world.getBlockState( posBelow );

        FallingBlockEntity.spawnFromBlock( world, pos, state );

        if ( stateBelow.isSideSolid( world, posBelow, Direction.UP, SideShapeType.RIGID ) )
        {
            BlockPos belowHeldPos = posBelow.down();
            BlockState belowHeld = world.getBlockState( belowHeldPos );

            if ( belowHeld.isAir() && belowHeld.getBlock() != this )
            {
                world.removeBlock( pos, true );
                return;
            }

            FallingBlockEntity.spawnFromBlock( world, posBelow, stateBelow );
        }
    }
}
