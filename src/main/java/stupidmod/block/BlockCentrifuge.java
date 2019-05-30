package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.LazyOptional;
import stupidmod.entity.tile.TileEntityCentrifuge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCentrifuge extends Block implements IForgeTileEntity {
    
    public BlockCentrifuge(String name) {
        super(Properties.create(Material.WOOD));
        
        this.setRegistryName(name);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return null;
    }
    
    @Override
    public NBTTagCompound getTileData() {
        return null;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityCentrifuge();
    }
    
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        /*// if (!world.isRemote) {
        TileEntityCentrifuge te = (TileEntityCentrifuge)world.getTileEntity(pos);
        if (te != null) {
            if (te.isSpinning())
                player.openGui(StupidMod.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            else
                player.openGui(StupidMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        //}*/
        
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            ((TileEntityCentrifuge) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
        this.neighborChanged(state, world, pos, state.getBlock(), new BlockPos(0,0,0));
    }
    
    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.isCreative()) {
            TileEntityCentrifuge te = (TileEntityCentrifuge) world.getTileEntity(pos);
            
            if (te != null)
                InventoryHelper.dropInventoryItems(world, pos, te);
            
            super.onBlockHarvested(world, pos, state, player);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
        TileEntity ent = world.getTileEntity(pos);
        return ent != null && ent.receiveClientEvent(id, param);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (world.isBlockPowered(pos)) {
            world.addBlockEvent(pos, world.getBlockState(pos).getBlock(), 69, 1);
        }
        else {
            world.addBlockEvent(pos, world.getBlockState(pos).getBlock(), 69, 0);
        }
    }
}
