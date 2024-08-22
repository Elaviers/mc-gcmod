package gcmod.item;

import gcmod.GCMod;
import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FertiliserItem extends Item
{
    public static final int MAX_DAMAGE = 69;

    public FertiliserItem( Settings settings )
    {
        super( settings.maxDamage( MAX_DAMAGE ) );
    }

    // Mostly a copy of BonemealItem but cooler.

    @Override
    public ActionResult useOnBlock( ItemUsageContext context )
    {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockPos blockPos2 = blockPos.offset( context.getSide() );

        BlockState blockState = world.getBlockState( blockPos );

        if ( blockState.isOf( Blocks.FARMLAND ) && world.getBlockState( blockPos2 ).isAir() )
        {
            if ( !world.isClient )
            {
                context.getPlayer().emitGameEvent( GameEvent.ITEM_INTERACT_FINISH );
                world.setBlockState( blockPos, GCMod.COMPOST.getDefaultState().with( FarmlandBlock.MOISTURE, blockState.get( FarmlandBlock.MOISTURE ) ) );
                context.getStack().damage( 1, context.getPlayer(), EquipmentSlot.MAINHAND );
            }

            return ActionResult.success( world.isClient );
        }

        if ( useOnFertilizable( context.getStack(), world, blockPos, context.getPlayer() ) )
        {
            if ( !world.isClient )
            {
                context.getPlayer().emitGameEvent( GameEvent.ITEM_INTERACT_FINISH );
                world.syncWorldEvent( WorldEvents.BONE_MEAL_USED, blockPos, 15 );
            }

            return ActionResult.success( world.isClient );
        }

        if ( world.getBlockState( blockPos ).isSideSolidFullSquare( world, blockPos, context.getSide() ) && useOnGround( context.getStack(), world, blockPos2, context.getSide(), context.getPlayer() ) )
        {
            if ( !world.isClient )
            {
                context.getPlayer().emitGameEvent( GameEvent.ITEM_INTERACT_FINISH );
                world.syncWorldEvent( WorldEvents.BONE_MEAL_USED, blockPos2, 15 );
            }

            return ActionResult.success( world.isClient );
        }

        return ActionResult.PASS;
    }

    public static boolean useOnFertilizable( ItemStack stack, World world, BlockPos pos, LivingEntity user )
    {
        BlockState blockState = world.getBlockState( pos );
        if ( blockState.getBlock() instanceof Fertilizable fertilizable && fertilizable.isFertilizable( world, pos, blockState ) )
        {
            if ( world instanceof ServerWorld )
            {
                final int GROW_COUNT = 3;
                for ( int c = 0; c < GROW_COUNT && fertilizable.canGrow( world, world.random, pos, blockState ); ++c )
                    fertilizable.grow( (ServerWorld) world, world.random, pos, blockState );

                stack.damage( 1, user, EquipmentSlot.MAINHAND );
            }

            return true;
        }

        return false;
    }

    public static boolean useOnGround( ItemStack stack, World world, BlockPos blockPos, @Nullable Direction facing, LivingEntity user )
    {
        if ( world.getBlockState( blockPos ).isOf( Blocks.WATER ) && world.getFluidState( blockPos ).getLevel() == 8 )
        {
            if ( !(world instanceof ServerWorld) )
                return true;

            Random random = world.getRandom();

            notGrowable:
            for ( int i = 0; i < 400; ++i )
            {
                BlockPos growthPos = blockPos;
                BlockState blockState = Blocks.SEAGRASS.getDefaultState();

                for ( int j = 0; j < i / 16; ++j )
                {
                    growthPos = growthPos.add(
                            random.nextInt( 3 ) - 1,
                            (random.nextInt( 3 ) - 1) * random.nextInt( 3 ) / 2,
                            random.nextInt( 3 ) - 1
                    );

                    if ( world.getBlockState( growthPos ).isFullCube( world, growthPos ) )
                        continue notGrowable;
                }

                RegistryEntry<Biome> registryEntry = world.getBiome( growthPos );
                if ( registryEntry.isIn( BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL ) )
                {
                    if ( i == 0 && facing != null && facing.getAxis().isHorizontal() )
                    {
                        blockState = (BlockState) Registries.BLOCK
                                .getRandomEntry( BlockTags.WALL_CORALS, world.random )
                                .map( blockEntry -> ((Block) blockEntry.value()).getDefaultState() )
                                .orElse( blockState );
                        if ( blockState.contains( DeadCoralWallFanBlock.FACING ) )
                        {
                            blockState = blockState.with( DeadCoralWallFanBlock.FACING, facing );
                        }
                    }
                    else if ( random.nextInt( 4 ) == 0 )
                    {
                        blockState = (BlockState) Registries.BLOCK
                                .getRandomEntry( BlockTags.UNDERWATER_BONEMEALS, world.random )
                                .map( blockEntry -> ((Block) blockEntry.value()).getDefaultState() )
                                .orElse( blockState );
                    }
                }

                if ( blockState.isIn( BlockTags.WALL_CORALS, state -> state.contains( DeadCoralWallFanBlock.FACING ) ) )
                {
                    for ( int k = 0; !blockState.canPlaceAt( world, growthPos ) && k < 4; ++k )
                    {
                        blockState = blockState.with( DeadCoralWallFanBlock.FACING, Direction.Type.HORIZONTAL.random( random ) );
                    }
                }

                if ( blockState.canPlaceAt( world, growthPos ) )
                {
                    BlockState blockState2 = world.getBlockState( growthPos );
                    if ( blockState2.isOf( Blocks.WATER ) && world.getFluidState( growthPos ).getLevel() == 8 )
                    {
                        world.setBlockState( growthPos, blockState, Block.NOTIFY_ALL );
                    }
                    else if ( blockState2.isOf( Blocks.SEAGRASS ) && random.nextInt( 10 ) == 0 )
                    {
                        ((Fertilizable) Blocks.SEAGRASS).grow( (ServerWorld) world, random, growthPos, blockState2 );
                    }
                }
            }

            stack.damage( 1, user, EquipmentSlot.MAINHAND );
            return true;
        }

        return false;
    }
}
