package gcmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class GCModClient implements ClientModInitializer
{
    public static final EntityModelLayer POO_LAYER = new EntityModelLayer( Identifier.of( "gcmod", "poo" ), "main" );
    public static final EntityModelLayer POO_BRICK_LAYER = new EntityModelLayer( Identifier.of( "gcmod", "poo_brick" ), "main" );
    public static final EntityModelLayer CENTRIFUGE_LAYER = new EntityModelLayer( Identifier.of( "gcmod", "centrifuge" ), "main" );

    @Override
    public void onInitializeClient()
    {
        FabricLoader.getInstance().getModContainer("gcmod").ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack( Identifier.of( "gcmod", "tileable_textures" ), container, Text.translatable("resourcepack.tileable_textures.name"), ResourcePackActivationType.NORMAL );
        });

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

        ParticleFactoryRegistry.getInstance().register( GCMod.PARTICLE_POO_SPLAT, SpitParticle.Factory::new );

        ClientPlayNetworking.registerGlobalReceiver( PooSplatPayload.ID, GCModClient::HandlePooSplat );
    }

    private static void HandlePooSplat( PooSplatPayload payload, ClientPlayNetworking.Context ctx )
    {
        ClientWorld world = ctx.client().world;
        final int numParticles = (int)(10f + payload.severity() * 20f);

        final float x = payload.position().x;
        final float y = payload.position().y;
        final float z = payload.position().z;
        final float impulse = payload.severity() * payload.severity() * .25f;
        Random rand = Random.create();

        for ( int i = 0; i < numParticles; ++i )
        {
            world.addParticle( GCMod.PARTICLE_POO_SPLAT, x, y, z,
                    (rand.nextFloat() * 2f - 1f) * impulse,
                    .33f + rand.nextFloat() * impulse,
                    (rand.nextFloat() * 2f - 1f) * impulse
                    );
        }

        world.playSound( x, y, z, GCMod.POO_BLOCK_SOUND, SoundCategory.BLOCKS, 2f, 1f, true );
    }
}
