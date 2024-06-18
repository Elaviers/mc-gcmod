package gcmod;

import gcmod.entity.ExplosiveEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ExplosiveEntityRenderer extends EntityRenderer<ExplosiveEntity>
{
    private final BlockRenderManager blockRenderManager;

    public ExplosiveEntityRenderer( EntityRendererFactory.Context context )
    {
        super( context );
        this.shadowRadius = 0.5F;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render( ExplosiveEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light )
    {
        matrices.push();
        matrices.translate( 0f, .5f, 0f );
        final int fuse = entity.getFuse();
        if ( (float) fuse - tickDelta + 1f < 10f )
        {
            float h = 1F - ((float) fuse - tickDelta + 1f) / 10f;
            h = MathHelper.clamp( h, 0f, 1f );
            h *= h;
            h *= h;
            float k = 1.0F + h * 0.3F;
            matrices.scale( k, k, k );
        }

        matrices.multiply( RotationAxis.POSITIVE_Y.rotationDegrees( -90.0F ) );
        matrices.translate( -0.5F, -0.5F, 0.5F );
        matrices.multiply( RotationAxis.POSITIVE_Y.rotationDegrees( 90.0F ) );
        TntMinecartEntityRenderer.renderFlashingBlock( this.blockRenderManager, entity.getBlockState(), matrices, vertexConsumers, light, fuse / 5 % 2 == 0 );
        matrices.pop();

        super.render( entity, yaw, tickDelta, matrices, vertexConsumers, light );
    }

    @SuppressWarnings( "deprecation" ) // idk why BLOCK_ATLAS_TEXTURE is deprecated?
    @Override
    public Identifier getTexture( ExplosiveEntity entity )
    {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
