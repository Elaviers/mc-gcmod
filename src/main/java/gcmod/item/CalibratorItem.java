package gcmod.item;

import gcmod.GCMod;
import gcmod.TorchNetworkComponent;
import gcmod.block.WirelessTorchBlock;
import gcmod.entity.WirelessTorchEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class CalibratorItem extends Item
{
    public CalibratorItem( Properties settings )
    {
        super( settings );
    }

    @Override
    public InteractionResult useOn( UseOnContext context )
    {
        final Level world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final Block block = world.getBlockState( pos ).getBlock();
        if ( block instanceof WirelessTorchBlock )
        {
            if ( world.isClientSide() )
                return InteractionResult.SUCCESS;

            ItemStack stack = context.getItemInHand();

            TorchNetworkComponent torchNetwork = stack.get( GCMod.DATA_TORCH_NETWORK );
            ArrayList<BlockPos> list = torchNetwork != null ? new ArrayList<>( torchNetwork.positions() ) : new ArrayList<>();
            if ( !list.contains( pos ))
                list.add( pos );

            WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( pos );
            te.setTorchNetwork( list );
            te.removeInvalidPositionsFromNetwork();
            stack.set( GCMod.DATA_TORCH_NETWORK, te.getTorchNetwork() );
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
