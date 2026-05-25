package gcmod;

import gcmod.entity.CentrifugeEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CentrifugeScreen extends AbstractContainerScreen<CentrifugeScreenHandler>
{
    private final static Identifier BG_TEX = Identifier.fromNamespaceAndPath( "gcmod", "textures/gui/centrifuge.png" );

    public CentrifugeScreen( CentrifugeScreenHandler handler, Inventory inventory, Component title )
    {
        super( handler, inventory, title );
    }

    @Override
    protected void renderBg( GuiGraphics context, float delta, int mouseX, int mouseY )
    {
        final int x = (this.width - this.imageWidth) / 2;
        final int y = (this.height - this.imageHeight) / 2;
        context.blit( RenderPipelines.GUI_TEXTURED, BG_TEX, x, y, 0f, 0f, this.imageWidth, this.imageHeight, 256, 256 );

        final float progress = Math.min( 1f, (float) this.menu.properties.get( 0 ) / CentrifugeEntity.PROCESS_DURATION_TICKS );
        context.blit( RenderPipelines.GUI_TEXTURED, BG_TEX, x + 79, y + 33, 176, 0, 18, (int) (progress * 19f), 256, 256 );
    }

    @Override
    public void render( GuiGraphics context, int mouseX, int mouseY, float delta )
    {
        super.render( context, mouseX, mouseY, delta );
        this.renderTooltip( context, mouseX, mouseY );
    }
}
