package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class PooBrickEntity extends Entity
{
    public float prevAngleX, prevAngleY, prevAngleZ, angleX, angleY, angleZ, spinX, spinY, spinZ;

    public int age;

    public PlayerEntity thrower;

    public int explosionRadius; // 0 == just a normal old brick

    public PooBrickEntity( EntityType<?> type, World world )
    {
        super( type, world );
        this.spinX = world.random.nextFloat() * 30 - 15;
        this.spinY = world.random.nextFloat() * 20 - 10;
        this.spinZ = world.random.nextFloat() * 30 - 15;
        this.angleX += this.spinX * 5;
        this.angleY += this.spinY * 5;
        this.angleZ += this.spinZ * 5;

        this.thrower = null;
        this.age = 0;
        this.explosionRadius = 0;
        this.intersectionChecked = true;
    }

    public static PooBrickEntity createExplosive( EntityType<?> type, World world )
    {
        // EXPLOSIVE_POO_BRICK_ENTITY uses this.
        // We have a separate entity type because we want clients to be able to distinguish between explosive / non-explosive bricks for the collidesWith check.

        PooBrickEntity poo = new PooBrickEntity( type, world );
        poo.explosionRadius = 1;
        return poo;
    }

    @Override
    public boolean isCollidable()
    {
        // note - I've mixed in an override for players that are the thrower so that they don't get hit by outgoing dung flings
        return true;
    }

    @Override
    public boolean collidesWith( Entity other )
    {
        if ( explosionRadius != 0 && other instanceof PooBrickEntity otherBrick && otherBrick.explosionRadius != 0 )
            return false;

        return super.collidesWith( other );
    }

    @Override
    public boolean canHit()
    {
        return !this.isRemoved();
    }

    @Override
    public boolean damage( DamageSource source, float amount )
    {
        if ( !getWorld().isClient && this.explosionRadius == 0 )
        {
            this.discard();

            if ( source.getSource() instanceof PlayerEntity player && !player.isCreative() )
                this.dropItem( GCMod.POO_BRICK );
        }

        return true;
    }

    @Override
    protected double getGravity()
    {
        return 0.04;
    }

    @Override
    public void tick()
    {
        if ( !this.getWorld().isClient )
        {
            if ( this.age >= 3000 )
            {
                this.discard();
                this.dropItem( GCMod.POO_BRICK );
                return;
            }

            if ( this.explosionRadius != 0 && (this.isOnGround() || this.horizontalCollision || this.verticalCollision) )
            {
                this.discard();
                this.getWorld().createExplosion( this, this.getX(), this.getY() + (double) (this.getHeight() / 16.0F), this.getZ(), this.explosionRadius, World.ExplosionSourceType.TNT );
                return;
            }
        }

        this.age++;

        this.prevAngleX = this.angleX;
        this.prevAngleY = this.angleY;
        this.prevAngleZ = this.angleZ;

        this.applyGravity();
        this.move( MovementType.SELF, this.getVelocity() );
        this.setVelocity( this.getVelocity().multiply( 0.98 ) );
        if ( this.isOnGround() )
        {
            this.setVelocity( this.getVelocity().multiply( 0.7, -0.5, 0.7 ) );
            this.angleX = 0;
            this.angleZ = 0;
        }
        else
        {
            this.angleX += this.spinX;
            this.angleY += this.spinY;
            this.angleZ += this.spinZ;

            if ( !getWorld().isClient && this.explosionRadius == 0 )
            {
                List<Entity> overlaps = this.getWorld().getOtherEntities( this, this.getBoundingBox(),
                        EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and( Entity::isAlive ).and( entity -> entity.collidesWith( this ) ) );

                for ( Entity ent : overlaps )
                {
                    ent.damage( getWorld().getDamageSources().fallingBlock( this.thrower ), (float) (8 * this.getVelocity().length()) );
                    ent.playSound( SoundEvents.ENTITY_PLAYER_BIG_FALL, 1.f, .5f + getWorld().random.nextFloat() * .4f );
                }
            }
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket( EntityTrackerEntry entityTrackerEntry )
    {
        return new EntitySpawnS2CPacket( this, entityTrackerEntry, this.thrower == null ? 0 : this.thrower.getId() );
    }

    @Override
    public void onSpawnPacket( EntitySpawnS2CPacket packet )
    {
        super.onSpawnPacket( packet );

        Entity throwerEnt = getWorld().getEntityById( packet.getEntityData() );
        this.thrower = null;
        if ( throwerEnt instanceof PlayerEntity player )
            this.thrower = player;
    }

    @Override
    protected void initDataTracker( DataTracker.Builder builder )
    {

    }

    @Override
    protected void readCustomDataFromNbt( NbtCompound nbt )
    {
        this.explosionRadius = 0;
        if ( nbt.contains( "Volatility" ) )
            this.explosionRadius = nbt.getByte( "Volatility" );
    }

    @Override
    protected void writeCustomDataToNbt( NbtCompound nbt )
    {
        if ( this.explosionRadius != 0 )
            nbt.putByte( "Volatility", (byte) this.explosionRadius );
    }
}
