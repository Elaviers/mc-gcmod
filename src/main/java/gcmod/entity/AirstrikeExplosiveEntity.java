package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AirstrikeExplosiveEntity extends ExplosiveEntity
{
    public int spread;
    public int pieces;
    public int height;

    private boolean activated;

    private double initialY;

    public AirstrikeExplosiveEntity( EntityType<?> type, Level world )
    {
        super( type, world );
    }

    public static AirstrikeExplosiveEntity create( Level world, double x, double y, double z, int strength, int spread, int pieces, int height )
    {
        AirstrikeExplosiveEntity explosive = GCMod.AIRSTRIKE_EXPLOSIVE_ENTITY.create( world, EntitySpawnReason.TRIGGERED );

        explosive.setPos( x, y, z );
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

        if ( this.getDeltaMovement().y < 1.f )
            this.setDeltaMovement( this.getDeltaMovement().add( 0d, 0.02d, 0d ) );

        this.move( MoverType.SELF, this.getDeltaMovement() );
        this.setDeltaMovement( this.getDeltaMovement().multiply( 0.999d, 1d, 0.999d ) );

        if ( (this.verticalCollision || this.getY() - this.initialY >= height) && !this.level().isClientSide() )
        {
            this.discard();
            this.airStrike();
        }

        if ( this.level().isClientSide() )
            this.level().addParticle( ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() - 0.25, this.getZ(), 0.0, -.25d, 0.0 );
    }

    @Override
    protected void onFuseComplete()
    {
        this.activated = true;
    }

    protected void airStrike()
    {
        this.level().explode( this, this.getX(), this.getY(), this.getZ(), 5, Level.ExplosionInteraction.TNT );
        double AngleStep = 2 * Math.PI / this.pieces;
        double xmot, ymot, zmot;
        final float mod = spread * .12f;
        for ( int i = 0; i < this.pieces; i++ )
        {
            ymot = this.getDeltaMovement().y + Math.random() * mod;

            final double power = Math.random() * mod;
            xmot = Math.sin( AngleStep * i ) * power;
            zmot = Math.cos( AngleStep * i ) * power;

            createNewBomb( xmot, ymot, zmot );
        }
    }

    private void createNewBomb( double motionX, double motionY, double motionZ )
    {
        PooBrickEntity bomb = GCMod.EXPLOSIVE_BOMB_ENTITY.create( level(), EntitySpawnReason.TRIGGERED );
        bomb.setPos( this.position() );

        if ( this.instigator instanceof Player player )
            bomb.thrower = player;

        bomb.explosionRadius = Math.max( 1, this.explosionRadius );
        bomb.setDeltaMovement( motionX, motionY, motionZ );
        level().addFreshEntity( bomb );
    }

    @Override
    protected void addAdditionalSaveData( ValueOutput view )
    {
        super.addAdditionalSaveData( view );
        view.putShort( "Spread", (short)this.spread );
        view.putShort( "Pieces", (short)this.pieces );
        view.putShort( "Height", (short)this.height );
    }

    @Override
    protected void readAdditionalSaveData( ValueInput view )
    {
        super.readAdditionalSaveData( view );
        this.spread = view.getShortOr( "Spread", (short)0 );
        this.pieces = view.getShortOr( "Pieces", (short)0 );
        this.height = view.getShortOr( "Height", (short)0 );
        this.initialY = this.getY(); // chances are the fuse was active. if we were midair - who cares?
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.AIRSTRIKE_TNT.defaultBlockState();
    }
}
