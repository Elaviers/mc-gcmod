package gcmod.mixin;

import gcmod.GCMod;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends PlantBlock implements Fertilizable
{
    @Shadow @Final public static IntProperty AGE;

    @Shadow protected abstract IntProperty getAgeProperty();

    @Shadow protected abstract ItemConvertible getSeedsItem();

    protected CropBlockMixin( Settings settings )
    {
        super( settings );
    }

    @Inject( method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void canPlantOnTop( BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir )
    {
        if ( floor.isOf( GCMod.COMPOST ) )
            cir.setReturnValue( true );
    }

    @Override
    public void onBroken( WorldAccess world, BlockPos pos, BlockState state )
    {
        super.onBroken( world, pos, state );

        if ( world.getBlockState( pos.down() ).isOf( GCMod.COMPOST ) )
            world.setBlockState( pos, state.with( this.getAgeProperty(), 0 ), Block.NOTIFY_ALL );
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
        player.incrementStat( Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);

        if ( world instanceof ServerWorld )
        {
            List<ItemStack> droppedStacks = getDroppedStacks( state, (ServerWorld) world, pos, blockEntity, player, tool );

            if ( !droppedStacks.isEmpty() )
            {
                int numSeeds = 0;
                for ( ItemStack stack : droppedStacks )
                {
                    if ( stack.isOf( this.getSeedsItem().asItem() ) )
                        ++numSeeds;
                }

                if ( numSeeds == droppedStacks.size() )
                {
                    droppedStacks.removeLast();
                }
                else
                {
                    droppedStacks.removeIf( stack -> stack.isOf( this.getSeedsItem().asItem() ) );
                }

                droppedStacks.forEach(stack -> dropStack(world, pos, stack));
            }

            state.onStacksDropped((ServerWorld)world, pos, tool, true);
        }
    }
}
