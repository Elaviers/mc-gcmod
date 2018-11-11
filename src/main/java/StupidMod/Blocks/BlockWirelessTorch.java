package StupidMod.Blocks;

import StupidMod.Entities.Tile.TileEntityWirelessTorchData;
import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockWirelessTorch extends BlockTorch implements ITileEntityProvider {
    private final boolean isOn;
    
    public BlockWirelessTorch(String name, boolean isOn) {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        
        this.isOn = isOn;
    }
    
    boolean shouldBeOn(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
        return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
    }
    
    //NOTE: TileEntityWirelessTorchData does NOT get recreated when the block state changes on or off
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWirelessTorchData();
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        
        ((TileEntityWirelessTorchData) world.getTileEntity(pos)).side = state.getValue(FACING).getOpposite();
        
        if (this.isOn) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, true);
            }
        }
    }
    
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (this.isOn) {
            TileEntityWirelessTorchData te = (TileEntityWirelessTorchData) world.getTileEntity(pos);
            
            if (!te.changingState) {
                te.LinkedPositions.remove(pos);
                te.updateNetworkState(world);
            }
            
            for (EnumFacing enumfacing : EnumFacing.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, true);
            }
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.onNeighborChangeInternal(world, pos, state)) {
            TileEntityWirelessTorchData te = (TileEntityWirelessTorchData)world.getTileEntity(pos);
            
            if (!te.locked && this.isOn != this.shouldBeOn(world, pos, state)) {
                te.updateNetworkState(world);
            }
        }
    }
    
    @Override
    public boolean isAssociatedBlock(Block other) {
        return other instanceof BlockWirelessTorch;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.isOn && blockState.getValue(FACING) != side ? 15 : 0;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(StupidMod.instance.blocks.blockWirelessTorch);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }
    
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (this.isOn)
        {
            double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        
            if (enumfacing.getAxis().isHorizontal())
            {
                EnumFacing enumfacing1 = enumfacing.getOpposite();
                double d3 = 0.27D;
                d0 += 0.27D * (double)enumfacing1.getFrontOffsetX();
                d1 += 0.22D;
                d2 += 0.27D * (double)enumfacing1.getFrontOffsetZ();
            }
    
            world.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }
}
