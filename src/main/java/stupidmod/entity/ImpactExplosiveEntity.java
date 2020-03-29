package stupidmod.entity;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;

public class ImpactExplosiveEntity extends Entity {
    private int explosionRadius;
    
    private float spinX, spinY, spinZ;
    public float prevAngleX, prevAngleY, prevAngleZ;
    public float angleX, angleY, angleZ;

    public ImpactExplosiveEntity(EntityType<? extends ImpactExplosiveEntity> type, World world)
    {
        super(type, world);
        this.spinX = world.rand.nextFloat() * 20 - 10;
        this.spinY = world.rand.nextFloat() * 20 - 10;
        this.spinZ = world.rand.nextFloat() * 20 - 10;
    }

    public ImpactExplosiveEntity(World worldIn) {
        this(StupidModEntities.IMPACT_EXPLOSIVE, worldIn);
    }
    
    public ImpactExplosiveEntity(EntityType<? extends ImpactExplosiveEntity> type, World world, double x, double y, double z, int explosionRadius)
    {
        this(type, world);
        
        this.setPosition(x, y, z);
        this.explosionRadius = explosionRadius;
    }

    public ImpactExplosiveEntity(World world, double x, double y, double z, int explosionRadius)
    {
        this(StupidModEntities.IMPACT_EXPLOSIVE, world, x, y, z, explosionRadius);
    }
    
    @Override
    protected void registerData() {
    
    }
    
    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevAngleX = this.angleX;
        this.prevAngleY = this.angleY;
        this.prevAngleZ = this.angleZ;
    
        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0d, -0.04d, 0d));
        }
    
        this.move(MoverType.SELF, this.getMotion());
        this.angleX += this.spinX;
        this.angleY += this.spinY;
        this.angleZ += this.spinZ;

        this.setMotion(this.getMotion().scale(0.98d));

        if (this.collided)
        {
            this.remove();
            if(!this.world.isRemote)
                this.world.createExplosion(this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ, this.explosionRadius, Explosion.Mode.BREAK);
        }
        
    }
    
    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.explosionRadius = compound.getShort("Strength");
    }
    
    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Strength", (short)this.explosionRadius);
    }
    
    @Override
    public float getEyeHeight(Pose pose, EntitySize size) {
        return 0;
    }

    @Override
    protected boolean canTriggerWalking() { return false; }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.removed;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
