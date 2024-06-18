package gcmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class GCModClient implements ClientModInitializer
{
    public static final EntityModelLayer POO_LAYER = new EntityModelLayer( Identifier.of( "gcmod", "poo" ), "main" );
    public static final EntityModelLayer POO_BRICK_LAYER = new EntityModelLayer( Identifier.of( "gcmod", "poo_brick" ), "main" );
    public static final EntityModelLayer CENTRIFUGE_LAYER = new EntityModelLayer( Identifier.of( "gcmod", "centrifuge" ), "main" );

    @Override
    public void onInitializeClient()
    {
        EntityRendererRegistry.register( GCMod.POO_ENTITY, PooEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.POO_BRICK_ENTITY, PooBrickEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.EXPLOSIVE_POO_BRICK_ENTITY, PooBrickEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.EXPLOSIVE_BOMB_ENTITY, ExplosiveBombEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.EXPLOSIVE_ENTITY, ExplosiveEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.AIRSTRIKE_EXPLOSIVE_ENTITY, ExplosiveEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.CONSTRUCTIVE_EXPLOSIVE_ENTITY, ExplosiveEntityRenderer::new );
        EntityRendererRegistry.register( GCMod.DIG_EXPLOSIVE_ENTITY, ExplosiveEntityRenderer::new );

        EntityModelLayerRegistry.registerModelLayer( POO_LAYER, PooEntityRenderer::getTexturedModelData );
        EntityModelLayerRegistry.registerModelLayer( POO_BRICK_LAYER, PooBrickEntityRenderer::getTexturedModelData );
        EntityModelLayerRegistry.registerModelLayer( CENTRIFUGE_LAYER, CentrifugeEntityRenderer::getTexturedModelData );

        BlockEntityRendererFactories.register( GCMod.CENTRIFUGE_ENTITY, CentrifugeEntityRenderer::new );

        HandledScreens.register( GCMod.CENTRIFUGE_SCREEN_HANDLER, CentrifugeScreen::new );

        BlockRenderLayerMap.INSTANCE.putBlock( GCMod.WIRELESS_TORCH, RenderLayer.getCutout() );
        BlockRenderLayerMap.INSTANCE.putBlock( GCMod.WIRELESS_TORCH_WALL, RenderLayer.getCutout() );
    }
}
