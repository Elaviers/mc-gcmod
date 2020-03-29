package stupidmod.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.StupidModRecipes;

public class RecipeConstructiveExplosive extends SpecialRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    public RecipeConstructiveExplosive(ResourceLocation location)
    {
        super(location);
    }



    @Override
    public boolean matches(CraftingInventory inv, World world) {


        //This is WAY too OP because I can't make it take more than 1 item out of the grid when crafting
        return false;


        /*
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

                    if (ib.getBlock() instanceof BlockExplosive) {
                        TntCount++;
                        NBTTagCompound nbt = stack.getCompound();
                        fuse += nbt.getShort("Fuse");
                        strength += nbt.getShort("Strength");
                    }
                    else {
                        if (state == null) {
                            state = ib.getBlock().getDefaultState();

                            sameBlockCount += stack.getCount();
                        }
                        else if (ib.getBlock() == state.getBlock()) {
                            sameBlockCount += stack.getCount();
                        }
                    }
                }
            }
        }

        int explosionVolume = (int)(4f / 3f * (float)Math.PI * strength * strength * strength + 1f);

        if (state != null && TntCount >= 1 && sameBlockCount >= explosionVolume) {
            this.outputStack = ItemBlockExplosive.makeStackConstructive(fuse, strength, state);
            return true;
        }
    
        return false;

         */
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return StupidModRecipes.CONSTRUCTIVE_EXPLOSIVE;
    }


}
