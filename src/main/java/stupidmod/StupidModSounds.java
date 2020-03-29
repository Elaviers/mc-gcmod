package stupidmod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("stupidmod")
public class StupidModSounds {
    
    private static final String
            nameFart = "sound.fart",
            nameMemeBlock = "sound.meme_block",
            namePooBlock = "sound.poo_block",
            namePooCannon = "sound.poo_cannon",
            nameCentrifuge = "sound.centrifuge",
            nameMusicBigWillies = "music_disc.big_willies",
            nameMusicGodlyPiss = "music_disc.godly_piss",
            nameMusicMassiveCrap = "music_disc.massive_crap",
            nameMusicPeaSizedAnus = "music_disc.pea_sized_anus",
            nameMusicPooeyLoo = "music_disc.pooey_loo",
            nameMusicPowerfulConstipation = "music_disc.powerful_constipation",
            nameMusicRedWeewee = "music_disc.red_weewee",
            nameMusicSmellyMethane = "music_disc.smelly_methane",
            nameMusicSymphonyOfStupidity = "music_disc.symphony_of_stupidity",
            nameMusicToiletWater = "music_disc.toilet_water";

    @ObjectHolder(nameFart)
    public static SoundEvent FART;

    @ObjectHolder(nameMemeBlock)
    public static SoundEvent MEME_BLOCK;

    @ObjectHolder(namePooBlock)
    public static SoundEvent POO_BLOCK;

    @ObjectHolder(namePooCannon)
    public static SoundEvent POO_CANNON;

    @ObjectHolder(nameCentrifuge)
    public static SoundEvent CENTRIFUGE;

    @ObjectHolder(nameMusicBigWillies)
    public static SoundEvent MUSIC_BIG_WILLIES;

    @ObjectHolder(nameMusicGodlyPiss)
    public static SoundEvent MUSIC_GODLY_PISS;

    @ObjectHolder(nameMusicMassiveCrap)
    public static SoundEvent MUSIC_MASSIVE_CRAP;

    @ObjectHolder(nameMusicPeaSizedAnus)
    public static SoundEvent MUSIC_PEA_SIZED_ANUS;

    @ObjectHolder(nameMusicPooeyLoo)
    public static SoundEvent MUSIC_POOEY_LOO;

    @ObjectHolder(nameMusicPowerfulConstipation)
    public static SoundEvent MUSIC_POWERFUL_CONSTIPATION;

    @ObjectHolder(nameMusicRedWeewee)
    public static SoundEvent MUSIC_RED_WEEWEE;

    @ObjectHolder(nameMusicSmellyMethane)
    public static SoundEvent MUSIC_SMELLY_METHANE;

    @ObjectHolder(nameMusicSymphonyOfStupidity)
    public static SoundEvent MUSIC_SYMPHONY_OF_STUPIDITY;

    @ObjectHolder(nameMusicToiletWater)
    public static SoundEvent MUSIC_TOILET_WATER;

    public static void createSoundEvents()
    {
        FART = new SoundEvent(new ResourceLocation(StupidMod.id, nameFart)).setRegistryName(nameFart);
        MEME_BLOCK = new SoundEvent(new ResourceLocation(StupidMod.id, nameMemeBlock)).setRegistryName(nameMemeBlock);
        POO_BLOCK = new SoundEvent(new ResourceLocation(StupidMod.id, namePooBlock)).setRegistryName(namePooBlock);
        POO_CANNON = new SoundEvent(new ResourceLocation(StupidMod.id, namePooCannon)).setRegistryName(namePooCannon);
        CENTRIFUGE = new SoundEvent(new ResourceLocation(StupidMod.id, nameCentrifuge)).setRegistryName(nameCentrifuge);
        MUSIC_BIG_WILLIES = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicBigWillies)).setRegistryName(nameMusicBigWillies);
        MUSIC_GODLY_PISS = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicGodlyPiss)).setRegistryName(nameMusicGodlyPiss);
        MUSIC_MASSIVE_CRAP = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicMassiveCrap)).setRegistryName(nameMusicMassiveCrap);
        MUSIC_PEA_SIZED_ANUS = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicPeaSizedAnus)).setRegistryName(nameMusicPeaSizedAnus);
        MUSIC_POOEY_LOO = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicPooeyLoo)).setRegistryName(nameMusicPooeyLoo);
        MUSIC_POWERFUL_CONSTIPATION = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicPowerfulConstipation)).setRegistryName(nameMusicPowerfulConstipation);
        MUSIC_RED_WEEWEE = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicRedWeewee)).setRegistryName(nameMusicRedWeewee);
        MUSIC_SYMPHONY_OF_STUPIDITY = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicSymphonyOfStupidity)).setRegistryName(nameMusicSymphonyOfStupidity);
        MUSIC_TOILET_WATER = new SoundEvent(new ResourceLocation(StupidMod.id, nameMusicToiletWater)).setRegistryName(nameMusicToiletWater);
    }
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration
    {
        @SubscribeEvent
        static void registerSounds(RegistryEvent.Register<SoundEvent> register)
        {
            register.getRegistry().registerAll(
                FART, MEME_BLOCK, POO_BLOCK, POO_CANNON, CENTRIFUGE, MUSIC_BIG_WILLIES, MUSIC_GODLY_PISS, MUSIC_MASSIVE_CRAP, MUSIC_PEA_SIZED_ANUS,
                    MUSIC_POOEY_LOO, MUSIC_POWERFUL_CONSTIPATION, MUSIC_RED_WEEWEE, MUSIC_SYMPHONY_OF_STUPIDITY, MUSIC_TOILET_WATER
            );
        }
    }
    
}
