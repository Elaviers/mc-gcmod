package gcmod.recipe;

import com.mojang.serialization.MapCodec;
import gcmod.ExplosiveBlockComponent;
import gcmod.GCMod;
import gcmod.block.ExplosiveBlock;
import gcmod.item.ExplosiveBlockItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveRecipe extends CustomRecipe
{
    public static final ExplosiveRecipe INSTANCE = new ExplosiveRecipe();
    public static final MapCodec<ExplosiveRecipe> MAP_CODEC = MapCodec.unit( INSTANCE );
    public static final StreamCodec<RegistryFriendlyByteBuf, ExplosiveRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private static ItemStack getResult( CraftingInput inv )
    {
        boolean validForDigConversion = false;
        boolean tntInCenter = false;
        int strength, fuse, spread, pieces, height;
        int powder, string, arrow, coal, feather;
        strength = fuse = spread = pieces = height = feather = 0;
        powder = string = arrow = coal = 0;

        BlockState constructiveBlockState = null;
        BlockState blockStateMod = null;

        int strength_AS = 0;

        ExplosiveBlock.Type finalType = null;
        int TNTCount = 0;

        for ( int i = 0; i < inv.size(); ++i )
        {
            ItemStack stack = inv.getItem( i );

            if ( !stack.isEmpty() )
            {
                final Item currentItem = stack.getItem();

                if ( currentItem instanceof ExplosiveBlockItem && stack.get( GCMod.DATA_EXPLOSIVE_INFO ) != null )
                {
                    ExplosiveBlockComponent info = stack.get( GCMod.DATA_EXPLOSIVE_INFO );
                    assert info != null;

                    fuse += info.fuse();

                    final ExplosiveBlock.Type type = ((ExplosiveBlock) Block.byItem( currentItem )).type;
                    if ( type == ExplosiveBlock.Type.AIRSTRIKE )
                        strength_AS += info.strength();
                    else
                        strength += info.strength();

                    switch ( type )
                    {
                        case CONSTRUCTIVE:
                        {
                            final BlockState bs = info.block();
                            if ( constructiveBlockState == null )
                                constructiveBlockState = bs;
                            else if ( bs != constructiveBlockState )
                                return null;
                        }
                        break;

                        case AIRSTRIKE:
                            spread += info.spread();
                            pieces += info.pieces();
                            height += info.height();
                            break;

                    }

                    finalType = type;
                    TNTCount++;

                    if ( !tntInCenter )
                    {
                        final int w = inv.width();
                        final int h = inv.height();
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
                    final BlockState bs = Block.byItem( currentItem ).defaultBlockState();
                    if ( blockStateMod == null )
                        blockStateMod = bs;
                    else if ( bs != blockStateMod )
                        return null;
                }
                else
                    return null;
            }
        }

        if ( TNTCount == 0 )
            return null;

        final boolean validForAirstrikeConversion = finalType == ExplosiveBlock.Type.BLAST && feather == 8 && tntInCenter;
        validForDigConversion = validForDigConversion && finalType == ExplosiveBlock.Type.BLAST;

        final boolean modifyingAirstrike = finalType == ExplosiveBlock.Type.AIRSTRIKE;
        boolean validModification = powder + string > 0 || TNTCount > 1;
        validModification = validModification || (finalType == ExplosiveBlock.Type.CONSTRUCTIVE && blockStateMod != null);
        validModification = validModification || (modifyingAirstrike && (string + arrow + coal > 0));

        if ( !validModification && !validForAirstrikeConversion && !validForDigConversion )
            return null;

        if (validForAirstrikeConversion )
        {
            if ( validForDigConversion || validModification )
                return null;

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
                return null;
        }
        else
        {
            pieces += strength;
            strength = (short) (strength_AS + powder);
            if ( feather > 0 )
                return null;
        }

        fuse += (short) (string * 20);
        spread += arrow;
        height += (short) (coal * 5);

        ItemStack result = null;
        switch ( finalType )
        {
            case BLAST:
                result = new ItemStack( GCMod.BLAST_TNT );
                break;

            case CONSTRUCTIVE:
                if ( blockStateMod != null )
                    constructiveBlockState = blockStateMod;

                result = new ItemStack( GCMod.CONSTRUCTIVE_TNT );
                break;

            case DIG:
                result = new ItemStack( GCMod.DIG_TNT );
                break;

            case AIRSTRIKE:
                result = new ItemStack( GCMod.AIRSTRIKE_TNT );
                break;
        }

        if ( constructiveBlockState == null )
            constructiveBlockState = Blocks.AIR.defaultBlockState();

        result.set( GCMod.DATA_EXPLOSIVE_INFO, new ExplosiveBlockComponent( fuse, strength, constructiveBlockState, spread, pieces, height ) );
        return result;
    }

    @Override
    public boolean matches( CraftingInput inv, Level world )
    {
        return getResult( inv ) != null;
    }

    @Override
    public ItemStack assemble( CraftingInput inv )
    {
        return getResult( inv );
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer()
    {
        return GCMod.FERTILIZER_RECIPE_SERIALIZER;
    }
}
