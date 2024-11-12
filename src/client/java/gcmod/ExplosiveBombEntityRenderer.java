package gcmod;

import gcmod.block.ExplosiveBlock;
import gcmod.entity.PooBrickEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class ExplosiveBombEntityRenderer extends EntityRenderer<PooBrickEntity, PooBrickEntityRenderState>
{
    private final BlockRenderManager blockRenderManager;

    public ExplosiveBombEntityRenderer( EntityRendererFactory.Context context )
    {
        super( context );
        this.shadowRadius = 0.5F;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public PooBrickEntityRenderState createRenderState()
    {
        return new PooBrickEntityRenderState();
    }

    @Override
    public void render( PooBrickEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light )
    {
        matrices.push();

        matrices.scale( .5f, .5f, .5f );
        matrices.translate( 0, .5, 0 );
        matrices.multiply( RotationAxis.POSITIVE_X.rotationDegrees( state.angleX ) );
        matrices.multiply( RotationAxis.POSITIVE_Y.rotationDegrees( state.angleY ) );
        matrices.multiply( RotationAxis.POSITIVE_Z.rotationDegrees( state.angleZ ) );
        matrices.translate(-0.5, -0.5, -0.5);

        this.blockRenderManager.renderBlockAsEntity( GCMod.BLAST_TNT.getDefaultState().with( ExplosiveBlock.TIER, 1 ), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV );
        matrices.pop();

        super.render( state, matrices, vertexConsumers, light );
    }

    @Override
    public void updateRenderState( PooBrickEntity entity, PooBrickEntityRenderState state, float tickDelta )
    {
        super.updateRenderState( entity, state, tickDelta );
        state.angleX = entity.prevAngleX + tickDelta * (entity.angleX - entity.prevAngleX );
        state.angleY = entity.prevAngleY + tickDelta * (entity.angleY - entity.prevAngleY );
        state.angleZ = entity.prevAngleZ + tickDelta * (entity.angleZ - entity.prevAngleZ );
    }
}
