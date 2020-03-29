package stupidmod;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.client.CentrifugeScreen;
import stupidmod.misc.CentrifugeContainer;

@ObjectHolder(StupidMod.id)
public class StupidModContainers {
    static private final String nameCentrifugeContainer = "centrifuge_container";

    @ObjectHolder(nameCentrifugeContainer)
    public static ContainerType<CentrifugeContainer> CENTRIFUGE;

    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        static void RegisterContainerTypes(RegistryEvent.Register<ContainerType<?>> register)
        {
            register.getRegistry().registerAll(new ContainerType<CentrifugeContainer>((IContainerFactory<CentrifugeContainer>)CentrifugeContainer::new).setRegistryName(nameCentrifugeContainer));
        }
    }

    public static void RegisterScreenFactories()
    {
        ScreenManager.registerFactory(CENTRIFUGE, CentrifugeScreen::new);
    }
}
