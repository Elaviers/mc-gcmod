package stupidmod;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
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
import stupidmod.entity.mob.PooCowEntity;
import stupidmod.entity.mob.PooPigEntity;
import stupidmod.entity.mob.PooSheepEntity;
import stupidmod.entity.tile.CentrifugeTileEntity;
import stupidmod.entity.tile.ExplosiveTileEntity;
import stupidmod.entity.tile.WirelessTorchTileEntity;

@ObjectHolder(StupidMod.id)
public class StupidModEntities {
    
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
    public static EntityType<PooEntity> POO;
    
    @ObjectHolder(namePooBrick)
    public static EntityType<PooBrickEntity> POO_BRICK;
    
    @ObjectHolder(nameExplosivePoo)
    public static EntityType<PooExplosiveEntity> POO_EXPLOSIVE;
    
    @ObjectHolder(nameExplosive)
    public static EntityType<ExplosiveEntity> EXPLOSIVE;
    
    @ObjectHolder(nameConstructiveExplosive)
    public static EntityType<ConstructiveExplosiveEntity> CONSTRUCTIVE_EXPLOSIVE;

    @ObjectHolder(nameDigExplosive)
    public static EntityType<DigExplosiveEntity> DIG_EXPLOSIVE;
    
    @ObjectHolder(nameAirstrikeExplosive)
    public static EntityType<AirStrikeExplosiveEntity> AIR_STRIKE_EXPLOSIVE;
    
    @ObjectHolder(nameImpactExplosive)
    public static EntityType<ImpactExplosiveEntity> IMPACT_EXPLOSIVE;
    
    @ObjectHolder(namePooCow)
    public static EntityType<PooCowEntity> POO_COW;
    
    @ObjectHolder(namePooPig)
    public static EntityType<PooPigEntity> POO_PIG;
    
    @ObjectHolder(namePooSheep)
    public static EntityType<PooSheepEntity> POO_SHEEP;
    
    @ObjectHolder(nameCentrifuge)
    public static TileEntityType<CentrifugeTileEntity> TE_CENTRIFUGE;
    
    @ObjectHolder(nameExplosiveTE)
    public static TileEntityType<ExplosiveTileEntity> TE_EXPLOSIVE;
    
    @ObjectHolder(nameWirelessTorch)
    public static TileEntityType<WirelessTorchTileEntity> TE_WIRELESS_TORCH;

    public static void createEntities()
    {
        POO = EntityType.Builder.<PooEntity>create(PooEntity::new, EntityClassification.MISC)
                .size(0.8f, 0.25f)
                .setTrackingRange(64)
                .setUpdateInterval(20)
                .build(namePoo);
        POO.setRegistryName(namePoo);

       POO_BRICK = EntityType.Builder.<PooBrickEntity>create(PooBrickEntity::new, EntityClassification.MISC)
                .size(0.5f, 0.25f)
                .setTrackingRange(64)
                .setUpdateInterval(20)
                .build(namePooBrick);
       POO_BRICK.setRegistryName(namePooBrick);


        POO_EXPLOSIVE = EntityType.Builder.<PooExplosiveEntity>create(PooExplosiveEntity::new, EntityClassification.MISC)
                .size(0.33f, 0.33f)
                .setTrackingRange(256)
                .setUpdateInterval(20)
                .build(nameExplosivePoo);
        POO_EXPLOSIVE.setRegistryName(nameExplosivePoo);

        EXPLOSIVE = EntityType.Builder.<ExplosiveEntity>create(ExplosiveEntity::new, EntityClassification.MISC)
                        .size(0.98f, 0.98f)
                        .setTrackingRange(160)
                        .setUpdateInterval(10)
                        .build(nameExplosive);
        EXPLOSIVE.setRegistryName(nameExplosive);

        CONSTRUCTIVE_EXPLOSIVE = EntityType.Builder.<ConstructiveExplosiveEntity>create(ConstructiveExplosiveEntity::new, EntityClassification.MISC)
                .size(0.98f, 0.98f)
                .setTrackingRange(160)
                .setUpdateInterval(10)
                .build(nameConstructiveExplosive);
        CONSTRUCTIVE_EXPLOSIVE.setRegistryName(nameConstructiveExplosive);

        DIG_EXPLOSIVE = EntityType.Builder.<DigExplosiveEntity>create(DigExplosiveEntity::new, EntityClassification.MISC)
                        .size(0.98f, 0.98f)
                        .setTrackingRange(256)
                        .setUpdateInterval(10)
                        .build(nameDigExplosive);
        DIG_EXPLOSIVE.setRegistryName(nameDigExplosive);

        AIR_STRIKE_EXPLOSIVE = EntityType.Builder.<AirStrikeExplosiveEntity>create(AirStrikeExplosiveEntity::new, EntityClassification.MISC)
                        .size(0.98f, 0.98f)
                        .setTrackingRange(256)
                        .setUpdateInterval(10)
                        .build(nameAirstrikeExplosive);
        AIR_STRIKE_EXPLOSIVE.setRegistryName(nameAirstrikeExplosive);

        IMPACT_EXPLOSIVE = EntityType.Builder.<ImpactExplosiveEntity>create(ImpactExplosiveEntity::new, EntityClassification.MISC)
                        .size(0.33f, 0.33f)
                        .setTrackingRange(256)
                        .setUpdateInterval(10)
                        .build(nameImpactExplosive);
        IMPACT_EXPLOSIVE.setRegistryName(nameImpactExplosive);

        POO_COW = EntityType.Builder.<PooCowEntity>create(PooCowEntity::new, EntityClassification.CREATURE)
                        .size(EntityType.COW.getSize().width, EntityType.COW.getSize().height)
                        .setTrackingRange(80)
                        .setUpdateInterval(3)
                        .build(namePooCow);
        POO_COW.setRegistryName(namePooCow);

        POO_PIG = EntityType.Builder.<PooPigEntity>create(PooPigEntity::new, EntityClassification.CREATURE)
                        .size(EntityType.PIG.getSize().width, EntityType.PIG.getSize().height)
                        .setTrackingRange(80)
                        .setUpdateInterval(3)
                        .build(namePooPig);
        POO_PIG.setRegistryName(namePooPig);

        POO_SHEEP = EntityType.Builder.<PooSheepEntity>create(PooSheepEntity::new, EntityClassification.CREATURE)
                        .size(EntityType.SHEEP.getSize().width, EntityType.SHEEP.getSize().height)
                        .setTrackingRange(80)
                        .setUpdateInterval(3)
                        .build(namePooSheep);
        POO_SHEEP.setRegistryName(namePooSheep);
    }

    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration
    {
        @SubscribeEvent
        static void registerEntities(RegistryEvent.Register<EntityType<?>> register)
        {
            register.getRegistry().registerAll(
                POO, POO_BRICK, POO_EXPLOSIVE, EXPLOSIVE, CONSTRUCTIVE_EXPLOSIVE, DIG_EXPLOSIVE, AIR_STRIKE_EXPLOSIVE, IMPACT_EXPLOSIVE, POO_COW, POO_PIG, POO_SHEEP
            );
        }
        
        @SubscribeEvent
        static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> register)
        {
            register.getRegistry().registerAll(
                    TileEntityType.Builder.create(CentrifugeTileEntity::new, StupidModBlocks.CENTRIFUGE).build(null).setRegistryName(nameCentrifuge),
                    TileEntityType.Builder.create(ExplosiveTileEntity::new, StupidModBlocks.AIR_STRIKE_TNT, StupidModBlocks.BLAST_TNT, StupidModBlocks.CONSTRUCTIVE_TNT, StupidModBlocks.DIG_TNT).build(null).setRegistryName(nameExplosiveTE),
                    TileEntityType.Builder.create(WirelessTorchTileEntity::new, StupidModBlocks.WIRELESS_TORCH, StupidModBlocks.WIRELESS_TORCH_WALL).build(null).setRegistryName(nameWirelessTorch)
            );
        }
        
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(POO,                    PooEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(POO_BRICK,              PooBrickEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(POO_EXPLOSIVE,          PooExplosiveEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EXPLOSIVE,              manager -> new ExplosiveEntityRenderer(manager, StupidModBlocks.BLAST_TNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(CONSTRUCTIVE_EXPLOSIVE, manager -> new ExplosiveEntityRenderer(manager, StupidModBlocks.CONSTRUCTIVE_TNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(DIG_EXPLOSIVE,          manager -> new ExplosiveEntityRenderer(manager, StupidModBlocks.DIG_TNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(AIR_STRIKE_EXPLOSIVE,   manager -> new ExplosiveEntityRenderer(manager, StupidModBlocks.AIR_STRIKE_TNT.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(IMPACT_EXPLOSIVE,       manager -> new ImpactExplosiveEntityRenderer(manager, StupidModBlocks.BLAST_TNT.getDefaultState(), .5f));
        RenderingRegistry.registerEntityRenderingHandler(POO_COW,                CowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(POO_PIG,                PigRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(POO_SHEEP,              SheepRenderer::new);

        ClientRegistry.bindTileEntityRenderer(TE_CENTRIFUGE, CentrifugeTileEntityRenderer::new);
    }
    
}
