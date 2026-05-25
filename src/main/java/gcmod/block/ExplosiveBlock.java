package gcmod.block;

import gcmod.ExplosiveBlockComponent;
import gcmod.GCMod;
import gcmod.entity.ExplosiveBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

public class ExplosiveBlock extends Block implements EntityBlock
{
    public enum Type
    {
        BLAST,
        CONSTRUCTIVE,
        DIG,
        AIRSTRIKE
    }

    public final Type type;

    public static final IntegerProperty TIER = IntegerProperty.create( "tier", 1, 3 );

    public ExplosiveBlock( Type type, Properties settings )
    {
        super( settings );
        this.type = type;

        this.registerDefaultState( this.defaultBlockState().setValue( TIER, 2 ) );
    }

    @Override
    protected void createBlockStateDefinition( StateDefinition.Builder<Block, BlockState> builder )
    {
        builder.add( TIER );
    }

    public void explode( Level world, BlockPos pos, boolean chain )
    {
        ExplosiveBlockEntity blockEntity = (ExplosiveBlockEntity) world.getBlockEntity( pos );
        world.removeBlock( pos, false );

        assert blockEntity != null;
        blockEntity.explode( chain );
    }

    @Override
    protected void onPlace( BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify )
    {
        if ( !oldState.is( state.getBlock() ) && world.hasNeighborSignal( pos ) )
            explode( world, pos, false );
    }

    @Override
    protected void neighborChanged( BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify )
    {
        if ( world.hasNeighborSignal( pos ) )
            explode( world, pos, false );
    }

    @Override
    protected void onExplosionHit( BlockState state, ServerLevel world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger )
    {
        if ( explosion.getBlockInteraction() == Explosion.BlockInteraction.TRIGGER_BLOCK )
        {
            super.onExplosionHit( state, world, pos, explosion, stackMerger );
            return;
        }

        if ( !world.isClientSide() )
            explode( world, pos, true );
    }

    @Override
    public void setPlacedBy( Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack )
    {
        if ( !world.isClientSide() )
        {
            ExplosiveBlockComponent explosiveInfo = stack.get( GCMod.DATA_EXPLOSIVE_INFO );
            if ( explosiveInfo != null )
            {
                ExplosiveBlockEntity te = (ExplosiveBlockEntity) world.getBlockEntity( pos );
                te.setExplosiveInfo( explosiveInfo );
                te.setChanged();
            }
        }

        super.setPlacedBy( world, pos, state, placer, stack );
    }

    @Override
    protected InteractionResult useItemOn( ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
    {
        if ( !stack.is( Items.FLINT_AND_STEEL ) && !stack.is( Items.FIRE_CHARGE ) )
            return super.useItemOn( stack, state, world, pos, player, hand, hit );

        explode( world, pos, false );

        if ( stack.is( Items.FLINT_AND_STEEL ) )
            stack.hurtAndBreak( 1, player, hand.asEquipmentSlot() );
        else
            stack.consume( 1, player );

        player.awardStat( Stats.ITEM_USED.get( stack.getItem() ) );
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onProjectileHit( Level world, BlockState state, BlockHitResult hit, Projectile projectile )
    {
        if ( !world.isClientSide() )
        {
            BlockPos blockPos = hit.getBlockPos();
            if ( projectile.isOnFire() && projectile.mayInteract( (ServerLevel) world, blockPos ) )
                explode( world, blockPos, false );
        }
    }

    @Override
    public boolean dropFromExplosion( Explosion explosion )
    {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity( BlockPos pos, BlockState state )
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
