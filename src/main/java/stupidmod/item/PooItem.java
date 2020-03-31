package stupidmod.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import stupidmod.StupidModItems;
import stupidmod.StupidMod;

public class PooItem extends Item {
    
    public PooItem(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getItem().getItem() == StupidModItems.FERMENTED_POO)
        {
            World world = context.getWorld();
            BlockPos blockpos = context.getPos();
            BlockPos blockpos1 = blockpos.offset(context.getFace());
            if (applyFermentedPoo(context.getItem(), world, blockpos, context.getPlayer())) {
                if (!world.isRemote) {
                    world.playEvent(2005, blockpos, 0);
                }

                return ActionResultType.SUCCESS;
            } else {
                BlockState blockState = world.getBlockState(blockpos);
                boolean flag = blockState.func_224755_d(world, blockpos, context.getFace()); //Is solid side
                if (flag && BoneMealItem.growSeagrass(context.getItem(), world, blockpos1, context.getFace())) {
                    if (!world.isRemote) {
                        world.playEvent(2005, blockpos1, 0);
                    }

                    return ActionResultType.SUCCESS;
                } else {
                    return ActionResultType.PASS;
                }
            }
        }

        return super.onItemUse(context);
    }

    public static boolean applyFermentedPoo(ItemStack stack, World world, BlockPos pos, PlayerEntity player) {
        BlockState state = world.getBlockState(pos);

        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, world, pos, state, stack);
        if (hook != 0) return hook > 0;

        if (state.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable)state.getBlock();
            if (igrowable.canGrow(world, pos, state, world.isRemote)) {
                if (world instanceof ServerWorld) {
                    int gCount = 0;

                    if (igrowable.canUseBonemeal(world, world.rand, pos, state)) {
                        do {
                            igrowable.grow((ServerWorld) world, world.rand, pos, state);
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
