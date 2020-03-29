package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import stupidmod.entity.tile.ExplosiveTileEntity;

import javax.annotation.Nullable;

public class ExplosiveBlock extends Block {

    public enum Type
    {
        BLAST,
        CONSTRUCTIVE,
        DIG,
        AIRSTRIKE
    }
    
    public final Type type;
    
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 1, 3);
    
    public ExplosiveBlock(String name, Type type) {
        super(Properties.create(Material.WOOD));
        
        this.setRegistryName(name);
        this.type = type;
        
        this.setDefaultState(this.getDefaultState().with(TIER, 2));
    }
    
    public void explode(World world, BlockPos pos, BlockState state)
    {
        ExplosiveTileEntity te = (ExplosiveTileEntity)world.getTileEntity(pos);

        //The block snapshot stuff is disgusting but I have to do it or the game will try to get the invalidated tile entity to write stuff...
        boolean prevCaptureSnapshots = world.captureBlockSnapshots;
        world.captureBlockSnapshots = false;
        world.removeBlock(pos, false);
        world.captureBlockSnapshots = prevCaptureSnapshots;

        //Tile Entity is removed from world now, do explosion
        te.explode(world, pos, state);
    }

    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, state);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            if (worldIn.isBlockPowered(pos)) {
                explode(worldIn, pos, state);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isBlockPowered(pos)) {
            explode(worldIn, pos, state);
        }
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rt) {
        ItemStack itemstack = player.getHeldItem(hand);
        Item item = itemstack.getItem();
        if (item == Items.FLINT_AND_STEEL || item == Items.FIRE_CHARGE) {
            explode(world, pos, state);
            if (item == Items.FLINT_AND_STEEL) {
                itemstack.damageItem(1, player, (p_220287_1_) -> {
                    p_220287_1_.sendBreakAnimation(hand);
                });
            } else {
                itemstack.shrink(1);
            }
            return true;
        }
    
        return false;
    }

    @Override
    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
        if (!worldIn.isRemote && projectile instanceof AbstractArrowEntity) {
            AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity)projectile;
            Entity entity = abstractarrowentity.getShooter();
            if (abstractarrowentity.isBurning()) {
                BlockPos blockpos = hit.getPos();
                explode(worldIn, blockpos, state);
            }
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion p_149659_1_) {
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TIER);
    }

    //Tile Ent
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack stack) {
        ExplosiveTileEntity te = (ExplosiveTileEntity) world.getTileEntity(pos);
        te.read(stack.getTag());

        super.onBlockPlacedBy(world, pos, state, player, stack);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ExplosiveTileEntity();
    }

}
