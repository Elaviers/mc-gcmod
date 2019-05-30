package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import stupidmod.SoundRegister;

import javax.annotation.Nullable;

public class BlockMeme extends Block {

    public BlockMeme(String name, SoundType sound) {
        super(Properties.create(Material.GRASS).sound(sound).hardnessAndResistance(3));
        
        this.setRegistryName(name);
    }
    
    @Override
    public float getSlipperiness(IBlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        return 1.3f;
    }
}
