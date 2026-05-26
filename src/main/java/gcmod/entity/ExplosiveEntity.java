package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ExplosiveEntity extends Entity
{
    protected static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId( ExplosiveEntity.class, EntityDataSerializers.INT );

    protected int explosionRadius;

    public LivingEntity instigator;

    public ExplosiveEntity( EntityType<?> type, Level world )
    {
        super( type, world );

        this.entityData.set( FUSE, 80 );
        this.explosionRadius = 1;
        this.blocksBuilding = true;
    }

    public void startFuse( int fuseIn )
    {
        this.entityData.set( FUSE, fuseIn );

        if ( fuseIn <= 0 )
        {
            onFuseComplete();
        }
        else if ( !level().isClientSide() )
        {
            float randAngle = level().getRandom().nextFloat() * (float) (Math.PI * 2);
            this.setDeltaMovement( -Math.sin( randAngle ) * 0.02, 0.2F, -Math.cos( randAngle ) * 0.02 );
        }
    }

    public int getFuse()
    {
        return this.entityData.get( FUSE );
    }

    @Override
    protected void defineSynchedData( SynchedEntityData.Builder builder )
    {
        builder.define( FUSE, 80 );
    }

    @Override
    protected void readAdditionalSaveData( ValueInput view )
    {
        this.entityData.set( FUSE, (int)view.getShortOr( "Fuse", (short)0 ) );
        this.explosionRadius = view.getShortOr( "Strength", (short)0 );
    }

    @Override
    protected void addAdditionalSaveData( ValueOutput view )
    {
        view.putShort( "Fuse", (short) this.getFuse() );
        view.putShort( "Strength", (short) this.explosionRadius );
    }

    @Override
    protected Entity.MovementEmission getMovementEmission()
    {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean hurtServer( ServerLevel world, DamageSource source, float amount )
    {
        return false;
    }

    @Override
    public boolean isPickable()
    {
        return !this.isRemoved();
    }

    @Override
    protected double getDefaultGravity()
    {
        return 0.04;
    }

    @Override
    public void tick()
    {
        this.handlePortal();
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.applyEffectsFromBlocks();
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }

        final int fuse = this.getFuse();
        this.entityData.set( FUSE, fuse - 1 );

        if ( fuse > 0 )
        {
            this.updateFluidInteraction();
            if ( this.level().isClientSide() )
                this.level().addParticle( ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0 );
        }
        else if ( fuse == 0 )
        {
            this.onFuseComplete();
        }
    }

    protected void onFuseComplete()
    {
        if ( !this.level().isClientSide() )
        {
            this.discard();
            this.level().explode( this, this.getX(), this.getY( 0.0625 ), this.getZ(), this.explosionRadius, Level.ExplosionInteraction.TNT );
        }
    }

    public BlockState getBlockState()
    {
        return GCMod.BLAST_TNT.defaultBlockState();
    }
}
