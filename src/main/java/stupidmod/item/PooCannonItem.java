package stupidmod.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import stupidmod.StupidMod;
import stupidmod.StupidModEnchantments;
import stupidmod.StupidModItems;
import stupidmod.StupidModSounds;
import stupidmod.entity.PooExplosiveEntity;

public class PooCannonItem extends Item {
    
    public PooCannonItem(String name) {
        super(new Properties().group(StupidMod.GROUP).maxDamage(32));

        this.setRegistryName(name);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 25;
    }

    private ItemStack findAmmo(PlayerEntity player) {
        if (player.getHeldItem(Hand.OFF_HAND).getItem() instanceof PooBrickItem) {
            return player.getHeldItem(Hand.OFF_HAND);
        }
        else if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof PooBrickItem) {
            return player.getHeldItem(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                
                if (itemstack.getItem() instanceof PooBrickItem) {
                    return itemstack;
                }
            }
            
            return ItemStack.EMPTY;
        }
    }

    int amountForConstipation(int constipation)
    {
        switch (constipation)
        {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 7;
        }

        return 0;
    }

    void shootPoo(World world, PlayerEntity player, ItemStack stack)
    {
        ItemStack ammoStack = null;

        int constipationLvl = EnchantmentHelper.getEnchantmentLevel(StupidModEnchantments.CONSTIPATION, stack);

        int maxCount = 1;
        float spread = 0;
        float zDiff = 0;
        switch (constipationLvl)
        {
            case 0:
                break;
            case 1:
                maxCount = 2;
                spread = 2.f;
                zDiff = 0.2f;
                break;
            case 2:
                maxCount = 4;
                spread = 8.f;
                zDiff = 0.33f;
                break;
            case 3:
                maxCount = 8;
                spread = 15.f;
                zDiff = 0.5f;
                break;
            default:
                maxCount = (int)Math.pow(2, constipationLvl);
                spread = 20.f;
                zDiff = 0.8f;
        }

        if (!player.isCreative()) {
            ammoStack = this.findAmmo(player);
            if (ammoStack.isEmpty()) return;
        }

        if (!world.isRemote) {
            int count;
            if (player.isCreative() || EnchantmentHelper.getEnchantmentLevel(StupidModEnchantments.LAXATIVES, stack) > 0)
                count = maxCount;
            else
            {
                stack.damageItem(1, player, livingEntity -> livingEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));

                for (count = 0; count < maxCount; ++count) {
                    ammoStack = this.findAmmo(player);
                    if (!ammoStack.isEmpty())
                        ammoStack.setCount(ammoStack.getCount() - 1);
                    else break;
                }
            }

            if (count == 0) return;

            float speed = 2.f;
            if (EnchantmentHelper.getEnchantmentLevel(StupidModEnchantments.DIARRHOEA, stack) <= 0)
            {
                speed = Math.min(0.4f + player.getItemInUseMaxCount() / 15.f, 2.f);
            }

            for (int i = 0; i < count; ++i) {
                PooExplosiveEntity poo = new PooExplosiveEntity(world, 0, 0, 0, 4 + EnchantmentHelper.getEnchantmentLevel(StupidModEnchantments.POOER, stack));
                poo.setLocationAndAngles(player.getPosX(), player.getPosY() + player.getEyeHeight(), player.getPosZ(), player.rotationYaw, player.rotationPitch);

                float yaw = (poo.rotationYaw + spread * (random.nextFloat() * 2.f - 1f)) / 180.0F * (float) Math.PI;
                float pitch = (poo.rotationPitch + spread * (random.nextFloat() * 2.f - 1f)) / 180.0F * (float) Math.PI;
                float finalSpeed = Math.max(0.5f, speed + zDiff * (random.nextFloat() * 2.f - 1f));

                double motx = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
                double motz = MathHelper.cos(yaw) * MathHelper.cos(pitch);
                double moty = -MathHelper.sin(pitch);
                poo.setMotion(motx * finalSpeed, moty * finalSpeed, motz * finalSpeed);
                world.addEntity(poo);
            }
        }
        else
        {
            player.playSound(StupidModSounds.POO_CANNON, 1 + random.nextFloat() * 0.2f, 1 + (random.nextFloat() * 0.5f - 0.25f));
        }
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity player, int timeLeft) {
        shootPoo(world, (PlayerEntity)player, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (EnchantmentHelper.getEnchantmentLevel(StupidModEnchantments.DIARRHOEA, stack) > 0)
        {
            shootPoo(world, (PlayerEntity)entityLiving, stack);
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ActionResult<ItemStack> event = net.minecraftforge.event.ForgeEventFactory.onArrowNock(player.getHeldItem(hand), world, player, hand, true);
        if (event != null) return event;
        
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(ActionResultType.PASS, player.getHeldItem(hand));
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(StupidModEnchantments.DIARRHOEA, stack) > 0 ? 1 : 10000000;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == StupidModItems.POO_CANNON && repair.getItem() == Items.OBSIDIAN;
    }
}
