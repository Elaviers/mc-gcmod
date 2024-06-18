package gcmod.block;

import gcmod.GCMod;
import gcmod.entity.ExplosiveBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ExplosiveBlock extends Block implements BlockEntityProvider
{
    public enum Type
    {
        BLAST,
        CONSTRUCTIVE,
        DIG,
        AIRSTRIKE
    }

    public final Type type;

    public static final IntProperty TIER = IntProperty.of( "tier", 1, 3 );

    public ExplosiveBlock( Type type, Settings settings )
    {
        super( settings );
        this.type = type;

        this.setDefaultState( this.getDefaultState().with( TIER, 2 ) );
    }

    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder )
    {
        builder.add( TIER );
    }

    public void explode( World world, BlockPos pos, boolean chain )
    {
        ExplosiveBlockEntity blockEntity = (ExplosiveBlockEntity) world.getBlockEntity( pos );
        world.removeBlock( pos, false );

        assert blockEntity != null;
        blockEntity.explode( chain );
    }

    @Override
    protected void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify )
    {
        if ( !oldState.isOf( state.getBlock() ) && world.isReceivingRedstonePower( pos ) )
            explode( world, pos, false );
    }

    @Override
    protected void neighborUpdate( BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify )
    {
        if ( world.isReceivingRedstonePower( pos ) )
            explode( world, pos, false );
    }

    @Override
    protected void onExploded( BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger )
    {
        if ( explosion.getDestructionType() == Explosion.DestructionType.TRIGGER_BLOCK )
        {
            super.onExploded( state, world, pos, explosion, stackMerger );
            return;
        }

        if ( !world.isClient )
            explode( world, pos, true );
    }

    @Override
    public void onPlaced( World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack )
    {
        if ( !world.isClient )
        {
            NbtComponent explosiveInfo = stack.get( GCMod.DATA_EXPLOSIVE_INFO );
            if ( explosiveInfo != null )
            {
                ExplosiveBlockEntity te = (ExplosiveBlockEntity) world.getBlockEntity( pos );
                te.readExplosiveInfo( explosiveInfo.copyNbt() );
                te.markDirty();
            }
        }

        super.onPlaced( world, pos, state, placer, stack );
    }

    @Override
    protected ItemActionResult onUseWithItem( ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit )
    {
        if ( !stack.isOf( Items.FLINT_AND_STEEL ) && !stack.isOf( Items.FIRE_CHARGE ) )
            return super.onUseWithItem( stack, state, world, pos, player, hand, hit );

        explode( world, pos, false );

        if ( !player.isCreative() )
        {
            if ( stack.isOf( Items.FLINT_AND_STEEL ) )
                stack.damage( 1, player, LivingEntity.getSlotForHand( hand ) );
            else
                stack.decrement( 1 );
        }

        player.incrementStat( Stats.USED.getOrCreateStat( stack.getItem() ) );
        return ItemActionResult.success( world.isClient );
    }

    @Override
    protected void onProjectileHit( World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile )
    {
        if ( !world.isClient )
        {
            BlockPos blockPos = hit.getBlockPos();
            if ( projectile.isOnFire() && projectile.canModifyAt( world, blockPos ) )
                explode( world, blockPos, false );
        }
    }

    @Override
    public boolean shouldDropItemsOnExplosion( Explosion explosion )
    {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity( BlockPos pos, BlockState state )
    {
        BlockEntityType<ExplosiveBlockEntity> entityType = GCMod.BLAST_TNT_BLOCK_ENTITY;
        switch ( type )
        {
            case AIRSTRIKE -> entityType = GCMod.AIRSTRIKE_TNT_BLOCK_ENTITY;
            case CONSTRUCTIVE -> entityType = GCMod.CONSTRUCTIVE_TNT_BLOCK_ENTITY;
            case DIG -> entityType = GCMod.DIG_TNT_BLOCK_ENTITY;
        }

        return new ExplosiveBlockEntity( entityType, pos, state );
    }
}
