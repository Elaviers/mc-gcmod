package gcmod.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ExplosionImpl.class)
public interface ExplosionImplMixin
{
    @Invoker("getBlocksToDestroy")
    List<BlockPos> callGetBlocksToDestroy();

    @Invoker("damageEntities")
    void callDamageEntities();
}
