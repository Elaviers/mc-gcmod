package stupidmod.item;

import net.minecraft.item.Item;
import stupidmod.StupidMod;

public class ItemCalibrator extends Item {
    public ItemCalibrator(String name) {
        super(new Properties().group(StupidMod.GROUP).maxStackSize(1));
        
        this.setRegistryName(name);
    }
}
