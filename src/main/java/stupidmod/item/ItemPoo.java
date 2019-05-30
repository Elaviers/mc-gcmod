package stupidmod.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import stupidmod.ItemRegister;
import stupidmod.StupidMod;

public class ItemPoo extends Item {
    
    public ItemPoo(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }
    
    @Override
    public String getTranslationKey() {
        if (this == ItemRegister.itemPoo)
            return Util.makeTranslationKey("item", new ResourceLocation(StupidMod.id,"poo" + Integer.toString((int)(Math.random() * 13))));
        
        return super.getTranslationKey();
    }
}
