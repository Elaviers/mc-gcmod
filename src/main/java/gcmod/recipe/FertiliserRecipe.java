package gcmod.recipe;

import gcmod.GCMod;
import gcmod.item.FertiliserItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class FertiliserRecipe extends SpecialCraftingRecipe
{
    private static final ItemStack defaultStack;

    static
    {
        defaultStack = new ItemStack( GCMod.FERTILISER );
        defaultStack.setDamage( FertiliserItem.MAX_DAMAGE - 8 );
    }

    ItemStack outputStack = defaultStack;

    public FertiliserRecipe( CraftingRecipeCategory category )
    {
        super( category );
    }

    @Override
    public boolean matches( CraftingRecipeInput inv, World world )
    {
        outputStack = defaultStack.copy();

        int prevDamage = 0;
        float pooValue = 0;

        for ( int i = 0; i < inv.size(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );

            if ( stack.getItem() == Items.BUCKET )
            {
                if ( prevDamage > 0 )
                    return false;

                prevDamage = FertiliserItem.MAX_DAMAGE;
            }
            else if ( stack.getItem() == GCMod.FERTILISER )
            {
                if ( prevDamage > 0 )
                    return false;

                prevDamage = stack.getDamage();
                if ( prevDamage == 0 )
                    return false;
            }
            else if ( stack.getItem() == GCMod.POO )
            {
                pooValue += .25f;
            }
            else if ( stack.getItem() == GCMod.FERMENTED_POO )
            {
                pooValue += 1.25f;
            }
            else if ( !stack.isEmpty() )
            {
                return false;
            }
        }

        int extra = (int)pooValue;
        if ( prevDamage > 0 && extra > 0 )
        {
            final int newDmg = prevDamage - extra;
            if ( newDmg <= 0 )
                return false;

            outputStack.setDamage( newDmg );
            return true;
        }

        return false;
    }

    @Override
    public ItemStack craft( CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup )
    {
        return this.outputStack.copy();
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer()
    {
        return GCMod.FERTILIZER_RECIPE_SERIALIZER;
    }
}
