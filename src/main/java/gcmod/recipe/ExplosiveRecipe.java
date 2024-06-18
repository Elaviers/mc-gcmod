package gcmod.recipe;

import gcmod.GCMod;
import gcmod.block.ExplosiveBlock;
import gcmod.item.ExplosiveBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class ExplosiveRecipe extends SpecialCraftingRecipe
{
    ItemStack outputStack = ItemStack.EMPTY;

    public ExplosiveRecipe( CraftingRecipeCategory category )
    {
        super( category );
    }

    @Override
    public boolean matches( CraftingRecipeInput inv, World world )
    {
        outputStack = ItemStack.EMPTY;

        boolean validForDigConversion = false;
        boolean tntInCenter = false;
        short strength, fuse, spread, pieces, height;
        short powder, string, arrow, coal, feather;
        strength = fuse = spread = pieces = height = feather = 0;
        powder = string = arrow = coal = 0;

        BlockState constructiveBlockState = null;
        BlockState blockStateMod = null;

        short strength_AS = 0;

        ExplosiveBlock.Type finalType = null;
        int TNTCount = 0;

        for ( int i = 0; i < inv.getSize(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );

            if ( !stack.isEmpty() )
            {
                final Item currentItem = stack.getItem();

                if ( currentItem instanceof ExplosiveBlockItem && stack.get( GCMod.DATA_EXPLOSIVE_INFO ) != null )
                {
                    NbtCompound nbt = stack.get( GCMod.DATA_EXPLOSIVE_INFO ).copyNbt();

                    fuse += nbt.getShort( "Fuse" );

                    final ExplosiveBlock.Type type = ((ExplosiveBlock) Block.getBlockFromItem( currentItem )).type;
                    if ( type == ExplosiveBlock.Type.AIRSTRIKE )
                        strength_AS += nbt.getShort( "Strength" );
                    else
                        strength += nbt.getShort( "Strength" );

                    switch ( type )
                    {
                        case CONSTRUCTIVE:
                        {
                            final BlockState bs = NbtHelper.toBlockState( world.createCommandRegistryWrapper( RegistryKeys.BLOCK ), nbt.getCompound( "Block" ) );
                            if ( constructiveBlockState == null )
                                constructiveBlockState = bs;
                            else if ( bs != constructiveBlockState )
                                return false;
                        }
                        break;

                        case AIRSTRIKE:
                            spread += nbt.getShort( "Spread" );
                            pieces += nbt.getShort( "Pieces" );
                            height += nbt.getShort( "Height" );
                            break;

                    }

                    finalType = type;
                    TNTCount++;

                    if ( !tntInCenter )
                    {
                        final int w = inv.getWidth();
                        final int h = inv.getHeight();
                        final int slotX = i % w;
                        final int slotY = i / w;
                        if ( slotX > 0 && slotY > 0 && slotX < w - 1 && slotY < h - 1 )
                            tntInCenter = true;
                    }
                }
                else if ( currentItem == GCMod.BLACK_POWDER )
                    powder++;
                else if ( currentItem == Items.STRING )
                    string++;
                else if ( currentItem == Items.ARROW )
                    arrow++;
                else if ( currentItem == Items.COAL )
                    coal++;
                else if ( currentItem == Items.FEATHER )
                    feather++;
                else if ( currentItem == Items.DIAMOND_PICKAXE || currentItem == Items.NETHERITE_PICKAXE )
                    validForDigConversion = true;
                else if ( currentItem instanceof BlockItem )
                {
                    final BlockState bs = Block.getBlockFromItem( currentItem ).getDefaultState();
                    if ( blockStateMod == null )
                        blockStateMod = bs;
                    else if ( bs != blockStateMod )
                        return false;
                }
                else
                    return false;
            }
        }

        if ( TNTCount == 0 )
            return false;

        final boolean validForAirstrikeConversion = finalType == ExplosiveBlock.Type.BLAST && feather == 8 && tntInCenter;
        validForDigConversion = validForDigConversion && finalType == ExplosiveBlock.Type.BLAST;

        final boolean modifyingAirstrike = finalType == ExplosiveBlock.Type.AIRSTRIKE;
        boolean validModification = powder + string > 0 || TNTCount > 1;
        validModification = validModification || (finalType == ExplosiveBlock.Type.CONSTRUCTIVE && blockStateMod != null);
        validModification = validModification || (modifyingAirstrike && (string + arrow + coal > 0));

        if ( !validModification && !validForAirstrikeConversion && !validForDigConversion )
            return false;

        if (validForAirstrikeConversion )
        {
            if ( validForDigConversion || validModification )
                return false;

            finalType = ExplosiveBlock.Type.AIRSTRIKE;
            spread += 3;
            pieces += 5;
            height += 20;
            feather -= 8;
        }
        else if ( validForDigConversion )
        {
            finalType = ExplosiveBlock.Type.DIG;
        }

        if ( !modifyingAirstrike )
        {
            strength += (short) (strength_AS + powder);
            if ( arrow + coal + feather > 0 )
                return false;
        }
        else
        {
            pieces += strength;
            strength = (short) (strength_AS + powder);
            if ( feather > 0 )
                return false;
        }

        fuse += (short) (string * 20);
        spread += arrow;
        height += (short) (coal * 5);

        switch ( finalType )
        {
            case BLAST:
                this.outputStack = ExplosiveBlockItem.makeStackBlast( fuse, strength );
                return true;

            case CONSTRUCTIVE:
                if ( blockStateMod != null )
                    constructiveBlockState = blockStateMod;

                this.outputStack = ExplosiveBlockItem.makeStackConstructive( fuse, strength, constructiveBlockState );
                return true;

            case DIG:
                this.outputStack = ExplosiveBlockItem.makeStackDig( fuse, strength );
                return true;

            case AIRSTRIKE:
                this.outputStack = ExplosiveBlockItem.makeStackAirstrike( fuse, strength, spread, pieces, height );
                return true;
        }

        return true;
    }

    @Override
    public ItemStack craft( CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup )
    {
        return this.outputStack.copy();
    }

    @Override
    public boolean fits( int width, int height )
    {
        return width * height > 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return GCMod.FERTILIZER_RECIPE_SERIALIZER;
    }
}
