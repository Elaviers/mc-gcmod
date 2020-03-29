package stupidmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

public class MemeBlock extends Block {

    public MemeBlock(String name, SoundType sound) {
        super(Properties.create(Material.ORGANIC).sound(sound).hardnessAndResistance(3));
        
        this.setRegistryName(name);
    }
    
    @Override
    public float getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        return 1.3f;
    }
}
