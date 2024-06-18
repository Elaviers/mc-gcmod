package gcmod;

import gcmod.entity.CentrifugeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class CentrifugeEntityRenderer implements BlockEntityRenderer<CentrifugeEntity>
{
    private final ModelPart bone;
    private final ModelPart bb_main;
    private final static Identifier TEXTURE = new Identifier( "gcmod", "textures/entity/centrifuge.png" );

    CentrifugeEntityRenderer( BlockEntityRendererFactory.Context ctx)
    {
        this.bone = ctx.getLayerModelPart( GCModClient.CENTRIFUGE_LAYER ).getChild( "bone" );
        this.bb_main = ctx.getLayerModelPart( GCModClient.CENTRIFUGE_LAYER ).getChild( "bb_main" );
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(56, 17).cuboid(-1.0F, 3.0F, -1.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-8.0F, 15.0F, -1.0F, 7.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(18, 0).cuboid(-1.0F, 15.0F, 1.0F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(1.0F, 15.0F, -1.0F, 7.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(36, 0).cuboid(-1.0F, 15.0F, -8.0F, 2.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(54, 5).cuboid(-8.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(24, 8).cuboid(-2.0F, 13.0F, 7.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 11).cuboid(7.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 17).cuboid(-2.0F, 13.0F, -8.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData drum12_r1 = bone.addChild("drum12_r1", ModelPartBuilder.create().uv(36, 23).cuboid(-2.0F, 13.0F, -8.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(44, 11).cuboid(7.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(44, 8).cuboid(-2.0F, 13.0F, 7.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(46, 17).cuboid(-8.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData drum8_r1 = bone.addChild("drum8_r1", ModelPartBuilder.create().uv(36, 20).cuboid(-2.0F, 13.0F, -8.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(34, 11).cuboid(7.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(34, 8).cuboid(-2.0F, 13.0F, 7.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(54, 11).cuboid(-8.0F, 13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 18).mirrored().cuboid(-6.0F, 0.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 9).cuboid(-4.0F, 2.0F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render( CentrifugeEntity centrifuge, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay )
    {
        CentrifugeSound.updateSoundForEntity( centrifuge );

        matrices.push();
        final float angle = centrifuge.prevAngle + tickDelta * (centrifuge.angle - centrifuge.prevAngle);

        matrices.translate( .5, 0, .5 );
        this.bb_main.render( matrices, vertexConsumers.getBuffer( RenderLayer.getEntitySolid( TEXTURE ) ), light, overlay );

        matrices.multiply( RotationAxis.POSITIVE_Y.rotationDegrees( angle ));
        this.bone.render( matrices, vertexConsumers.getBuffer( RenderLayer.getEntitySolid( TEXTURE ) ), light, overlay );

        matrices.pop();
    }
}
