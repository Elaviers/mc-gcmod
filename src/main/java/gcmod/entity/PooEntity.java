package gcmod.entity;

import com.sun.jna.platform.unix.X11;
import gcmod.GCMod;
import gcmod.PooSplatPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PooEntity extends Entity
{
    public float size, prevSize;

    int sizeVariant;

    float targetSize;
    float shrinkRate;
    float timer;
    boolean doDrop;

    boolean wasOnGround;

    public PooEntity( EntityType<?> type, Level world )
    {
        super( type, world );
        this.blocksBuilding = true;

        this.shrinkRate = 0f;

        this.size = 0.001f;
        this.targetSize = 1f;
        this.timer = 1000;

        if ( !world.isClientSide() )
            this.setSizeVariant( world.getRandom().nextInt( 1001 ) );
    }

    public static PooEntity create( Level world, Vec3 position )
    {
        PooEntity poo = new PooEntity( GCMod.POO_ENTITY, world );
        poo.setPos( position );
        return poo;
    }

    private void setSizeVariant( int sizeVariant )
    {
        this.sizeVariant = sizeVariant;
        this.targetSize = 0.8f + (this.sizeVariant / 1000f) * .8f;
        this.timer = 1200 * this.targetSize;
    }

    @Override
    public boolean canBeCollidedWith( @Nullable Entity entity )
    {
        // TODO - gamerules are not inherently replicated to the client, so I need to add a new thing analogous to GameStateChangeS2CPacket to use the gamerule here
        // For now fecal matter just never collides with mobs, its pretty amusing but can screw up farms.
        return false;
    }

    @Override
    public boolean canCollideWith( Entity other )
    {
        if ( other instanceof PooEntity )
            return true;

        return super.canCollideWith( other );
    }

    @Override
    public boolean isPickable()
    {
        return !this.isRemoved();
    }

    @Override
    public boolean skipAttackInteraction( Entity attacker )
    {
        if ( attacker instanceof Player player )
        {
            if ( player.isCreative() )
                return false;

            final boolean isShovel = player.getMainHandItem().getItem() == Items.WOODEN_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.STONE_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.IRON_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.GOLDEN_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.DIAMOND_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.NETHERITE_SHOVEL;

            return !isShovel;
        }

        return false;
    }

    private boolean commonDamage( DamageSource source )
    {
        if ( !(source.getDirectEntity() instanceof Player player) )
        {
            this.doDrop = false;
            this.shrinkRate = size;
            return true;
        }

        this.targetSize = this.size;

        if ( player.isCreative() )
        {
            this.doDrop = false;
            this.shrinkRate = size / 2;
        } else
        {
            final boolean isShovel = player.getMainHandItem().getItem() == Items.WOODEN_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.STONE_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.IRON_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.GOLDEN_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.DIAMOND_SHOVEL ||
                    player.getMainHandItem().getItem() == Items.NETHERITE_SHOVEL;

            if ( isShovel )
            {
                if ( !this.level().isClientSide() )
                    player.getMainHandItem().hurtAndBreak( 1, player, EquipmentSlot.MAINHAND );

                this.doDrop = true;
                this.shrinkRate = size / 3;
            }
        }
        return true;
    }

    @Override
    public boolean hurtClient( DamageSource source )
    {
        return commonDamage( source );
    }

    @Override
    public boolean hurtServer( ServerLevel world, DamageSource source, float amount )
    {
        return commonDamage( source );
    }

    void breakPoo()
    {
        this.discard();

        if ( this.doDrop )
            this.spawnAtLocation( (ServerLevel)this.level(), new ItemStack( GCMod.POO, this.targetSize > 1.1f ? 2 : 1 ) );
    }

    @Override
    protected AABB makeBoundingBox( Vec3 pos )
    {
        final float BB_SCALE = .9f; // the thing shrinks so lets sink in the bb a little
        return this.getDimensions( this.getPose() ).scale( this.targetSize * BB_SCALE ).makeBoundingBox( this.position() );
    }

    @Override
    protected double getDefaultGravity()
    {
        return 0.04;
    }

    private void emitPooFX( float severity, float randSeverity )
    {
        this.level().getServer().getPlayerList().broadcast( null, getX(), getY(), getZ(),
                100f, this.level().dimension(),
                ServerPlayNetworking.createClientboundPacket( new PooSplatPayload( this.position().toVector3f(), severity + randSeverity * this.level().getRandom().nextFloat() ) )
        );
    }

    @Override
    public void tick()
    {
        if ( !this.level().isClientSide() && this.size <= 0 )
            this.breakPoo();

        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
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
        this.move( MoverType.SELF, this.getDeltaMovement() );
        this.setDeltaMovement( this.getDeltaMovement().scale( 0.98 ) );
        if ( this.onGround() )
        {
            if ( !wasOnGround && !this.level().isClientSide() && this.level().getBlockState( this.blockPosition() ).is( Blocks.STONECUTTER ) )
            {
                this.doDrop = true;
                this.breakPoo();
                this.emitPooFX( 1f + 5f * Math.abs( (float)this.getDeltaMovement().y ), .5f );
            }

            this.setDeltaMovement( this.getDeltaMovement().multiply( 0.7, -0.5, 0.7 ) );
        }

        wasOnGround = this.onGround();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket( ServerEntity entityTrackerEntry )
    {
        return new ClientboundAddEntityPacket( this, entityTrackerEntry, this.sizeVariant );
    }

    @Override
    public void recreateFromPacket( ClientboundAddEntityPacket packet )
    {
        super.recreateFromPacket( packet );
        this.setSizeVariant( packet.getData() );
    }

    //
    //
    //

    @Override
    protected void defineSynchedData( SynchedEntityData.Builder builder )
    {
    }

    @Override
    protected void readAdditionalSaveData( ValueInput view )
    {
    }

    @Override
    protected void addAdditionalSaveData( ValueOutput view )
    {
    }

    @Override
    protected void onInsideBlock( BlockState state )
    {
        if ( !level().isClientSide() && state.is( Blocks.PISTON_HEAD ) && level().getBlockState( this.blockPosition() ).is( Blocks.PISTON_HEAD ) )
        {
            this.doDrop = true;
            this.breakPoo();
            this.emitPooFX( 1.25f, 2f );
        }

        super.onInsideBlock( state );
    }
}
