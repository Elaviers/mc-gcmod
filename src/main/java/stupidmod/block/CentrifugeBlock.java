package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.entity.tile.CentrifugeTileEntity;

import javax.annotation.Nullable;

public class CentrifugeBlock extends Block {
    private static VoxelShape BASE = Block.makeCuboidShape(2, 0, 2, 14, 2, 14);
    private static VoxelShape BASE_2 = Block.makeCuboidShape(4, 2, 4, 12, 3, 12);
    private static VoxelShape ROD = Block.makeCuboidShape(7, 0, 7, 9, 13, 9);
    private static VoxelShape TOP = Block.makeCuboidShape(0, 13, 0, 16, 16, 16);
    private static VoxelShape SHAPE = VoxelShapes.or(VoxelShapes.or(VoxelShapes.or(BASE, BASE_2), ROD), TOP);

    public CentrifugeBlock(String name) {
        super(Properties.create(Material.MISCELLANEOUS).sound(SoundType.METAL));
        
        this.setRegistryName(name);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public boolean causesSuffocation(BlockState p_229869_1_, IBlockReader p_229869_2_, BlockPos p_229869_3_) {
        return false;
    }

    @Override
    public boolean canEntitySpawn(BlockState p_220067_1_, IBlockReader p_220067_2_, BlockPos p_220067_3_, EntityType<?> p_220067_4_) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
        return false;
    }

    @Override
    public int getOpacity(BlockState p_200011_1_, IBlockReader p_200011_2_, BlockPos p_200011_3_) {
        return 0;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rt) {
         if (!world.isRemote) {

             INamedContainerProvider containerProvider = getContainer(state, world, pos);

             if (containerProvider != null)
             {
                 NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, pos);
             }
        }
        
        return ActionResultType.SUCCESS;
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new CentrifugeTileEntity();
    }
}
