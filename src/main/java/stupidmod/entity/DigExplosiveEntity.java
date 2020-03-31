package stupidmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;

public class DigExplosiveEntity extends ExplosiveEntity {
    double explosionY;
    int stuckTicks;
    
    public DigExplosiveEntity(EntityType<? extends DigExplosiveEntity> type, World world) {
        super(type, world);
    }
    
    public DigExplosiveEntity(World world, double x, double y, double z, int fuse, int strength) {
        super(StupidModEntities.DIG_EXPLOSIVE, world, x, y, z, fuse, strength);
    }
    
    void makeExplosion() {
        this.world.createExplosion(this, this.getPosX(), this.getPosY() + (double)(this.getHeight() / 16.0F), this.getPosZ(), this.strength, Explosion.Mode.BREAK);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.world.isRemote) {
            if (this.getPosY() < this.explosionY) {
                if (this.fuse % 2 == 0)
                    this.makeExplosion();
    
                if (this.getPosY() == this.prevPosY) {
                    if (this.stuckTicks >= 10)
                        this.remove();
                    else
                        this.stuckTicks++;
                }
                else this.stuckTicks = 0;
            }
            else if (this.explosionY > 0 && this.getMotion().y == 0.0)
                this.explosionY = 1000;
        }
    }
    
    @Override
    protected void onFuseCompleted() {
        this.explosionY = this.getPosY() + 0.5;
        this.setMotion(0d, 1d, 0f);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
