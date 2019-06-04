package stupidmod.entity.mob;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.world.World;
import stupidmod.EntityRegister;
import stupidmod.SoundRegister;
import stupidmod.entity.EntityPoo;

public class EntityPooCow extends EntityCow {
    int PooDropTimer;
    
    public EntityPooCow(World world) {
        super(EntityRegister.entityPooCow, world);
        PooDropTimer = this.rand.nextInt(400)+800;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(SoundRegister.soundFart, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
            
            this.world.spawnEntity(new EntityPoo(this.world, this.posX, this.posY, this.posZ));
            if (this.isChild())this.PooDropTimer = this.rand.nextInt(400) + 100;
            else this.PooDropTimer = this.rand.nextInt(1200) + 150;
        }
    }
    
    public EntityCow createChild(EntityAgeable ageable)
    {
        return new EntityPooCow(this.world);
    }
    
    
    
}
