package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class SulphurOreBlock extends Block {
    
    final boolean isNoahOre;
    
    public SulphurOreBlock(String name, boolean noah)
    {
        super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(3.f));
    
        this.setRegistryName(name);
        this.isNoahOre = noah;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        if (silktouch == 0)
        {
            return isNoahOre ? MathHelper.nextInt(RANDOM, 10, 50) : MathHelper.nextInt(RANDOM, 0, 3);
        }

        return 0;
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return isNoahOre ? 2 : 1;
    }
}
