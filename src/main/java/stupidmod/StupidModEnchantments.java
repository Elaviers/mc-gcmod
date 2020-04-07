package stupidmod;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.enchantment.ConstipationEnchantment;
import stupidmod.enchantment.DiarrhoeaEnchantment;
import stupidmod.enchantment.LaxativesEnchantment;
import stupidmod.enchantment.PooerEnchantment;

@ObjectHolder(StupidMod.id)
public class StupidModEnchantments {

    private static final String nameConstipation = "constipation", nameDiarrhoea = "diarrhoea", nameLaxatives = "laxatives", namePooer = "pooer";

    public static EnchantmentType TYPE_POO_CANNON;

    @ObjectHolder(nameConstipation)
    public static ConstipationEnchantment CONSTIPATION;

    @ObjectHolder(nameDiarrhoea)
    public static DiarrhoeaEnchantment DIARRHOEA;

    @ObjectHolder(nameLaxatives)
    public static LaxativesEnchantment LAXATIVES;

    @ObjectHolder(namePooer)
    public static PooerEnchantment POOER;

    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration {

        @SubscribeEvent
        static void registerEnchantments(RegistryEvent.Register<Enchantment> register) {
            TYPE_POO_CANNON = EnchantmentType.create("poo_cannon", item -> item == StupidModItems.POO_CANNON);
            StupidMod.GROUP.setRelevantEnchantmentTypes(TYPE_POO_CANNON);

            register.getRegistry().registerAll(
                    new ConstipationEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND).setRegistryName(nameConstipation),
                    new DiarrhoeaEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND).setRegistryName(nameDiarrhoea),
                    new LaxativesEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND).setRegistryName(nameLaxatives),
                    new PooerEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND).setRegistryName(namePooer)
            );
        }
    }
}
