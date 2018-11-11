package StupidMod.Entities;

import StupidMod.StupidMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPooBrick extends Entity {
    public float prevAngleX, prevAngleY, prevAngleZ, angleX, angleY, angleZ, spinX, spinY, spinZ;
    
    public EntityPooBrick(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.5f, 0.25f);
        this.spinX = world.rand.nextFloat() * 10 - 5;
        this.spinY = world.rand.nextFloat() * 5 - 2.5f;
        this.spinZ = world.rand.nextFloat() * 10 - 5;
    }
    
    public void breakPoo() {
        this.setDead();
        this.entityDropItem(new ItemStack(StupidMod.instance.items.itemPooBrick), 0);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!(source.getTrueSource() instanceof EntityPlayer)) {
            this.setDead();
            return false;
        }
        EntityPlayer p = (EntityPlayer)source.getTrueSource();
        if (!this.world.isRemote)
            if (!p.capabilities.isCreativeMode)
                this.breakPoo();
            else
                this.setDead();
    
        return true;
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
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
        
        if (this.onGround)
        {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
            this.motionY *= -0.5D;
            
            this.angleX = 0;
            this.angleZ = 0;
        }
        else
        {
            this.angleX += this.spinX;
            this.angleY += this.spinY;
            this.angleZ += this.spinZ;
        }
    }
    
    @Override
    protected void entityInit() {
    
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    
    }
    
    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
    
    @Override
    public float getEyeHeight()
    {
        return 0.0F;
    }
}
