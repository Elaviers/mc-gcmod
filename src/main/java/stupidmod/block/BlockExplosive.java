package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.LazyOptional;
import stupidmod.entity.tile.TileEntityExplosive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockExplosive extends Block implements IForgeTileEntity {
    
    public enum Type
    {
        BLAST,
        CONSTRUCTIVE,
        DIG,
        AIRSTRIKE
    }
    
    public final Type type;
    
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 1, 3);
    
    public BlockExplosive(String name, Type type) {
        super(Properties.create(Material.WOOD));
        
        this.setRegistryName(name);
        this.type = type;
        
        this.setDefaultState(this.getDefaultState().with(TIER, 2));
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(TIER);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase player, ItemStack stack) {
        TileEntityExplosive te = (TileEntityExplosive) world.getTileEntity(pos);
        te.read(stack.getTag());
    }
    
    public void explode(World world, BlockPos pos, IBlockState state)
    {
        TileEntityExplosive te = (TileEntityExplosive)world.getTileEntity(pos);
        te.explode(world, pos, state);
    }
    
    @Override
    public void onBlockAdded(IBlockState prev, World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isBlockPowered(pos))
            this.explode(worldIn, pos, state);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (world.isBlockPowered(pos))
            this.explode(world, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
            this.explode(world, pos, state);
            return true;
        }
    
        return false;
    }
    
    //Tile Ent
    
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
        return new TileEntityExplosive();
    }
}
