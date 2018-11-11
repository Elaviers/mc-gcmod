package StupidMod.Items;

import StupidMod.StupidMod;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPoo extends Item {
    public static PropertyBool FERMENTED;
    
    private final String originalName;
    
    public ItemPoo(String name)
    {
        super();
        
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.originalName = name;
        
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        
        if (stack.getItemDamage() == 1)
            return "item." + originalName + "_fermented";
        else if (stack.getItemDamage() == 2)
        {
            this.setUnlocalizedName(originalName + (int) (Math.random() * 13.0));
            stack.setItemDamage(0);
        }
        
        return this.getUnlocalizedName();
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack item = player.getHeldItem(hand);
        
            if (item.getItemDamage() == 1) {
            boolean passed = true;
            for (int i = 0; i < 4; i++)
                if(ItemDye.applyBonemeal(item.copy(), world, pos, player, hand)) {
                    if(!world.isRemote)
                        world.playBroadcastSound(2005, pos, 0);
                }
                else passed = false;
            
            if (passed) item.setCount(item.getCount() - 1);;
            return EnumActionResult.PASS;
        }
        return EnumActionResult.FAIL;
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 2));
            items.add(new ItemStack(this, 1, 1));
        }
    }
}
