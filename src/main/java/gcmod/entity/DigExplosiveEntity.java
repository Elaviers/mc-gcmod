package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class DigExplosiveEntity extends ExplosiveEntity
{
    double explosionY;
    int stuckTicks;

    public DigExplosiveEntity( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Override
    public void tick()
    {
        super.tick();

        if ( !this.getWorld().isClient )
        {
            if ( this.getY() < this.explosionY )
            {
                if ( (this.getFuse() & 2) == 0 )
                    this.getWorld().createExplosion( this, this.getX(), this.getY() + (double) (this.getHeight() / 16.0F), this.getZ(), this.explosionRadius, World.ExplosionSourceType.TNT );

                if ( this.getY() == this.prevY )
                {
                    if ( this.stuckTicks >= 10 )
                        this.discard();
                    else
                        this.stuckTicks++;
                }
                else this.stuckTicks = 0;
            }
            else if ( this.explosionY > 0 && this.getVelocity().y == 0.0 )
                this.explosionY = 1000;
        }
    }

    @Override
    protected void onFuseComplete()
    {
        this.explosionY = this.getY() + 0.5;
        this.setVelocity(this.getVelocity().getX(), 1d, this.getVelocity().getZ() );
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.DIG_TNT.getDefaultState();
    }
}
