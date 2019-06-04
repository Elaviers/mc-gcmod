package stupidmod.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import stupidmod.BlockRegister;
import stupidmod.SoundRegister;

import java.util.Random;

public class BlockPoo extends Block {
    
    public BlockPoo(String name, SoundType sound) {
        super(Properties.create(Material.WOOD)
                .sound(sound)
                .hardnessAndResistance(1.0f)
                .needsRandomTick());
        
        this.setRegistryName(name);
    }
    
    @Override
    public void tick(IBlockState state, World world, BlockPos pos, Random random) {
        if (state.getBlock() == BlockRegister.blockPooFermented) return;
        
        BlockPos checkpos = pos.offset(EnumFacing.UP);
        if (world.getLight(pos) < 10 && world.getBlockState(checkpos).getBlock() instanceof BlockRope && world.getBlockState(checkpos).get(BlockRope.WET)){
            world.setBlockState(pos, BlockRegister.blockPooFermented.getDefaultState());
            return;
        }
    
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            if (world.getBlockState(pos.offset(enumfacing).down()).getMaterial() == Material.WATER)
                if (world.getLight(pos) < 5)
                    if (world.rand.nextInt(5) == 0)
                        world.setBlockState(pos, BlockRegister.blockPooFermented.getDefaultState());
    }
}
