package stupidmod.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import stupidmod.StupidModEntities;
import stupidmod.block.WirelessTorchBlock;

import java.util.ArrayList;

public class WirelessTorchTileEntity extends TileEntity {
    public ArrayList<BlockPos> linkedPositions;
    
    public boolean locked = false;
    public boolean changingState = false;
    
    public Direction side;
    
    public WirelessTorchTileEntity() {
        super(StupidModEntities.TE_WIRELESS_TORCH);

        this.linkedPositions = new ArrayList<BlockPos>();
    }
    
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
         super.write(nbt);

        nbt.putInt("Side", side.getIndex());

        if (this.linkedPositions == null) return nbt;

        nbt.put("Torches", this.getTorchList());
        
        return nbt;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        side = Direction.byIndex(nbt.getInt("Side"));

        if (!nbt.contains("Torches")) return;

        this.setTorchList(nbt.getList("Torches", Constants.NBT.TAG_COMPOUND));
    }

    public ListNBT getTorchList()
    {
        ListNBT list = new ListNBT();
        
        for (int i = 0; i < linkedPositions.size(); i++) {
            CompoundNBT tag = new CompoundNBT();
            BlockPos pos = linkedPositions.get(i);
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            list.add(tag);
        }
        
        return list;
    }
    
    public void setTorchList(ListNBT positions)
    {
        this.linkedPositions.clear();

        for (int i = 0; i < positions.size(); ++i)
        {
            CompoundNBT nbt = positions.getCompound(i);
            this.linkedPositions.add(new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));
        }
    }
    
    public void removeInvalidPositionsFromNetwork() {
        this.removeInvalidPositions(world);
        
        for (int i = 0; i < this.linkedPositions.size(); ++i) {
            WirelessTorchTileEntity te = (WirelessTorchTileEntity)world.getTileEntity(this.linkedPositions.get(i));
            te.linkedPositions = this.linkedPositions;
        }
    }
    
    private void removeInvalidPositions(World world) {
        for (int i = 0; i < this.linkedPositions.size();) {
            if (!(world.getBlockState(this.linkedPositions.get(i)).getBlock() instanceof WirelessTorchBlock))
                this.linkedPositions.remove(i);
            else i++;
        }
    }
    
    boolean positionIsPowered(World world, BlockPos pos) {
        WirelessTorchTileEntity te = (WirelessTorchTileEntity)world.getTileEntity(pos);

        Direction opposite = te.side.getOpposite();

        return world.isSidePowered(pos.offset(opposite), opposite);
    }
    
    public void updateNetworkState(World world) {
        boolean netPowered = false;
        
        this.removeInvalidPositions(world);
        
        for (int i = 0; i < this.linkedPositions.size(); ++i) {
            if (this.positionIsPowered(world, this.linkedPositions.get(i))) {
                netPowered = true;
                break;
            }
        }
        
        setNetworkState(world, netPowered);
    }
    
    private void setNetworkState(World world, boolean state) {
        if (linkedPositions == null)return;
        
        for (BlockPos pos : linkedPositions) {
            WirelessTorchTileEntity td = setIndividualState(world, pos, state);
            td.locked = state && !this.positionIsPowered(world, pos);
        }
        
    }

    static private WirelessTorchTileEntity setIndividualState(World world, BlockPos pos, boolean state) {
        WirelessTorchTileEntity td = (WirelessTorchTileEntity)world.getTileEntity(pos);
        td.changingState = true;

        world.setBlockState(pos, world.getBlockState(pos).with(WirelessTorchBlock.LIT, state));

        td.changingState = false;
        return td;
    }
}
