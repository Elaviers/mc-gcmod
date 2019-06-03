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
import stupidmod.block.BlockWirelessTorchWall;

import java.util.ArrayList;

public class TileEntityWirelessTorch extends TileEntity {
    public ArrayList<BlockPos> LinkedPositions;
    
    public boolean locked = false;
    public boolean changingState = false;
    
    public EnumFacing side;
    
    public TileEntityWirelessTorch(EnumFacing side) {
        super(EntityRegister.tileEntityWirelessTorch);

        this.LinkedPositions = new ArrayList<BlockPos>();

        this.side = side;
    }
    
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);

        compound.setInt("Side", side.getIndex());

        if (this.LinkedPositions == null) return compound;
        
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
        
        for (int i = 0;i < LinkedPositions.size();i++) {
            NBTTagCompound tag = new NBTTagCompound();
            BlockPos pos = LinkedPositions.get(i);
            tag.setInt("x", pos.getX());
            tag.setInt("y", pos.getY());
            tag.setInt("z", pos.getZ());
            list.add(tag);
        }
        
        return list;
    }
    
    public void setTorchList(NBTTagList positions)
    {
        for (int i = 0; i < positions.size(); ++i)
        {
            NBTTagCompound nbt = positions.getCompound(i);
            this.LinkedPositions.add(new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));
        }
    }
    
    public void removeInvalidPositionsFromNetwork() {
        this.removeInvalidPositions(world);
        
        for (int i = 0; i < this.LinkedPositions.size(); ++i) {
            TileEntityWirelessTorch te = (TileEntityWirelessTorch)world.getTileEntity(this.LinkedPositions.get(i));
            te.LinkedPositions = this.LinkedPositions;
        }
    }
    
    private void removeInvalidPositions(World world) {
        for (int i = 0; i < this.LinkedPositions.size();) {
            if (!(world.getBlockState(this.LinkedPositions.get(i)).getBlock() instanceof BlockWirelessTorch))
                this.LinkedPositions.remove(i);
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
            TileEntityWirelessTorch td = Utility.setIndividualState(world, pos, state);
            td.locked = state && !this.positionIsPowered(world, pos);
        }
        
    }
    
}
