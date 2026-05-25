package gcmod;

import com.mojang.blaze3d.vertex.PoseStack;
import gcmod.entity.PooEntity;
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
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class PooEntityRenderer extends EntityRenderer<PooEntity, PooEntityRenderState>
{
    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 17).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-4.0F, 3.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(modelData, 64, 32);
    }

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath( "gcmod", "textures/entity/poo.png" );
    public net.minecraft.client.model.geom.ModelPart model;

    protected PooEntityRenderer( EntityRendererProvider.Context ctx )
    {
        super( ctx );
        this.shadowRadius = 0;
        this.model = ctx.bakeLayer( GCModClient.POO_LAYER );
    }

    @Override
    public void submit( PooEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState )
    {
        if ( state.size > 0 )
        {
            matrices.pushPose();
            matrices.scale( state.size, state.size, state.size );
            queue.submitModelPart( this.model, matrices, RenderTypes.entitySolid( TEXTURE ), state.lightCoords, OverlayTexture.NO_OVERLAY, null );
            matrices.popPose();
        }

        super.submit( state, matrices, queue, cameraState );
    }

    @Override
    public PooEntityRenderState createRenderState()
    {
        return new PooEntityRenderState();
    }

    @Override
    public void extractRenderState( PooEntity entity, PooEntityRenderState state, float tickDelta )
    {
        super.extractRenderState( entity, state, tickDelta );
        state.size = entity.prevSize + tickDelta * (entity.size - entity.prevSize);
    }
}
