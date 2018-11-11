package StupidMod.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockStupidOre extends Block {
    private Item itemDrop;
    
    public BlockStupidOre(String name, Item itemDrop) {
        super(Material.ROCK);
        
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        
        this.setHardness(2);
        this.setHarvestLevel("pickaxe", 1);
        
        this.itemDrop = itemDrop;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int m)
    {
        return itemDrop;
    }
    
    @Override
    public int quantityDropped(Random rand)
    {
        int drop = rand.nextInt(3) + 1;
        return drop;
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int m) {
        return (int)(Math.random() * 3);
    }
}
