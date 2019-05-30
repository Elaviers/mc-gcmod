package stupidmod;

import net.minecraft.item.crafting.RecipeSerializers;
import stupidmod.recipe.RecipeAirStrikeExplosive;
import stupidmod.recipe.RecipeConstructiveExplosive;
import stupidmod.recipe.RecipeDigExplosive;
import stupidmod.recipe.RecipeExplosive;

public class RecipeRegister {
    
    public static RecipeSerializers.SimpleSerializer<RecipeAirStrikeExplosive> recipeAirStrikeExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("crafting_airstrike_tnt", RecipeAirStrikeExplosive::new));
    public static RecipeSerializers.SimpleSerializer<RecipeConstructiveExplosive> recipeConstructiveExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("crafting_constructive_tnt", RecipeConstructiveExplosive::new));
    public static RecipeSerializers.SimpleSerializer<RecipeDigExplosive> recipeDigExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("crafting_dig_tnt", RecipeDigExplosive::new));
    public static RecipeSerializers.SimpleSerializer<RecipeExplosive> recipeExplosive = RecipeSerializers.register(new RecipeSerializers.SimpleSerializer<>("crafting_explosive", RecipeExplosive::new));
}
