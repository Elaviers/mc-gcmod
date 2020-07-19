package stupidmod;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.world.gen.layer.EdgeLayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.recipe.*;

@ObjectHolder(StupidMod.id)
public class StupidModRecipes {

    protected static final String
            nameAirStrikeExplosiveRecipe = "crafting_airstrike_tnt",
            nameConstructiveExplosiveRecipe = "crafting_constructive_tnt",
            nameDigExplosiveRecipe = "crafting_dig_tnt",
            nameExplosiveRecipe = "crafting_explosive",
            nameFertiliserRecipe = "crafting_fertiliser";

    @ObjectHolder(nameAirStrikeExplosiveRecipe)
    public static SpecialRecipeSerializer<RecipeAirStrikeExplosive> AIR_STRIKE_EXPLOSIVE;

    @ObjectHolder(nameConstructiveExplosiveRecipe)
    public static SpecialRecipeSerializer<RecipeConstructiveExplosive> CONSTRUCTIVE_EXPLOSIVE;

    @ObjectHolder(nameDigExplosiveRecipe)
    public static SpecialRecipeSerializer<RecipeDigExplosive> DIG_EXPLOSIVE;

    @ObjectHolder(nameExplosiveRecipe)
    public static SpecialRecipeSerializer<RecipeExplosive> EXPLOSIVE;

    @ObjectHolder(nameFertiliserRecipe)
    public static SpecialRecipeSerializer<RecipeFertiliser> FERTILISER;

    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        static void RegisterRecipes(RegistryEvent.Register<IRecipeSerializer<?>> register)
        {
            register.getRegistry().registerAll(
                    new SpecialRecipeSerializer<RecipeAirStrikeExplosive>(RecipeAirStrikeExplosive::new).setRegistryName(nameAirStrikeExplosiveRecipe),
                    new SpecialRecipeSerializer<RecipeConstructiveExplosive>(RecipeConstructiveExplosive::new).setRegistryName(nameConstructiveExplosiveRecipe),
                    new SpecialRecipeSerializer<RecipeDigExplosive>(RecipeDigExplosive::new).setRegistryName(nameDigExplosiveRecipe),
                    new SpecialRecipeSerializer<RecipeExplosive>(RecipeExplosive::new).setRegistryName(nameExplosiveRecipe),
                    new SpecialRecipeSerializer<RecipeFertiliser>(RecipeFertiliser::new).setRegistryName(nameFertiliserRecipe)
            );
        }
    }
}