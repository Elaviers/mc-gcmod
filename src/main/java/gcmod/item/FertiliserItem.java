package gcmod.item;

import gcmod.GCMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FertiliserItem extends Item
{
    public static final int MAX_DAMAGE = 69;

    public FertiliserItem( Properties settings )
    {
        super( settings.durability( MAX_DAMAGE ) );
    }

    // Mostly a copy of BonemealItem but cooler.

    @Override
    public InteractionResult useOn( UseOnContext context )
    {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos2 = blockPos.relative( context.getClickedFace() );

        BlockState blockState = world.getBlockState( blockPos );

        if ( blockState.is( Blocks.FARMLAND ) && world.getBlockState( blockPos2 ).isAir() )
        {
            if ( !world.isClientSide() )
            {
                context.getPlayer().gameEvent( GameEvent.ITEM_INTERACT_FINISH );
                world.setBlockAndUpdate( blockPos, GCMod.COMPOST.defaultBlockState().setValue( FarmBlock.MOISTURE, blockState.getValue( FarmBlock.MOISTURE ) ) );
                context.getItemInHand().hurtAndBreak( 1, context.getPlayer(), EquipmentSlot.MAINHAND );
            }

            return InteractionResult.SUCCESS;
        }

        if ( useOnFertilizable( context.getItemInHand(), world, blockPos, context.getPlayer() ) )
        {
            if ( !world.isClientSide() )
            {
                context.getPlayer().gameEvent( GameEvent.ITEM_INTERACT_FINISH );
                world.levelEvent( LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos, 15 );
            }

            return InteractionResult.SUCCESS;
        }

        if ( world.getBlockState( blockPos ).isFaceSturdy( world, blockPos, context.getClickedFace() ) && useOnGround( context.getItemInHand(), world, blockPos2, context.getClickedFace(), context.getPlayer() ) )
        {
            if ( !world.isClientSide() )
            {
                context.getPlayer().gameEvent( GameEvent.ITEM_INTERACT_FINISH );
                world.levelEvent( LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos2, 15 );
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public static boolean useOnFertilizable( ItemStack stack, Level world, BlockPos pos, LivingEntity user )
    {
        BlockState blockState = world.getBlockState( pos );
        if ( blockState.getBlock() instanceof BonemealableBlock fertilizable && fertilizable.isValidBonemealTarget( world, pos, blockState ) )
        {
            if ( world instanceof ServerLevel )
            {
                final int GROW_COUNT = 3;
                for ( int c = 0; c < GROW_COUNT && fertilizable.isBonemealSuccess( world, world.random, pos, blockState ); ++c )
                    fertilizable.performBonemeal( (ServerLevel) world, world.random, pos, blockState );

                stack.hurtAndBreak( 1, user, EquipmentSlot.MAINHAND );
            }

            return true;
        }

        return false;
    }

    public static boolean useOnGround( ItemStack stack, Level world, BlockPos blockPos, @Nullable Direction facing, LivingEntity user )
    {
        if ( world.getBlockState( blockPos ).is( Blocks.WATER ) && world.getFluidState( blockPos ).getAmount() == 8 )
        {
            if ( !(world instanceof ServerLevel) )
                return true;

            RandomSource random = world.getRandom();

            notGrowable:
            for ( int i = 0; i < 400; ++i )
            {
                BlockPos growthPos = blockPos;
                BlockState blockState = Blocks.SEAGRASS.defaultBlockState();

                for ( int j = 0; j < i / 16; ++j )
                {
                    growthPos = growthPos.offset(
                            random.nextInt( 3 ) - 1,
                            (random.nextInt( 3 ) - 1) * random.nextInt( 3 ) / 2,
                            random.nextInt( 3 ) - 1
                    );

                    if ( world.getBlockState( growthPos ).isCollisionShapeFullBlock( world, growthPos ) )
                        continue notGrowable;
                }

                Holder<Biome> registryEntry = world.getBiome( growthPos );
                if ( registryEntry.is( BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL ) )
                {
                    if ( i == 0 && facing != null && facing.getAxis().isHorizontal() )
                    {
                        blockState = (BlockState) BuiltInRegistries.BLOCK
                                .getRandomElementOf( BlockTags.WALL_CORALS, world.random )
                                .map( blockEntry -> ((Block) blockEntry.value()).defaultBlockState() )
                                .orElse( blockState );
                        if ( blockState.hasProperty( BaseCoralWallFanBlock.FACING ) )
                        {
                            blockState = blockState.setValue( BaseCoralWallFanBlock.FACING, facing );
                        }
                    }
                    else if ( random.nextInt( 4 ) == 0 )
                    {
                        blockState = (BlockState) BuiltInRegistries.BLOCK
                                .getRandomElementOf( BlockTags.UNDERWATER_BONEMEALS, world.random )
                                .map( blockEntry -> ((Block) blockEntry.value()).defaultBlockState() )
                                .orElse( blockState );
                    }
                }

                if ( blockState.is( BlockTags.WALL_CORALS, state -> state.hasProperty( BaseCoralWallFanBlock.FACING ) ) )
                {
                    for ( int k = 0; !blockState.canSurvive( world, growthPos ) && k < 4; ++k )
                    {
                        blockState = blockState.setValue( BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection( random ) );
                    }
                }

                if ( blockState.canSurvive( world, growthPos ) )
                {
                    BlockState blockState2 = world.getBlockState( growthPos );
                    if ( blockState2.is( Blocks.WATER ) && world.getFluidState( growthPos ).getAmount() == 8 )
                    {
                        world.setBlock( growthPos, blockState, Block.UPDATE_ALL );
                    }
                    else if ( blockState2.is( Blocks.SEAGRASS ) && random.nextInt( 10 ) == 0 )
                    {
                        ((BonemealableBlock) Blocks.SEAGRASS).performBonemeal( (ServerLevel) world, random, growthPos, blockState2 );
                    }
                }
            }

            stack.hurtAndBreak( 1, user, EquipmentSlot.MAINHAND );
            return true;
        }

        return false;
    }
}
