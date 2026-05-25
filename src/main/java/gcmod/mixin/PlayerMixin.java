package gcmod.mixin;

import gcmod.GCMod;
import gcmod.entity.PooBrickEntity;
import gcmod.entity.PooEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Player.class )
public abstract class PlayerMixin extends LivingEntity
{
    @Unique
    private static final int NUM_SNEAKS_FOR_ACCIDENT = 10;

    @Unique
    private static final int ACCIDENT_WINDOW_TICKS = 40; // you need to complete all sneaks within this many ticks

    @Unique
    private static final int ACCIDENT_COOLDOWN_TICKS = 60 * 20;

    @Unique
    long[] sneakRing;

    @Unique
    int sneakIdx;

    @Unique long timeOfLastAccident;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor( CallbackInfo ci )
    {
        sneakRing = new long[NUM_SNEAKS_FOR_ACCIDENT];
    }

    @Override
    public boolean canCollideWith( Entity other )
    {
        if ( other instanceof PooBrickEntity brick && (Player)(Object)this == brick.thrower )
            return brick.tickCount > 5; // stops throwers from hitting themself

        return super.canCollideWith( other );
    }

    @Override
    public void setShiftKeyDown( boolean sneaking )
    {
        if ( !level().isClientSide() && sneaking && !isShiftKeyDown() )
        {
            ++sneakIdx;
            if ( sneakIdx == NUM_SNEAKS_FOR_ACCIDENT )
                sneakIdx = 0;

            final long now = level().getGameTime();
            final long oldestSneakTime = sneakRing[sneakIdx];

            sneakRing[sneakIdx] = now;

            if ( now - timeOfLastAccident > ACCIDENT_COOLDOWN_TICKS && now - oldestSneakTime < ACCIDENT_WINDOW_TICKS )
            {
                timeOfLastAccident = now;

                final float pitch = (this.random.nextFloat() - this.random.nextFloat()) * 1.8F + 0.1F;
                PooEntity poo = PooEntity.create( this.level(), this.position() );
                this.level().addFreshEntity( poo );
                this.level().playSound( poo, this.position().x, this.position().y + 0.3, this.position().z, GCMod.FART_SOUND, SoundSource.PLAYERS, 2.0f, pitch );
            }
        }

        super.setShiftKeyDown( sneaking );
    }

    protected PlayerMixin( EntityType<? extends LivingEntity> entityType, Level world )
    {
        super( entityType, world );
    }
}
