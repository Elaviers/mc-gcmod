package gcmod.block;

import com.mojang.serialization.MapCodec;
import gcmod.GCMod;
import gcmod.entity.CentrifugeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CentrifugeBlock extends BaseEntityBlock
{
    public static final MapCodec<CentrifugeBlock> CODEC = simpleCodec( CentrifugeBlock::new );
    private static VoxelShape BASE = box( 2, 0, 2, 14, 2, 14 );
    private static VoxelShape BASE_2 = box( 4, 2, 4, 12, 3, 12 );
    private static VoxelShape ROD = box( 7, 0, 7, 9, 13, 9 );
    private static VoxelShape TOP = box( 0, 13, 0, 16, 16, 16 );
    private static VoxelShape SHAPE = Shapes.or( Shapes.or( Shapes.or( BASE, BASE_2 ), ROD ), TOP );

    public CentrifugeBlock( Properties settings )
    {
        super( settings );
    }

    @Override
    protected VoxelShape getShape( BlockState state, BlockGetter world, BlockPos pos, CollisionContext context )
    {
        return SHAPE;
    }

    @Override
    protected boolean propagatesSkylightDown( BlockState state )
    {
        return true;
    }

    @Override
    protected RenderShape getRenderShape( BlockState state ) { return RenderShape.INVISIBLE; }

    @Override
    protected InteractionResult useWithoutItem( BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit )
    {
        if ( world.isClientSide() )
            return InteractionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity( pos );
        if ( blockEntity instanceof CentrifugeEntity centrifuge )
            player.openMenu( centrifuge );

        return InteractionResult.CONSUME;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity( BlockPos pos, BlockState state )
    {
        return new CentrifugeEntity( pos, state );
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker( Level world, BlockState state, BlockEntityType<T> type )
    {
        return createTickerHelper( type, GCMod.CENTRIFUGE_ENTITY, CentrifugeEntity::tick );
    }

    @Override
    protected void affectNeighborsAfterRemoval( BlockState state, ServerLevel world, BlockPos pos, boolean moved )
    {
        Containers.updateNeighboursAfterDestroy( state, world, pos );
        super.affectNeighborsAfterRemoval( state, world, pos, moved );
    }

    @Override
    protected boolean isPathfindable( BlockState state, PathComputationType type )
    {
        return false;
    }

    //
    //

    @Override
    protected void neighborChanged( BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify )
    {
        super.neighborChanged( state, world, pos, sourceBlock, wireOrientation, notify );
        if ( world.getBlockEntity( pos ) instanceof CentrifugeEntity centrifuge )
            centrifuge.setPowered( world.hasNeighborSignal( pos ) );
    }

    @Override
    protected void onPlace( BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify )
    {
        super.onPlace( state, world, pos, oldState, notify );
        if ( world.getBlockEntity( pos ) instanceof CentrifugeEntity centrifuge )
            centrifuge.setPowered( world.hasNeighborSignal( pos ) );
    }
}
