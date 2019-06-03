package stupidmod.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.ItemRegister;
import stupidmod.RecipeRegister;
import stupidmod.block.BlockExplosive;
import stupidmod.item.ItemBlockExplosive;

public class RecipeExplosive extends IRecipeHidden {
    ItemStack outputStack = ItemStack.EMPTY;

    public RecipeExplosive(ResourceLocation location) { super (location); }
    
    @Override
    public boolean matches(IInventory inv, World world) {
        outputStack = ItemStack.EMPTY;
    
        short strength, fuse, spread, pieces, height;
        short strengthmod,fusemod,spreadmod,heightmod;
        strength = fuse = spread = pieces = height = 0;
        strengthmod = fusemod = spreadmod = heightmod = 0;
    
        IBlockState blockState = null;
        IBlockState blockStateMod = null;
        
        short strength_AS = 0;
        
        BlockExplosive.Type finalType = null;
        int TNTCount = 0;
    
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
        
            if (!stack.isEmpty()) {
                Item currentItem = stack.getItem();
                
                if (currentItem instanceof ItemBlockExplosive && stack.hasTag()) {
                    NBTTagCompound tag = stack.getTag();
    
                    fuse += tag.getShort("Fuse");
    
                    BlockExplosive.Type type = ((BlockExplosive)((ItemBlock)currentItem).getBlock()).type;
                    
                    if (type == BlockExplosive.Type.AIRSTRIKE)
                        strength_AS += tag.getShort("Strength");
                    else
                        strength += tag.getShort("Strength");
                    
                    switch (type) {
                        case CONSTRUCTIVE:
                            if (blockState == null)
                                blockState = NBTUtil.readBlockState(tag.getCompound("Block"));
                            else if (NBTUtil.readBlockState(tag.getCompound("Block")) != blockState)
                                return false;
                            break;
                            
                        case AIRSTRIKE:
                            spread += tag.getShort("Spread");
                            pieces += tag.getShort("Pieces");
                            height += tag.getShort("Height");
                            break;
                        
                    }
                    
                    finalType = type;
                
                    TNTCount++;
                }
                else if (currentItem == Items.GUNPOWDER)
                    strengthmod++;
                else if (currentItem == ItemRegister.itemBlackPowder)
                    strengthmod += 4;
                else if (currentItem == Items.STRING)
                    fusemod += 20;
                else if (currentItem == Items.ARROW)
                    spreadmod++;
                else if (currentItem == Items.COAL)
                    heightmod += 10;
                else {
                    Block block = Block.getBlockFromItem(currentItem);
                    blockStateMod = block.getDefaultState();
                }
            }
        }
        if (TNTCount > 0 && (strengthmod > 0 || fusemod > 0 || (finalType == BlockExplosive.Type.CONSTRUCTIVE && blockStateMod != null) || (finalType == BlockExplosive.Type.AIRSTRIKE && (fusemod > 0 || spreadmod > 0 || heightmod > 0)) || TNTCount > 1))
        {
            if (finalType != BlockExplosive.Type.AIRSTRIKE)
                strength += strengthmod;
            else
                strength_AS += strengthmod;
            
            fuse += fusemod;
            spread += spreadmod;
            height += heightmod;
            
            switch (finalType) {
                case BLAST:
                    this.outputStack = ItemBlockExplosive.makeStackBlast(fuse, (short)(strength + strength_AS));
                    return true;
                
                case CONSTRUCTIVE:
                    if (blockStateMod != null)
                        blockState = blockStateMod;
                    
                    this.outputStack = ItemBlockExplosive.makeStackConstructive(fuse, (short)(strength + strength_AS), blockState);
                    return true;
                    
                case DIG:
                    this.outputStack = ItemBlockExplosive.makeStackDig(fuse, (short)(strength + strength_AS));
                    return true;
                    
                case AIRSTRIKE:
                    pieces += strength;
                    
                    this.outputStack = ItemBlockExplosive.makeStackAirstrike(fuse, strength_AS, spread, pieces, height);
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
    public boolean canFit(int width, int height) { return width * height >= 2; }
    
    @Override
    public IRecipeSerializer<?> getSerializer() { return RecipeRegister.recipeExplosive; }
}
