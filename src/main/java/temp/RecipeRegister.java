package stupidmod;

import net.minecraft.item.crafting.RecipeSerializers;
import stupidmod.recipe.RecipeAirStrikeExplosive;
import stupidmod.recipe.RecipeConstructiveExplosive;
import stupidmod.recipe.RecipeDigExplosive;
import stupidmod.recipe.RecipeExplosive;

public class RecipeRegister {
    
    public static RecipeSerializers.SimpleSerializer<RecipeAirStrikeExplosive> recipeAirStrikeExplosive;
    public static RecipeSerializers.SimpleSerializer<RecipeConstructiveExplosive> recipeConstructiveExplosive ;
    public static RecipeSerializers.SimpleSerializer<RecipeDigExplosive> recipeDigExplosive;
    public static RecipeSerializers.SimpleSerializer<RecipeExplosive> recipeExplosive;

    public static void registerRecipes()
    {
        recipeAirStrikeExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("stupidmod:crafting_airstrike_tnt", RecipeAirStrikeExplosive::new));
        recipeConstructiveExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("stupidmod:crafting_constructive_tnt", RecipeConstructiveExplosive::new));
        recipeDigExplosive  = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("stupidmod:crafting_dig_tnt", RecipeDigExplosive::new));
        recipeExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("stupidmod:crafting_explosive", RecipeExplosive::new));
    }

}
