package gcmod.entity;

import com.sun.jna.platform.unix.X11;
import gcmod.GCMod;
import gcmod.PooSplatPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PooEntity extends Entity
{
    public float size, prevSize;

    int sizeVariant;

    float targetSize;
    float shrinkRate;
    float timer;
    boolean doDrop;

    boolean wasOnGround;

    public PooEntity( EntityType<?> type, World world )
    {
        super( type, world );
        this.intersectionChecked = true;

        this.shrinkRate = 0f;

        this.size = 0.001f;
        this.targetSize = 1f;
        this.timer = 1000;

        if ( !world.isClient )
            this.setSizeVariant( world.random.nextInt( 1001 ) );
    }

    public static PooEntity create( World world, Vec3d position )
    {
        PooEntity poo = new PooEntity( GCMod.POO_ENTITY, world );
        poo.setPosition( position );
        return poo;
    }

    private void setSizeVariant( int sizeVariant )
    {
        this.sizeVariant = sizeVariant;
        this.targetSize = 0.8f + (this.sizeVariant / 1000f) * .8f;
        this.timer = 1200 * this.targetSize;
    }

    @Override
    public boolean isCollidable()
    {
        // return getWorld().getGameRules().getBoolean( GCMod.RULE_SOLID_TURDS )
        // TODO - gamerules are not inherently replicated to the client, so I need to add a new thing analogous to GameStateChangeS2CPacket to make the above work correctly
        // For now fecal matter just never collides with mobs, its pretty amusing but can screw up farms.

        return false;
    }

    @Override
    public boolean collidesWith( Entity other )
    {
        if ( other instanceof PooEntity )
            return true;

        return super.collidesWith( other );
    }

    @Override
    public boolean canHit()
    {
        return !this.isRemoved();
    }

    @Override
    public boolean handleAttack( Entity attacker )
    {
        if ( attacker instanceof PlayerEntity player )
        {
            if ( player.isCreative() )
                return false;

            final boolean isShovel = player.getMainHandStack().getItem() == Items.WOODEN_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.STONE_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.IRON_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.GOLDEN_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.DIAMOND_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.NETHERITE_SHOVEL;

            return !isShovel;
        }

        return false;
    }

    @Override
    public boolean damage( DamageSource source, float amount )
    {
        if ( !(source.getSource() instanceof PlayerEntity player) )
        {
            this.doDrop = false;
            this.shrinkRate = size;
            return false;
        }

        this.targetSize = this.size;

        if ( player.isCreative() )
        {
            this.doDrop = false;
            this.shrinkRate = size / 2;
        } else
        {
            final boolean isShovel = player.getMainHandStack().getItem() == Items.WOODEN_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.STONE_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.IRON_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.GOLDEN_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.DIAMOND_SHOVEL ||
                    player.getMainHandStack().getItem() == Items.NETHERITE_SHOVEL;

            if ( isShovel )
            {
                if ( !this.getWorld().isClient )
                    player.getMainHandStack().damage( 1, player, EquipmentSlot.MAINHAND );

                this.doDrop = true;
                this.shrinkRate = size / 3;
            }
        }
        return true;
    }

    void breakPoo()
    {
        this.discard();

        if ( this.doDrop )
            this.dropStack( new ItemStack( GCMod.POO, this.targetSize > 1.1f ? 2 : 1 ) );
    }

    @Override
    protected Box calculateBoundingBox()
    {
        final float BB_SCALE = .9f; // the thing shrinks so lets sink in the bb a little
        return this.getDimensions( this.getPose() ).scaled( this.targetSize * BB_SCALE ).getBoxAt( this.getPos() );
    }

    @Override
    protected double getGravity()
    {
        return 0.04;
    }

    private void emitPooFX( float severity, float randSeverity )
    {
        this.getServer().getPlayerManager().sendToAround( null, getX(), getY(), getZ(),
                100f, this.getWorld().getRegistryKey(),
                ServerPlayNetworking.createS2CPacket( new PooSplatPayload( this.getPos().toVector3f(), severity + randSeverity * this.getWorld().random.nextFloat() ) )
        );
    }

    @Override
    public void tick()
    {
        if ( !this.getWorld().isClient && this.size <= 0 )
            this.breakPoo();

        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
        this.prevSize = this.size;

        if ( this.shrinkRate > 0 )
        {
            this.size -= this.shrinkRate;
        } else
        {
            this.size += 0.1f;

            if ( this.size >= this.targetSize )
            {
                this.size = this.targetSize;
                this.shrinkRate = 0.005f / 20;
            }
        }

        if ( this.timer <= 0 )
            this.shrinkRate += 0.01f / 20;
        else
            timer--;

        this.applyGravity();
        this.move( MovementType.SELF, this.getVelocity() );
        this.setVelocity( this.getVelocity().multiply( 0.98 ) );
        if ( this.isOnGround() )
        {
            if ( !wasOnGround && !this.getWorld().isClient && this.getWorld().getBlockState( this.getBlockPos() ).isOf( Blocks.STONECUTTER ) )
            {
                this.doDrop = true;
                this.breakPoo();
                this.emitPooFX( 1f + 5f * Math.abs( (float)this.getVelocity().y ), .5f );
            }

            this.setVelocity( this.getVelocity().multiply( 0.7, -0.5, 0.7 ) );
        }

        wasOnGround = this.isOnGround();
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket( EntityTrackerEntry entityTrackerEntry )
    {
        return new EntitySpawnS2CPacket( this, entityTrackerEntry, this.sizeVariant );
    }

    @Override
    public void onSpawnPacket( EntitySpawnS2CPacket packet )
    {
        super.onSpawnPacket( packet );
        this.setSizeVariant( packet.getEntityData() );
    }

    //
    //
    //

    @Override
    protected void initDataTracker( DataTracker.Builder builder )
    {
    }

    @Override
    protected void readCustomDataFromNbt( NbtCompound nbt )
    {

    }

    @Override
    protected void writeCustomDataToNbt( NbtCompound nbt )
    {

    }

    @Override
    protected void onBlockCollision( BlockState state )
    {
        if ( !getWorld().isClient && state.isOf( Blocks.PISTON_HEAD ) && getWorld().getBlockState( this.getBlockPos() ).isOf( Blocks.PISTON_HEAD ) )
        {
            this.doDrop = true;
            this.breakPoo();
            this.emitPooFX( 1.25f, 2f );
        }

        super.onBlockCollision( state );
    }
}
