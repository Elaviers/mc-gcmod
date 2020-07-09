package stupidmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;

public class AirStrikeExplosiveEntity extends ExplosiveEntity {
    private int spread;
    private int pieces;
    private int height;
    
    private boolean activated;
    
    private double initialY;
    
    public AirStrikeExplosiveEntity(EntityType<? extends AirStrikeExplosiveEntity> type, World world) {
        super(type, world);
    }
    
    public AirStrikeExplosiveEntity(World world, double x, double y, double z, int fuse, int strength, int spread, int pieces, int height) {
        super(StupidModEntities.AIR_STRIKE_EXPLOSIVE, world, x, y, z, fuse, strength);
        
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
            this.prevPosX = this.getPosX();
            this.prevPosY = this.getPosY();
            this.prevPosZ = this.getPosZ();

            this.setMotion(this.getMotion().add(0d, 0.5d, 0d));

            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().mul(0.98d, 0d, 0.98d));
    
            if((this.collidedVertically || this.getPosY() - this.initialY >= height) && !this.world.isRemote) {
                this.remove();
                this.airStrike();
            }
    
            this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() - 0.25D, this.getPosZ(), 0.0D, -0.25D, 0.0D);
        }
    }
    
    @Override
    protected void onFuseCompleted() {
        this.activated = true;
    }
    
    protected void airStrike()
    {
        this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 5, Explosion.Mode.BREAK);
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
        ImpactExplosiveEntity bomb = new ImpactExplosiveEntity(world, this.getPosX(), this.getPosY(), this.getPosZ(), this.strength);
        bomb.setMotion(motionX, motionY, motionZ);
        world.addEntity(bomb);
    }
    
    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        
        compound.putShort("Spread", (short)this.spread);
        compound.putShort("Pieces", (short)this.pieces);
        compound.putShort("Height", (short)this.height);
    }
    
    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        
        this.spread = compound.getShort("Spread");
        this.pieces = compound.getShort("Pieces");
        this.height = compound.getShort("Height");
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
