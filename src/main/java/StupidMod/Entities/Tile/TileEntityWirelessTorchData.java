package StupidMod.Entities.Tile;

import StupidMod.Blocks.BlockWirelessTorch;
import StupidMod.Utility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class TileEntityWirelessTorchData extends TileEntity {
    public ArrayList<BlockPos> LinkedPositions;
    
    public boolean locked = false;
    public boolean changingState = false;
    
    public EnumFacing side;
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    
        nbt.setInteger("Side", side.getIndex());
        
        if (this.LinkedPositions == null) return nbt;
        
        nbt.setTag("Torches", this.getTorchList());
        
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        
        side = EnumFacing.getFront(nbt.getInteger("Side"));
        
        if (!nbt.hasKey("Torches")) return;
        
        this.setTorchList(nbt.getTagList("Torches", Constants.NBT.TAG_COMPOUND));
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock() instanceof BlockWirelessTorch && newState.getBlock() instanceof BlockWirelessTorch)
            return false;        //Keep tile entity during state change
        
        return true;
    }
    
    public NBTTagList getTorchList()
    {
        NBTTagList list = new NBTTagList();
    
        for (int i = 0;i < LinkedPositions.size();i++) {
            NBTTagCompound tag = new NBTTagCompound();
            BlockPos pos = LinkedPositions.get(i);
            tag.setInteger("x", pos.getX());
            tag.setInteger("y", pos.getY());
            tag.setInteger("z", pos.getZ());
            list.appendTag(tag);
        }
        
        return list;
    }
    
    public void setTorchList(NBTTagList positions)
    {
        this.LinkedPositions = new ArrayList<BlockPos>();
    
        for (int i = 0; i < positions.tagCount(); ++i)
        {
            NBTTagCompound nbt = positions.getCompoundTagAt(i);
            this.LinkedPositions.add(new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z")));
        }
    }
    
    public void removeInvalidPositionsFromNetwork() {
        this.removeInvalidPositions(world);
        
        for (int i = 0; i < this.LinkedPositions.size(); ++i) {
            TileEntityWirelessTorchData te = (TileEntityWirelessTorchData)world.getTileEntity(this.LinkedPositions.get(i));
            te.LinkedPositions = this.LinkedPositions;
        }
    }
    
    private void removeInvalidPositions(World world) {
        for (int i = 0; i < this.LinkedPositions.size(); i++) {
            if (!(world.getBlockState(this.LinkedPositions.get(i)).getBlock() instanceof BlockWirelessTorch)) {
                this.LinkedPositions.remove(i);
            }
        }
    }
    
    boolean positionIsPowered(World world, BlockPos pos) {
        TileEntityWirelessTorchData te = (TileEntityWirelessTorchData)world.getTileEntity(pos);
        return world.isSidePowered(pos.offset(te.side), te.side);
    }
    
    public void updateNetworkState(World world) {
        boolean netPowered = false;
    
        this.removeInvalidPositions(world);
        
        for (int i = 0; i < this.LinkedPositions.size(); ++i) {
            if (this.positionIsPowered(world, this.LinkedPositions.get(i))) {
                netPowered = true;
                break;
            }
        }
    
        setNetworkState(world, netPowered);
    }
    
    private void setNetworkState(World world, boolean state) {
        if (LinkedPositions == null)return;
        
        for (BlockPos pos : LinkedPositions) {
            TileEntityWirelessTorchData td = Utility.setIndividualState(world, pos, state);
            td.locked = state && !this.positionIsPowered(world, pos);
        }
        
    }
}
