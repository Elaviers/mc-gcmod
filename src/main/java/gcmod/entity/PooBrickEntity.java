package gcmod.entity;

import gcmod.GCMod;
import org.jspecify.annotations.Nullable;

import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class PooBrickEntity extends Entity
{
    public float prevAngleX, prevAngleY, prevAngleZ, angleX, angleY, angleZ, spinX, spinY, spinZ;

    public int age;

    public Player thrower;

    public int explosionRadius; // 0 == just a normal old brick

    public PooBrickEntity( EntityType<?> type, Level world )
    {
        super( type, world );
        this.spinX = world.getRandom().nextFloat() * 30 - 15;
        this.spinY = world.getRandom().nextFloat() * 20 - 10;
        this.spinZ = world.getRandom().nextFloat() * 30 - 15;
        this.angleX += this.spinX * 5;
        this.angleY += this.spinY * 5;
        this.angleZ += this.spinZ * 5;

        this.thrower = null;
        this.tickCount = 0;
        this.explosionRadius = 0;
        this.blocksBuilding = true;
    }

    public static PooBrickEntity createExplosive( EntityType<?> type, Level world )
    {
        // EXPLOSIVE_POO_BRICK_ENTITY uses this.
        // We have a separate entity type because we want clients to be able to distinguish between explosive / non-explosive bricks for the collidesWith check.

        PooBrickEntity poo = new PooBrickEntity( type, world );
        poo.explosionRadius = 1;
        return poo;
    }

    @Override
    public boolean canBeCollidedWith( @Nullable Entity entity )
    {
        // note - I've mixed in an override for players that are the thrower so that they don't get hit by outgoing dung flings
        return true;
    }

    @Override
    public boolean canCollideWith( Entity other )
    {
        if ( explosionRadius != 0 && other instanceof PooBrickEntity otherBrick && otherBrick.explosionRadius != 0 )
            return false;

        return super.canCollideWith( other );
    }

    @Override
    public boolean isPickable()
    {
        return !this.isRemoved();
    }

    @Override
    public boolean hurtServer( ServerLevel world, DamageSource source, float amount )
    {
        if ( !level().isClientSide() && this.explosionRadius == 0 )
        {
            this.discard();

            if ( source.getDirectEntity() instanceof Player player && !player.isCreative() )
                this.spawnAtLocation( (ServerLevel)level(), GCMod.POO_BRICK );
        }

        return true;
    }

    @Override
    protected double getDefaultGravity()
    {
        return 0.04;
    }

    @Override
    public void tick()
    {
        if ( !this.level().isClientSide() )
        {
            if ( this.tickCount >= 3000 )
            {
                this.discard();
                this.spawnAtLocation( (ServerLevel)level(), GCMod.POO_BRICK );
                return;
            }

            if ( this.explosionRadius != 0 && (this.onGround() || this.horizontalCollision || this.verticalCollision) )
            {
                this.discard();
                this.level().explode( this, this.getX(), this.getY() + (double) (this.getBbHeight() / 16.0F), this.getZ(), this.explosionRadius, Level.ExplosionInteraction.TNT );
                return;
            }
        }

        this.tickCount++;

        this.prevAngleX = this.angleX;
        this.prevAngleY = this.angleY;
        this.prevAngleZ = this.angleZ;

        this.applyGravity();
        this.move( MoverType.SELF, this.getDeltaMovement() );
        this.setDeltaMovement( this.getDeltaMovement().scale( 0.98 ) );
        if ( this.onGround() )
        {
            this.setDeltaMovement( this.getDeltaMovement().multiply( 0.7, -0.5, 0.7 ) );
            this.angleX = 0;
            this.angleZ = 0;
        }
        else
        {
            this.angleX += this.spinX;
            this.angleY += this.spinY;
            this.angleZ += this.spinZ;

            if ( !level().isClientSide() && this.explosionRadius == 0 )
            {
                List<Entity> overlaps = this.level().getEntities( this, this.getBoundingBox(),
                        EntitySelector.NO_CREATIVE_OR_SPECTATOR.and( Entity::isAlive ).and( entity -> entity.canCollideWith( this ) ) );

                for ( Entity ent : overlaps )
                {
                    ent.hurtServer( (ServerLevel)level(), level().damageSources().fallingBlock( this.thrower ), (float) (8 * this.getDeltaMovement().length()) );
                    ent.playSound( SoundEvents.PLAYER_BIG_FALL, 1.f, .5f + level().getRandom().nextFloat() * .4f );
                }
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket( ServerEntity entityTrackerEntry )
    {
        return new ClientboundAddEntityPacket( this, entityTrackerEntry, this.thrower == null ? 0 : this.thrower.getId() );
    }

    @Override
    public void recreateFromPacket( ClientboundAddEntityPacket packet )
    {
        super.recreateFromPacket( packet );

        Entity throwerEnt = level().getEntity( packet.getData() );
        this.thrower = null;
        if ( throwerEnt instanceof Player player )
            this.thrower = player;
    }

    @Override
    protected void defineSynchedData( SynchedEntityData.Builder builder )
    {

    }

    @Override
    protected void readAdditionalSaveData( ValueInput view )
    {
        this.explosionRadius = view.getByteOr( "Volatility", (byte)0 );
    }

    @Override
    protected void addAdditionalSaveData( ValueOutput view )
    {
        if ( this.explosionRadius != 0 )
            view.putByte( "Volatility", (byte) this.explosionRadius );
    }
}
