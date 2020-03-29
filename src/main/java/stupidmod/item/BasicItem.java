package stupidmod.item;

import net.minecraft.item.Item;
import stupidmod.StupidMod;

public class BasicItem extends Item {
    
    public BasicItem(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }
}
