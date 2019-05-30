package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockRope extends Block {
    public static final BooleanProperty END = BooleanProperty.create("end");
    public static final BooleanProperty WET = BooleanProperty.create("wet");
    
    private static final VoxelShape SHAPE = Block.makeCuboidShape(7, 0, 7, 9, 16, 9);
    
    public BlockRope(String name) {
        super(Properties.create(Material.WOOD));
        
        this.setRegistryName(name);
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(END, WET);
    }
    
    @Override
    public boolean isLadder(IBlockState state, IWorldReader world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    private void updateState(World world, BlockPos pos, IBlockState state)
    {
        BlockPos CheckPos = pos.offset(EnumFacing.UP);
        boolean end = false;
        boolean wet = false;
        if ((world.getBlockState(CheckPos).getBlock() instanceof BlockRope && world.getBlockState(CheckPos).get(WET)) || world.getBlockState(CheckPos.offset(EnumFacing.UP)).getBlock() == Blocks.WATER)
            wet = true;
    
        CheckPos = pos.offset(EnumFacing.DOWN);
        if (world.getBlockState(CheckPos).getBlock() != this && world.getBlockState(CheckPos).isNormalCube())
            end = true;
    
        if (state.get(WET) != wet || state.get(END) != end)
            world.setBlockState(pos, this.getDefaultState().with(END, end).with(WET, wet));
    }
    
    @Override
    public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState) {
        if (!tryFall(world, pos, state))
            updateState(world, pos, state);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!tryFall(world, pos, state))
            updateState(world, pos, state);
    }
    
    public boolean tryFall(World world, BlockPos pos, IBlockState state)
    {
        if (world.getBlockState(pos.offset(EnumFacing.UP)).getBlock() == Blocks.AIR) {
            EntityFallingBlock fall = new EntityFallingBlock(world, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, state);
            fall.fallTime = 1;
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.spawnEntity(fall);
            
            //tryDropHeldBlock(world, pos);
            
            return true;
        }
        
        return false;
    }
    
    /*
    public void tryDropHeldBlock(World world, BlockPos pos) {
        if (world.getBlockState(pos.offset(EnumFacing.DOWN)).isNormalCube()) {
            BlockPos p = pos.offset(EnumFacing.DOWN);
            EntityFallingBlock fall = new EntityFallingBlock(world, p.getX()+.5f,p.getY(),p.getZ()+.5f, world.getBlockState(p));
            fall.fallTime = 1;
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.spawnEntity(fall);
        }
    }
    */
}
