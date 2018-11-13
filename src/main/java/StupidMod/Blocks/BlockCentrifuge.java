package StupidMod.Blocks;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCentrifuge extends Block implements ITileEntityProvider {
    
    boolean active;
    
    public BlockCentrifuge(String name)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        
        this.active = false;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCentrifuge();
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
       // if (!world.isRemote) {
            TileEntityCentrifuge te = (TileEntityCentrifuge)world.getTileEntity(pos);
            if (te != null) {
                if (te.isSpinning())
                    player.openGui(StupidMod.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
                else
                    player.openGui(StupidMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
        //}
        
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            ((TileEntityCentrifuge) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
        this.neighborChanged(this.blockState.getBaseState(), world, pos, new Block(Material.AIR), new BlockPos(0,0,0));
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityCentrifuge te = (TileEntityCentrifuge) world.getTileEntity(pos);
        if (te != null)
            InventoryHelper.dropInventoryItems(world, pos, te);
        super.breakBlock(world, pos, state);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
        TileEntity ent = world.getTileEntity(pos);
        return ent != null && ent.receiveClientEvent(id, param);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
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
