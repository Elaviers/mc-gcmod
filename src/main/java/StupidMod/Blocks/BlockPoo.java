package StupidMod.Blocks;

import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPoo extends Block {
    public static final PropertyBool FERMENTED = PropertyBool.create("fermented");
    
    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FERMENTED);
    }
    
    @SuppressWarnings( "deprecation" )
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 1)
            return this.getDefaultState().withProperty(FERMENTED, true);
        
        return this.getDefaultState().withProperty(FERMENTED, false);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FERMENTED) ? 1 : 0;
    }
    
    public BlockPoo(String name) {
        super(Material.WOOD);

        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        
        SoundEvent se = new SoundEvent(new ResourceLocation(StupidMod.id, "sound.poo_block"));
        this.setSoundType(new SoundType(1, 1, se, se, se, se, se));
        
        this.setHardness(1.0f);
        this.setTickRandomly(true);
        
        this.setDefaultState(this.blockState.getBaseState().withProperty(FERMENTED, false));
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(FERMENTED)) return;
        
        BlockPos checkpos = pos.offset(EnumFacing.UP);
        if (world.getLight(pos) < 10 && world.getBlockState(checkpos).getBlock() instanceof BlockRope && world.getBlockState(checkpos).getBlock().getActualState(null, world, pos.offset(EnumFacing.UP)).getValue(BlockRope.WET)){
            world.setBlockState(pos, this.getDefaultState().withProperty(FERMENTED, true));
            return;
        }
        
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            if (world.getBlockState(pos.offset(enumfacing).down()).getMaterial() == Material.WATER)
                if (world.getLight(pos) < 5)
                    if (world.rand.nextInt(5) == 0)
                        world.setBlockState(pos, this.getDefaultState().withProperty(FERMENTED, true));
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this,1,0));
        list.add(new ItemStack(this,1,1));
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        for (int i = 0;i < 9;i++)
            drops.add(new ItemStack(StupidMod.instance.items.itemPoo,1, state.getValue(FERMENTED) ? 1 : 2));
    }
}
