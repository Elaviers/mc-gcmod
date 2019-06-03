package stupidmod.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.LazyOptional;
import stupidmod.entity.tile.TileEntityWirelessTorch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Random;

public class BlockWirelessTorch extends BlockTorch implements IForgeTileEntity {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    public BlockWirelessTorch(String name) {
        super(Properties.create(Material.WOOD).needsRandomTick());
        
        this.setRegistryName(name);
        
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }
    
    private boolean shouldBeOn(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing face = getFacing(state).getOpposite();
        return worldIn.isSidePowered(pos.offset(face), face);
    }

    protected EnumFacing getFacing(IBlockState state) { return EnumFacing.UP;}
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(LIT);
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
    
    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityWirelessTorch(getFacing(state));
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    ///

    @Override
    public int tickRate(IWorldReaderBase p_149738_1_) {
        return 2;
    }

    @Override
    public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
        }
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
        TileEntityWirelessTorch te = (TileEntityWirelessTorch) world.getTileEntity(pos);

        if (!(newState.getBlock() instanceof BlockWirelessTorch))
        {
            te.LinkedPositions.remove(pos);
            te.updateNetworkState(world);
        }
        else
        {
            te.side = getFacing(newState);
        }

        if (!isMoving) {
            for(EnumFacing enumfacing : EnumFacing.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.onNeighborChange(state, world, pos, fromPos);

        TileEntityWirelessTorch te = (TileEntityWirelessTorch)world.getTileEntity(pos);

        if (!te.changingState && !te.locked && state.get(LIT) != this.shouldBeOn(world, pos, state)) {
            te.updateNetworkState(world);
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.get(LIT) && EnumFacing.DOWN != side ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            worldIn.spawnParticle(RedstoneParticleData.REDSTONE_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
