package gcmod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import gcmod.entity.PooBrickEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class PooBrickEntityRenderer extends EntityRenderer<PooBrickEntity, PooBrickEntityRenderState>
{
    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(
                "brick",
                CubeListBuilder.create().texOffs( 0, 0 ).addBox( -4, -2, -2, 8, 4, 4, new CubeDeformation( 0.0F ) ),
                PartPose.offset( 0.0F, 0.0F, 0.0F ) );

        return LayerDefinition.create( modelData, 64, 32 );
    }

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath( "gcmod", "textures/item/poo_brick.png" );
    public net.minecraft.client.model.geom.ModelPart model;

    protected PooBrickEntityRenderer( EntityRendererProvider.Context ctx )
    {
        super( ctx );
        this.shadowRadius = .25f;
        this.model = ctx.bakeLayer( GCModClient.POO_BRICK_LAYER );
    }

    @Override
    public void submit( PooBrickEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState )
    {
        matrices.pushPose();

        matrices.translate( 0.0, 0.125, 0.0 );
        matrices.mulPose( Axis.XP.rotationDegrees( state.angleX ) );
        matrices.mulPose( Axis.YP.rotationDegrees( state.angleY ) );
        matrices.mulPose( Axis.ZP.rotationDegrees( state.angleZ ) );
        queue.submitModelPart( this.model, matrices, RenderTypes.entitySolid( TEXTURE ), state.lightCoords, OverlayTexture.NO_OVERLAY, null );
        matrices.popPose();

        super.submit( state, matrices, queue, cameraState );
    }

    @Override
    public PooBrickEntityRenderState createRenderState()
    {
        return new PooBrickEntityRenderState();
    }

    @Override
    public void extractRenderState( PooBrickEntity entity, PooBrickEntityRenderState state, float tickDelta )
    {
        super.extractRenderState( entity, state, tickDelta );
        state.angleX = entity.prevAngleX + tickDelta * (entity.angleX - entity.prevAngleX);
        state.angleY = entity.prevAngleY + tickDelta * (entity.angleY - entity.prevAngleY);
        state.angleZ = entity.prevAngleZ + tickDelta * (entity.angleZ - entity.prevAngleZ);
    }
}
