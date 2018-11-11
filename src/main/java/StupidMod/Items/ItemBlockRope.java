package StupidMod.Items;

import StupidMod.Blocks.BlockRope;
import com.sun.jna.platform.win32.WinUser;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import java.util.Stack;

public class ItemBlockRope extends ItemBlock {
    
    public ItemBlockRope(Block block)
    {
        super(block);
        
        this.setUnlocalizedName(block.getUnlocalizedName());
        this.setRegistryName(block.getRegistryName());
    }
    
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.getBlockState(pos).getBlock() instanceof BlockRope) {
            BlockPos blockcheck = pos;
        
            while (world.getBlockState(blockcheck).getBlock() instanceof BlockRope)
                blockcheck = blockcheck.offset(EnumFacing.DOWN);
        
            if (world.getBlockState(blockcheck).getBlock() == Blocks.AIR) {
                if (player.isCreative())
                {
                    ItemStack itemStack = player.getHeldItem(hand);
                    int count = itemStack.getCount();
                    int data = itemStack.getMetadata();
                    EnumActionResult result = super.onItemUse(player, world, blockcheck.offset(EnumFacing.UP), hand, EnumFacing.DOWN, 0.5f, 0.5f, 0.5f);
                    itemStack.setCount(count);
                    itemStack.setItemDamage(data);
                    return result;
                }
                
                return super.onItemUse(player, world, blockcheck.offset(EnumFacing.UP), hand, EnumFacing.DOWN, 0.5f, 0.5f, 0.5f);
            }
        }
        
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
    
}
