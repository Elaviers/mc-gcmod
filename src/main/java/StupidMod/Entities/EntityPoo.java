package StupidMod.Entities;

import StupidMod.StupidMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPoo extends Entity {
    public float size, prevSize;
    float targetSize;
    float shrinkRate;
    float timer;
    boolean doDrop;
    
    public EntityPoo(World world) {
        super(world);
        this.preventEntitySpawning = true;
    
        this.targetSize = this.world.rand.nextFloat() * .8f + .8f;
        this.setSize(this.targetSize - .1875f,this.targetSize - .1875f);
        this.size = 0.001f;
        
        this.timer = 1200;
    }
    
    public EntityPoo(World world, double x, double y, double z) {
        this(world);
        
        this.setPosition(x, y, z);
    }
    
    void breakPoo() {
        this.setDead();
        
        if (this.doDrop)
            this.dropItem(StupidMod.instance.items.itemPoo, this.size > 1.1f ? 2 : 1);
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!(source.getTrueSource() instanceof EntityPlayer)) {
            this.setDead();
            return false;
        }
        
        EntityPlayer player = (EntityPlayer)source.getTrueSource();
        
        if (player.isCreative()) {
            this.doDrop = false;
            this.shrinkRate = size / 2;
        }
        else {
            boolean Condition =
                    player.getHeldItemMainhand().getItem() == Items.WOODEN_SHOVEL ||
                    player.getHeldItemMainhand().getItem() == Items.STONE_SHOVEL ||
                    player.getHeldItemMainhand().getItem() == Items.IRON_SHOVEL ||
                    player.getHeldItemMainhand().getItem() == Items.GOLDEN_SHOVEL ||
                    player.getHeldItemMainhand().getItem() == Items.DIAMOND_SHOVEL;
    
            if (Condition) {
                if (!this.world.isRemote) player.getHeldItemMainhand().damageItem(1, player);
                this.doDrop = true;
                this.shrinkRate = size / 3;
            }
        }
        return true;
    }
    
    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.size <= 0)
            this.breakPoo();
            
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevSize = this.size;
        
        if (this.shrinkRate > 0)
            this.size -= this.shrinkRate;
        else if (this.size < this.targetSize) {
            this.size += 0.1f;
            
            if (this.size >= this.targetSize)
                this.shrinkRate = 0.01f / 20;
        }
    
        if (!this.hasNoGravity())
        {
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
        }
        
        timer--;
        if (this.timer <= 0)
            this.shrinkRate += 0.01f / 20;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
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
}
