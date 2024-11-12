package gcmod.block;

import com.mojang.serialization.MapCodec;
import gcmod.GCMod;
import gcmod.entity.CentrifugeEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class CentrifugeBlock extends BlockWithEntity
{
    public static final MapCodec<CentrifugeBlock> CODEC = createCodec( CentrifugeBlock::new );
    private static VoxelShape BASE = createCuboidShape( 2, 0, 2, 14, 2, 14 );
    private static VoxelShape BASE_2 = createCuboidShape( 4, 2, 4, 12, 3, 12 );
    private static VoxelShape ROD = createCuboidShape( 7, 0, 7, 9, 13, 9 );
    private static VoxelShape TOP = createCuboidShape( 0, 13, 0, 16, 16, 16 );
    private static VoxelShape SHAPE = VoxelShapes.union( VoxelShapes.union( VoxelShapes.union( BASE, BASE_2 ), ROD ), TOP );

    public CentrifugeBlock( Settings settings )
    {
        super( settings );
    }

    @Override
    protected VoxelShape getOutlineShape( BlockState state, BlockView world, BlockPos pos, ShapeContext context )
    {
        return SHAPE;
    }

    @Override
    protected boolean isTransparent( BlockState state )
    {
        return true;
    }

    @Override
    protected BlockRenderType getRenderType( BlockState state )
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected ActionResult onUse( BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit )
    {
        if ( world.isClient )
            return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity( pos );
        if ( blockEntity instanceof CentrifugeEntity centrifuge )
            player.openHandledScreen( centrifuge );

        return ActionResult.CONSUME;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec()
    {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity( BlockPos pos, BlockState state )
    {
        return new CentrifugeEntity( pos, state );
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker( World world, BlockState state, BlockEntityType<T> type )
    {
        return validateTicker( type, GCMod.CENTRIFUGE_ENTITY, CentrifugeEntity::tick );
    }

    @Override
    protected void onStateReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean moved )
    {
        ItemScatterer.onStateReplaced( state, newState, world, pos );
        super.onStateReplaced( state, world, pos, newState, moved );
    }

    @Override
    protected boolean canPathfindThrough( BlockState state, NavigationType type )
    {
        return false;
    }

    //
    //

    @Override
    protected void neighborUpdate( BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify )
    {
        super.neighborUpdate( state, world, pos, sourceBlock, wireOrientation, notify );
        if ( world.getBlockEntity( pos ) instanceof CentrifugeEntity centrifuge )
            centrifuge.setPowered( world.isReceivingRedstonePower( pos ) );
    }

    @Override
    protected void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify )
    {
        super.onBlockAdded( state, world, pos, oldState, notify );
        if ( world.getBlockEntity( pos ) instanceof CentrifugeEntity centrifuge )
            centrifuge.setPowered( world.isReceivingRedstonePower( pos ) );
    }
}
