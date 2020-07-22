package stupidmod.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.StupidModBlocks;
import stupidmod.StupidModRecipes;
import stupidmod.item.ExplosiveBlockItem;

public class RecipeAirStrikeExplosive extends ShapedRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    private static final Ingredient ING_FEATHER = Ingredient.fromItems(Items.FEATHER);
    
    public RecipeAirStrikeExplosive(ResourceLocation location) {
        
        super(location, "misc", 3, 3,
                NonNullList.from(Ingredient.EMPTY,
                        ING_FEATHER, ING_FEATHER, ING_FEATHER,
                        ING_FEATHER, Ingredient.fromItems(StupidModBlocks.BLAST_TNT.asItem()), ING_FEATHER,
                        ING_FEATHER, ING_FEATHER, ING_FEATHER),
                new ItemStack(StupidModBlocks.AIR_STRIKE_TNT));
    }
    
    @Override
    public boolean matches(CraftingInventory inv, World world) {
        this.outputStack = ItemStack.EMPTY;
    
        if (!super.matches(inv, world))
            return false;
    
        for (int i = 0;i < inv.getSizeInventory();++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == new ItemStack(StupidModBlocks.BLAST_TNT).getItem()) {
                CompoundNBT nbt = stack.getTag();
                this.outputStack = ExplosiveBlockItem.makeStackAirstrike(nbt.getShort("Fuse"), nbt.getShort("Strength"), (short)3,(short)5,(short)20);
                return true;
            }
        }
    
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return this.outputStack.copy();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return StupidModRecipes.AIR_STRIKE_EXPLOSIVE;
    }
}
