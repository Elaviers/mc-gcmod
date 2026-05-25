package gcmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerExplosion;

@Mixin(ServerExplosion.class)
public interface ExplosionImplMixin
{
    @Invoker("calculateExplodedPositions")
    List<BlockPos> callGetBlocksToDestroy();

    @Invoker("hurtEntities")
    void callDamageEntities();
}
