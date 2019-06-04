package stupidmod.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import stupidmod.SoundRegister;
import stupidmod.StupidMod;
import stupidmod.entity.EntityPooExplosive;

public class ItemPooCannon extends Item {
    
    public ItemPooCannon(String name) {
        super(new Properties().group(StupidMod.GROUP));
    
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
        
        if (!player.isCreative()) {
            ammoStack = this.findAmmo(player);
            if (ammoStack.isEmpty()) return;
        }
        
        player.playSound(SoundRegister.soundPooCannon, 1, 1);
        if (!world.isRemote) {
            if(!player.isCreative())
                ammoStack.setCount(ammoStack.getCount() - 1);
            
            
            EntityPooExplosive tst = new EntityPooExplosive(world, 0, 0, 0, 4);
            tst.setPosition(MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F, 0.1, MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
            tst.setLocationAndAngles(player.posX,player.posY+player.getEyeHeight(),player.posZ, player.rotationYaw, player.rotationPitch);
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
    
    @Override
    public EnumAction getUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 10000000;
    }
    
}
