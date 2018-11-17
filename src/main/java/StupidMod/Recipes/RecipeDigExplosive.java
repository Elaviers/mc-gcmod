package StupidMod.Recipes;

import StupidMod.Blocks.BlockExplosive;
import StupidMod.Items.ItemBlockExplosive;
import StupidMod.StupidMod;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeDigExplosive extends ShapelessRecipes {
    ItemStack outputStack = ItemStack.EMPTY;
    
    public RecipeDigExplosive() {
        super("", new ItemStack(StupidMod.instance.blocks.itemBlockExplosive, 1, 7), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(StupidMod.instance.blocks.itemBlockExplosive), Ingredient.fromItem(Items.DIAMOND_PICKAXE)));
    }
    
    @Override
    public boolean matches(InventoryCrafting grid, World world) {
        this.outputStack = ItemStack.EMPTY;
    
        if (!super.matches(grid, world))
            return false;
        
            
        for (int i = 0;i < grid.getSizeInventory();++i) {
            ItemStack stack = grid.getStackInSlot(i);
            if (stack.getItem() == new ItemStack(StupidMod.instance.blocks.blockExplosive,1,0).getItem()) {
                NBTTagCompound nbt = stack.getTagCompound();
                this.outputStack = ItemBlockExplosive.MakeStack(nbt.getShort("Fuse"), nbt.getShort("Strength"));
                this.outputStack.setItemDamage(this.outputStack.getItemDamage() + BlockExplosive.tierCount * 2);
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.outputStack.copy();
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.outputStack;
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
}
