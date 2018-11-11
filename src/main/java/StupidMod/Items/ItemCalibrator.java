package StupidMod.Items;

import StupidMod.Blocks.BlockWirelessTorch;
import StupidMod.Entities.Tile.TileEntityWirelessTorchData;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCalibrator extends Item {
    
    public ItemCalibrator(String name)
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote)return EnumActionResult.FAIL;
        
        Block b = world.getBlockState(pos).getBlock();
        if (b instanceof BlockWirelessTorch){
            NBTTagCompound newLocation = new NBTTagCompound();
            newLocation.setInteger("x", pos.getX());
            newLocation.setInteger("y", pos.getY());
            newLocation.setInteger("z", pos.getZ());
            
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound nbt;
            
            if (!stack.hasTagCompound()) {
                nbt = new NBTTagCompound();
                stack.setTagCompound(nbt);
            }
            else
                nbt = stack.getTagCompound();
            
            NBTTagList list;
            boolean alreadyInList = false;
            
            if (nbt.hasKey("Torches", Constants.NBT.TAG_LIST)) {
                list = nbt.getTagList("Torches", Constants.NBT.TAG_COMPOUND);
                
                for (int i = 0; i < list.tagCount(); ++i)
                    if (list.getCompoundTagAt(i).equals(newLocation)) {
                        alreadyInList = true;
                        break;
                    }
            }
            else
            {
                list = new NBTTagList();
                nbt.setTag("Torches", list);
            }
            
            if (!alreadyInList) list.appendTag(newLocation);
            
            TileEntityWirelessTorchData te = (TileEntityWirelessTorchData)world.getTileEntity(pos);
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound())
            return;
    
        tooltip.add("Torches in network:");
    
        NBTTagList list = stack.getTagCompound().getTagList("Torches", Constants.NBT.TAG_COMPOUND);
        if (list != null) {
            for (int i = 0; i < list.tagCount(); ++i) {
                NBTTagCompound nbt = list.getCompoundTagAt(i);
                
                tooltip.add(nbt.getInteger("x") + ", " + nbt.getInteger("y") + ", " + nbt.getInteger("z"));
            }
        }
    }
}
