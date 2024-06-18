package gcmod.mixin;

import gcmod.GCMod;
import gcmod.entity.PooBrickEntity;
import gcmod.entity.PooEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( PlayerEntity.class )
public abstract class PlayerMixin extends LivingEntity
{
    @Shadow public abstract void playSoundToPlayer( SoundEvent sound, SoundCategory category, float volume, float pitch );

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
    public boolean collidesWith( Entity other )
    {
        if ( other instanceof PooBrickEntity brick && (PlayerEntity)(Object)this == brick.thrower )
            return brick.age > 5; // stops throwers from hitting themself

        return super.collidesWith( other );
    }

    @Override
    public void setSneaking( boolean sneaking )
    {
        if ( !getWorld().isClient && sneaking && !isSneaking() )
        {
            ++sneakIdx;
            if ( sneakIdx == NUM_SNEAKS_FOR_ACCIDENT )
                sneakIdx = 0;

            final long now = getWorld().getTime();
            final long oldestSneakTime = sneakRing[sneakIdx];

            sneakRing[sneakIdx] = now;

            if ( now - timeOfLastAccident > ACCIDENT_COOLDOWN_TICKS && now - oldestSneakTime < ACCIDENT_WINDOW_TICKS )
            {
                timeOfLastAccident = now;

                final float pitch = (this.random.nextFloat() - this.random.nextFloat()) * 1.8F + 0.1F;
                this.playSound( GCMod.FART_SOUND, 2.0f, pitch );
                this.playSoundToPlayer( GCMod.FART_SOUND, SoundCategory.PLAYERS, 2.0f, pitch );
                this.getWorld().spawnEntity( PooEntity.create( this.getWorld(), this.getPos() ) );
            }
        }

        super.setSneaking( sneaking );
    }

    protected PlayerMixin( EntityType<? extends LivingEntity> entityType, World world )
    {
        super( entityType, world );
    }
}
