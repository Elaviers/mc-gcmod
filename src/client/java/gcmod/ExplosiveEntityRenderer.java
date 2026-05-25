package gcmod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import gcmod.entity.ExplosiveEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class ExplosiveEntityRenderer extends EntityRenderer<ExplosiveEntity, ExplosiveEntityRenderState>
{
    private final BlockRenderDispatcher blockRenderManager;

    public ExplosiveEntityRenderer( EntityRendererProvider.Context context )
    {
        super( context );
        this.shadowRadius = 0.5F;
        this.blockRenderManager = context.getBlockRenderDispatcher();
    }

    @Override
    public ExplosiveEntityRenderState createRenderState()
    {
        return new ExplosiveEntityRenderState();
    }

    @Override
    public void submit( ExplosiveEntityRenderState state, PoseStack matrices, SubmitNodeCollector commands, CameraRenderState cameraRenderState )
    {
        matrices.pushPose();
        matrices.translate( 0.0F, 0.5F, 0.0F );
        float f = state.fuse;
        if ( state.fuse < 10.0F )
        {
            float g = 1.0F - state.fuse / 10.0F;
            g = Mth.clamp( g, 0.0F, 1.0F );
            g *= g;
            g *= g;
            float h = 1.0F + g * 0.3F;
            matrices.scale( h, h, h );
        }

        matrices.mulPose( Axis.YP.rotationDegrees( -90.0F ) );
        matrices.translate( -0.5F, -0.5F, 0.5F );
        matrices.mulPose( Axis.YP.rotationDegrees( 90.0F ) );
        if ( state.blockState != null )
        {
            TntMinecartRenderer.submitWhiteSolidBlock(
                    state.blockState, matrices, commands, state.lightCoords, (int) f / 5 % 2 == 0, state.outlineColor
            );
        }

        matrices.popPose();
        super.submit( state, matrices, commands, cameraRenderState );
    }

    @Override
    public void extractRenderState( ExplosiveEntity entity, ExplosiveEntityRenderState state, float tickDelta )
    {
        super.extractRenderState( entity, state, tickDelta );
        state.fuse = (float) entity.getFuse() - tickDelta + 1.0F;
        state.blockState = entity.getBlockState();
    }
}
