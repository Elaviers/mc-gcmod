package StupidMod.Items;

import StupidMod.Blocks.BlockExplosive;
import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockExplosive extends ItemBlock {
    
    public ItemBlockExplosive(Block block)
    {
        super(block);
    
        this.setUnlocalizedName(block.getUnlocalizedName());
        this.setRegistryName(block.getRegistryName());
        
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        final String[] suffixes = {"blast", "construct", "dig", "airstrike"};
        
        return this.getUnlocalizedName() + "_" + suffixes[stack.getItemDamage() / BlockExplosive.tierCount];
    }
    
    @Override
    public int getMetadata(int damage) {
        return damage;
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTagCompound())
            addDefaultNbtToStack(stack);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound())
            addDefaultNbtToStack(stack);
        
        NBTTagCompound tagCompound = stack.getTagCompound();
        tooltip.add(tagCompound.getShort("Strength") + " Strength");
        
        if (tagCompound.getShort("Fuse") > 0)
            tooltip.add(tagCompound.getShort("Fuse") + " Tick fuse");
        
        switch(stack.getMetadata() / BlockExplosive.tierCount)
        {
            case 1:
                tooltip.add(tagCompound.getString("Block") + " (" + tagCompound.getShort("BlockMeta") + ")");
                break;
                
            case 3:
                tooltip.add(tagCompound.getShort("Spread") + " Spread");
                tooltip.add(tagCompound.getShort("Pieces") + " Pieces");
                tooltip.add(tagCompound.getShort("Height") + " Height");
                break;
        }
    }
    
    
    
    public static void addDefaultNbtToStack(ItemStack stack)
    {
        NBTTagCompound tag = new NBTTagCompound();
        
        int fuse = 0, strength = 0;
        
        switch(stack.getMetadata() % BlockExplosive.tierCount)
        {
            case 0:
                strength = 1;
                break;
            case 1:
                strength = 4;
                break;
            case 2:
                strength = 8;
                break;
        }
        
        tag.setShort("Fuse", (short)fuse);
        tag.setShort("Strength", (short)strength);
        
        switch (stack.getMetadata() / BlockExplosive.tierCount)
        {
            case 1:
                tag.setString("Block", "minecraft:stone");
                tag.setShort("BlockMeta", (short)0);
                break;
                
            case 3:
                tag.setShort("Spread", (short)4);
                tag.setShort("Pieces", (short)5);
                tag.setShort("Height", (short)30);
        }
        
        stack.setTagCompound(tag);
    }
    
    public static ItemStack MakeStack(short fuse, short strength) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        
        ItemStack stack = new ItemStack(StupidMod.instance.blocks.itemBlockExplosive, 1, metaFromStats(0, strength));
        stack.setTagCompound(nbt);
        return stack;
    }
    
    public static ItemStack MakeStackConstructive(short fuse, short strength, String blockID, short blockMeta) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        nbt.setString("Block", blockID);
        nbt.setShort("BlockMeta", blockMeta);
        
        ItemStack stack = new ItemStack(StupidMod.instance.blocks.itemBlockExplosive, 1, metaFromStats(1, strength));
        stack.setTagCompound(nbt);
        return stack;
    }
    
    public static ItemStack makeStackAirstrike(short fuse, short strength, short spread, short pieces, short height) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        nbt.setShort("Spread", spread);
        nbt.setShort("Pieces", pieces);
        nbt.setShort("Height", height);
    
        ItemStack stack = new ItemStack(StupidMod.instance.blocks.itemBlockExplosive, 1, metaFromStatsAirstrike(strength, pieces));
        stack.setTagCompound(nbt);
        return stack;
    }
    
    public static int metaFromStats(int group, int strength) {
        int tier = 0;
        if (strength >= 9)
            tier = 2;
        else if (strength >= 3)
            tier = 1;
        
        return  BlockExplosive.tierCount * group + tier;
    }
    
    public static int metaFromStatsAirstrike(int strength, int pieces) {
        int tier = 0;
        if (strength * pieces >= 20)
            tier = 2;
        else if (strength * pieces >= 10)
            tier = 1;
        
        return BlockExplosive.tierCount * 3 + tier;
    }
}
