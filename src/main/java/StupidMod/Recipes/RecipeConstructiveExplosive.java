package StupidMod.Recipes;

import StupidMod.Blocks.BlockExplosive;
import StupidMod.Items.ItemBlockExplosive;
import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeConstructiveExplosive extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        this.outputStack = ItemStack.EMPTY;
        int sameBlockCount = 0,TntCount = 0;
        
        String blockID = "";
        short blockMeta = (short)42069;
        short fuse = 0, strength = 0;
    
        for (int i = 0;i < inv.getSizeInventory();++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                Item item = stack.getItem();
                
                if (blockID.isEmpty() && !(stack.getItem() instanceof ItemBlockExplosive)) {
                    blockID = item.getRegistryName().toString();
                    blockMeta = (short)stack.getItemDamage();
                    
                    sameBlockCount++;
                }
                else {
                    if (item.getRegistryName().toString().equals(blockID) && (short)stack.getItemDamage() == blockMeta)
                        sameBlockCount++;
                    else if (stack.getItem() instanceof  ItemBlockExplosive && stack.hasTagCompound()) {
                        TntCount++;
                        NBTTagCompound nbt = stack.getTagCompound();
                        fuse += nbt.getShort("Fuse");
                        strength += nbt.getShort("Strength");
                    }
                }
            }
        }
        
        if (Block.getBlockFromName(blockID) != Blocks.AIR && TntCount == 1 && sameBlockCount >= 8) {
            this.outputStack = ItemBlockExplosive.MakeStackConstructive(fuse, strength, blockID, blockMeta);
            return true;
        }
    
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.outputStack.copy();
    }
    
    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 9;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.outputStack;
    }
    
    @Override
    public boolean isDynamic() { return true; }
}
