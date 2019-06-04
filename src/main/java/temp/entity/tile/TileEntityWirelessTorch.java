package stupidmod.entity.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import stupidmod.EntityRegister;
import stupidmod.Utility;
import stupidmod.block.BlockWirelessTorch;

import java.util.ArrayList;

public class TileEntityWirelessTorch extends TileEntity {
    public ArrayList<BlockPos> linkedPositions;
    
    public boolean locked = false;
    public boolean changingState = false;
    
    public EnumFacing side;
    
    public TileEntityWirelessTorch(EnumFacing side) {
        super(EntityRegister.tileEntityWirelessTorch);

        this.linkedPositions = new ArrayList<BlockPos>();

        this.side = side;
    }
    
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);

        compound.setInt("Side", side.getIndex());

        if (this.linkedPositions == null) return compound;
        
        compound.setTag("Torches", this.getTorchList());
        
        return compound;
    }
    
    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);

        side = EnumFacing.byIndex(compound.getInt("Side"));

        if (!compound.hasKey("Torches")) return;
    
        this.setTorchList(compound.getList("Torches", Constants.NBT.TAG_COMPOUND));
    }
    
    /*
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock() instanceof BlockWirelessTorch && newState.getBlock() instanceof BlockWirelessTorch)
            return false;        //Keep tile entity during state change
        
        return true;
    }
    */
    
    
    
    public NBTTagList getTorchList()
    {
        NBTTagList list = new NBTTagList();
        
        for (int i = 0; i < linkedPositions.size(); i++) {
            NBTTagCompound tag = new NBTTagCompound();
            BlockPos pos = linkedPositions.get(i);
            tag.setInt("x", pos.getX());
            tag.setInt("y", pos.getY());
            tag.setInt("z", pos.getZ());
            list.add(tag);
        }
        
        return list;
    }
    
    public void setTorchList(NBTTagList positions)
    {
        this.linkedPositions.clear();

        for (int i = 0; i < positions.size(); ++i)
        {
            NBTTagCompound nbt = positions.getCompound(i);
            this.linkedPositions.add(new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));
        }
    }
    
    public void removeInvalidPositionsFromNetwork() {
        this.removeInvalidPositions(world);
        
        for (int i = 0; i < this.linkedPositions.size(); ++i) {
            TileEntityWirelessTorch te = (TileEntityWirelessTorch)world.getTileEntity(this.linkedPositions.get(i));
            te.linkedPositions = this.linkedPositions;
        }
    }
    
    private void removeInvalidPositions(World world) {
        for (int i = 0; i < this.linkedPositions.size();) {
            if (!(world.getBlockState(this.linkedPositions.get(i)).getBlock() instanceof BlockWirelessTorch))
                this.linkedPositions.remove(i);
            else i++;
        }
    }
    
    boolean positionIsPowered(World world, BlockPos pos) {
        TileEntityWirelessTorch te = (TileEntityWirelessTorch)world.getTileEntity(pos);

        EnumFacing opposite = te.side.getOpposite();

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
            TileEntityWirelessTorch td = Utility.setIndividualState(world, pos, state);
            td.locked = state && !this.positionIsPowered(world, pos);
        }
        
    }
    
}
