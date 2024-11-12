package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ExplosiveEntity extends Entity
{
    protected static final TrackedData<Integer> FUSE = DataTracker.registerData( ExplosiveEntity.class, TrackedDataHandlerRegistry.INTEGER );

    protected int explosionRadius;

    public LivingEntity instigator;

    public ExplosiveEntity( EntityType<?> type, World world )
    {
        super( type, world );

        this.dataTracker.set( FUSE, 80 );
        this.explosionRadius = 1;
        this.intersectionChecked = true;
    }

    public void startFuse( int fuseIn )
    {
        this.dataTracker.set( FUSE, fuseIn );

        if ( fuseIn <= 0 )
        {
            onFuseComplete();
        }
        else if ( !getWorld().isClient )
        {
            float randAngle = getWorld().random.nextFloat() * (float) (Math.PI * 2);
            this.setVelocity( -Math.sin( randAngle ) * 0.02, 0.2F, -Math.cos( randAngle ) * 0.02 );
        }
    }

    public int getFuse()
    {
        return this.dataTracker.get( FUSE );
    }

    @Override
    protected void initDataTracker( DataTracker.Builder builder )
    {
        builder.add( FUSE, 80 );
    }

    @Override
    protected void readCustomDataFromNbt( NbtCompound nbt )
    {
        this.dataTracker.set( FUSE, (int)nbt.getShort( "Fuse" ) );
        this.explosionRadius = nbt.getShort( "Strength" );
    }

    @Override
    protected void writeCustomDataToNbt( NbtCompound nbt )
    {
        nbt.putShort( "Fuse", (short) this.getFuse() );
        nbt.putShort( "Strength", (short) this.explosionRadius );
    }

    @Override
    protected Entity.MoveEffect getMoveEffect()
    {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public boolean damage( ServerWorld world, DamageSource source, float amount )
    {
        return false;
    }

    @Override
    public boolean canHit()
    {
        return !this.isRemoved();
    }

    @Override
    protected double getGravity()
    {
        return 0.04;
    }

    @Override
    public void tick()
    {
        this.applyGravity();
        this.move( MovementType.SELF, this.getVelocity() );
        this.setVelocity( this.getVelocity().multiply( 0.98 ) );
        if ( this.isOnGround() )
            this.setVelocity( this.getVelocity().multiply( 0.7, -0.5, 0.7 ) );

        final int fuse = this.getFuse();

        this.dataTracker.set( FUSE, fuse - 1 );

        if ( fuse > 0 )
        {
            this.updateWaterState();
            if ( this.getWorld().isClient )
                this.getWorld().addParticle( ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0 );
        }
        else if ( fuse == 0 )
        {
            this.onFuseComplete();
        }
    }

    protected void onFuseComplete()
    {
        if ( !this.getWorld().isClient )
        {
            this.discard();
            this.getWorld().createExplosion( this, this.getX(), this.getBodyY( 0.0625 ), this.getZ(), this.explosionRadius, World.ExplosionSourceType.TNT );
        }
    }

    public BlockState getBlockState()
    {
        return GCMod.BLAST_TNT.getDefaultState();
    }
}
