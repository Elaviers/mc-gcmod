package gcmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class PooBrickEntityRenderState extends EntityRenderState {
    public float angleX;
    public float angleY;
    public float angleZ;
}
