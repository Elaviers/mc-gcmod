package stupidmod.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.StupidModBlocks;
import stupidmod.StupidModRecipes;
import stupidmod.item.ExplosiveBlockItem;

public class RecipeDigExplosive extends ShapelessRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    public RecipeDigExplosive(ResourceLocation location) {
        super(location, StupidModRecipes.DIG_EXPLOSIVE.getRegistryName().toString(), new ItemStack(StupidModBlocks.DIG_TNT_ITEM), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(StupidModBlocks.BLAST_TNT_ITEM), Ingredient.fromItems(Items.DIAMOND_PICKAXE)));
    }
    
    @Override
    public boolean matches(CraftingInventory inv, World world) {
        this.outputStack = ItemStack.EMPTY;
    
        if (!super.matches(inv, world))
            return false;
    
    
        for (int i = 0;i < inv.getSizeInventory();++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == StupidModBlocks.BLAST_TNT_ITEM) {
                CompoundNBT nbt = stack.getTag();
                this.outputStack = ExplosiveBlockItem.makeStackDig(nbt.getShort("Fuse"), nbt.getShort("Strength"));
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
        return StupidModRecipes.DIG_EXPLOSIVE;
    }
}
