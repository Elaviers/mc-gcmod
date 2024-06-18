package gcmod.mixin;

import gcmod.GCMod;
import gcmod.MobTurdInfo;
import gcmod.entity.PooEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(AnimalEntity.class)
public abstract class AnimalMixin extends PassiveEntity
{
    @Unique
    private static final int INVALID_TIMER = -Integer.MAX_VALUE;

    @Unique
    private int pooDropTimer;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor( CallbackInfo ci )
    {
        MobTurdInfo info = MobTurdInfo.forClass( this );
        this.pooDropTimer = INVALID_TIMER;
        if ( info != null )
        {
            final int turdRate = this.getWorld().getGameRules().getInt( GCMod.RULE_TURD_RATE );
            if ( turdRate > 0 )
            {
                this.pooDropTimer = this.random.nextInt( info.maxInterval ) * turdRate;
            }
        }
    }

    @Inject(method = "mobTick", at = @At("TAIL"))
    public void mobTick( CallbackInfo ci )
    {
        if ( !this.getWorld().isClient && pooDropTimer != INVALID_TIMER && --pooDropTimer <= 0 )
        {
            if ( !this.isBaby() )
            {
                this.playSound( GCMod.FART_SOUND, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 1.8F + 0.1F );
                this.getWorld().spawnEntity( PooEntity.create( this.getWorld(), this.getPos() ) );
            }

            MobTurdInfo info = MobTurdInfo.forClass( this );
            assert info != null;

            final int turdRate = this.getWorld().getGameRules().getInt( GCMod.RULE_TURD_RATE );
            this.pooDropTimer = turdRate > 0 ? (this.random.nextBetween( info.minInterval, info.maxInterval ) * turdRate) : INVALID_TIMER;
        }
    }

    protected AnimalMixin( EntityType<? extends AnimalEntity> entityType, World world )
    {
        super( entityType, world );
    }
}
