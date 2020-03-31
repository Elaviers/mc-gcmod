package stupidmod.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;

public class ExplosiveEntity extends Entity {
    
    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(TNTEntity.class, DataSerializers.VARINT);

    protected int fuse;
    protected int strength;
    
    private boolean completedFuse;
    
    public ExplosiveEntity(EntityType<? extends ExplosiveEntity> type, World world)
    {
        super(type, world);
    
        this.setFuse(80);
        this.strength = 1;
        this.completedFuse = false;
        this.preventEntitySpawning = true;
    }
    
    public ExplosiveEntity(World world) {
        this(StupidModEntities.EXPLOSIVE, world);
    }
    
    protected ExplosiveEntity(EntityType type, World world, double x, double y, double z, int fuse, int strength) {
        this(type, world);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (double)((float)Math.PI * 2F));
        this.setMotion((double)(-((float)Math.sin((double)f)) * 0.02F), 0.2d, (double)(-((float)Math.cos((double)f)) * 0.02F));
        this.setFuse(fuse);
        this.strength = strength;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    
    public ExplosiveEntity(World world, double x, double y, double z, int fuse, int strength) {
        this(StupidModEntities.EXPLOSIVE, world, x, y, z, fuse, strength);
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
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0d, -0.04d, 0d));
        }
        
        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98d));
        if (this.onGround) {
            this.setMotion(this.getMotion().mul(0.7d, -0.5d, 0.7d));
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
            this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
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
            this.world.createExplosion(this, this.getPosX(), this.getPosY() + (double)(this.getHeight() / 16.0F), this.getPosZ(), this.strength, Explosion.Mode.BREAK);
    }
    
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Fuse", (short)this.getFuse());
        compound.putShort("Strength", (short)this.strength);
    }
    
    protected void readAdditional(CompoundNBT compound) {
        this.setFuse(compound.getShort("Fuse"));
        this.strength = compound.getShort("Strength");
    }

    @Override
    public float getEyeHeight(Pose pose, EntitySize size) {
        return 0f;
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

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
