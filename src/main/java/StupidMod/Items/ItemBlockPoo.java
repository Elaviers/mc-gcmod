package StupidMod.Items;

import StupidMod.Blocks.BlockPoo;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPoo extends ItemBlock {
    
    
    public ItemBlockPoo(BlockPoo block) {
        super(block);
        
        this.setUnlocalizedName(block.getUnlocalizedName());
        this.setRegistryName(block.getRegistryName());
        
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getItemDamage() == 1)
            return this.getUnlocalizedName() + "_fermented";
        
        return this.getUnlocalizedName();
    }
    
    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
