package stupidmod.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import stupidmod.StupidMod;

public class FertiliserItem extends Item {
    public static final int MAX_DAMAGE = 200;

    public FertiliserItem(String name) {
        super(new Properties().group(StupidMod.GROUP).maxDamage(MAX_DAMAGE).setNoRepair());

        this.setRegistryName(name);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockPos blockpos1 = blockpos.offset(context.getFace());
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();
        if (applyBonemeal(stack, world, blockpos, player, context.getHand())) {
            if (!world.isRemote) {
                world.playEvent(2005, blockpos, 0);

                if (stack.getDamage() >= MAX_DAMAGE - 2) {
                    if (!player.isCreative())
                        player.setHeldItem(context.getHand(), new ItemStack(Items.BUCKET));
                }
                else
                    stack.damageItem(2, player, null);
            }

            return ActionResultType.SUCCESS;
        } else {
            BlockState blockstate = world.getBlockState(blockpos);
            boolean flag = blockstate.isSolidSide(world, blockpos, context.getFace());
            if (flag && BoneMealItem.growSeagrass(stack, world, blockpos1, context.getFace())) {
                if (!world.isRemote) {
                    world.playEvent(2005, blockpos1, 0);

                    if (stack.getDamage() >= MAX_DAMAGE - 2) {
                        if (!player.isCreative())
                            player.setHeldItem(context.getHand(), new ItemStack(Items.BUCKET));
                    }
                    else
                        stack.damageItem(2, player, null);
                }

                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos pos, net.minecraft.entity.player.PlayerEntity player, Hand hand) {
        BlockState blockstate = worldIn.getBlockState(pos);
        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, pos, blockstate, stack);
        if (hook != 0) return hook > 0;
        if (blockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable)blockstate.getBlock();
            if (igrowable.canGrow(worldIn, pos, blockstate, worldIn.isRemote)) {
                if (worldIn instanceof ServerWorld) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos, blockstate)) {
                        igrowable.grow((ServerWorld)worldIn, worldIn.rand, pos, blockstate);
                    }
                }

                return true;
            }
        }

        return false;
    }
}