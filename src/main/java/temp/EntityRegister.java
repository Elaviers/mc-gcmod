package stupidmod;

import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.client.render.*;
import stupidmod.entity.*;
import stupidmod.entity.mob.EntityPooCow;
import stupidmod.entity.mob.EntityPooPig;
import stupidmod.entity.mob.EntityPooSheep;
import stupidmod.entity.tile.TileEntityCentrifuge;
import stupidmod.entity.tile.TileEntityExplosive;
import stupidmod.entity.tile.TileEntityWirelessTorch;

@ObjectHolder(StupidMod.id)
public class EntityRegister {
    
    private static final String
            namePoo = "poo",
            namePooBrick = "poo_brick",
            nameExplosivePoo = "explosive_poo",
            nameExplosive = "explosive",
            nameConstructiveExplosive = "constructive_explosive",
            nameDigExplosive = "dig_explosive",
            nameAirstrikeExplosive = "airstrike_explosive",
            nameImpactExplosive = "impact_explosive",
            namePooCow = "poo_cow",
            namePooPig = "poo_pig",
            namePooSheep = "poo_sheep",
            nameCentrifuge = "centrifuge",
            nameExplosiveTE = "explosive",
            nameWirelessTorch = "wireless_torch";

    @ObjectHolder(namePoo)
    public static EntityType<EntityPoo> entityPoo;
    
    @ObjectHolder(namePooBrick)
    public static EntityType<EntityPooBrick> entityPooBrick;
    
    @ObjectHolder(nameExplosivePoo)
    public static EntityType<EntityPooExplosive> entityPooExplosive;
    
    @ObjectHolder(nameExplosive)
    public static EntityType<EntityExplosive> entityExplosive;
    
    @ObjectHolder(nameConstructiveExplosive)
    public static EntityType<EntityConstructiveExplosive> entityConstructiveExplosive;

    @ObjectHolder(nameDigExplosive)
    public static EntityType<EntityDigExplosive> entityDigExplosive;
    
    @ObjectHolder(nameAirstrikeExplosive)
    public static EntityType<EntityAirStrikeExplosive> entityAirstrikeExplosive;
    
    @ObjectHolder(nameImpactExplosive)
    public static EntityType<EntityImpactExplosive> entityImpactExplosive;
    
    @ObjectHolder(namePooCow)
    public static EntityType<EntityPooCow> entityPooCow;
    
    @ObjectHolder(namePooPig)
    public static EntityType<EntityPooPig> entityPooPig;
    
    @ObjectHolder(namePooSheep)
    public static EntityType<EntityPooSheep> entityPooSheep;
    
    @ObjectHolder(nameCentrifuge)
    public static TileEntityType<TileEntityCentrifuge> tileEntityCentrifuge;
    
    @ObjectHolder(nameExplosiveTE)
    public static TileEntityType<TileEntityExplosive> tileEntityExplosiveData;
    
    @ObjectHolder(nameWirelessTorch)
    public static TileEntityType<TileEntityWirelessTorch> tileEntityWirelessTorch;
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration
    {
        @SubscribeEvent
        static void registerEntities(RegistryEvent.Register<EntityType<?>> register)
        {
            register.getRegistry().registerAll(
                    EntityType.Builder.create(EntityPoo.class, EntityPoo::new)
                            .tracker(64, 20, false)
                            .build(namePoo).setRegistryName(namePoo),
                    
                    EntityType.Builder.create(EntityPooBrick.class, EntityPooBrick::new)
                            .tracker(64, 20, true)
                            .build(namePooBrick).setRegistryName(namePooBrick),
                    
                    EntityType.Builder.create(EntityPooExplosive.class, EntityPooExplosive::new)
                            .tracker(256, 20, true)
                            .build(nameExplosivePoo)
                            .setRegistryName(nameExplosivePoo),
                    
                    EntityType.Builder.create(EntityExplosive.class, EntityExplosive::new)
                            .tracker(160, 10, true)
                            .build(nameExplosive).setRegistryName(nameExplosive),
                    
                    EntityType.Builder.create(EntityConstructiveExplosive.class, EntityConstructiveExplosive::new)
                            .tracker(160, 10, true)
                            .build(nameConstructiveExplosive).setRegistryName(nameConstructiveExplosive),
                    
                    EntityType.Builder.create(EntityDigExplosive.class, EntityDigExplosive::new)
                            .tracker(256, 10, true)
                            .build(nameDigExplosive).setRegistryName(nameDigExplosive),
                    
                    EntityType.Builder.create(EntityAirStrikeExplosive.class, EntityAirStrikeExplosive::new)
                            .tracker(256, 10, true)
                            .build(nameAirstrikeExplosive).setRegistryName(nameAirstrikeExplosive),
                    
                    EntityType.Builder.create(EntityImpactExplosive.class, EntityImpactExplosive::new)
                            .tracker(256, 10, true)
                            .build(nameImpactExplosive).setRegistryName(nameImpactExplosive),
                    
                    EntityType.Builder.create(EntityPooCow.class, EntityPooCow::new)
                            .tracker(80, 3, true)
                            .build(namePooCow).setRegistryName(namePooCow),
                    
                    EntityType.Builder.create(EntityPooPig.class, EntityPooPig::new)
                            .tracker(80, 3, true)
                            .build(namePooPig).setRegistryName(namePooPig),
                    
                    EntityType.Builder.create(EntityPooSheep.class, EntityPooSheep::new)
                            .tracker(80, 3, true)
                            .build(namePooSheep).setRegistryName(namePooSheep)
            );
        }
        
        @SubscribeEvent
        static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> register)
        {
            register.getRegistry().registerAll(
                    tileEntityCentrifuge = TileEntityType.register(nameCentrifuge, TileEntityType.Builder.create(TileEntityCentrifuge::new)),
                    tileEntityExplosiveData = TileEntityType.register(nameExplosiveTE, TileEntityType.Builder.create(TileEntityExplosive::new)),
                    tileEntityWirelessTorch = TileEntityType.register(nameWirelessTorch, TileEntityType.Builder.create(() -> new TileEntityWirelessTorch(EnumFacing.UP)))
            );
        }
        
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityPoo.class,                   RenderPoo::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPooBrick.class,              RenderPooBrick::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPooExplosive.class,          RenderPooExplosive::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosive.class,             manager -> new RenderExplosive(manager, BlockRegister.blockBlastTNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityConstructiveExplosive.class, manager -> new RenderExplosive(manager, BlockRegister.blockConstructiveTNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDigExplosive.class,          manager -> new RenderExplosive(manager, BlockRegister.blockDigTNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityAirStrikeExplosive.class,    manager -> new RenderExplosive(manager, BlockRegister.blockAirstrikeTNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityImpactExplosive.class,       manager -> new RenderImpactExplosive(manager, BlockRegister.blockDigTNT.getDefaultState(), .5f));
        RenderingRegistry.registerEntityRenderingHandler(EntityPooCow.class,                RenderCow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPooPig.class,                RenderPig::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPooSheep.class,              RenderSheep::new);
    
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCentrifuge.class, new RenderCentrifuge());
    }
    
}
