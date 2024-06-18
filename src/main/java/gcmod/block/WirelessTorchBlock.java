package gcmod.block;

import com.mojang.serialization.MapCodec;
import gcmod.entity.WirelessTorchEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WirelessTorchBlock extends AbstractTorchBlock implements BlockEntityProvider
{
    private static final MapCodec<WirelessTorchBlock> CODEC = createCodec( WirelessTorchBlock::new );
    public static final BooleanProperty LIT = Properties.LIT;

    public WirelessTorchBlock( Settings settings )
    {
        super( settings );
        this.setDefaultState( this.getDefaultState().with( LIT, false ) );
    }

    @Override
    protected MapCodec<? extends AbstractTorchBlock> getCodec()
    {
        return CODEC;
    }

    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder )
    {
        builder.add( LIT );
    }

    @Override
    protected void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify )
    {
        for ( Direction direction : Direction.values() )
            world.updateNeighborsAlways( pos.offset( direction ), this );

        super.onBlockAdded( state, world, pos, oldState, notify );
    }

    @Override
    protected void onStateReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean moved )
    {
        WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( pos );
        if ( !(newState.getBlock() instanceof WirelessTorchBlock) )
        {
            te.linkedPositions.remove( pos );
            te.markDirty();

            te.updateNetworkState( world );
        }
        else
        {
            te.side = getFacing( newState );
        }

        if ( !moved )
            for ( Direction direction : Direction.values() )
                world.updateNeighborsAlways( pos.offset( direction ), this );

        super.onStateReplaced( state, world, pos, newState, moved );
    }

    Direction getFacing( BlockState state )
    {
        return Direction.UP;
    }

    @Override
    protected void neighborUpdate( BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify )
    {
        super.neighborUpdate( state, world, pos, sourceBlock, sourcePos, notify );

        Direction dir = this.getFacing( state ).getOpposite();
        final boolean shouldBeOn = world.isEmittingRedstonePower( pos.offset( dir ), dir );

        WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( pos );
        if ( !te.changingState && !te.locked && state.get( LIT ) != shouldBeOn )
            te.updateNetworkState( world );
    }

    @Override
    protected int getWeakRedstonePower( BlockState state, BlockView world, BlockPos pos, Direction direction )
    {
        return state.get( LIT ) && getFacing( state ) != direction ? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower( BlockState state, BlockView world, BlockPos pos, Direction direction )
    {
        return direction == getFacing( state ).getOpposite() ? state.getWeakRedstonePower( world, pos, direction ) : 0;
    }

    @Override
    protected boolean emitsRedstonePower( BlockState state )
    {
        return true;
    }

    @Override
    public void randomDisplayTick( BlockState state, World world, BlockPos pos, Random random )
    {
        if ( state.get( LIT ) )
        {
            double d = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double e = (double) pos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2;
            double f = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            world.addParticle( DustParticleEffect.DEFAULT, d, e, f, 0.0, 0.0, 0.0 );
        }
    }

    //
    //
    //

    @Nullable
    @Override
    public BlockEntity createBlockEntity( BlockPos pos, BlockState state )
    {
        WirelessTorchEntity te = new WirelessTorchEntity( pos, state );
        te.side = getFacing( state );
        return te;
    }
}
