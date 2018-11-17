package StupidMod;

import StupidMod.Entities.*;
import StupidMod.Entities.Mob.EntityPooCow;
import StupidMod.Entities.Mob.EntityPooPig;
import StupidMod.Entities.Mob.EntityPooSheep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class EntityRegister {
    
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    void registerEntities(RegistryEvent.Register<EntityEntry> register)
    {
        int netID = 0;
        
        EntityEntry explosiveBlast = EntityEntryBuilder.create()
                .entity(EntityExplosive.class)
                .name("Explosive")
                .id("explosive_blast", netID++)
                .tracker(160, 10, true)
                .build();
    
        EntityEntry explosiveConstructive = EntityEntryBuilder.create()
                .entity(EntityConstructiveExplosive.class)
                .name("ConstructiveExplosive")
                .id("explosive_constructive", netID++)
                .tracker(160, 10, true)
                .build();
    
        EntityEntry explosiveDig = EntityEntryBuilder.create()
                .entity(EntityDigExplosive.class)
                .name("DigExplosive")
                .id("explosive_dig", netID++)
                .tracker(256, 10, true)
                .build();
        
        EntityEntry explosiveAirStrike = EntityEntryBuilder.create()
                .entity(EntityAirStrikeExplosive.class)
                .name("AirstrikeExplosive")
                .id("explosive_airstrike", netID++)
                .tracker(256, 10, true)
                .build();
    
        EntityEntry explosiveImpact = EntityEntryBuilder.create()
                .entity(EntityImpactExplosive.class)
                .name("ImpactExplosive")
                .id("explosive_impact", netID++)
                .tracker(256, 10, true)
                .build();
    
        EntityEntry poo = EntityEntryBuilder.create()
                .entity(EntityPoo.class)
                .name("Poo")
                .id("poo", netID++)
                .tracker(64, 20, false)
                .build();
    
        EntityEntry pooBrick = EntityEntryBuilder.create()
                .entity(EntityPooBrick.class)
                .name("PooBrick")
                .id("poo_brick", netID++)
                .tracker(64, 80, true)
                .build();
    
        EntityEntry explosivePoo = EntityEntryBuilder.create()
                .entity(EntityPooExplosive.class)
                .name("ExplosivePoo")
                .id("explosive_poo", netID++)
                .tracker(256, 20, true)
                .build();
    
        EntityEntry pooPig = EntityEntryBuilder.create()
                .entity(EntityPooPig.class)
                .name("Pig")
                .id("poo_pig", 90)
                .tracker(80, 3, true)
                .build();
    
        EntityEntry pooSheep = EntityEntryBuilder.create()
                .entity(EntityPooSheep.class)
                .name("Sheep")
                .id("poo_sheep", 91)
                .tracker(80, 3, true)
                .build();
    
        EntityEntry pooCow = EntityEntryBuilder.create()
                .entity(EntityPooCow.class)
                .name("Cow")
                .id("poo_cow", 92)
                .tracker(80, 3, true)
                .build();
        
        register.getRegistry().registerAll(explosiveBlast, explosiveConstructive, explosiveDig, explosiveAirStrike, explosiveImpact, poo, pooBrick, explosivePoo, pooPig, pooSheep, pooCow);
    }
}
