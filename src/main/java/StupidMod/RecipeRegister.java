package StupidMod;

import StupidMod.Recipes.RecipeAirStrikeExplosive;
import StupidMod.Recipes.RecipeConstructiveExplosive;
import StupidMod.Recipes.RecipeDigExplosive;
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
    RecipeDigExplosive recipeDigExplosive;
    
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        
        recipeExplosive = new RecipeExplosive();
        recipeAirStrikeExplosive = new RecipeAirStrikeExplosive();
        recipeConstructiveExplosive = new RecipeConstructiveExplosive();
        recipeDigExplosive = new RecipeDigExplosive();
        
        recipeExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive"));
        recipeAirStrikeExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive_airstrike"));
        recipeConstructiveExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive_construct"));
        recipeDigExplosive.setRegistryName(new ResourceLocation(StupidMod.id, "recipe_explosive_dig"));
    }
    
    @SubscribeEvent
    void registerRecipes(RegistryEvent.Register<IRecipe> register) {
        register.getRegistry().registerAll(recipeExplosive, recipeAirStrikeExplosive, recipeDigExplosive /*, recipeConstructiveExplosive*/);
        //Constructive explosive crafting disabled because it is digusting
    
        GameRegistry.addSmelting(StupidMod.instance.items.itemPooPowder,new ItemStack(StupidMod.instance.items.itemPooBrick), 0);
    }
    
}
