package gcmod.mixin;

import gcmod.GCMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends VegetationBlock implements BonemealableBlock
{
    @Shadow @Final public static IntegerProperty AGE;

    @Shadow protected abstract IntegerProperty getAgeProperty();

    @Shadow protected abstract ItemLike getBaseSeedId();

    protected CropBlockMixin( Properties settings )
    {
        super( settings );
    }

    @Inject( method = "mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void canPlantOnTop( BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir )
    {
        if ( floor.is( GCMod.COMPOST ) )
            cir.setReturnValue( true );
    }

    @Override
    public void destroy( LevelAccessor world, BlockPos pos, BlockState state )
    {
        super.destroy( world, pos, state );

        if ( world.getBlockState( pos.below() ).is( GCMod.COMPOST ) )
            world.setBlock( pos, state.setValue( this.getAgeProperty(), 0 ), Block.UPDATE_ALL );
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
        player.awardStat( Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);

        if ( world instanceof ServerLevel )
        {
            List<ItemStack> droppedStacks = getDrops( state, (ServerLevel) world, pos, blockEntity, player, tool );

            if ( !droppedStacks.isEmpty() )
            {
                int numSeeds = 0;
                for ( ItemStack stack : droppedStacks )
                {
                    if ( stack.is( this.getBaseSeedId().asItem() ) )
                        ++numSeeds;
                }

                if ( numSeeds == droppedStacks.size() )
                {
                    droppedStacks.removeLast();
                }
                else
                {
                    droppedStacks.removeIf( stack -> stack.is( this.getBaseSeedId().asItem() ) );
                }

                droppedStacks.forEach(stack -> popResource(world, pos, stack));
            }

            state.spawnAfterBreak((ServerLevel)world, pos, tool, true);
        }
    }
}
