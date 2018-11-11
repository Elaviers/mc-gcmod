package StupidMod;

import StupidMod.Entities.Tile.TileEntityWirelessTorchData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Utility {
    
    public static void registerRendererForItem(Item item, int meta, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
    }
    
    public static void registerRendererForItem(Item item) { registerRendererForItem(item, 0, "");}
    
    
    static public TileEntityWirelessTorchData setIndividualState(World world, BlockPos pos, boolean state) {
        System.out.println("Setting block at "+pos+" to "+state);
        IBlockState currentstate = world.getBlockState(pos);
        IBlockState newstate;
        if (state)newstate = StupidMod.instance.blocks.blockWirelessTorchOn.getDefaultState().withProperty(BlockTorch.FACING, currentstate.getValue(BlockTorch.FACING));
        else newstate = StupidMod.instance.blocks.blockWirelessTorch.getDefaultState().withProperty(BlockTorch.FACING, currentstate.getValue(BlockTorch.FACING));
    
        //TileEntityWirelessTorchData td = (TileEntityWirelessTorchData)world.getTileEntity(pos);
        //ArrayList<BlockPos> network = td.LinkedPositions;
    
        TileEntityWirelessTorchData td = (TileEntityWirelessTorchData)world.getTileEntity(pos);
        td.changingState = true;
        world.setBlockState(pos, newstate);
        td.changingState = false;
        
        //td = (TileEntityWirelessTorchData)world.getTileEntity(pos);
        //td.LinkedPositions = network;
        return td;
    }

}
