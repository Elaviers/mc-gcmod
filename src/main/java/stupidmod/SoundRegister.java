package stupidmod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("stupidmod")
public class SoundRegister {
    
    private static final String
        nameFart = "fart",
        nameMemeBlock = "meme_block",
        namePooBlock = "poo_block",
        namePooCannon = "poo_cannon",
        nameCentrifuge = "centrifuge";
    
    public static SoundEvent soundFart, soundMemeBlock, soundPooBlock, soundPooCannon, soundCentrifuge;
    
    public static void createSoundEvents()
    {
        final String prefix = "stupidmod:sound.";
        
        soundFart = new SoundEvent(new ResourceLocation(prefix + nameFart)).setRegistryName(nameFart);
        soundMemeBlock = new SoundEvent(new ResourceLocation(prefix + nameMemeBlock)).setRegistryName(nameMemeBlock);
        soundPooBlock = new SoundEvent(new ResourceLocation(prefix + namePooBlock)).setRegistryName(namePooBlock);
        soundPooCannon = new SoundEvent(new ResourceLocation(prefix + namePooCannon)).setRegistryName(namePooCannon);
        soundCentrifuge = new SoundEvent(new ResourceLocation(prefix + nameCentrifuge)).setRegistryName(nameCentrifuge);
    }
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration
    {
        
        @SubscribeEvent
        static void registerSounds(RegistryEvent.Register<SoundEvent> register)
        {
            register.getRegistry().registerAll(
                    soundFart,
                    soundMemeBlock,
                    soundPooBlock,
                    soundPooCannon,
                    soundCentrifuge
            );
        }
    }
    
}
