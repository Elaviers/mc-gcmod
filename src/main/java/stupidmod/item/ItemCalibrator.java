package stupidmod.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import stupidmod.StupidMod;
import stupidmod.block.BlockWirelessTorch;
import stupidmod.entity.tile.TileEntityWirelessTorch;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCalibrator extends Item {
    public ItemCalibrator(String name) {
        super(new Properties().group(StupidMod.GROUP).maxStackSize(1));
        
        this.setRegistryName(name);
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if (world.isRemote)return EnumActionResult.FAIL;

        BlockPos pos = context.getPos();
        Block b = world.getBlockState(pos).getBlock();
        if (b instanceof BlockWirelessTorch){
            NBTTagCompound newLocation = new NBTTagCompound();
            newLocation.setInt("x", pos.getX());
            newLocation.setInt("y", pos.getY());
            newLocation.setInt("z", pos.getZ());

            ItemStack stack = context.getItem();
            NBTTagCompound nbt;

            if (!stack.hasTag()) {
                nbt = new NBTTagCompound();
                stack.setTag(nbt);
            }
            else
                nbt = stack.getTag();

            NBTTagList list;
            boolean alreadyInList = false;

            try {
                list = nbt.getList("Torches", Constants.NBT.TAG_COMPOUND);

                for (int i = 0; i < list.size(); ++i)
                    if (list.getTag(i).equals(newLocation)) {
                        alreadyInList = true;
                        break;
                    }
            }
            catch (NullPointerException XD)
            {
                list = new NBTTagList();
                nbt.setTag("Torches", list);
            }

            if (!alreadyInList) list.add(newLocation);

            TileEntityWirelessTorch te = (TileEntityWirelessTorch)world.getTileEntity(pos);
            te.setTorchList(list);
            te.removeInvalidPositionsFromNetwork();
            nbt.setTag("Torches", te.getTorchList());
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTag())
            return;

        tooltip.add(new TextComponentString("Torches in network:"));

        try {
            NBTTagList list = stack.getTag().getList("Torches", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.size(); ++i) {
                NBTTagCompound nbt = list.getCompound(i);

                tooltip.add(new TextComponentString(nbt.getInt("x") + ", " + nbt.getInt("y") + ", " + nbt.getInt("z")));
            }
        }
        catch (NullPointerException XD) {}
    }

}
