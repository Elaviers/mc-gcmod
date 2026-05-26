package gcmod.mixin;

import gcmod.GCMod;
import gcmod.MobTurdInfo;
import gcmod.entity.PooEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Animal.class)
public abstract class AnimalMixin extends AgeableMob
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
        if ( !this.level().isClientSide() && info != null )
        {
            final int turdRate = ((ServerLevel)this.level()).getGameRules().get( GCMod.RULE_TURD_RATE );
            if ( turdRate > 0 )
            {
                this.pooDropTimer = this.getRandom().nextInt( info.maxInterval ) * turdRate;
            }
        }
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    public void mobTick( CallbackInfo ci )
    {
        if ( !this.level().isClientSide() && pooDropTimer != INVALID_TIMER && --pooDropTimer <= 0 )
        {
            if ( !this.isBaby() )
            {
                this.playSound( GCMod.FART_SOUND, 1.0f, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 1.8F + 0.1F );
                this.level().addFreshEntity( PooEntity.create( this.level(), this.position() ) );
            }

            MobTurdInfo info = MobTurdInfo.forClass( this );
            assert info != null;

            final int turdRate = ((ServerLevel)this.level()).getGameRules().get( GCMod.RULE_TURD_RATE );
            this.pooDropTimer = turdRate > 0 ? (this.getRandom().nextIntBetweenInclusive( info.minInterval, info.maxInterval ) * turdRate) : INVALID_TIMER;
        }
    }

    protected AnimalMixin( EntityType<? extends Animal> entityType, Level world )
    {
        super( entityType, world );
    }
}
