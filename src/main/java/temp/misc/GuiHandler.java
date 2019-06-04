package stupidmod.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.network.FMLPlayMessages;
import stupidmod.client.GuiCentrifuge;
import stupidmod.entity.tile.TileEntityCentrifuge;

public class GuiHandler {

    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer)
    {
        BlockPos pos = openContainer.getAdditionalData().readBlockPos();
        TileEntityCentrifuge te = (TileEntityCentrifuge)Minecraft.getInstance().world.getTileEntity(pos);

        if (openContainer.getId().toString().equals(TileEntityCentrifuge.GUI_ID))
            return new GuiCentrifuge(Minecraft.getInstance().player.inventory, te, te.isSpinning());

        return null;
    }
}