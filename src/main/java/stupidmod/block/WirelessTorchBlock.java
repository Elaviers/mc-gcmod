package stupidmod.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import stupidmod.entity.tile.WirelessTorchTileEntity;

import javax.annotation.Nullable;
import java.util.Random;

public class WirelessTorchBlock extends Block {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(6, 0, 6, 10, 10, 10);

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public WirelessTorchBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    public WirelessTorchBlock(String name) {
        this(Properties.create(Material.MISCELLANEOUS).sound(SoundType.METAL).hardnessAndResistance(0).tickRandomly().doesNotBlockMovement());
        this.setRegistryName(name);
    }

    @Override
    public boolean isTransparent(BlockState p_220074_1_) {
        return true;
    }



    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        return direction == Direction.DOWN && !this.isValidPosition(state, world, pos) ?
                Blocks.AIR.getDefaultState() :
                super.updatePostPlacement(state, direction, facingState, world, pos, facingPos);
    }

    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return hasEnoughSolidSide(world, pos.down(), Direction.UP);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    private boolean shouldBeOn(World worldIn, BlockPos pos, BlockState state) {
        Direction facing = getFacing(state);
        return worldIn.isSidePowered(pos.offset(facing.getOpposite()), facing);
    }

    protected Direction getFacing(BlockState state) { return Direction.UP;}
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        WirelessTorchTileEntity te = new WirelessTorchTileEntity();
        te.side = getFacing(state);
        return te;
    }


    ///


    @Override
    public int getLightValue(BlockState state) {
        return (boolean)state.get(LIT) ? 14 : 0;
    }

    @Override
    public int tickRate(IWorldReader world) {
        return 2;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        for (Direction enumfacing : Direction.values()) {
            world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        WirelessTorchTileEntity te = (WirelessTorchTileEntity) world.getTileEntity(pos);

        if (!(newState.getBlock() instanceof WirelessTorchBlock))
        {
            te.linkedPositions.remove(pos);
            te.updateNetworkState(world);
        }
        else
        {
            te.side = getFacing(newState);
        }

        if (!isMoving) {
            for(Direction enumfacing : Direction.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.onNeighborChange(state, world, pos, fromPos);

        WirelessTorchTileEntity te = (WirelessTorchTileEntity)world.getTileEntity(pos);

        if (!te.changingState && !te.locked && state.get(LIT) != this.shouldBeOn(world, pos, state)) {
            te.updateNetworkState(world);
        }
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(LIT) && Direction.DOWN != side ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return side == Direction.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
