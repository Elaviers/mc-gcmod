package gcmod;

import gcmod.block.ExplosiveBlock;
import gcmod.entity.ExplosiveBlockEntity;
import gcmod.entity.PooBrickEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ExplosiveBombEntityRenderer extends EntityRenderer<PooBrickEntity>
{
    private final BlockRenderManager blockRenderManager;

    public ExplosiveBombEntityRenderer( EntityRendererFactory.Context context )
    {
        super( context );
        this.shadowRadius = 0.5F;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render( PooBrickEntity pooBrick, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light )
    {
        matrices.push();

        matrices.scale( .5f, .5f, .5f );
        matrices.translate( 0, .5, 0 );
        matrices.multiply( RotationAxis.POSITIVE_X.rotationDegrees( pooBrick.prevAngleX + tickDelta * (pooBrick.angleX - pooBrick.prevAngleX )));
        matrices.multiply( RotationAxis.POSITIVE_Y.rotationDegrees( pooBrick.prevAngleY + tickDelta * (pooBrick.angleY - pooBrick.prevAngleY )));
        matrices.multiply( RotationAxis.POSITIVE_Z.rotationDegrees( pooBrick.prevAngleZ + tickDelta * (pooBrick.angleZ - pooBrick.prevAngleZ )));
        matrices.translate(-0.5, -0.5, -0.5);

        this.blockRenderManager.renderBlockAsEntity( GCMod.BLAST_TNT.getDefaultState().with( ExplosiveBlock.TIER, 1 ), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV );
        matrices.pop();

        super.render( pooBrick, yaw, tickDelta, matrices, vertexConsumers, light );
    }

    @SuppressWarnings( "deprecation" ) // idk why BLOCK_ATLAS_TEXTURE is deprecated?
    @Override
    public Identifier getTexture( PooBrickEntity entity )
    {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
