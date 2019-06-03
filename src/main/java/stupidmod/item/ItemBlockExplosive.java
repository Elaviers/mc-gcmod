package stupidmod.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import stupidmod.BlockRegister;
import stupidmod.StupidMod;
import stupidmod.block.BlockExplosive;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockExplosive extends ItemBlock {
    
    public ItemBlockExplosive(Block block) {
        super(block, new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(block.getRegistryName());
    }
    
    void addDefaultTag(ItemStack stack)
    {
        NBTTagCompound tag = new NBTTagCompound();
    
        tag.setShort("Strength", (short)2);
    
        switch(((BlockExplosive)((ItemBlockExplosive)stack.getItem()).getBlock()).type)
        {
            case CONSTRUCTIVE:
                tag.setTag("Block", NBTUtil.writeBlockState(Blocks.STONE.getDefaultState()));
                break;
        
            case AIRSTRIKE:
                tag.setShort("Pieces", (short)5);
                tag.setShort("Spread", (short)3);
                tag.setShort("Height", (short)20);
                break;
        }
    
        stack.setTag(tag);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (!stack.hasTag())
            addDefaultTag(stack);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (!stack.hasTag())
            addDefaultTag(stack);
    
        NBTTagCompound tag = stack.getTag();
        tooltip.add(new TextComponentString(tag.getShort("Strength") + " Strength"));
    
        if (tag.getShort("Fuse") > 0)
            tooltip.add(new TextComponentString(tag.getShort("Fuse") + " Tick fuse"));
    
        switch(((BlockExplosive)((ItemBlockExplosive)stack.getItem()).getBlock()).type)
        {
            case CONSTRUCTIVE:
                tooltip.add(new TextComponentString(NBTUtil.readBlockState(tag.getCompound("Block")).getBlock().getNameTextComponent().getString()));
                break;
        
            case AIRSTRIKE:
                tooltip.add(new TextComponentString(tag.getShort("Spread") + " Spread"));
                tooltip.add(new TextComponentString(tag.getShort("Pieces") + " Pieces"));
                tooltip.add(new TextComponentString(tag.getShort("Height") + " Height"));
                break;
        }
    }
    
    public static ItemStack makeStackBlast(short fuse, short strength) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        
        ItemStack stack = new ItemStack(BlockRegister.blockBlastTNT);
        stack.setTag(nbt);
        return stack;
    }
    
    public static ItemStack makeStackConstructive(short fuse, short strength, IBlockState state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        nbt.setTag("Block", NBTUtil.writeBlockState(state));
        
        ItemStack stack = new ItemStack(BlockRegister.itemBlockConstructiveTNT);
        stack.setTag(nbt);
        return stack;
    }
    
    public static ItemStack makeStackDig(short fuse, short strength) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        
        ItemStack stack = new ItemStack(BlockRegister.blockDigTNT);
        stack.setTag(nbt);
        return stack;
    }
    
    public static ItemStack makeStackAirstrike(short fuse, short strength, short spread, short pieces, short height) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("Fuse", fuse);;
        nbt.setShort("Strength", strength);;
        nbt.setShort("Spread", spread);
        nbt.setShort("Pieces", pieces);
        nbt.setShort("Height", height);
        
        ItemStack stack = new ItemStack(BlockRegister.itemBlockAirstrikeTNT);
        stack.setTag(nbt);
        return stack;
    }

    @Override
    public EnumActionResult tryPlace(BlockItemUseContext context) {
        if (!context.canPlace()) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockstate = this.getStateForPlacement(context);
            if (iblockstate == null) {
                return EnumActionResult.FAIL;
            } else if (!this.placeBlock(context, iblockstate)) {
                return EnumActionResult.FAIL;
            } else {
                BlockPos blockpos = context.getPos();
                World world = context.getWorld();
                EntityPlayer entityplayer = context.getPlayer();
                ItemStack itemstack = context.getItem();
                IBlockState iblockstate1 = world.getBlockState(blockpos);
                Block block = iblockstate1.getBlock();
                if (block == iblockstate.getBlock()) {
                    this.onBlockPlaced(blockpos, world, entityplayer, itemstack, iblockstate1);
                    block.onBlockPlacedBy(world, blockpos, iblockstate1, entityplayer, itemstack);
                    if (entityplayer instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)entityplayer, blockpos, itemstack);
                    }
                }

                SoundType soundtype = iblockstate1.getSoundType(world, blockpos, context.getPlayer());
                world.playSound(entityplayer, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (!entityplayer.isCreative())
                    itemstack.shrink(1);

                return EnumActionResult.SUCCESS;
            }
        }
    }
}
