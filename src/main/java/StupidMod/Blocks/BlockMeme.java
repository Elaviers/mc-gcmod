package StupidMod.Blocks;

import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class BlockMeme extends Block {
    
    public BlockMeme(String name) {
        super(Material.GRASS);
        
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    
        SoundEvent se = new SoundEvent(new ResourceLocation(StupidMod.id, "sound.meme_block"));
        this.setSoundType(new SoundType(1, 1, se, se, se, se, se));
        
        this.setHardness(3);
    }
    
    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
        return 1.3f;
    }
}
