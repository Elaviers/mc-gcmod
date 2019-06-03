package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockTorchWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import stupidmod.entity.tile.TileEntityWirelessTorch;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockWirelessTorchWall extends BlockWirelessTorch {
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    
    public BlockWirelessTorchWall(String name) {
        super(name);
        
        this.setDefaultState(this.getDefaultState().with(FACING, EnumFacing.NORTH));
    }

    @Override
    protected EnumFacing getFacing(IBlockState state) {
        return state.get(FACING);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    //

    public String getTranslationKey() {
        return this.asItem().getTranslationKey();
    }

    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return Blocks.WALL_TORCH.getShape(state, worldIn, pos);
    }

    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
        return Blocks.WALL_TORCH.isValidPosition(state, worldIn, pos);
    }

    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return Blocks.WALL_TORCH.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Nullable
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockState iblockstate = Blocks.WALL_TORCH.getStateForPlacement(context);
        return iblockstate == null ? null : this.getDefaultState().with(FACING, iblockstate.get(FACING));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            EnumFacing enumfacing = stateIn.get(FACING).getOpposite();
            double d0 = 0.27D;
            double d1 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)enumfacing.getXOffset();
            double d2 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D + 0.22D;
            double d3 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)enumfacing.getZOffset();
            worldIn.spawnParticle(RedstoneParticleData.REDSTONE_DUST, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getWeakPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.get(LIT) && blockState.get(FACING) != side ? 15 : 0;
    }

    public IBlockState rotate(IBlockState state, Rotation rot) {
        return Blocks.WALL_TORCH.rotate(state, rot);
    }

    public IBlockState mirror(IBlockState state, Mirror mirrorIn) {
        return Blocks.WALL_TORCH.mirror(state, mirrorIn);
    }
}
