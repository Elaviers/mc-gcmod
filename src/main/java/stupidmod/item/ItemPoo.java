package stupidmod.item;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoneMeal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import stupidmod.ItemRegister;
import stupidmod.StupidMod;

public class ItemPoo extends Item {
    
    public ItemPoo(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        if (context.getItem().getItem() == ItemRegister.itemFermentedPoo)
        {
            World world = context.getWorld();
            BlockPos blockpos = context.getPos();
            BlockPos blockpos1 = blockpos.offset(context.getFace());
            if (applyFermentedPoo(context.getItem(), world, blockpos, context.getPlayer())) {
                if (!world.isRemote) {
                    world.playEvent(2005, blockpos, 0);
                }

                return EnumActionResult.SUCCESS;
            } else {
                IBlockState iblockstate = world.getBlockState(blockpos);
                boolean flag = iblockstate.getBlockFaceShape(world, blockpos, context.getFace()) == BlockFaceShape.SOLID;
                if (flag && ItemBoneMeal.growSeagrass(context.getItem(), world, blockpos1, context.getFace())) {
                    if (!world.isRemote) {
                        world.playEvent(2005, blockpos1, 0);
                    }

                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }
        }

        return super.onItemUse(context);
    }

    public static boolean applyFermentedPoo(ItemStack stack, World world, BlockPos pos, net.minecraft.entity.player.EntityPlayer player) {
        IBlockState state = world.getBlockState(pos);

        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, world, pos, state, stack);
        if (hook != 0) return hook > 0;

        if (state.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable)state.getBlock();
            if (igrowable.canGrow(world, pos, state, world.isRemote)) {
                if (!world.isRemote) {
                    int gCount = 0;

                    if (igrowable.canUseBonemeal(world, world.rand, pos, state)) {
                        do {
                            igrowable.grow(world, world.rand, pos, state);
                            gCount++;
                        }
                        while (gCount < 10 && igrowable.canUseBonemeal(world, world.rand, pos, state));
                    }

                    stack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }
}
