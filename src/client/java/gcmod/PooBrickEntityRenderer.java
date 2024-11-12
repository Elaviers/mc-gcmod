package gcmod;

import gcmod.entity.PooBrickEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class PooBrickEntityRenderer extends EntityRenderer<PooBrickEntity, PooBrickEntityRenderState>
{
    public static TexturedModelData getTexturedModelData()
    {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                "brick",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4, -2, -2, 8, 4, 4, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 32);
    }

    public static final Identifier TEXTURE = Identifier.of( "gcmod", "textures/item/poo_brick.png" );
    public ModelPart model;

    protected PooBrickEntityRenderer( EntityRendererFactory.Context ctx )
    {
        super( ctx );
        this.shadowRadius = .25f;
        this.model = ctx.getPart( GCModClient.POO_BRICK_LAYER );
    }

    @Override
    public void render( PooBrickEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light )
    {
        matrices.push();

        matrices.translate(0.0, 0.125, 0.0);
        matrices.multiply( RotationAxis.POSITIVE_X.rotationDegrees( state.angleX ) );
        matrices.multiply( RotationAxis.POSITIVE_Y.rotationDegrees( state.angleY ) );
        matrices.multiply( RotationAxis.POSITIVE_Z.rotationDegrees( state.angleZ ) );

        model.render( matrices, vertexConsumers.getBuffer( RenderLayer.getEntitySolid( TEXTURE ) ), light, OverlayTexture.DEFAULT_UV );

        matrices.pop();

        super.render( state, matrices, vertexConsumers, light );
    }

    @Override
    public PooBrickEntityRenderState createRenderState()
    {
        return new PooBrickEntityRenderState();
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
