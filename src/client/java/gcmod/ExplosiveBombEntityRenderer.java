package gcmod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import gcmod.block.ExplosiveBlock;
import gcmod.entity.PooBrickEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ExplosiveBombEntityRenderer extends EntityRenderer<PooBrickEntity, PooBrickEntityRenderState>
{
    private final BlockModelResolver blockModelResolver;
    private final BlockModelRenderState blockModelState;
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    public ExplosiveBombEntityRenderer( EntityRendererProvider.Context context )
    {
        super( context );
        this.shadowRadius = 0.5F;
        this.blockModelResolver = context.getBlockModelResolver();
        this.blockModelState = new BlockModelRenderState();
    }

    @Override
    public PooBrickEntityRenderState createRenderState()
    {
        return new PooBrickEntityRenderState();
    }

    @Override
    public void submit( PooBrickEntityRenderState state, PoseStack matrices, SubmitNodeCollector commands, CameraRenderState cameraRenderState )
    {
        matrices.pushPose();

        matrices.scale( .5f, .5f, .5f );
        matrices.translate( 0, .5, 0 );
        matrices.mulPose( Axis.XP.rotationDegrees( state.angleX ) );
        matrices.mulPose( Axis.YP.rotationDegrees( state.angleY ) );
        matrices.mulPose( Axis.ZP.rotationDegrees( state.angleZ ) );
        matrices.translate( -0.5, -0.5, -0.5 );

        this.blockModelState.submit( matrices, commands, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor );
        matrices.popPose();

        super.submit( state, matrices, commands, cameraRenderState );
    }

    @Override
    public void extractRenderState( PooBrickEntity entity, PooBrickEntityRenderState state, float tickDelta )
    {
        super.extractRenderState( entity, state, tickDelta );
        this.blockModelResolver.update( this.blockModelState, GCMod.BLAST_TNT.defaultBlockState().setValue( ExplosiveBlock.TIER, 1 ), BLOCK_DISPLAY_CONTEXT );
        state.angleX = entity.prevAngleX + tickDelta * (entity.angleX - entity.prevAngleX);
        state.angleY = entity.prevAngleY + tickDelta * (entity.angleY - entity.prevAngleY);
        state.angleZ = entity.prevAngleZ + tickDelta * (entity.angleZ - entity.prevAngleZ);
    }
}
