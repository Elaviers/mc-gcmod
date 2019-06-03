package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import stupidmod.BlockRegister;

public class BlockRope extends Block {
    public static final BooleanProperty END = BooleanProperty.create("end");
    public static final BooleanProperty WET = BooleanProperty.create("wet");
    
    private static final VoxelShape SHAPE = Block.makeCuboidShape(7, 0, 7, 9, 16, 9);
    
    public BlockRope(String name) {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD));
        
        this.setRegistryName(name);

        this.setDefaultState(this.getDefaultState().with(END, false).with(WET, false));
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

    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase world, BlockPos pos) {
        IBlockState upState = world.getBlockState(pos.offset(EnumFacing.UP));

        return upState.getBlock() == BlockRegister.blockRope || upState.getBlockFaceShape(world, pos.offset(EnumFacing.UP), EnumFacing.DOWN) != BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() == BlockRegister.itemBlockRope)
        {
            if (!world.isRemote())
            {
                BlockPos checkPos = pos.offset(EnumFacing.DOWN);

                while (world.getBlockState(checkPos).getBlock() == BlockRegister.blockRope)
                    checkPos = checkPos.offset(EnumFacing.DOWN);

                if (world.getBlockState(checkPos).getBlock() == Blocks.AIR)
                {
                    if (!player.isCreative())
                        stack.shrink(1);

                    world.setBlockState(checkPos, BlockRegister.blockRope.getDefaultState());
                    return true;
                }
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, side, hitX, hitY, hitZ);
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
        if (!world.isRemote() && !tryFall(world, pos, state))
            updateState(world, pos, state);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote() && !tryFall(world, pos, state))
            updateState(world, pos, state);
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (!world.isRemote() && !(newState.getBlock() instanceof BlockRope)) {
            BlockPos heldPos = pos.offset(EnumFacing.DOWN);
            IBlockState heldState = world.getBlockState(heldPos);

            if (heldState.isNormalCube() && world.getBlockState(heldPos.offset(EnumFacing.DOWN)).getBlock() == Blocks.AIR)
                makeBlockFall(world, heldPos, heldState);
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    private boolean tryFall(World world, BlockPos pos, IBlockState state)
    {
        if (!isValidPosition(state, world, pos)) {
            recursiveFall(world, pos, state);
            return true;
        }

        return false;
    }

    private void recursiveFall(World world, BlockPos pos, IBlockState state) {
        BlockPos heldPos = pos.offset(EnumFacing.DOWN);
        IBlockState heldState = world.getBlockState(heldPos);

        if (heldState.isNormalCube())
        {
            IBlockState belowHeld = world.getBlockState(heldPos.offset(EnumFacing.DOWN));

            if (belowHeld.getBlock() != Blocks.AIR && belowHeld.getBlock() != BlockRegister.blockRope)
            {
                world.destroyBlock(pos, true);
                return;
            }

            makeBlockFall(world, heldPos, heldState);
        }

        makeBlockFall(world, pos, state);
    }

    //State is NOT always a rope block here lol
    public void makeBlockFall(World world, BlockPos pos, IBlockState state)
    {
        EntityFallingBlock fall = new EntityFallingBlock(world, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, state);
        fall.fallTime = 1;
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        world.spawnEntity(fall);
    }
}
