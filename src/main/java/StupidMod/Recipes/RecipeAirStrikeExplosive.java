package StupidMod.Recipes;

import StupidMod.Items.ItemBlockExplosive;
import StupidMod.StupidMod;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeAirStrikeExplosive extends ShapedRecipes {
    ItemStack outputStack = ItemStack.EMPTY;
    
    
    public RecipeAirStrikeExplosive() {
        super("", 3, 3, NonNullList.from(  Ingredient.EMPTY, Ingredient.fromItem(Items.FEATHER), Ingredient.fromItem(Items.FEATHER), Ingredient.fromItem(Items.FEATHER),
                                                                Ingredient.fromItem(Items.FEATHER), Ingredient.fromItem(StupidMod.instance.blocks.itemBlockExplosive), Ingredient.fromItem(Items.FEATHER),
                                                                Ingredient.fromItem(Items.FEATHER), Ingredient.fromItem(Items.FEATHER), Ingredient.fromItem(Items.FEATHER)),
                new ItemStack(StupidMod.instance.blocks.blockExplosive, 1, 10));
    }
    
    @Override
    public boolean matches(InventoryCrafting grid,World world) {
        this.outputStack = ItemStack.EMPTY;
        
        if (!super.matches(grid, world)) {
            return false;
        }
        
        else {
            for (int i = 0;i < grid.getSizeInventory();++i) {
                ItemStack stack = grid.getStackInSlot(i);
                if (stack.getItem() == new ItemStack(StupidMod.instance.blocks.blockExplosive,1,0).getItem()) {
                    NBTTagCompound nbt = stack.getTagCompound();
                    this.outputStack = ItemBlockExplosive.MakeAirStrikeStack(nbt.getShort("Fuse"), nbt.getShort("Strength"),(short)1,(short)3,(short)1);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting grid) {
        return this.outputStack.copy();
    }
    
    @Override
    public ItemStack getRecipeOutput()
    {
        return this.outputStack;
    }
    
    @Override
    public boolean isDynamic() { return true; }
}
