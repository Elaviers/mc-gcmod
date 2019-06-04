package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import stupidmod.EntityRegister;

public class EntityExplosive extends Entity {
    
    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);

    protected int fuse;
    protected int strength;
    
    private boolean completedFuse;
    
    protected  EntityExplosive(EntityType type, World world)
    {
        super(type, world);
    
        this.setFuse(80);
        this.strength = 1;
        this.completedFuse = false;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }
    
    public EntityExplosive(World world) {
        this(EntityRegister.entityExplosive, world);
    }
    
    protected EntityExplosive(EntityType type, World world, double x, double y, double z, int fuse, int strength) {
        this(type, world);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (double)((float)Math.PI * 2F));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = (double)0.2F;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.setFuse(fuse);
        this.strength = strength;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    
    public EntityExplosive(World world, double x, double y, double z, int fuse, int strength) {
        this(EntityRegister.entityExplosive, world, x, y, z, fuse, strength);
    }
    
    protected void registerData() {
        this.dataManager.register(FUSE, 80);
    }
    
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public boolean canBeCollidedWith() {
        return !this.removed;
    }
    
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= (double)0.04F;
        }
        
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.98F;
        this.motionY *= (double)0.98F;
        this.motionZ *= (double)0.98F;
        if (this.onGround) {
            this.motionX *= (double)0.7F;
            this.motionZ *= (double)0.7F;
            this.motionY *= -0.5D;
        }
    
        --this.fuse;
    
        if (this.fuse <= 0 && !this.completedFuse)
        {
            this.onFuseCompleted();
            this.completedFuse = true;
        }
        else
        {
            this.handleWaterMovement();
            this.world.spawnParticle(Particles.SMOKE, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
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
        this.remove();
        
        if (!this.world.isRemote)
            this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, this.strength, true);
    }
    
    protected void writeAdditional(NBTTagCompound compound) {
        compound.setShort("Fuse", (short)this.getFuse());
        compound.setShort("Strength", (short)this.strength);
    }
    
    protected void readAdditional(NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
        this.strength = compound.getShort("Strength");
    }
    
    public float getEyeHeight() {
        return 0.0F;
    }
    
    public void setFuse(int fuseIn) {
        this.dataManager.set(FUSE, fuseIn);
        this.fuse = fuseIn;
    }
    
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }
        
    }
    
    public int getFuseDataManager() {
        return this.dataManager.get(FUSE);
    }
    
    public int getFuse() {
        return this.fuse;
    }
    
}
