package stupidmod.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import stupidmod.StupidMod;
import stupidmod.block.WirelessTorchBlock;
import stupidmod.entity.tile.WirelessTorchTileEntity;

import javax.annotation.Nullable;
import java.util.List;

public class CalibratorItem extends Item {
    public CalibratorItem(String name) {
        super(new Properties().group(StupidMod.GROUP).maxStackSize(1));
        
        this.setRegistryName(name);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if (world.isRemote)return ActionResultType.FAIL;

        BlockPos pos = context.getPos();
        Block b = world.getBlockState(pos).getBlock();
        if (b instanceof WirelessTorchBlock){
            CompoundNBT newLocation = new CompoundNBT();
            newLocation.putInt("x", pos.getX());
            newLocation.putInt("y", pos.getY());
            newLocation.putInt("z", pos.getZ());

            ItemStack stack = context.getItem();
            CompoundNBT nbt;

            if (!stack.hasTag()) {
                nbt = new CompoundNBT();
                stack.setTag(nbt);
            }
            else
                nbt = stack.getTag();

            ListNBT list;
            boolean alreadyInList = false;

            try {
                list = nbt.getList("Torches", Constants.NBT.TAG_COMPOUND);

                for (int i = 0; i < list.size(); ++i)
                    if (list.getCompound(i).equals(newLocation)) {
                        alreadyInList = true;
                        break;
                    }
            }
            catch (NullPointerException XD)
            {
                list = new ListNBT();
                nbt.put("Torches", list);
            }

            if (!alreadyInList) list.add(newLocation);

            WirelessTorchTileEntity te = (WirelessTorchTileEntity)world.getTileEntity(pos);
            te.setTorchList(list);
            te.removeInvalidPositionsFromNetwork();
            nbt.put("Torches", te.getTorchList());
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTag())
            return;

        tooltip.add(new StringTextComponent("Torches in network:"));

        try {
            ListNBT list = stack.getTag().getList("Torches", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.size(); ++i) {
                CompoundNBT nbt = list.getCompound(i);

                tooltip.add(new StringTextComponent(nbt.getInt("x") + ", " + nbt.getInt("y") + ", " + nbt.getInt("z")));
            }
        }
        catch (NullPointerException XD) {}
    }

}
