package stupidmod.item;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;
import stupidmod.StupidMod;

public class PooDiscItem extends MusicDiscItem {
    public PooDiscItem(String name, SoundEvent sound, int comparatorValue) {
        super(comparatorValue, sound, new Properties().maxStackSize(1).group(StupidMod.GROUP).rarity(Rarity.RARE));

        this.setRegistryName(name);
    }
}
