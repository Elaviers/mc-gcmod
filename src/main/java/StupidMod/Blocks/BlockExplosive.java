package StupidMod.Blocks;

import StupidMod.Entities.EntityAirStrikeExplosive;
import StupidMod.Entities.EntityConstructiveExplosive;
import StupidMod.Entities.EntityExplosive;
import StupidMod.Entities.Tile.TileEntityExplosiveData;
import StupidMod.Items.ItemBlockExplosive;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockExplosive extends Block implements ITileEntityProvider {
    public static final int tierCount = 3;
    
    public enum TntType implements IStringSerializable {
        BLAST("blast", 0),
        CONSTRUCT("construct", 1),
        DIG("dig", 2),
        AIRSTRIKE("airstrike", 3);
    
        private String name;
        private int index;
        
        private TntType(String name, int index)
        {
            this.name = name;
            this.index = index;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public int GetMetaGroup()
        {
            return this.index;
        }
        
        public static TntType GetFromMetaGroup(int metaGroup)
        {
            for (TntType tntType : values())
                if (tntType.index == metaGroup)
                    return tntType;
                
                return BLAST;
        }
    }
    
    public static final PropertyEnum TYPE = PropertyEnum.create("type", TntType.class);
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 2);
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, TIER);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TntType)state.getValue(TYPE)).GetMetaGroup() * tierCount + state.getValue(TIER);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, TntType.GetFromMetaGroup(meta / tierCount)).withProperty(TIER, meta % tierCount);
    }
    
    ////
    
    public BlockExplosive(String name)
    {
        super(Material.TNT);
        
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityExplosiveData();
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!stack.hasTagCompound())
            ItemBlockExplosive.addDefaultNbtToStack(stack);
        
        TileEntityExplosiveData te = (TileEntityExplosiveData) worldIn.getTileEntity(pos);
        
        te.readFromNBT(stack.getTagCompound());
    }
    
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 12; ++i)
            if (i / tierCount != 2)
                items.add(new ItemStack(this, 1, i));
    }
    
    void explode(World world, BlockPos pos, IBlockState state)
    {
        TileEntityExplosiveData te = (TileEntityExplosiveData)world.getTileEntity(pos);
        te.explode(world, pos, state);
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isBlockIndirectlyGettingPowered(pos) > 0)
            this.explode(worldIn, pos, state);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (world.isBlockIndirectlyGettingPowered(pos) > 0)
            this.explode(world, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
            this.explode(world, pos, state);
            return true;
        }
        
        return false;
    }
    
    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosionIn) {
        this.explode(world, pos, world.getBlockState(pos));
    }
}
