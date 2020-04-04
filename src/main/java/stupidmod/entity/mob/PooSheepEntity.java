package stupidmod.entity.mob;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;
import stupidmod.StupidModSounds;
import stupidmod.entity.PooEntity;

public class PooSheepEntity extends SheepEntity {
    int PooDropTimer;
    
    public PooSheepEntity(EntityType<? extends PooSheepEntity> type, World world) {
        super(StupidModEntities.POO_SHEEP, world);
        PooDropTimer = this.rand.nextInt(2000) + 160;
    }
    
    @Override
    public EntityType<?> getType() {
        return StupidModEntities.POO_SHEEP;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(StupidModSounds.FART, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
            
            this.world.addEntity(new PooEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ()));
            if (this.isChild())this.PooDropTimer = this.rand.nextInt(550) + 400;
            else this.PooDropTimer = this.rand.nextInt(1334) + 800;
        }
    }
    
    public SheepEntity createChild(AgeableEntity ageable)
    {
        return new PooSheepEntity(StupidModEntities.POO_SHEEP, this.world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


}
