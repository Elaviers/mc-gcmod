package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ConstructiveExplosiveEntity extends ExplosiveEntity
{
    public BlockState createdBlock;

    public ConstructiveExplosiveEntity( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Override
    protected void onFuseComplete()
    {
        if ( !getWorld().isClient )
        {
            this.discard();

            Explosion explosion = this.getWorld().createExplosion( this, this.getX(), this.getY() + (double) (this.getHeight() / 16.0F), this.getZ(), this.explosionRadius, false, World.ExplosionSourceType.TNT );
            for ( BlockPos pos : explosion.getAffectedBlocks() )
            {
                getWorld().setBlockState( pos, this.createdBlock );
            }
        }
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.CONSTRUCTIVE_TNT.getDefaultState();
    }
}
