package stupidmod.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import stupidmod.StupidModBlocks;
import stupidmod.StupidMod;
import stupidmod.block.ExplosiveBlock;

import javax.annotation.Nullable;
import java.util.List;

public class ExplosiveBlockItem extends BlockItem {
    
    public ExplosiveBlockItem(Block block) {
        super(block, new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(block.getRegistryName());
    }
    
    void addDefaultTag(ItemStack stack)
    {
        CompoundNBT tag = new CompoundNBT();
    
        tag.putShort("Strength", (short)2);
    
        switch(((ExplosiveBlock)((ExplosiveBlockItem)stack.getItem()).getBlock()).type)
        {
            case BLAST:
                tag.putShort("Strength", (short)1);
                break;

            case CONSTRUCTIVE:
                tag.put("Block", NBTUtil.writeBlockState(Blocks.STONE.getDefaultState()));
                break;
        
            case AIRSTRIKE:
                tag.putShort("Pieces", (short)5);
                tag.putShort("Spread", (short)3);
                tag.putShort("Height", (short)20);
                break;
        }
    
        stack.setTag(tag);
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
    
        CompoundNBT tag = stack.getTag();
        tooltip.add(new StringTextComponent(tag.getShort("Strength") + " Strength"));
    
        if (tag.getShort("Fuse") > 0)
            tooltip.add(new StringTextComponent(tag.getShort("Fuse") + " Tick fuse"));
    
        switch(((ExplosiveBlock)((ExplosiveBlockItem)stack.getItem()).getBlock()).type)
        {
            case CONSTRUCTIVE:
                tooltip.add(new StringTextComponent(NBTUtil.readBlockState(tag.getCompound("Block")).getBlock().getNameTextComponent().getString()));
                break;
        
            case AIRSTRIKE:
                tooltip.add(new StringTextComponent(tag.getShort("Spread") + " Spread"));
                tooltip.add(new StringTextComponent(tag.getShort("Pieces") + " Pieces"));
                tooltip.add(new StringTextComponent(tag.getShort("Height") + " Height"));
                break;
        }
    }
    
    public static ItemStack makeStackBlast(short fuse, short strength) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putShort("Fuse", fuse);;
        nbt.putShort("Strength", strength);;
        
        ItemStack stack = new ItemStack(StupidModBlocks.BLAST_TNT);
        stack.setTag(nbt);
        return stack;
    }
    
    public static ItemStack makeStackConstructive(short fuse, short strength, BlockState state) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putShort("Fuse", fuse);;
        nbt.putShort("Strength", strength);;
        nbt.put("Block", NBTUtil.writeBlockState(state));
        
        ItemStack stack = new ItemStack(StupidModBlocks.CONSTRUCTIVE_TNT_ITEM);
        stack.setTag(nbt);
        return stack;
    }
    
    public static ItemStack makeStackDig(short fuse, short strength) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putShort("Fuse", fuse);;
        nbt.putShort("Strength", strength);;
        
        ItemStack stack = new ItemStack(StupidModBlocks.DIG_TNT);
        stack.setTag(nbt);
        return stack;
    }
    
    public static ItemStack makeStackAirstrike(short fuse, short strength, short spread, short pieces, short height) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putShort("Fuse", fuse);;
        nbt.putShort("Strength", strength);;
        nbt.putShort("Spread", spread);
        nbt.putShort("Pieces", pieces);
        nbt.putShort("Height", height);
        
        ItemStack stack = new ItemStack(StupidModBlocks.AIR_STRIKE_TNT_ITEM);
        stack.setTag(nbt);
        return stack;
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        if (!context.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            BlockState iblockstate = this.getStateForPlacement(context);
            if (iblockstate == null) {
                return ActionResultType.FAIL;
            } else if (!this.placeBlock(context, iblockstate)) {
                return ActionResultType.FAIL;
            } else {
                BlockPos blockpos = context.getPos();
                World world = context.getWorld();
                PlayerEntity entityplayer = context.getPlayer();
                ItemStack itemstack = context.getItem();
                BlockState iblockstate1 = world.getBlockState(blockpos);
                Block block = iblockstate1.getBlock();
                if (block == iblockstate.getBlock()) {
                    this.onBlockPlaced(blockpos, world, entityplayer, itemstack, iblockstate1);
                    block.onBlockPlacedBy(world, blockpos, iblockstate1, entityplayer, itemstack);
                    if (entityplayer instanceof ServerPlayerEntity) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)entityplayer, blockpos, itemstack);
                    }
                }

                SoundType soundtype = iblockstate1.getSoundType(world, blockpos, context.getPlayer());
                world.playSound(entityplayer, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (!entityplayer.isCreative())
                    itemstack.shrink(1);

                return ActionResultType.SUCCESS;
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == StupidMod.GROUP) {
            switch (((ExplosiveBlock) this.getBlock()).type) {
                case BLAST:
                    items.add(makeStackBlast((short) 0, (short) 4));
                    break;
                case CONSTRUCTIVE:
                    items.add(makeStackConstructive((short) 20, (short) 4, Blocks.STONE.getDefaultState()));
                    break;
                case DIG:
                    items.add(makeStackDig((short) 0, (short) 4));
                    break;
                case AIRSTRIKE:
                    items.add(makeStackAirstrike((short) 0, (short) 4, (short) 2, (short) 8, (short) 30));
                    break;
            }
        }
    }
}
