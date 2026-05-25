package gcmod.entity;

import gcmod.GCMod;
import gcmod.TorchNetworkComponent;
import gcmod.block.WirelessTorchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WirelessTorchEntity extends BlockEntity
{
    public ArrayList<BlockPos> linkedPositions;

    public boolean locked = false;
    public boolean changingState = false;

    public Direction side;

    public WirelessTorchEntity( BlockPos pos, BlockState state )
    {
        super( GCMod.WIRELESS_TORCH_ENTITY, pos, state );
        this.linkedPositions = new ArrayList<>();
        this.side = Direction.UP;
    }

    public TorchNetworkComponent getTorchNetwork()
    {
        return new TorchNetworkComponent( linkedPositions );
    }

    public void setTorchNetwork( List<BlockPos> positions )
    {
        this.linkedPositions = new ArrayList<>( positions );
    }

    @Override
    protected void loadAdditional( ValueInput view )
    {
        super.loadAdditional( view );
        this.side = Direction.from3DDataValue( view.getByteOr( "Side", (byte)0 ) );
        this.linkedPositions.clear();
        view.list( "Torches", BlockPos.CODEC ).ifPresent( torches -> torches.forEach( pos -> this.linkedPositions.add( pos ) ) );
    }

    @Override
    protected void saveAdditional( ValueOutput view )
    {
        super.saveAdditional( view );
        view.putByte( "Side", (byte)side.get3DDataValue() );
        if ( !linkedPositions.isEmpty() )
        {
            ValueOutput.TypedOutputList<BlockPos> torches = view.list( "Torches", BlockPos.CODEC );
            for ( BlockPos position : this.linkedPositions )
                torches.add( position );
        }
    }

    public void updateNetworkState( Level world )
    {
        boolean netPowered = false;

        this.removeInvalidPositions( world );

        for ( BlockPos linkedPosition : this.linkedPositions )
        {
            if ( positionIsPowered( world, linkedPosition ) )
            {
                netPowered = true;
                break;
            }
        }

        setNetworkState( world, netPowered );
    }

    public void removeInvalidPositionsFromNetwork()
    {
        this.removeInvalidPositions( getLevel() );

        for ( int i = 0; i < this.linkedPositions.size(); ++i )
        {
            WirelessTorchEntity te = (WirelessTorchEntity)level.getBlockEntity( this.linkedPositions.get( i ) );
            te.linkedPositions = this.linkedPositions;
        }
    }

    private void removeInvalidPositions( Level world )
    {
        final int initialSize = this.linkedPositions.size();

        for ( int i = 0; i < this.linkedPositions.size(); )
        {
            if ( !(world.getBlockState( this.linkedPositions.get( i ) ).getBlock() instanceof WirelessTorchBlock) )
                this.linkedPositions.remove( i );
            else i++;
        }

        if ( initialSize != this.linkedPositions.size() )
            this.setChanged();
    }

    private void setNetworkState( Level world, boolean state )
    {
        if ( linkedPositions == null )
            return;

        for ( BlockPos pos : linkedPositions )
        {
            WirelessTorchEntity td = setIndividualState( world, pos, state );
            td.locked = state && !positionIsPowered( world, pos );
        }
    }

    private static boolean positionIsPowered( Level world, BlockPos pos )
    {
        final WirelessTorchEntity te = (WirelessTorchEntity)world.getBlockEntity( pos );
        final Direction dir = te.side.getOpposite();
        return world.hasSignal( pos.relative( dir ), dir );
    }

    private static WirelessTorchEntity setIndividualState( Level world, BlockPos pos, boolean state )
    {
        WirelessTorchEntity td = (WirelessTorchEntity)world.getBlockEntity( pos );

        td.changingState = true;
        world.setBlockAndUpdate( pos, world.getBlockState( pos ).setValue( WirelessTorchBlock.LIT, state ) );
        td.changingState = false;

        return td;
    }

    @Override
    public void preRemoveSideEffects( BlockPos pos, BlockState state )
    {
        super.preRemoveSideEffects( pos, state );

        this.linkedPositions.remove( pos );
        this.setChanged();
        this.updateNetworkState( getLevel() );
    }
}
