package StupidMod;

import StupidMod.Items.*;
import net.minecraft.item.Item;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemRegister {
    
    public ItemPoo itemPoo;
    public ItemBasic itemSulphur, itemNoahSulphur, itemMemeEssence, itemPowder, itemPooPowder;
    public ItemPooProtein itemPooProtein;
    public ItemPooBrick itemPooBrick;
    public ItemPooCannon itemPooCannon;
    public ItemCalibrator itemCalibrator;
    
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
        
        itemPoo = new ItemPoo("poo");
        itemSulphur = new ItemBasic("sulphur");
        itemNoahSulphur = new ItemBasic("noah_sulphur");
        itemMemeEssence = new ItemBasic("meme_essence");
        itemPowder = new ItemBasic("powder");
        itemPooPowder = new ItemBasic("poo_powder");
        itemPooProtein = new ItemPooProtein("poo_protein");
        itemPooBrick = new ItemPooBrick("poo_brick");
        itemPooCannon = new ItemPooCannon("poo_cannon");
        itemCalibrator = new ItemCalibrator("calibrator");
        
        itemPoo.setCreativeTab(StupidMod.instance.creativeTab);
        itemSulphur.setCreativeTab(StupidMod.instance.creativeTab).setUnlocalizedName("sulphur2");
        itemNoahSulphur.setCreativeTab(StupidMod.instance.creativeTab);
        itemMemeEssence.setCreativeTab(StupidMod.instance.creativeTab);
        itemPowder.setCreativeTab(StupidMod.instance.creativeTab);
        itemPooPowder.setCreativeTab(StupidMod.instance.creativeTab);
        itemPooProtein.setCreativeTab(StupidMod.instance.creativeTab);
        itemPooBrick.setCreativeTab(StupidMod.instance.creativeTab);
        itemPooCannon.setCreativeTab(StupidMod.instance.creativeTab);
        itemCalibrator.setCreativeTab(StupidMod.instance.creativeTab);
    }
    
    @SubscribeEvent
    void registerItems(RegistryEvent.Register<Item> register)
    {
        register.getRegistry().registerAll(itemPoo, itemSulphur, itemNoahSulphur, itemMemeEssence, itemPowder, itemPooPowder, itemPooProtein, itemPooBrick, itemPooCannon, itemCalibrator);
    }
}
