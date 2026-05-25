package gcmod;

import gcmod.mixin.ExplosionImplMixin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ConstructiveExplosionImpl extends ServerExplosion
{
    public BlockState blockState;

    public ConstructiveExplosionImpl( ServerLevel world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator behavior, Vec3 pos, float power, BlockState blockState )
    {
        super( world, entity, damageSource, behavior, pos, power, false, BlockInteraction.KEEP );
        this.blockState = blockState;
    }

    @Override
    public int explode()
    {
        this.level().gameEvent( this.getDirectSourceEntity(), GameEvent.EXPLODE, this.center() );
        ((ExplosionImplMixin) this).callDamageEntities();

        List<BlockPos> positions = ((ExplosionImplMixin) this).callGetBlocksToDestroy();
        for ( BlockPos pos : positions )
        {
            level().setBlockAndUpdate( pos, blockState );
        }

        return positions.size();
    }
}
