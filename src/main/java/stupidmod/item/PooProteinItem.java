package stupidmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import stupidmod.StupidMod;

import java.util.Collection;

public class PooProteinItem extends Item {

    static final Food FOOD = (new Food.Builder()).saturation(5).setAlwaysEdible().hunger(0).fastToEat().build();

    public PooProteinItem(String name) {
        super(new Item.Properties().group(StupidMod.GROUP).food(FOOD));

        
        this.setRegistryName(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity player) {
        if (!world.isRemote) {
            Collection<EffectInstance> effects = player.getActivePotionEffects();

            int durationJump = 200;
            int durationStrength = 200;
            int durationHaste = 200;
            int durationSpeed = 200;

            for (EffectInstance effect : effects) {
                if (effect.getAmplifier() >= 4) {
                    if (effect.getPotion() == Effects.JUMP_BOOST)
                        durationJump += effect.getDuration();
                    else if (effect.getPotion() == Effects.STRENGTH)
                        durationStrength += effect.getDuration();
                    else if (effect.getPotion() == Effects.HASTE)
                        durationHaste += effect.getDuration();
                    else if (effect.getPotion() == Effects.SPEED)
                        durationSpeed += effect.getDuration();
                }
            }

            player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, durationJump, 4));
            player.addPotionEffect(new EffectInstance(Effects.STRENGTH, durationStrength, 4));
            player.addPotionEffect(new EffectInstance(Effects.HASTE, durationHaste, 4));
            player.addPotionEffect(new EffectInstance(Effects.SPEED, durationSpeed, 4));
        }

        return super.onItemUseFinish(stack, world, player);
    }

    /*
    @Override
    protected void onFoodEaten(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isRemote) {
            Collection<EffectInstance> effects = player.getActivePotionEffects();
            
            int durationJump = 200;
            int durationStrength = 200;
            int durationHaste = 200;
            int durationSpeed = 200;
            
            for (EffectInstance effect : effects) {
                if (effect.getAmplifier() >= 4) {
                    if (effect.getPotion() == Effects.JUMP_BOOST)
                        durationJump += effect.getDuration();
                    else if (effect.getPotion() == Effects.STRENGTH)
                        durationStrength += effect.getDuration();
                    else if (effect.getPotion() == Effects.HASTE)
                        durationHaste += effect.getDuration();
                    else if (effect.getPotion() == Effects.SPEED)
                        durationSpeed += effect.getDuration();
                }
            }
            
            player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, durationJump, 4));
            player.addPotionEffect(new EffectInstance(Effects.STRENGTH, durationStrength, 4));
            player.addPotionEffect(new EffectInstance(Effects.HASTE, durationHaste, 4));
            player.addPotionEffect(new EffectInstance(Effects.SPEED, durationSpeed, 4));
        }
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 12;
    }
    

    */
}
