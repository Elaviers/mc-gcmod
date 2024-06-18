package gcmod.item;

import gcmod.GCMod;
import gcmod.block.WirelessTorchBlock;
import gcmod.entity.WirelessTorchEntity;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class CalibratorItem extends Item
{
    public CalibratorItem( Settings settings )
    {
        super( settings );
    }

    @Override
    public ActionResult useOnBlock( ItemUsageContext context )
    {
        World world = context.getWorld();

        if ( world.isClient )
            return ActionResult.FAIL;

        BlockPos pos = context.getBlockPos();
        Block block = world.getBlockState( pos ).getBlock();
        if ( block instanceof WirelessTorchBlock )
        {
            final int[] newLocation = { pos.getX(), pos.getY(), pos.getZ() };

            ItemStack stack = context.getStack();

            NbtCompound nbt;
            {
                NbtComponent torchNetwork = stack.get( GCMod.DATA_TORCH_NETWORK );
                nbt = torchNetwork != null ? torchNetwork.copyNbt() : new NbtCompound();
            }

            NbtList list = nbt.getList( "Torches", NbtElement.INT_ARRAY_TYPE );

            boolean alreadyInList = false;
            for ( int i = 0; i < list.size(); ++i )
            {
                if ( Arrays.equals( list.getIntArray( i ), newLocation ) )
                {
                    alreadyInList = true;
                    break;
                }
            }

            if ( !alreadyInList )
                list.add( new NbtIntArray( newLocation ) );

            WirelessTorchEntity te = (WirelessTorchEntity) world.getBlockEntity( pos );
            te.setTorchNetwork( list );
            te.removeInvalidPositionsFromNetwork();
            nbt.put( "Torches", te.getTorchNetwork() );
            stack.set( GCMod.DATA_TORCH_NETWORK, NbtComponent.of( nbt ) );
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip( ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type )
    {
        NbtComponent torchNetwork = stack.get( GCMod.DATA_TORCH_NETWORK );
        if ( torchNetwork == null )
            return;

        tooltip.add( Text.translatable( "item.gcmod.calibrator.tooltip1" ) );

        NbtCompound nbt = torchNetwork.copyNbt();
        NbtList list = nbt.getList( "Torches", NbtElement.INT_ARRAY_TYPE );
        for ( int i = 0; i < list.size(); ++i )
        {
            int[] pos = list.getIntArray( i );
            if ( pos.length == 3 )
                tooltip.add( Text.translatable( "item.gcmod.calibrator.tooltipentry", pos[0], pos[1], pos[2] ) );
        }
    }
}
