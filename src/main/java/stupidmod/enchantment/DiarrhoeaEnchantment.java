package stupidmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import stupidmod.StupidModEnchantments;

public class DiarrhoeaEnchantment extends Enchantment {
    public DiarrhoeaEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, StupidModEnchantments.TYPE_POO_CANNON, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }
}
