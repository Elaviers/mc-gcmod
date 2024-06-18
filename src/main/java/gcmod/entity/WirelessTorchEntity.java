package gcmod.entity;

import gcmod.GCMod;
import gcmod.block.WirelessTorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;

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

    public NbtList getTorchNetwork()
    {
        NbtList list = new NbtList();
        for ( BlockPos pos : linkedPositions )
            list.add( NbtHelper.fromBlockPos( pos ) );

        return list;
    }

    public void setTorchNetwork( NbtList positions )
    {
        if ( positions.getType() == NbtElement.END_TYPE )
            return;;

        assert positions.getType() == NbtElement.INT_ARRAY_TYPE;

        this.linkedPositions.clear();
        this.linkedPositions.ensureCapacity( positions.size() );
        for ( int i = 0; i < positions.size(); ++i )
        {
            int[] pos = positions.getIntArray( i );
            if ( pos.length == 3 ) // should always be true unless people are screwing around
                this.linkedPositions.add( new BlockPos( pos[0], pos[1], pos[2] ) );
        }
    }

    @Override
    protected void readNbt( NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup )
    {
        super.readNbt( nbt, registryLookup );
        this.side = Direction.byId( nbt.getByte( "Side" ) );
        this.setTorchNetwork( nbt.getList( "Torches", NbtElement.INT_ARRAY_TYPE ) );
    }

    @Override
    protected void writeNbt( NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup )
    {
        super.writeNbt( nbt, registryLookup );
        nbt.putByte( "Side", (byte) side.getId() );
        if ( !linkedPositions.isEmpty() )
            nbt.put( "Torches", this.getTorchNetwork() );
    }

    public void updateNetworkState( World world )
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
        this.removeInvalidPositions( getWorld() );

        for ( int i = 0; i < this.linkedPositions.size(); ++i )
        {
            WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( this.linkedPositions.get( i ) );
            te.linkedPositions = this.linkedPositions;
        }
    }

    private void removeInvalidPositions( World world )
    {
        final int initialSize = this.linkedPositions.size();

        for ( int i = 0; i < this.linkedPositions.size(); )
        {
            if ( !(world.getBlockState( this.linkedPositions.get( i ) ).getBlock() instanceof WirelessTorchBlock) )
                this.linkedPositions.remove( i );
            else i++;
        }

        if ( initialSize != this.linkedPositions.size() )
            this.markDirty();
    }

    private void setNetworkState( World world, boolean state )
    {
        if ( linkedPositions == null )
            return;

        for ( BlockPos pos : linkedPositions )
        {
            WirelessTorchEntity td = setIndividualState( world, pos, state );
            td.locked = state && !positionIsPowered( world, pos );
        }
    }

    private static boolean positionIsPowered( World world, BlockPos pos )
    {
        final WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( pos );
        final Direction dir = te.side.getOpposite();
        return world.isEmittingRedstonePower( pos.offset( dir ), dir );
    }

    private static WirelessTorchEntity setIndividualState( World world, BlockPos pos, boolean state )
    {
        WirelessTorchEntity td = (WirelessTorchEntity) world.getBlockEntity( pos );

        td.changingState = true;
        world.setBlockState( pos, world.getBlockState( pos ).with( WirelessTorchBlock.LIT, state ) );
        td.changingState = false;

        return td;
    }
}
