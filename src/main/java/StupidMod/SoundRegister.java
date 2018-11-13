package StupidMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundRegister {
    public SoundEvent soundFart;
    public SoundEvent soundMemeBlock;
    public SoundEvent soundPooBlock;
    public SoundEvent soundPooCannon;
    public SoundEvent soundCentrifuge;
    
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
        
        soundFart = new SoundEvent(new ResourceLocation("stupidmod:sound.fart")).setRegistryName("fart");
        soundMemeBlock = new SoundEvent(new ResourceLocation("stupidmod:sound.meme_block")).setRegistryName("meme_block");
        soundPooBlock = new SoundEvent(new ResourceLocation("stupidmod:sound.poo_block")).setRegistryName("poo_block");
        soundPooCannon = new SoundEvent(new ResourceLocation("stupidmod:sound.poo_cannon")).setRegistryName("poo_cannon");
        soundCentrifuge = new SoundEvent(new ResourceLocation("stupidmod:sound.centrifuge")).setRegistryName("centrifuge");
    }
    
    @SubscribeEvent
    void registerSounds(RegistryEvent.Register<SoundEvent> register) {
        register.getRegistry().registerAll(soundFart, soundMemeBlock, soundPooBlock, soundPooCannon, soundCentrifuge);
    }
}