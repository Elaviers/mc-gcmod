package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class AirstrikeExplosiveEntity extends ExplosiveEntity
{
    public int spread;
    public int pieces;
    public int height;

    private boolean activated;

    private double initialY;

    public AirstrikeExplosiveEntity( EntityType<?> type, World world )
    {
        super( type, world );
    }

    public static AirstrikeExplosiveEntity create( World world, double x, double y, double z, int strength, int spread, int pieces, int height )
    {
        AirstrikeExplosiveEntity explosive = GCMod.AIRSTRIKE_EXPLOSIVE_ENTITY.create( world, SpawnReason.TRIGGERED );

        explosive.setPosition( x, y, z );
        explosive.explosionRadius = strength;

        explosive.initialY = y;
        explosive.spread = spread;
        explosive.pieces = pieces;
        explosive.height = height;
        explosive.activated = false;
        return explosive;
    }

    @Override
    public void tick()
    {
        if ( !activated )
        {
            super.tick();
            return;
        }

        if ( this.getVelocity().y < 1.f )
            this.setVelocity( this.getVelocity().add( 0d, 0.02d, 0d ) );

        this.move( MovementType.SELF, this.getVelocity() );
        this.setVelocity( this.getVelocity().multiply( 0.999d, 1d, 0.999d ) );

        if ( (this.verticalCollision || this.getY() - this.initialY >= height) && !this.getWorld().isClient )
        {
            this.discard();
            this.airStrike();
        }

        if ( this.getWorld().isClient )
            this.getWorld().addParticle( ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() - 0.25, this.getZ(), 0.0, -.25d, 0.0 );
    }

    @Override
    protected void onFuseComplete()
    {
        this.activated = true;
    }

    protected void airStrike()
    {
        this.getWorld().createExplosion( this, this.getX(), this.getY(), this.getZ(), 5, World.ExplosionSourceType.TNT );
        double AngleStep = 2 * Math.PI / this.pieces;
        double xmot, ymot, zmot;
        final float mod = spread * .12f;
        for ( int i = 0; i < this.pieces; i++ )
        {
            ymot = this.getVelocity().y + Math.random() * mod;

            final double power = Math.random() * mod;
            xmot = Math.sin( AngleStep * i ) * power;
            zmot = Math.cos( AngleStep * i ) * power;

            createNewBomb( xmot, ymot, zmot );
        }
    }

    private void createNewBomb( double motionX, double motionY, double motionZ )
    {
        PooBrickEntity bomb = GCMod.EXPLOSIVE_BOMB_ENTITY.create( getWorld(), SpawnReason.TRIGGERED );
        bomb.setPosition( this.getPos() );

        if ( this.instigator instanceof PlayerEntity player )
            bomb.thrower = player;

        bomb.explosionRadius = Math.max( 1, this.explosionRadius );
        bomb.setVelocity( motionX, motionY, motionZ );
        getWorld().spawnEntity( bomb );
    }

    @Override
    protected void writeCustomDataToNbt( NbtCompound nbt )
    {
        super.writeCustomDataToNbt( nbt );

        nbt.putShort( "Spread", (short) this.spread );
        nbt.putShort( "Pieces", (short) this.pieces );
        nbt.putShort( "Height", (short) this.height );
    }

    @Override
    protected void readCustomDataFromNbt( NbtCompound nbt )
    {
        super.readCustomDataFromNbt( nbt );

        this.spread = nbt.getShort( "Spread" );
        this.pieces = nbt.getShort( "Pieces" );
        this.height = nbt.getShort( "Height" );
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.AIRSTRIKE_TNT.getDefaultState();
    }
}
