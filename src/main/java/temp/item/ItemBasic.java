package stupidmod.item;

import net.minecraft.item.Item;
import stupidmod.StupidMod;

public class ItemBasic extends Item {
    
    public ItemBasic(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }
}
