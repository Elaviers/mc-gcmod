package gcmod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import gcmod.entity.CentrifugeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CentrifugeEntityRenderer implements BlockEntityRenderer<CentrifugeEntity, CentrifugeEntityRenderState>
{
    private final net.minecraft.client.model.geom.ModelPart bone;
    private final net.minecraft.client.model.geom.ModelPart bb_main;
    private final static Identifier TEXTURE = Identifier.fromNamespaceAndPath( "gcmod", "textures/entity/centrifuge.png" );

    CentrifugeEntityRenderer( BlockEntityRendererProvider.Context ctx)
    {
        this.bone = ctx.bakeLayer( GCModClient.CENTRIFUGE_LAYER ).getChild( "bone" );
        this.bb_main = ctx.bakeLayer( GCModClient.CENTRIFUGE_LAYER ).getChild( "bb_main" );
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition bone = modelPartData.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(56, 17).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-8.0F, 15.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-1.0F, 15.0F, 1.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(1.0F, 15.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-1.0F, 15.0F, -8.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(54, 5).addBox(-8.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 8).addBox(-2.0F, 13.0F, 7.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 11).addBox(7.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 17).addBox(-2.0F, 13.0F, -8.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition drum12_r1 = bone.addOrReplaceChild("drum12_r1", CubeListBuilder.create().texOffs(36, 23).addBox(-2.0F, 13.0F, -8.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 11).addBox(7.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 8).addBox(-2.0F, 13.0F, 7.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 17).addBox(-8.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition drum8_r1 = bone.addOrReplaceChild("drum8_r1", CubeListBuilder.create().texOffs(36, 20).addBox(-2.0F, 13.0F, -8.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 11).addBox(7.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(34, 8).addBox(-2.0F, 13.0F, 7.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 11).addBox(-8.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-6.0F, 0.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 9).addBox(-4.0F, 2.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public CentrifugeEntityRenderState createRenderState()
    {
        return new CentrifugeEntityRenderState();
    }

    @Override
    public void submit( CentrifugeEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState )
    {
        matrices.pushPose();
        matrices.translate( .5, 0, .5 );
        queue.submitModelPart( this.bb_main, matrices, RenderTypes.entitySolid( TEXTURE ), state.lightCoords, OverlayTexture.NO_OVERLAY, null );
        matrices.mulPose( Axis.YP.rotationDegrees( state.yaw ));
        queue.submitModelPart( this.bone, matrices, RenderTypes.entitySolid( TEXTURE ), state.lightCoords, OverlayTexture.NO_OVERLAY, null );
        matrices.popPose();
    }

    @Override
    public void extractRenderState( CentrifugeEntity centrifuge, CentrifugeEntityRenderState state, float tickProgress, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay )
    {
        BlockEntityRenderer.super.extractRenderState( centrifuge, state, tickProgress, cameraPos, crumblingOverlay );

        CentrifugeSound.updateSoundForEntity( centrifuge ); // suboptimal

        state.yaw = centrifuge.prevAngle + tickProgress * (centrifuge.angle - centrifuge.prevAngle);
    }
}
