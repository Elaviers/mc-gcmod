package StupidMod.Items;

import StupidMod.Entities.EntityImpactExplosive;
import StupidMod.Entities.EntityPooExplosive;
import StupidMod.StupidMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemPooCannon extends Item {
    public ItemPooCannon(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
    
    private ItemStack findAmmo(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemPooBrick) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPooBrick) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                
                if (itemstack.getItem() instanceof ItemPooBrick) {
                    return itemstack;
                }
            }
            
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase m, int timeLeft) {
        EntityPlayer player = (EntityPlayer)m;
        ItemStack ammoStack = null;
        
        if (!player.capabilities.isCreativeMode) {
            ammoStack = this.findAmmo(player);
            if (ammoStack.isEmpty()) return;
        }
        
        player.playSound(StupidMod.instance.sounds.soundPooCannon, 1, 1);
        if (!world.isRemote) {
            if(!player.capabilities.isCreativeMode)
                ammoStack.setCount(ammoStack.getCount() - 1);
            
            
            EntityPooExplosive tst = new EntityPooExplosive(world, 0, 0, 0, 4);
            tst.setPosition(MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F, 0.1, MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
            tst.setLocationAndAngles(player.posX,player.posY+player.eyeHeight,player.posZ, player.rotationYaw, player.rotationPitch);
            tst.setPosition(tst.posX, tst.posY, tst.posZ);
            double motx = -MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI);
            double motz = MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI);
            double moty = -MathHelper.sin(tst.rotationPitch / 180.0F * (float)Math.PI);
            tst.motionX = motx * 2;
            tst.motionY = moty * 2;
            tst.motionZ = motz * 2;
            world.spawnEntity(tst);
        }
        
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ActionResult<ItemStack> event = net.minecraftforge.event.ForgeEventFactory.onArrowNock(player.getHeldItem(hand), world, player, hand, true);
        if (event != null) return event;
        
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
    
    /*
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity ent, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote) {
            if (ent.isSneaking())
                stack.setItemDamage(1);
            else
                stack.setItemDamage(0);
        }
    }
    */
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 10000000;
    }
}
