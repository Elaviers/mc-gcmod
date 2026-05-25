package gcmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ExplosiveEntityRenderState extends EntityRenderState
{
    public float fuse;
    @Nullable
    public BlockState blockState;
}
