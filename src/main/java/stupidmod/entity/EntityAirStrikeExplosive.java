package stupidmod.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.init.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stupidmod.EntityRegister;

public class EntityAirStrikeExplosive extends EntityExplosive {
    private int spread;
    private int pieces;
    private int height;
    
    private boolean activated;
    
    private double initialY;
    
    public EntityAirStrikeExplosive(World world) {
        super(EntityRegister.entityAirstrikeExplosive, world);
    }
    
    public EntityAirStrikeExplosive(World world, double x, double y, double z, int fuse, int strength, int spread, int pieces, int height) {
        super(EntityRegister.entityAirstrikeExplosive, world, x, y, z, fuse, strength);
        
        this.initialY = y;
        this.spread = spread;
        this.pieces = pieces;
        this.height = height;
        this.activated = false;
    }
    
    @Override
    public void tick() {
        if (!activated)
            super.tick();
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY += 0.05;
            this.move(MoverType.SELF,this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
    
            if((this.collided || this.posY - this.initialY >= height) && !this.world.isRemote) {
                this.remove();
                this.airStrike();
            }
    
            this.world.spawnParticle(Particles.SMOKE, this.posX, this.posY - 0.25D, this.posZ, 0.0D, -0.25D, 0.0D);
        }
    }
    
    @Override
    protected void onFuseCompleted() {
        this.activated = true;
    }
    
    protected void airStrike()
    {
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5, true);
        double AngleStep = 2 * Math.PI / this.pieces;
        double xmot, ymot, zmot;
        float mod = (spread - 1) * .25f + .5f;
        for (int i = 0;i < this.pieces;i++) {
            ymot =  Math.random() * mod;
            xmot = Math.sin(AngleStep * i) * (Math.random()*mod);
            zmot = Math.cos(AngleStep * i) * (Math.random()*mod);
            createNewBomb(xmot,ymot,zmot);
        }
    }
    
    private void createNewBomb(double motionX,double motionY,double motionZ) {
        EntityImpactExplosive bomb = new EntityImpactExplosive(world, this.posX, this.posY, this.posZ, this.strength);
        bomb.motionX = motionX;
        bomb.motionY = motionY;
        bomb.motionZ = motionZ;
        world.spawnEntity(bomb);
    }
    
    @Override
    protected void writeAdditional(NBTTagCompound compound) {
        super.writeAdditional(compound);
        
        compound.setShort("Spread", (short)this.spread);
        compound.setShort("Pieces", (short)this.pieces);
        compound.setShort("Height", (short)this.height);
    }
    
    @Override
    protected void readAdditional(NBTTagCompound compound) {
        super.readAdditional(compound);
        
        this.spread = compound.getShort("Spread");
        this.pieces = compound.getShort("Pieces");
        this.height = compound.getShort("Height");
    }
}
