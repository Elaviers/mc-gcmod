package StupidMod.Misc;

import StupidMod.Client.GuiCentrifuge;
import StupidMod.Entities.Tile.TileEntityCentrifuge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x,y,z));
        if(tileEntity instanceof TileEntityCentrifuge)
            return new ContainerCentrifuge(player.inventory, (TileEntityCentrifuge) tileEntity,ID == 1);
        
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x,y,z));
        if(tileEntity instanceof TileEntityCentrifuge)
            return new GuiCentrifuge(player.inventory, (TileEntityCentrifuge) tileEntity,ID == 1);
        
        return null;
    }
    
}