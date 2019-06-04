package stupidmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import stupidmod.StupidMod;

import java.util.Collection;

public class ItemPooProtein extends ItemFood {
    
    public ItemPooProtein(String name) {
        super(1, 1, false, new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }
    
    
    
    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            Collection<PotionEffect> effects = player.getActivePotionEffects();
            
            int durationJump = 200;
            int durationStrength = 200;
            int durationHaste = 200;
            int durationSpeed = 200;
            
            for (PotionEffect effect : effects) {
                if (effect.getAmplifier() >= 4) {
                    if (effect.getPotion() == MobEffects.JUMP_BOOST)
                        durationJump += effect.getDuration();
                    else if (effect.getPotion() == MobEffects.STRENGTH)
                        durationStrength += effect.getDuration();
                    else if (effect.getPotion() == MobEffects.HASTE)
                        durationHaste += effect.getDuration();
                    else if (effect.getPotion() == MobEffects.SPEED)
                        durationSpeed += effect.getDuration();
                }
            }
            
            player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, durationJump, 4));
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, durationStrength, 4));
            player.addPotionEffect(new PotionEffect(MobEffects.HASTE, durationHaste, 4));
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, durationSpeed, 4));
        }
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 12;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    
}
