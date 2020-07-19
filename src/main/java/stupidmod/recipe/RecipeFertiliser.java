package stupidmod.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.StupidModItems;
import stupidmod.StupidModRecipes;
import stupidmod.item.FertiliserItem;

public class RecipeFertiliser extends ShapelessRecipe {
    private static final ItemStack defaultStack;
    static
    {
        defaultStack = new ItemStack(StupidModItems.FERTILISER);
        defaultStack.setDamage(FertiliserItem.MAX_DAMAGE - 16);
    }

    ItemStack outputStack = defaultStack;

    public RecipeFertiliser(ResourceLocation location)
    {
        super(location, StupidModRecipes.FERTILISER.getRegistryName().toString(), defaultStack.copy(), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.BUCKET), Ingredient.fromItems(StupidModItems.FERMENTED_POO)));
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        outputStack = defaultStack.copy();

        int prevDamage = 0;
        int extra = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack.getItem() == Items.BUCKET)
            {
                if (prevDamage > 0) return false;
                prevDamage = FertiliserItem.MAX_DAMAGE;
            }
            else if (stack.getItem() == StupidModItems.FERTILISER)
            {
                if (prevDamage > 0) return false;
                prevDamage = stack.getDamage();
            }
            else if (stack.getItem() == StupidModItems.POO)
            {
                extra += 1;
            }
            else if (stack.getItem() == StupidModItems.FERMENTED_POO)
            {
                extra += 16;
            }
            else if (!stack.isEmpty())
            {
                return false;
            }
        }

        if (prevDamage > 0 && extra > 0)
        {
            int newDmg = prevDamage - extra;
            if (newDmg <= 0) return false;

            outputStack.setDamage(newDmg);
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return outputStack.copy();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return StupidModRecipes.FERTILISER;
    }
}