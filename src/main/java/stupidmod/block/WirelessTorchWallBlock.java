package stupidmod.block;

import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidModBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class WirelessTorchWallBlock extends WirelessTorchBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    
    public WirelessTorchWallBlock(String name, WirelessTorchBlock lootFrom) {
        super(Properties.create(Material.WOOD).sound(SoundType.METAL).hardnessAndResistance(0).tickRandomly().doesNotBlockMovement().lootFrom(lootFrom));
        this.setRegistryName(name);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    //


    @Override
    public String getTranslationKey() {
        return this.asItem().getTranslationKey();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Blocks.WALL_TORCH.getShape(state, worldIn, pos, context);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return Blocks.WALL_TORCH.isValidPosition(state, worldIn, pos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return Blocks.WALL_TORCH.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState iblockstate = Blocks.WALL_TORCH.getStateForPlacement(context);
        return iblockstate == null ? null : this.getDefaultState().with(FACING, iblockstate.get(FACING));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            Direction enumfacing = stateIn.get(FACING).getOpposite();
            double d0 = 0.27D;
            double d1 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)enumfacing.getXOffset();
            double d2 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D + 0.22D;
            double d3 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)enumfacing.getZOffset();
            worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(LIT) && blockState.get(FACING) != side ? 15 : 0;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return Blocks.WALL_TORCH.rotate(state, rot);
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return Blocks.WALL_TORCH.mirror(state, mirrorIn);
    }
}
