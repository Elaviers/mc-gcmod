package StupidMod.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityImpactExplosive extends Entity {
    private int explosionRadius;
    
    private float spinX, spinY, spinZ;
    public float prevAngleX, prevAngleY, prevAngleZ;
    public float angleX, angleY, angleZ;
    
    public EntityImpactExplosive(World worldIn) {
        super(worldIn);
        this.setSize(.33f,.33f);
        this.spinX = world.rand.nextFloat() * 20 - 10;
        this.spinY = world.rand.nextFloat() * 20 - 10;
        this.spinZ = world.rand.nextFloat() * 20 - 10;
    }
    
    public EntityImpactExplosive(World world, double x, double y, double z, int explosionRadius)
    {
        super(world);
        
        this.setPosition(x, y, z);
        this.explosionRadius = explosionRadius;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevAngleX = this.angleX;
        this.prevAngleY = this.angleY;
        this.prevAngleZ = this.angleZ;
    
        if (!this.hasNoGravity()) {
            this.motionY -= 0.04D;
        }
    
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.angleX += this.spinX;
        this.angleY += this.spinY;
        this.angleZ += this.spinZ;
        
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
        
        if (this.collided)
        {
            this.setDead();
            if(!this.world.isRemote)
                this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, this.explosionRadius, true);
        }
        
    }
    
    @Override
    protected void entityInit() {
    
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.explosionRadius = compound.getShort("Strength");
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Strength", (short)this.explosionRadius);
    }
    
    @Override
    public float getEyeHeight() {
        return 0;
    }
    
    @Override
    protected boolean canTriggerWalking() { return false; }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
}
