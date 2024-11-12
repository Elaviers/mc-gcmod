package gcmod;

import gcmod.mixin.ExplosionImplMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConstructiveExplosionImpl extends ExplosionImpl
{
    public BlockState blockState;

    public ConstructiveExplosionImpl( ServerWorld world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, Vec3d pos, float power, BlockState blockState )
    {
        super( world, entity, damageSource, behavior, pos, power, false, DestructionType.KEEP );
        this.blockState = blockState;
    }

    @Override
    public void explode()
    {
        this.getWorld().emitGameEvent(this.getEntity(), GameEvent.EXPLODE, this.getPosition());
        ((ExplosionImplMixin)this).callDamageEntities();

        List<BlockPos> positions = ((ExplosionImplMixin)this).callGetBlocksToDestroy();
        for ( BlockPos pos : positions )
        {
            getWorld().setBlockState( pos, blockState );
        }
    }
}
