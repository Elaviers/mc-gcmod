package stupidmod.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.item.ItemBlockExplosive;

public class RecipeConstructiveExplosive implements IRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    ResourceLocation id;
    
    public RecipeConstructiveExplosive(ResourceLocation location)
    {
        this.id = location;
    }
    
    @Override
    public boolean matches(IInventory inv, World world) {
        this.outputStack = ItemStack.EMPTY;
        int sameBlockCount = 0,TntCount = 0;
        
        IBlockState state = null;
        short fuse = 0, strength = 0;
    
        for (int i = 0;i < inv.getSizeInventory();++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                Item item = stack.getItem();
                
                if (item instanceof ItemBlock) {
                    ItemBlock ib = (ItemBlock)item;
                    
                    if (state == null && !(stack.getItem() instanceof ItemBlockExplosive)) {
                        state = ib.getBlock().getDefaultState();
        
                        sameBlockCount++;
                    } else {
                        if (ib == state)
                            sameBlockCount++;
                        else if (stack.getItem() instanceof ItemBlockExplosive && stack.hasTag()) {
                            TntCount++;
                            NBTTagCompound nbt = stack.getTag();
                            fuse += nbt.getShort("Fuse");
                            strength += nbt.getShort("Strength");
                        }
                    }
                }
            }
        }
        
        if (state != null && TntCount == 1 && sameBlockCount >= 8) {
            this.outputStack = ItemBlockExplosive.makeStackConstructive(fuse, strength, state);
            return true;
        }
    
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(IInventory inv) {
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
    
    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new RecipeSerializers.SimpleSerializer<>("constructive_explosive", RecipeConstructiveExplosive::new);
    }
}
