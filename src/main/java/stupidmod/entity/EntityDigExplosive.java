package stupidmod.entity;

import net.minecraft.world.World;
import stupidmod.EntityRegister;

public class EntityDigExplosive extends EntityExplosive {
    double explosionY;
    int stuckTicks;
    
    public EntityDigExplosive(World world) {
        super(EntityRegister.entityDigExplosive, world);
    }
    
    public EntityDigExplosive(World world, double x, double y, double z, int fuse, int strength) {
        super(EntityRegister.entityDigExplosive, world, x, y, z, fuse, strength);
    }
    
    void makeExplosion() {
        this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, this.strength, true);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.world.isRemote) {
            if (this.posY < this.explosionY) {
                if (this.fuse % 2 == 0)
                    this.makeExplosion();
    
                if (this.posY == this.prevPosY) {
                    if (this.stuckTicks >= 10)
                        this.remove();
                    else
                        this.stuckTicks++;
                }
                else this.stuckTicks = 0;
            }
            else if (this.explosionY > 0 && this.motionY == 0.0)
                this.explosionY = 1000;
        }
    }
    
    @Override
    protected void onFuseCompleted() {
        this.explosionY = this.posY + 0.5;
        this.motionY = 1.0;
    }
}
