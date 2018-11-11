package StupidMod.Recipes;

import StupidMod.Blocks.BlockExplosive;
import StupidMod.Items.ItemBlockExplosive;
import StupidMod.StupidMod;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeExplosive extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    ItemStack outputStack = ItemStack.EMPTY;
    
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        outputStack = ItemStack.EMPTY;
    
        short strength_nonAS, fuse, blockMeta, spread, pieces, height;
        short strengthmod,fusemod,spreadmod,heightmod;
        strength_nonAS = fuse = spread = pieces = height = blockMeta = 0;
        strengthmod = fusemod = spreadmod = heightmod = 0;
        
        String blockID = "";
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
                        strength_nonAS += tag.getShort("Strength");
                    
                    switch (type) {
                        case 1: //Construct
                            if (blockID.isEmpty()) {
                                blockID = tag.getString("Block");
                                blockMeta = tag.getShort("BlockMeta");
                            }
                            else if (tag.getString("Block") == blockID || tag.getShort("BlockMeta") == blockMeta)
                                return false;
                            break;
                            
                        case 2: return false; //Unused (Impact skin)
                            
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
                    fusemod++;
                else if (currentItem == Items.ARROW)
                    spreadmod++;
                else if (currentItem == Items.COAL)
                    heightmod++;
            
                else return false;
            }
        }
        if (TNTCount > 0 && (strengthmod > 0 || fusemod > 0 || (mtype == 3 && (fusemod > 0 || spreadmod > 0 || heightmod > 0)) ||TNTCount > 1))
        {
            if (mtype == 3) {
                pieces += strength_nonAS;
                strength_nonAS = strength_AS;
            }
            
            strength_nonAS += strengthmod;
            fuse += fusemod;
            spread += spreadmod;
            height += heightmod;
            
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setShort("Fuse", fuse);
            nbt.setShort("Strength", strength_nonAS);
            
            if (type == 1) {
                nbt.setString("Block", blockID);
                nbt.setShort("BlockMeta", blockMeta);
            } else {
                nbt.setShort("Spread", spread);
                nbt.setShort("Pieces", pieces);
                nbt.setShort("Height", height);
            }
            
            outputStack = new ItemStack(StupidMod.instance.blocks.blockExplosive, 1, mtype == 3 ? ItemBlockExplosive.metaFromStatsAirstrike(strength_nonAS, pieces) : ItemBlockExplosive.metaFromStats(mtype, strength_nonAS));
            outputStack.setTagCompound(nbt);
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
        return width * height > 1;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.outputStack;
    }
    
    @Override
    public boolean isDynamic() { return true; }
}
