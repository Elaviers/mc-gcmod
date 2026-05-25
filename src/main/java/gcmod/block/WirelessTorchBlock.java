package gcmod.block;

import com.mojang.serialization.MapCodec;
import gcmod.entity.WirelessTorchEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

public class WirelessTorchBlock extends BaseTorchBlock implements EntityBlock
{
    private static final MapCodec<WirelessTorchBlock> CODEC = simpleCodec( WirelessTorchBlock::new );
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public WirelessTorchBlock( Properties settings )
    {
        super( settings );
        this.registerDefaultState( this.defaultBlockState().setValue( LIT, false ) );
    }

    @Override
    protected MapCodec<? extends BaseTorchBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition( StateDefinition.Builder<Block, BlockState> builder )
    {
        builder.add( LIT );
    }

    @Override
    protected void onPlace( BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify )
    {
        for ( Direction direction : Direction.values() )
            world.updateNeighborsAt( pos.relative( direction ), this, ExperimentalRedstoneUtils.initialOrientation(world, null, Direction.UP) );

        super.onPlace( state, world, pos, oldState, notify );
    }

    @Override
    protected void affectNeighborsAfterRemoval( BlockState state, ServerLevel world, BlockPos pos, boolean moved )
    {
        if ( !moved )
            for ( Direction direction : Direction.values() )
                world.updateNeighborsAt( pos.relative( direction ), this, ExperimentalRedstoneUtils.initialOrientation(world, null, Direction.UP) );

        super.affectNeighborsAfterRemoval( state, world, pos, moved );
    }

    Direction getFacing( BlockState state )
    {
        return Direction.UP;
    }

    @Override
    protected void neighborChanged( BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify )
    {
        super.neighborChanged( state, world, pos, sourceBlock, wireOrientation, notify );

        Direction dir = this.getFacing( state ).getOpposite();
        final boolean shouldBeOn = world.hasSignal( pos.relative( dir ), dir );

        WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( pos );
        if ( !te.changingState && !te.locked && state.getValue( LIT ) != shouldBeOn )
            te.updateNetworkState( world );
    }

    @Override
    protected int getSignal( BlockState state, BlockGetter world, BlockPos pos, Direction direction )
    {
        return state.getValue( LIT ) && getFacing( state ) != direction ? 15 : 0;
    }

    @Override
    protected int getDirectSignal( BlockState state, BlockGetter world, BlockPos pos, Direction direction )
    {
        return direction == getFacing( state ).getOpposite() ? state.getSignal( world, pos, direction ) : 0;
    }

    @Override
    protected boolean isSignalSource( BlockState state )
    {
        return true;
    }

    @Override
    public void animateTick( BlockState state, Level world, BlockPos pos, RandomSource random )
    {
        if ( state.getValue( LIT ) )
        {
            double d = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double e = (double) pos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2;
            double f = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            world.addParticle( DustParticleOptions.REDSTONE, d, e, f, 0.0, 0.0, 0.0 );
        }
    }

    //
    //
    //

    @Nullable
    @Override
    public BlockEntity newBlockEntity( BlockPos pos, BlockState state )
    {
        WirelessTorchEntity te = new WirelessTorchEntity( pos, state );
        te.side = getFacing( state );
        return te;
    }
}
