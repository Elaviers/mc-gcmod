package stupidmod.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import stupidmod.StupidModBlocks;

public class RopeBlock extends Block {
    public static final BooleanProperty END = BooleanProperty.create("end");
    public static final BooleanProperty WET = BooleanProperty.create("wet");
    
    private static final VoxelShape SHAPE = Block.makeCuboidShape(7, 0, 7, 9, 16, 9);
    
    public RopeBlock(String name) {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD));
        
        this.setRegistryName(name);

        this.setDefaultState(this.getDefaultState().with(END, false).with(WET, false));
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(END, WET);
    }


    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) { return SHAPE; }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) { return SHAPE; }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState upState = world.getBlockState(pos.offset(Direction.UP));

        return upState.getBlock() == StupidModBlocks.ROPE || upState.isNormalCube(world, pos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rt) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() == StupidModBlocks.ROPE_ITEM)
        {
            if (!world.isRemote())
            {
                BlockPos checkPos = pos.offset(Direction.DOWN);

                while (world.getBlockState(checkPos).getBlock() == StupidModBlocks.ROPE)
                    checkPos = checkPos.offset(Direction.DOWN);

                if (world.getBlockState(checkPos).isAir(world, pos))
                {
                    if (!world.isRemote) {
                        if (!player.isCreative())
                            stack.shrink(1);

                        world.setBlockState(checkPos, StupidModBlocks.ROPE.getDefaultState());
                    }

                    return ActionResultType.SUCCESS;
                }
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, rt);
    }

    private void updateState(World world, BlockPos pos, BlockState state)
    {
        BlockPos CheckPos = pos.offset(Direction.UP);
        boolean end = false;
        boolean wet = false;
        if ((world.getBlockState(CheckPos).getBlock() instanceof RopeBlock && world.getBlockState(CheckPos).get(WET)) || world.getBlockState(CheckPos.offset(Direction.UP)).getBlock() == Blocks.WATER)
            wet = true;
    
        CheckPos = pos.offset(Direction.DOWN);
        if (world.getBlockState(CheckPos).getBlock() != this && world.getBlockState(CheckPos).isNormalCube(world, CheckPos))
            end = true;
    
        if (state.get(WET) != wet || state.get(END) != end)
            world.setBlockState(pos, this.getDefaultState().with(END, end).with(WET, wet));
    }


    
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isRemote() && !tryFall(world, pos, state))
            updateState(world, pos, state);
    }
    
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!world.isRemote() && !tryFall(world, pos, state))
            updateState(world, pos, state);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isRemote() && !(newState.getBlock() instanceof RopeBlock)) {
            BlockPos heldPos = pos.offset(Direction.DOWN);
            BlockState heldState = world.getBlockState(heldPos);

            BlockPos belowHeld = heldPos.offset(Direction.DOWN);

            if (heldState.isNormalCube(world, pos) && world.getBlockState(belowHeld).isAir(world, belowHeld))
                makeBlockFall(world, heldPos, heldState);
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    private boolean tryFall(World world, BlockPos pos, BlockState state)
    {
        if (!isValidPosition(state, world, pos)) {
            recursiveFall(world, pos, state);
            return true;
        }

        return false;
    }

    private void recursiveFall(World world, BlockPos pos, BlockState state) {
        BlockPos heldPos = pos.offset(Direction.DOWN);
        BlockState heldState = world.getBlockState(heldPos);

        makeBlockFall(world, pos, state);

        if (heldState.isNormalCube(world, heldPos))
        {
            BlockPos belowHeldPos = heldPos.offset(Direction.DOWN);
            BlockState belowHeld = world.getBlockState(belowHeldPos);

            if (belowHeld.isAir(world, belowHeldPos) && belowHeld.getBlock() != StupidModBlocks.ROPE)
            {
                world.destroyBlock(pos, true);
                return;
            }

            makeBlockFall(world, heldPos, heldState);
        }
    }

    //State is NOT always a rope block here lol
    public void makeBlockFall(World world, BlockPos pos, BlockState state)
    {
        FallingBlockEntity fall = new FallingBlockEntity(world, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, state);
        fall.fallTime = 1;
        world.removeBlock(pos, false);
        world.addEntity(fall);
    }
}
