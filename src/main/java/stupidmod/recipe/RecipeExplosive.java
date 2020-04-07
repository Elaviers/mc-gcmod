package stupidmod.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stupidmod.StupidModItems;
import stupidmod.StupidModRecipes;
import stupidmod.block.ExplosiveBlock;
import stupidmod.item.ExplosiveBlockItem;

public class RecipeExplosive extends SpecialRecipe {
    ItemStack outputStack = ItemStack.EMPTY;

    public RecipeExplosive(ResourceLocation location) { super (location); }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        outputStack = ItemStack.EMPTY;

        short strength, fuse, spread, pieces, height;
        short powder,string,arrow,coal;
        strength = fuse = spread = pieces = height = 0;
        powder = string = arrow = coal = 0;

        BlockState blockState = null;
        BlockState blockStateMod = null;

        short strength_AS = 0;

        ExplosiveBlock.Type finalType = null;
        int TNTCount = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {
                Item currentItem = stack.getItem();

                if (currentItem instanceof ExplosiveBlockItem && stack.hasTag()) {
                    CompoundNBT tag = stack.getTag();

                    fuse += tag.getShort("Fuse");

                    ExplosiveBlock.Type type = ((ExplosiveBlock)((BlockItem)currentItem).getBlock()).type;

                    if (type == ExplosiveBlock.Type.AIRSTRIKE)
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
                else if (currentItem == StupidModItems.BLACK_POWDER)
                    powder++;
                else if (currentItem == Items.STRING)
                    string++;
                else if (currentItem == Items.ARROW)
                    arrow++;
                else if (currentItem == Items.COAL)
                    coal++;
                else {
                    Block block = Block.getBlockFromItem(currentItem);
                    blockStateMod = block.getDefaultState();
                }
            }
        }
        if (TNTCount > 0 && (powder > 0 || string > 0 || (finalType == ExplosiveBlock.Type.CONSTRUCTIVE && blockStateMod != null) || (finalType == ExplosiveBlock.Type.AIRSTRIKE && (string > 0 || arrow > 0 || coal > 0)) || TNTCount > 1))
        {
            if (finalType != ExplosiveBlock.Type.AIRSTRIKE) {
                strength += strength_AS + powder;
            }
            else
            {
                pieces += strength;
                strength = (short)(strength_AS + powder);
            }

            fuse += string * 20;
            spread += arrow;
            height += coal * 5;

            switch (finalType) {
                case BLAST:
                    this.outputStack = ExplosiveBlockItem.makeStackBlast(fuse, strength);
                    return true;

                case CONSTRUCTIVE:
                    if (blockStateMod != null)
                        blockState = blockStateMod;

                    this.outputStack = ExplosiveBlockItem.makeStackConstructive(fuse, strength, blockState);
                    return true;

                case DIG:
                    this.outputStack = ExplosiveBlockItem.makeStackDig(fuse, strength);
                    return true;

                case AIRSTRIKE:
                    this.outputStack = ExplosiveBlockItem.makeStackAirstrike(fuse, strength, spread, pieces, height);
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
    public boolean canFit(int width, int height) { return width * height > 1; }

    @Override
    public IRecipeSerializer<?> getSerializer() { return StupidModRecipes.EXPLOSIVE; }
}