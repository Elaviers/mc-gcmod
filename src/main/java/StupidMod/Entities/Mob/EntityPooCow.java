package StupidMod.Entities.Mob;

import StupidMod.Entities.EntityPoo;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityPooCow extends EntityCow {
    int PooDropTimer;
    
    public EntityPooCow(World world) {
        super(world);
        PooDropTimer = this.rand.nextInt(400)+800;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(new SoundEvent(new ResourceLocation("stupidmod:sound.fart")), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
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
