package StupidMod.Entities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityExplosive extends Entity
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
    
    protected int fuse;
    protected int explosionRadius;
    
    public EntityExplosive(World world)
    {
        super(world);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }
    
    public EntityExplosive(World world, double x, double y, double z, int fuse, int strength)
    {
        super(world);
        
        this.setPosition(x, y, z);
        this.explosionRadius = strength;
        
        float f = (float) (Math.random() * (Math.PI * 2D));
        this.motionX = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;

        this.setFuse(fuse);
    }
    
    public boolean exploded() {
        if (this.fuse <= 0)
        {
            this.onFuseCompleted();
            return true;
        }
        
        return false;
    }
    
    protected void onFuseCompleted()
    {
        this.setDead();
    
        if (!this.world.isRemote)
            this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, this.explosionRadius, true);
    }
    
    @Override
    protected void entityInit()
    {
        this.dataManager.register(FUSE, Integer.valueOf(80));
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.fuse = (int)compound.getShort("Fuse");
        this.explosionRadius = (int)compound.getShort("Strength");
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Fuse", (short)this.fuse);
        compound.setShort("Strength", (short)this.explosionRadius);
    }
    
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }
        
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        
        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }
        
        --this.fuse;
        
        if (this.fuse <= 0)
        {
            this.onFuseCompleted();
        }
        else
        {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    protected boolean canTriggerWalking() { return false; }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    @Override
    public float getEyeHeight()
    {
        return 0.0F;
    }
    
    public void setFuse(int fuseIn)
    {
        this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }
    
    public int getFuseDataManager()
    {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }
    
    
    public int getFuse()
    {
        return fuse;
    }
    
    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
    }
}
