package StupidMod;

import StupidMod.Recipes.RecipeAirStrikeExplosive;
import StupidMod.Recipes.RecipeConstructiveExplosive;
import StupidMod.Recipes.RecipeExplosive;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeRegister {
    RecipeExplosive recipeExplosive;
    RecipeAirStrikeExplosive recipeAirStrikeExplosive;
    RecipeConstructiveExplosive recipeConstructiveExplosive;
    
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        
        recipeExplosive = new RecipeExplosive();
        recipeAirStrikeExplosive = new RecipeAirStrikeExplosive();
        recipeConstructiveExplosive = new RecipeConstructiveExplosive();
        
        recipeExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive"));
        recipeAirStrikeExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive_airstrike"));
        recipeConstructiveExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive_construct"));
    }
    
    @SubscribeEvent
    void registerRecipes(RegistryEvent.Register<IRecipe> register) {
        register.getRegistry().registerAll(recipeExplosive, recipeAirStrikeExplosive, recipeConstructiveExplosive);
    
        GameRegistry.addSmelting(StupidMod.instance.items.itemPooPowder,new ItemStack(StupidMod.instance.items.itemPooBrick), 0);
    }
    
}
