package StupidMod.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRope extends Block {
    public static final PropertyBool END = PropertyBool.create("end");
    public static final PropertyBool WET = PropertyBool.create("drip");
    
    private static final AxisAlignedBB aabb = new AxisAlignedBB(.4375f,0,.4375f,.5625f,1,.5625f);
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, END, WET);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPos CheckPos = pos.offset(EnumFacing.UP);
        boolean end = false;
        boolean wet = false;
        if ((world.getBlockState(CheckPos).getBlock() instanceof BlockRope && this.getActualState(world.getBlockState(CheckPos), world, CheckPos).getValue(WET)) || world.getBlockState(CheckPos.offset(EnumFacing.UP)).getBlock() == Blocks.WATER)
            wet = true;
    
        CheckPos = pos.offset(EnumFacing.DOWN);
        if (world.getBlockState(CheckPos).getBlock() != this && world.getBlockState(CheckPos).isNormalCube())
            end = true;
    
        return this.getDefaultState().withProperty(END, end).withProperty(WET, wet);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
    public BlockRope(String name) {
        super(Material.WOOD);
        
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        
        this.setDefaultState(this.blockState.getBaseState().withProperty(END, false).withProperty(WET, false));
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState above = world.getBlockState(pos.offset(EnumFacing.UP));
        
        return above.getBlock() != Blocks.AIR;
    }
    
    
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        tryDropHeldBlock(world,  pos);
        super.breakBlock(world, pos, state);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        tryFall(world, pos, state);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        tryFall(world, pos, state);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return aabb;
    }
    
    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    public void DoItemDrop(World world, BlockPos pos) {
        if(!world.isRemote) {
            EntityItem item = new EntityItem(world, pos.getX() + .5f,pos.getY() + .5f, pos.getZ() + .5f, new ItemStack(this));
            world.spawnEntity(item);
        }
    }
    
    public void tryFall(World world, BlockPos pos, IBlockState state)
    {
        if (world.getBlockState(pos.offset(EnumFacing.UP)).getBlock() == Blocks.AIR) {
            EntityFallingBlock fall = new EntityFallingBlock(world, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, state);
            fall.fallTime = 1;
            world.setBlockToAir(pos);
            world.spawnEntity(fall);
    
            tryDropHeldBlock(world, pos);
        }
    }
    
    public void tryDropHeldBlock(World world, BlockPos pos) {
        if (world.getBlockState(pos.offset(EnumFacing.DOWN)).isNormalCube()) {
            BlockPos p = pos.offset(EnumFacing.DOWN);
            EntityFallingBlock fall = new EntityFallingBlock(world, p.getX()+.5f,p.getY(),p.getZ()+.5f, world.getBlockState(p));
            fall.fallTime = 1;
            world.setBlockToAir(p);
            world.spawnEntity(fall);
        }
    }
}
