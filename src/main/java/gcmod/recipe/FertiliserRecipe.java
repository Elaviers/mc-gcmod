package gcmod.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gcmod.GCMod;
import gcmod.item.FertiliserItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class FertiliserRecipe extends CustomRecipe
{
    public static final FertiliserRecipe INSTANCE = new FertiliserRecipe();
    public static final MapCodec<FertiliserRecipe> MAP_CODEC = MapCodec.unit( INSTANCE );
    public static final StreamCodec<RegistryFriendlyByteBuf, FertiliserRecipe> STREAM_CODEC = StreamCodec.unit( INSTANCE );

    private static int calculateDamageResult( CraftingInput inv )
    {
        int prevDamage = 0;
        float pooValue = 0;

        for ( int i = 0; i < inv.size(); ++i )
        {
            ItemStack stack = inv.getItem( i );

            if ( stack.getItem() == Items.BUCKET )
            {
                if ( prevDamage > 0 )
                    return -1;

                prevDamage = FertiliserItem.MAX_DAMAGE;
            } else if ( stack.getItem() == GCMod.FERTILISER )
            {
                if ( prevDamage > 0 )
                    return -1;

                prevDamage = stack.getDamageValue();
                if ( prevDamage == 0 )
                    return -1;
            } else if ( stack.getItem() == GCMod.POO )
            {
                pooValue += .25f;
            } else if ( stack.getItem() == GCMod.FERMENTED_POO )
            {
                pooValue += 1.25f;
            } else if ( !stack.isEmpty() )
            {
                return -1;
            }
        }

        int extra = (int)pooValue;
        if ( prevDamage > 0 && extra > 0 )
        {
            final int newDmg = prevDamage - extra;
            if ( newDmg <= 0 )
                return -1;

            return newDmg;
        }

        return -1;
    }

    @Override
    public boolean matches( CraftingInput inv, Level world )
    {
        return calculateDamageResult( inv ) > 0;
    }

    @Override
    public ItemStack assemble( CraftingInput inv )
    {
        ItemStack stack = new ItemStack( GCMod.FERTILISER );
        stack.setDamageValue( calculateDamageResult( inv ) );
        return stack;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer()
    {
        return GCMod.FERTILIZER_RECIPE_SERIALIZER;
    }
}
