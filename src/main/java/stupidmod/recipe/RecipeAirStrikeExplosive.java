package stupidmod.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.BlockRegister;
import stupidmod.item.ItemBlockExplosive;

public class RecipeAirStrikeExplosive extends ShapedRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    private static final Ingredient ING_FEATHER = Ingredient.fromItems(Items.FEATHER);
    
    public RecipeAirStrikeExplosive(ResourceLocation location) {
        
        super(location, "misc", 3, 3,
                NonNullList.from(Ingredient.EMPTY,
                        ING_FEATHER, ING_FEATHER, ING_FEATHER,
                        ING_FEATHER, Ingredient.fromItems(BlockRegister.blockBlastTNT.asItem()), ING_FEATHER,
                        ING_FEATHER, ING_FEATHER, ING_FEATHER),
                new ItemStack(BlockRegister.blockAirstrikeTNT));
    }
    
    @Override
    public boolean matches(IInventory inv, World world) {
        this.outputStack = ItemStack.EMPTY;
    
        if (!super.matches(inv, world))
            return false;
    
        for (int i = 0;i < inv.getSizeInventory();++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == new ItemStack(BlockRegister.blockBlastTNT).getItem()) {
                NBTTagCompound nbt = stack.getTag();
                this.outputStack = ItemBlockExplosive.makeStackAirstrike(nbt.getShort("Fuse"), nbt.getShort("Strength"),(short)1,(short)3,(short)1);
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
    public ItemStack getRecipeOutput()
    {
        return this.outputStack;
    }
    
    @Override
    public boolean isDynamic() { return true; }
}
