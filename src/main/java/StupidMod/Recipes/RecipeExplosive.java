package StupidMod.Recipes;

import StupidMod.Blocks.BlockExplosive;
import StupidMod.Items.ItemBlockExplosive;
import StupidMod.StupidMod;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeExplosive extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        outputStack = ItemStack.EMPTY;
    
        short strength, fuse, blockMeta, spread, pieces, height;
        short strengthmod,fusemod,spreadmod,heightmod;
        strength = fuse = spread = pieces = height = blockMeta = 0;
        strengthmod = fusemod = spreadmod = heightmod = 0;
        
        String blockID = "";
        String blockIDMod = "";
        short blockMetaMod = 0;
        
        short strength_AS = 0;
        
        int mtype = -1,type = -1;
        int TNTCount = 0;
    
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
        
            if (!stack.isEmpty()) {
                Item currentItem = stack.getItem();
                
                if (currentItem instanceof ItemBlockExplosive && stack.hasTagCompound()) {
                    NBTTagCompound tag = stack.getTagCompound();
                    
                    type = stack.getItemDamage() / BlockExplosive.tierCount;
    
                    fuse += tag.getInteger("Fuse");
                    
                    if (type == 3)
                        strength_AS += tag.getInteger("Strength");
                    else
                        strength += tag.getShort("Strength");
                    
                    switch (type) {
                        case 1: //Construct
                            if (blockID.isEmpty()) {
                                blockID = tag.getString("Block");
                                blockMeta = tag.getShort("BlockMeta");
                            }
                            else if (tag.getString("Block") == blockID || tag.getShort("BlockMeta") == blockMeta)
                                return false;
                            break;
                            
                        case 3: //Airstrike
                            spread += tag.getShort("Spread");
                            pieces += tag.getShort("Pieces");
                            height += tag.getShort("Height");
                            break;
                        
                    }
                    
                    if (type > mtype)
                        mtype = type;
                
                    TNTCount++;
                }
            
                else if (currentItem == Items.GUNPOWDER)
                    strengthmod++;
                else if (currentItem == StupidMod.instance.items.itemPowder)
                    strengthmod += 4;
                else if (currentItem == Items.STRING)
                    fusemod += 20;
                else if (currentItem == Items.ARROW)
                    spreadmod++;
                else if (currentItem == Items.COAL)
                    heightmod += 10;
                else if (Block.getBlockFromItem(currentItem) != null) {
                    blockIDMod = currentItem.getRegistryName().toString();
                    blockMetaMod = (short)stack.getMetadata();
                }
            
                else return false;
            }
        }
        if (TNTCount > 0 && (strengthmod > 0 || fusemod > 0 || (mtype == 1 && !blockIDMod.isEmpty()) || (mtype == 3 && (fusemod > 0 || spreadmod > 0 || heightmod > 0)) || TNTCount > 1))
        {
            if (type != 3)
                strength += strengthmod;
            else
                strength_AS += strengthmod;
            
            fuse += fusemod;
            spread += spreadmod;
            height += heightmod;
            
            switch (mtype) {
                case 0:
                    this.outputStack = ItemBlockExplosive.MakeStack(fuse, strength);
                    return true;
                
                case 1:
                    if (!blockIDMod.isEmpty()) {
                        blockID = blockIDMod;
                        blockMeta = blockMetaMod;
                    }
                    
                    this.outputStack = ItemBlockExplosive.MakeStackConstructive(fuse, strength, blockID, blockMeta);
                    return true;
                    
                case 3:
                    pieces += strength;
                    
                    this.outputStack = ItemBlockExplosive.makeStackAirstrike(fuse, strength_AS, spread, pieces, height);
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
    public boolean canFit(int width, int height) {
        return width * height > 1;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.outputStack;
    }
    
    @Override
    public boolean isDynamic() { return true; }
}
