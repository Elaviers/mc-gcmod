package gcmod;

import gcmod.entity.CentrifugeEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CentrifugeScreen extends HandledScreen<CentrifugeScreenHandler>
{
    private final static Identifier BG_TEX = new Identifier( "gcmod", "textures/gui/centrifuge.png" );

    public CentrifugeScreen( CentrifugeScreenHandler handler, PlayerInventory inventory, Text title )
    {
        super( handler, inventory, title );
    }

    @Override
    protected void drawBackground( DrawContext context, float delta, int mouseX, int mouseY )
    {
        final int x = (this.width - this.backgroundWidth) / 2;
        final int y = (this.height - this.backgroundHeight) / 2;

        context.drawTexture( BG_TEX, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight );

        final float progress = Math.min( 1f, (float)this.handler.properties.get( 0 ) / CentrifugeEntity.PROCESS_DURATION_TICKS );
        context.drawTexture( BG_TEX, x + 79, y + 33, 176, 0, 18,  (int)(progress * 19f) );
    }

    @Override
    public void render( DrawContext context, int mouseX, int mouseY, float delta )
    {
        super.render( context, mouseX, mouseY, delta );
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
