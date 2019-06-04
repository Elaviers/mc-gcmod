package stupidmod.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.BlockRegister;
import stupidmod.RecipeRegister;
import stupidmod.item.ItemBlockExplosive;

public class RecipeDigExplosive extends ShapelessRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    public RecipeDigExplosive(ResourceLocation location) {
        super(location, "misc", new ItemStack(BlockRegister.itemBlockDigTNT), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(BlockRegister.itemBlockBlastTNT), Ingredient.fromItems(Items.DIAMOND_PICKAXE)));
    }
    
    @Override
    public boolean matches(IInventory inv, World world) {
        this.outputStack = ItemStack.EMPTY;
    
        if (!super.matches(inv, world))
            return false;
    
    
        for (int i = 0;i < inv.getSizeInventory();++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == BlockRegister.itemBlockBlastTNT) {
                NBTTagCompound nbt = stack.getTag();
                this.outputStack = ItemBlockExplosive.makeStackDig(nbt.getShort("Fuse"), nbt.getShort("Strength"));
                return true;
            }
        }
    
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(IInventory inv) {
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

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegister.recipeDigExplosive;
    }
}
