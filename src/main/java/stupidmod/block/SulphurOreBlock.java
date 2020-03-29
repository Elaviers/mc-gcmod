package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class SulphurOreBlock extends Block {
    
    final boolean isNoahOre;
    
    public SulphurOreBlock(String name, boolean noah)
    {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2));
    
        this.setRegistryName(name);
        this.isNoahOre = noah;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? (int)(Math.random() * 3) : 0;
    }
}
