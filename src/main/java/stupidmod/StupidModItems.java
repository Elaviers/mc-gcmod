package stupidmod;

import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.item.*;

@ObjectHolder(StupidMod.id)
public class StupidModItems {
    
    private static final String
            namePoo = "poo",
            nameFermentedPoo = "fermented_poo",
            nameFertiliser = "fertiliser",
            nameSulphur = "sulphur",
            nameNoahSulphur = "noah_sulphur",
            nameMemeEssence = "meme_essence",
            nameBlackPowder = "black_powder",
            namePooPowder = "poo_powder",
            namePooProtein = "poo_protein",
            namePooBrick = "poo_brick",
            namePooCannon = "poo_cannon",
            nameCalibrator = "calibrator",
            nameDiscBigWillies = "music_disc_big_willies",
            nameDiscGodlyPiss = "music_disc_godly_piss",
            nameDiscMassiveCrap = "music_disc_massive_crap",
            nameDiscPeaSizedAnus = "music_disc_pea_sized_anus",
            nameDiscPooeyLoo = "music_disc_pooey_loo",
            nameDiscPowerfulConstipation = "music_disc_powerful_constipation",
            nameDiscRedWeewee = "music_disc_red_weewee",
            nameDiscSmellyMethane = "music_disc_smelly_methane",
            nameDiscSymphonyOfStupidity = "music_disc_symphony_of_stupidity",
            nameDiscToiletWater = "music_disc_toilet_water",
            nameDiscFlightOfTheChineseCommuter = "music_disc_flight_of_the_chinese_commuter",
            nameDiscRageOverACloggedCommode = "music_disc_rage_over_a_clogged_commode",
            namePooCowSpawnEgg = "poo_cow_spawn_egg",
            namePooHorseSpawnEgg = "poo_horse_spawn_egg",
            namePooMooshroomSpawnEgg = "poo_mooshroom_spawn_egg",
            namePooPigSpawnEgg = "poo_pig_spawn_egg",
            namePooSheepSpawnEgg = "poo_sheep_spawn_egg";
    
    @ObjectHolder(namePoo)
    public static BasicItem POO;
    
    @ObjectHolder(nameFermentedPoo)
    public static BasicItem FERMENTED_POO;

    @ObjectHolder(nameFertiliser)
    public static FertiliserItem FERTILISER;
    
    @ObjectHolder(nameSulphur)
    public static BasicItem SULPHUR;
    
    @ObjectHolder(nameNoahSulphur)
    public static BasicItem NOAH_SULPHUR;
    
    @ObjectHolder(nameMemeEssence)
    public static BasicItem MEME_ESSENCE;
    
    @ObjectHolder(nameBlackPowder)
    public static BasicItem BLACK_POWDER;
    
    @ObjectHolder(namePooPowder)
    public static BasicItem POO_POWDER;

    @ObjectHolder(namePooProtein)
    public static PooProteinItem POO_PROTEIN;
    
    @ObjectHolder(namePooBrick)
    public static PooBrickItem POO_BRICK;
    
    @ObjectHolder(namePooCannon)
    public static PooCannonItem POO_CANNON;
    
    @ObjectHolder(nameCalibrator)
    public static CalibratorItem CALIBRATOR;

    @ObjectHolder(nameDiscBigWillies)
    public static PooDiscItem DISC_BIG_WILLIES;

    @ObjectHolder(nameDiscGodlyPiss)
    public static PooDiscItem DISC_GODLY_PISS;

    @ObjectHolder(nameDiscMassiveCrap)
    public static PooDiscItem DISC_MASSIVE_CRAP;

    @ObjectHolder(nameDiscPeaSizedAnus)
    public static PooDiscItem DISC_PEA_SIZED_ANUS;

    @ObjectHolder(nameDiscPooeyLoo)
    public static PooDiscItem DISC_POOEY_LOO;

    @ObjectHolder(nameDiscPowerfulConstipation)
    public static PooDiscItem DISC_POWERFUL_CONSTIPATION;

    @ObjectHolder(nameDiscRedWeewee)
    public static PooDiscItem DISC_RED_WEEWEE;

    @ObjectHolder(nameDiscSmellyMethane)
    public static PooDiscItem DISC_SMELLY_METHANE;

    @ObjectHolder(nameDiscSymphonyOfStupidity)
    public static PooDiscItem DISC_SYMPHONY_OF_STUPIDITY;

    @ObjectHolder(nameDiscToiletWater)
    public static PooDiscItem DISC_TOILET_WATER;

    @ObjectHolder(nameDiscFlightOfTheChineseCommuter)
    public static PooDiscItem DISC_FLIGHT_OF_THE_CHINESE_COMMUTER;

    @ObjectHolder(nameDiscRageOverACloggedCommode)
    public static PooDiscItem DISC_RAGE_OVER_A_CLOGGED_COMMODE;

    @ObjectHolder(namePooCowSpawnEgg)
    public static SpawnEggItem POO_COW_SPAWN_EGG;

    @ObjectHolder(namePooHorseSpawnEgg)
    public static SpawnEggItem POO_HORSE_SPAWN_EGG;

    @ObjectHolder(namePooMooshroomSpawnEgg)
    public static SpawnEggItem POO_MOOSHROOM_SPAWN_EGG;

    @ObjectHolder(namePooPigSpawnEgg)
    public static SpawnEggItem POO_PIG_SPAWN_EGG;

    @ObjectHolder(namePooSheepSpawnEgg)
    public static SpawnEggItem POO_SHEEP_SPAWN_EGG;
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration
    {
        @SubscribeEvent
        static void registerItems(RegistryEvent.Register<Item> register)
        {
            StupidModEntities.createEntities();

            register.getRegistry().registerAll(
                    new BasicItem(namePoo),
                    new BasicItem(nameFermentedPoo),
                    new FertiliserItem(nameFertiliser),
                    new BasicItem(nameSulphur),
                    new BasicItem(nameNoahSulphur),
                    new BasicItem(nameMemeEssence),
                    new BasicItem(nameBlackPowder),
                    new BasicItem(namePooPowder),
                    new PooProteinItem(namePooProtein),
                    new PooBrickItem(namePooBrick),
                    new PooCannonItem(namePooCannon),
                    new CalibratorItem(nameCalibrator),
                    new PooDiscItem(nameDiscBigWillies, StupidModSounds.MUSIC_BIG_WILLIES, 1),
                    new PooDiscItem(nameDiscGodlyPiss, StupidModSounds.MUSIC_GODLY_PISS, 2),
                    new PooDiscItem(nameDiscMassiveCrap, StupidModSounds.MUSIC_MASSIVE_CRAP, 3),
                    new PooDiscItem(nameDiscPeaSizedAnus, StupidModSounds.MUSIC_PEA_SIZED_ANUS, 4),
                    new PooDiscItem(nameDiscPooeyLoo, StupidModSounds.MUSIC_POOEY_LOO, 5),
                    new PooDiscItem(nameDiscPowerfulConstipation, StupidModSounds.MUSIC_POWERFUL_CONSTIPATION, 6),
                    new PooDiscItem(nameDiscRedWeewee, StupidModSounds.MUSIC_RED_WEEWEE, 7),
                    new PooDiscItem(nameDiscSmellyMethane, StupidModSounds.MUSIC_SMELLY_METHANE, 8),
                    new PooDiscItem(nameDiscSymphonyOfStupidity, StupidModSounds.MUSIC_SYMPHONY_OF_STUPIDITY, 9),
                    new PooDiscItem(nameDiscToiletWater, StupidModSounds.MUSIC_TOILET_WATER, 10),
                    new PooDiscItem(nameDiscFlightOfTheChineseCommuter, StupidModSounds.MUSIC_FLIGHT_OF_THE_CHINESE_COMMUTER, 11),
                    new PooDiscItem(nameDiscRageOverACloggedCommode, StupidModSounds.MUSIC_RAGE_OVER_A_CLOGGED_COMMODE, 12),
                    new SpawnEggItem(StupidModEntities.POO_COW, 4470310, 10592673, (new Item.Properties()).group(StupidMod.GROUP)).setRegistryName(namePooCowSpawnEgg),
                    new SpawnEggItem(StupidModEntities.POO_HORSE, 12623485, 15656192, (new Item.Properties()).group(StupidMod.GROUP)).setRegistryName(namePooHorseSpawnEgg),
                    new SpawnEggItem(StupidModEntities.POO_MOOSHROOM, 10489616, 12040119, (new Item.Properties()).group(StupidMod.GROUP)).setRegistryName(namePooMooshroomSpawnEgg),
                    new SpawnEggItem(StupidModEntities.POO_PIG, 15771042, 14377823, (new Item.Properties()).group(StupidMod.GROUP)).setRegistryName(namePooPigSpawnEgg),
                    new SpawnEggItem(StupidModEntities.POO_SHEEP, 15198183, 16758197, (new Item.Properties()).group(StupidMod.GROUP)).setRegistryName(namePooSheepSpawnEgg)
            );
        }
    }
}
