package gcmod.recipe;

import gcmod.GCMod;
import gcmod.item.FertiliserItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class FertiliserRecipe extends CustomRecipe
{
    private static final ItemStack defaultStack;

    static
    {
        defaultStack = new ItemStack( GCMod.FERTILISER );
        defaultStack.setDamageValue( FertiliserItem.MAX_DAMAGE - 8 );
    }

    ItemStack outputStack = defaultStack;

    public FertiliserRecipe( CraftingBookCategory category )
    {
        super( category );
    }

    @Override
    public boolean matches( CraftingInput inv, Level world )
    {
        outputStack = defaultStack.copy();

        int prevDamage = 0;
        float pooValue = 0;

        for ( int i = 0; i < inv.size(); ++i )
        {
            ItemStack stack = inv.getItem( i );

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

                prevDamage = stack.getDamageValue();
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

            outputStack.setDamageValue( newDmg );
            return true;
        }

        return false;
    }

    @Override
    public ItemStack assemble( CraftingInput recipeInput, HolderLookup.Provider provider ) { return this.outputStack.copy(); }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer()
    {
        return GCMod.FERTILIZER_RECIPE_SERIALIZER;
    }
}
