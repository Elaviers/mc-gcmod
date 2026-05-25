package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DigExplosiveEntity extends ExplosiveEntity
{
    double explosionY;
    int stuckTicks;

    public DigExplosiveEntity( EntityType<?> type, Level world )
    {
        super( type, world );
    }

    @Override
    public void tick()
    {
        super.tick();

        if ( !this.level().isClientSide() )
        {
            if ( this.getY() < this.explosionY )
            {
                if ( (this.getFuse() & 2) == 0 )
                    this.level().explode( this, this.getX(), this.getY() + (double) (this.getBbHeight() / 16.0F), this.getZ(), this.explosionRadius, Level.ExplosionInteraction.TNT );

                if ( this.getY() == this.yOld )
                {
                    if ( this.stuckTicks >= 10 )
                        this.discard();
                    else
                        this.stuckTicks++;
                }
                else this.stuckTicks = 0;
            }
            else if ( this.explosionY > 0 && this.getDeltaMovement().y == 0.0 )
                this.explosionY = 1000;
        }
    }

    @Override
    protected void onFuseComplete()
    {
        this.explosionY = this.getY() + 0.5;
        this.setDeltaMovement(this.getDeltaMovement().x(), 1d, this.getDeltaMovement().z() );
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.DIG_TNT.defaultBlockState();
    }
}
