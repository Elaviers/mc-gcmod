package stupidmod;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.item.*;

@ObjectHolder(StupidMod.id)
public class ItemRegister {
    
    private static final String
            namePoo = "poo",
            nameFermentedPoo = "fermented_poo",
            nameSulphur = "sulphur",
            nameNoahSulphur = "noah_sulphur",
            nameMemeEssence = "meme_essence",
            nameBlackPowder = "black_powder",
            namePooPowder = "poo_powder",
            namePooProtein = "poo_protein",
            namePooBrick = "poo_brick",
            namePooCannon = "poo_cannon",
            nameCalibrator = "calibrator";
    
    @ObjectHolder(namePoo)
    public static ItemPoo itemPoo;
    
    @ObjectHolder(nameFermentedPoo)
    public static ItemPoo itemFermentedPoo;
    
    @ObjectHolder(nameSulphur)
    public static ItemBasic itemSulphur;
    
    @ObjectHolder(nameNoahSulphur)
    public static ItemBasic itemNoahSulphur;
    
    @ObjectHolder(nameMemeEssence)
    public static ItemBasic itemMemeEssence;
    
    @ObjectHolder(nameBlackPowder)
    public static ItemBasic itemBlackPowder;
    
    @ObjectHolder(namePooPowder)
    public static ItemBasic itemPooPowder;

    @ObjectHolder(namePooProtein)
    public static ItemPooProtein itemPooProtein;
    
    @ObjectHolder(namePooBrick)
    public static ItemPooBrick itemPooBrick;
    
    @ObjectHolder(namePooCannon)
    public static ItemPooCannon itemPooCannon;
    
    @ObjectHolder(nameCalibrator)
    public static ItemCalibrator itemCalibrator;
    
    //public ItemPooBrick itemPooBrick;
    //public ItemPooCannon itemPooCannon;
    //public ItemCalibrator itemCalibrator;
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration
    {
        @SubscribeEvent
        static void registerItems(RegistryEvent.Register<Item> register)
        {
            register.getRegistry().registerAll(
                    new ItemPoo(namePoo),
                    new ItemPoo(nameFermentedPoo),
                    new ItemBasic(nameSulphur),
                    new ItemBasic(nameNoahSulphur),
                    new ItemBasic(nameMemeEssence),
                    new ItemBasic(nameBlackPowder),
                    new ItemBasic(namePooPowder),
                    new ItemPooProtein(namePooProtein),
                    new ItemPooBrick(namePooBrick),
                    new ItemPooCannon(namePooCannon),
                    new ItemCalibrator(nameCalibrator)
            );
            
        }
    }
}
