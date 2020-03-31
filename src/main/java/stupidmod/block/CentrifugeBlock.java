package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.entity.tile.CentrifugeTileEntity;

import javax.annotation.Nullable;

public class CentrifugeBlock extends Block {
    public CentrifugeBlock(String name) {
        super(Properties.create(Material.WOOD));
        
        this.setRegistryName(name);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rt) {
         if (!world.isRemote) {

             INamedContainerProvider containerProvider = getContainer(state, world, pos);

             if (containerProvider != null)
             {
                 NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, pos);
             }
        }
        
        return true;
    }

    @Nullable
    @Override
    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
        return (CentrifugeTileEntity)world.getTileEntity(pos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            ((CentrifugeTileEntity) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
        this.neighborChanged(state, world, pos, state.getBlock(), new BlockPos(0,0,0), false);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param) {
        TileEntity ent = world.getTileEntity(pos);
        return ent != null && ent.receiveClientEvent(id, param);
    }


    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        if (world.isBlockPowered(pos)) {
            world.addBlockEvent(pos, world.getBlockState(pos).getBlock(), 69, 1);
        }
        else {
            world.addBlockEvent(pos, world.getBlockState(pos).getBlock(), 69, 0);
        }
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new CentrifugeTileEntity();
    }
}
