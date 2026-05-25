package gcmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class GCModClient implements ClientModInitializer
{
    public static final ModelLayerLocation POO_LAYER = new ModelLayerLocation( Identifier.fromNamespaceAndPath( "gcmod", "poo" ), "main" );
    public static final ModelLayerLocation POO_BRICK_LAYER = new ModelLayerLocation( Identifier.fromNamespaceAndPath( "gcmod", "poo_brick" ), "main" );
    public static final ModelLayerLocation CENTRIFUGE_LAYER = new ModelLayerLocation( Identifier.fromNamespaceAndPath( "gcmod", "centrifuge" ), "main" );

    @Override
    public void onInitializeClient()
    {
        FabricLoader.getInstance().getModContainer("gcmod").ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack( Identifier.fromNamespaceAndPath( "gcmod", "tileable_textures" ), container, Component.translatable("resourcepack.tileable_textures.name"), ResourcePackActivationType.NORMAL );
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

        BlockEntityRenderers.register( GCMod.CENTRIFUGE_ENTITY, CentrifugeEntityRenderer::new );

        MenuScreens.register( GCMod.CENTRIFUGE_SCREEN_HANDLER, CentrifugeScreen::new );

        // BlockRenderLayerMap.INSTANCE.putBlock( GCMod.WIRELESS_TORCH, RenderLayer.getCutout() );
        // BlockRenderLayerMap.INSTANCE.putBlock( GCMod.WIRELESS_TORCH_WALL, RenderLayer.getCutout() );

        ParticleFactoryRegistry.getInstance().register( GCMod.PARTICLE_POO_SPLAT, SpitParticle.Provider::new );

        ClientPlayNetworking.registerGlobalReceiver( PooSplatPayload.ID, GCModClient::HandlePooSplat );

        ItemTooltipCallback.EVENT.register(
                ( ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipType, List<Component> lines) ->
                {
                    if ( stack.has( GCMod.DATA_EXPLOSIVE_INFO ) )
                        stack.get( GCMod.DATA_EXPLOSIVE_INFO ).addToTooltip( tooltipContext, lines::add, tooltipType, stack.getComponents() );

                    if ( stack.has( GCMod.DATA_TORCH_NETWORK ) )
                        stack.get( GCMod.DATA_TORCH_NETWORK ).addToTooltip( tooltipContext, lines::add, tooltipType, stack.getComponents() );
                }
        );
    }

    private static void HandlePooSplat( PooSplatPayload payload, ClientPlayNetworking.Context ctx )
    {
        ClientLevel world = ctx.client().level;
        final int numParticles = (int)(10f + payload.severity() * 20f);

        final float x = payload.position().x();
        final float y = payload.position().y();
        final float z = payload.position().z();
        final float impulse = payload.severity() * payload.severity() * .25f;
        RandomSource rand = RandomSource.create();

        for ( int i = 0; i < numParticles; ++i )
        {
            world.addParticle( GCMod.PARTICLE_POO_SPLAT, x, y, z,
                    (rand.nextFloat() * 2f - 1f) * impulse,
                    .33f + rand.nextFloat() * impulse,
                    (rand.nextFloat() * 2f - 1f) * impulse
                    );
        }

        world.playLocalSound( x, y, z, GCMod.POO_BLOCK_SOUND, SoundSource.BLOCKS, 2f, 1f, true );
    }
}
