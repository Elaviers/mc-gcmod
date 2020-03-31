package stupidmod.entity.mob;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;
import stupidmod.StupidModSounds;
import stupidmod.entity.PooEntity;

public class PooCowEntity extends CowEntity {
    int PooDropTimer;
    
    public PooCowEntity(EntityType<? extends PooCowEntity> type, World world) {
        super(type, world);
        PooDropTimer = this.rand.nextInt(400)+800;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(StupidModSounds.FART, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
            
            this.world.addEntity(new PooEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ()));
            if (this.isChild())this.PooDropTimer = this.rand.nextInt(400) + 100;
            else this.PooDropTimer = this.rand.nextInt(1200) + 150;
        }
    }
    
    public CowEntity createChild(AgeableEntity ageable)
    {
        return new PooCowEntity(StupidModEntities.POO_COW, this.world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
