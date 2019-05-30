package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import stupidmod.ItemRegister;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSulphurOre extends Block {
    
    final boolean isNoahOre;
    
    public BlockSulphurOre(String name, boolean noah)
    {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2));
    
        this.setRegistryName(name);
        this.isNoahOre = noah;
    }
    
    @Nullable
    @Override
    public ToolType getHarvestTool(IBlockState state) {
        return ToolType.PICKAXE;
    }
    
    @Override
    public int getHarvestLevel(IBlockState state) {
        return 1;
    }
    
    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return this.isNoahOre ? ItemRegister.itemNoahSulphur : ItemRegister.itemSulphur;
    }
    
    @Override
    public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
        return random.nextInt(3) + 1;
    }
    
    @Override
    public int getExpDrop(IBlockState state, IWorldReader world, BlockPos pos, int fortune) {
        return (int)(Math.random() * 3);
    }
}
