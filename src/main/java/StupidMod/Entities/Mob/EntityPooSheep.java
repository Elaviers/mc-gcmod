package StupidMod.Entities.Mob;

import StupidMod.Entities.EntityPoo;
import StupidMod.StupidMod;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityPooSheep extends EntitySheep {
    int PooDropTimer;
    
    public EntityPooSheep(World world) {
        super(world);
        PooDropTimer = this.rand.nextInt(1000)+450;
    }
    
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(StupidMod.instance.sounds.soundFart, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
            
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
