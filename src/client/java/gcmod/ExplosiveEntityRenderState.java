package gcmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ExplosiveEntityRenderState extends EntityRenderState {
    public float fuse;
    @Nullable
    public BlockState blockState;
}
