package stupidmod.entity.mob;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;
import stupidmod.EntityRegister;
import stupidmod.SoundRegister;
import stupidmod.entity.EntityPoo;

public class EntityPooSheep extends EntitySheep {
    int PooDropTimer;
    
    public EntityPooSheep(World world) {
        super(world);
        PooDropTimer = this.rand.nextInt(1000)+450;
    }
    
    @Override
    public EntityType<?> getType() {
        return EntityRegister.entityPooSheep;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(SoundRegister.soundFart, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
            
            this.world.spawnEntity(new EntityPoo(this.world, this.posX, this.posY, this.posZ));
            if (this.isChild())this.PooDropTimer = this.rand.nextInt(550) + 150;
            else this.PooDropTimer = this.rand.nextInt(1334) + 200;
        }
    }
    
    public EntitySheep createChild(EntityAgeable ageable)
    {
        return new EntityPooSheep(this.world);
    }
    
    
}
