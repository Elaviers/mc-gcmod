package gcmod;

import gcmod.block.*;
import gcmod.entity.*;
import gcmod.item.*;
import gcmod.recipe.ExplosiveRecipe;
import gcmod.recipe.FertiliserRecipe;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GCMod implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger( "gcmod" );

    public static final GameRule<Boolean> RULE_SOLID_TURDS = GameRuleBuilder.forBoolean( false ).category( GameRuleCategory.MISC ).buildAndRegister( Identifier.fromNamespaceAndPath( "gcmod", "turd_solid" ) );
    public static final GameRule<Integer> RULE_TURD_RATE = GameRuleBuilder.forInteger( 1 ).category( GameRuleCategory.MOBS  ).buildAndRegister( Identifier.fromNamespaceAndPath( "gcmod", "turd_rate" ) );

    public static final ResourceKey<Block> BLOCKKEY_MEME = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "meme_block" ) );
    public static final ResourceKey<Block> BLOCKKEY_POO = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "poo_block" ) );
    public static final ResourceKey<Block> BLOCKKEY_FERMENTEDPOO = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "fermented_poo_block" ) );
    public static final ResourceKey<Block> BLOCKKEY_ROPE = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "rope" ) );
    public static final ResourceKey<Block> BLOCKKEY_CENTRIFUGE = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "centrifuge" ) );
    public static final ResourceKey<Block> BLOCKKEY_SULPHURORE = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "sulphur_ore" ) );
    public static final ResourceKey<Block> BLOCKKEY_NOAHSULPHURORE = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "noah_sulphur_ore" ) );
    public static final ResourceKey<Block> BLOCKKEY_AIRSTRIKETNT = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "airstrike_tnt" ) );
    public static final ResourceKey<Block> BLOCKKEY_BLASTTNT = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "blast_tnt" ) );
    public static final ResourceKey<Block> BLOCKKEY_CONSTRUCTIVETNT = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "constructive_tnt" ) );
    public static final ResourceKey<Block> BLOCKKEY_DIGTNT = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "dig_tnt" ) );
    public static final ResourceKey<Block> BLOCKKEY_WIRELESSTORCH = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "wireless_torch" ) );
    public static final ResourceKey<Block> BLOCKKEY_WIRELESSWALLTORCH = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "wireless_wall_torch" ) );
    public static final ResourceKey<Block> BLOCKKEY_COMPOST = ResourceKey.create( Registries.BLOCK, Identifier.fromNamespaceAndPath( "gcmod", "compost" ) );

    public static final ResourceKey<Item> ITEMKEY_MEMEBLOCK = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "meme_block" ) );
    public static final ResourceKey<Item> ITEMKEY_POOBLOCK = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "poo_block" ) );
    public static final ResourceKey<Item> ITEMKEY_FERMENTEDPOOBLOCK = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "fermented_poo_block" ) );
    public static final ResourceKey<Item> ITEMKEY_ROPE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "rope" ) );
    public static final ResourceKey<Item> ITEMKEY_CENTRIFUGE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "centrifuge" ) );
    public static final ResourceKey<Item> ITEMKEY_SULPHURORE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "sulphur_ore" ) );
    public static final ResourceKey<Item> ITEMKEY_NOAHSULPHURORE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "noah_sulphur_ore" ) );
    public static final ResourceKey<Item> ITEMKEY_AIRSTRIKETNT = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "airstrike_tnt" ) );
    public static final ResourceKey<Item> ITEMKEY_BLASTTNT = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "blast_tnt" ) );
    public static final ResourceKey<Item> ITEMKEY_CONSTRUCTIVETNT = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "constructive_tnt" ) );
    public static final ResourceKey<Item> ITEMKEY_DIGTNT = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "dig_tnt" ) );
    public static final ResourceKey<Item> ITEMKEY_WIRELESSTORCH = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "wireless_torch" ) );
    public static final ResourceKey<Item> ITEMKEY_COMPOST = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "compost" ) );
    public static final ResourceKey<Item> ITEMKEY_POO = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "poo" ) );
    public static final ResourceKey<Item> ITEMKEY_FERMENTEDPOO = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "fermented_poo" ) );
    public static final ResourceKey<Item> ITEMKEY_POOBRICK = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "poo_brick" ) );
    public static final ResourceKey<Item> ITEMKEY_POOCANNON = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "poo_cannon" ) );
    public static final ResourceKey<Item> ITEMKEY_POOPOWDER = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "poo_powder" ) );
    public static final ResourceKey<Item> ITEMKEY_POOPROTEIN = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "poo_protein" ) );
    public static final ResourceKey<Item> ITEMKEY_FERTILISER = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "fertiliser" ) );
    public static final ResourceKey<Item> ITEMKEY_BLACKPOWDER = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "black_powder" ) );
    public static final ResourceKey<Item> ITEMKEY_SULPHUR = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "sulphur" ) );
    public static final ResourceKey<Item> ITEMKEY_NOAHSULPHUR = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "noah_sulphur" ) );
    public static final ResourceKey<Item> ITEMKEY_MEMEESSENCE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "meme_essence" ) );
    public static final ResourceKey<Item> ITEMKEY_CALIBRATOR = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "calibrator" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCBIGWILLIES = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_big_willies" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCFOTCC = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_flight_of_the_chinese_commuter" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCGODLYPISS = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_godly_piss" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCMASSIVECRAP = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_massive_crap" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCPEASIZEDANUS = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_pea_sized_anus" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCPOOEYLOO = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_pooey_loo" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCPOWERFULCONSTIPATION = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_powerful_constipation" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCRAGEOVERACLOGGEDCOMMODE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_rage_over_a_clogged_commode" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCREDWEEWEE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_red_weewee" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCSMELLYMETHANE = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_smelly_methane" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCSYMPHONYOFSTUPIDITY = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_symphony_of_stupidity" ) );
    public static final ResourceKey<Item> ITEMKEY_DISCTOILETWATER = ResourceKey.create( Registries.ITEM, Identifier.fromNamespaceAndPath( "gcmod", "music_disc_toilet_water" ) );

    public static final SoundEvent FART_SOUND = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "fart" ) );
    public static final SoundEvent POO_BLOCK_SOUND = SoundEvent.createVariableRangeEvent( BLOCKKEY_POO.identifier() );
    public static final SoundEvent NOAH_SOUND = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "meme" ) );
    public static final SoundEvent MEME_BLOCK_SOUND = SoundEvent.createVariableRangeEvent( BLOCKKEY_MEME.identifier() );
    public static final SoundEvent CENTRIFUGE_SOUND = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "centrifuge" ) );
    public static final SoundEvent POO_CANNON_SOUND = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "poo_cannon" ) );


    public static final ResourceKey<JukeboxSong> SONG_BIGWILLIES = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "big_willies" ) );
    public static final ResourceKey<JukeboxSong> SONG_FOTCC = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "fotcc" ) );
    public static final ResourceKey<JukeboxSong> SONG_GODLYPISS = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "godly_piss" ) );
    public static final ResourceKey<JukeboxSong> SONG_MASSIVECRAP = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "massive_crap" ) );
    public static final ResourceKey<JukeboxSong> SONG_PEASIZEDANUS = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "pea_sized_anus" ) );
    public static final ResourceKey<JukeboxSong> SONG_POOEYLOO = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "pooey_loo" ) );
    public static final ResourceKey<JukeboxSong> SONG_POWERFULCONSTIPATION = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "powerful_constipation" ) );
    public static final ResourceKey<JukeboxSong> SONG_REDWEEWEE = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "red_weewee" ) );
    public static final ResourceKey<JukeboxSong> SONG_ROACC = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "roacc" ) );
    public static final ResourceKey<JukeboxSong> SONG_SMELLYMETHANE = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "smelly_methane" ) );
    public static final ResourceKey<JukeboxSong> SONG_SOS = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "sos" ) );
    public static final ResourceKey<JukeboxSong> SONG_TOILETWATER = ResourceKey.create( Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath( "gcmod", "toilet_water" ) );

    private static final SoundType POO_BLOCK_SOUNDS = new SoundType( 1, 1, POO_BLOCK_SOUND, POO_BLOCK_SOUND, POO_BLOCK_SOUND, POO_BLOCK_SOUND, POO_BLOCK_SOUND );
    private static final SoundType MEME_BLOCK_SOUNDS = new SoundType( 1, 1, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND );
    private static final SoundType NOAH_BLOCK_SOUNDS = new SoundType( 1, 1,
            NOAH_SOUND, SoundType.STONE.getStepSound(), SoundType.STONE.getPlaceSound(), SoundType.STONE.getHitSound(), NOAH_SOUND
    );

    public static final Block POO_BLOCK = new PooBlock( BlockBehaviour.Properties.of().randomTicks().sound( POO_BLOCK_SOUNDS ).strength( 1 ).mapColor( MapColor.COLOR_BROWN ).setId( BLOCKKEY_POO ) );
    public static final Block FERMENTED_POO_BLOCK = new Block( BlockBehaviour.Properties.of().sound( POO_BLOCK_SOUNDS ).strength( .9f ).mapColor( MapColor.TERRACOTTA_BROWN ).setId( BLOCKKEY_FERMENTEDPOO ) );
    public static final Block ROPE = new RopeBlock( BlockBehaviour.Properties.of().randomTicks().sound( SoundType.CHERRY_WOOD ).strength( .5f ).noOcclusion().setId( BLOCKKEY_ROPE ) );
    public static final Block CENTRIFUGE = new CentrifugeBlock( BlockBehaviour.Properties.of().sound( SoundType.METAL ).strength( 2 ).noOcclusion().setId( BLOCKKEY_CENTRIFUGE ) );

    public static final Block SULPHUR_ORE = new DropExperienceBlock( UniformInt.of( 1, 3 ), BlockBehaviour.Properties.of().sound( SoundType.STONE ).requiresCorrectToolForDrops().strength( 3 ).mapColor( MapColor.STONE ).setId( BLOCKKEY_SULPHURORE ) );
    public static final Block NOAH_SULPHUR_ORE = new DropExperienceBlock( UniformInt.of( 250, 400 ), BlockBehaviour.Properties.of().sound( NOAH_BLOCK_SOUNDS ).requiresCorrectToolForDrops().strength( 20 ).mapColor( MapColor.COLOR_BLACK ).setId( BLOCKKEY_NOAHSULPHURORE ) );
    public static final Block MEME_BLOCK = new Block( BlockBehaviour.Properties.of().sound( MEME_BLOCK_SOUNDS ).requiresCorrectToolForDrops().strength( 1 ).friction( 1.3f ).mapColor( MapColor.SNOW ).setId( BLOCKKEY_MEME ) );

    public static final Block AIRSTRIKE_TNT = new ExplosiveBlock( ExplosiveBlock.Type.AIRSTRIKE, BlockBehaviour.Properties.of().sound( SoundType.WOOD ).mapColor( MapColor.COLOR_YELLOW ).setId( BLOCKKEY_AIRSTRIKETNT ) );
    public static final Block BLAST_TNT = new ExplosiveBlock( ExplosiveBlock.Type.BLAST, BlockBehaviour.Properties.of().sound( SoundType.WOOD ).mapColor( MapColor.COLOR_RED ).setId( BLOCKKEY_BLASTTNT ) );
    public static final Block CONSTRUCTIVE_TNT = new ExplosiveBlock( ExplosiveBlock.Type.CONSTRUCTIVE, BlockBehaviour.Properties.of().sound( SoundType.WOOD ).mapColor( MapColor.COLOR_GREEN ).setId( BLOCKKEY_CONSTRUCTIVETNT ) );
    public static final Block DIG_TNT = new ExplosiveBlock( ExplosiveBlock.Type.DIG, BlockBehaviour.Properties.of().sound( SoundType.WOOD ).mapColor( MapColor.COLOR_CYAN ).setId( BLOCKKEY_DIGTNT ) );

    public static final Block WIRELESS_TORCH = new WirelessTorchBlock( BlockBehaviour.Properties.of()
            .sound( SoundType.METAL ).noCollision().instabreak().lightLevel( Blocks.litBlockEmission( 14 ) ).setId( BLOCKKEY_WIRELESSTORCH ) );

    public static final Block WIRELESS_TORCH_WALL = new WallWirelessTorchBlock( BlockBehaviour.Properties.of()
            .sound( SoundType.METAL ).noCollision().instabreak().lightLevel( Blocks.litBlockEmission( 14 ) ).setId( BLOCKKEY_WIRELESSWALLTORCH ) );

    public static final Block COMPOST = new FarmlandBlock( BlockBehaviour.Properties.of()
            .sound( SoundType.GRAVEL ).mapColor( MapColor.DIRT ).strength( 0.6f ).randomTicks().isViewBlocking(Blocks::always).isSuffocating(Blocks::always).setId( BLOCKKEY_COMPOST ) );

    public static final FoodProperties FOOD_POO_PROTEIN = new FoodProperties.Builder().alwaysEdible().saturationModifier( 5f ).nutrition( 0 ).build();

    public static final Item ROPE_ITEM = new BlockItem( ROPE, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_ROPE ) );
    public static final Item AIRSTRIKE_TNT_ITEM = new ExplosiveBlockItem( AIRSTRIKE_TNT, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_AIRSTRIKETNT ) );
    public static final Item BLAST_TNT_ITEM = new ExplosiveBlockItem( BLAST_TNT, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_BLASTTNT ) );
    public static final Item CONSTRUCTIVE_TNT_ITEM = new ExplosiveBlockItem( CONSTRUCTIVE_TNT, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_CONSTRUCTIVETNT ) );
    public static final Item DIG_TNT_ITEM = new ExplosiveBlockItem( DIG_TNT, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_DIGTNT ) );

    public static final Item POO = new Item( new Item.Properties().setId( ITEMKEY_POO ) );
    public static final Item FERMENTED_POO = new Item( new Item.Properties().setId( ITEMKEY_FERMENTEDPOO ) );
    public static final Item POO_BRICK = new PooBrickItem( new Item.Properties().setId( ITEMKEY_POOBRICK ) );
    public static final Item POO_CANNON = new PooCannonItem( new Item.Properties().durability( 32 ).setId( ITEMKEY_POOCANNON ) );
    public static final Item POO_POWDER = new Item( new Item.Properties().setId( ITEMKEY_POOPOWDER ) );
    public static final Item POO_PROTEIN = new PooProteinItem( new Item.Properties().food( FOOD_POO_PROTEIN ).setId( ITEMKEY_POOPROTEIN ) );
    public static final Item FERTILISER = new FertiliserItem( new Item.Properties().setId( ITEMKEY_FERTILISER ) );
    public static final Item BLACK_POWDER = new Item( new Item.Properties().setId( ITEMKEY_BLACKPOWDER ) );
    public static final Item SULPHUR = new Item( new Item.Properties().setId( ITEMKEY_SULPHUR ) );
    public static final Item NOAH_SULPHUR = new Item( new Item.Properties().setId( ITEMKEY_NOAHSULPHUR ) );
    public static final Item MEME_ESSENCE = new Item( new Item.Properties().setId( ITEMKEY_MEMEESSENCE ) );
    public static final Item CALIBRATOR = new CalibratorItem( new Item.Properties().stacksTo( 1 ).setId( ITEMKEY_CALIBRATOR ) );
    public static final Item DISC_BIG_WILLIES = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_BIGWILLIES ).setId( ITEMKEY_DISCBIGWILLIES ) );
    public static final Item DISC_FLIGHT_OF_THE_CHINESE_COMMUTER = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_FOTCC ).setId( ITEMKEY_DISCFOTCC ) );
    public static final Item DISC_GODLY_PISS = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_GODLYPISS ).setId( ITEMKEY_DISCGODLYPISS ) );
    public static final Item DISC_MASSIVE_CRAP = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_MASSIVECRAP ).setId( ITEMKEY_DISCMASSIVECRAP ) );
    public static final Item DISC_PEA_SIZED_ANUS = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_PEASIZEDANUS ).setId( ITEMKEY_DISCPEASIZEDANUS ) );
    public static final Item DISC_POOEY_LOO = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_POOEYLOO ).setId( ITEMKEY_DISCPOOEYLOO ) );
    public static final Item DISC_POWERFUL_CONSTIPATION = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_POWERFULCONSTIPATION ).setId( ITEMKEY_DISCPOWERFULCONSTIPATION ) );
    public static final Item DISC_RED_WEEWEE = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_REDWEEWEE ).setId( ITEMKEY_DISCREDWEEWEE ) );
    public static final Item DISC_RAGE_OVER_A_CLOGGED_COMMODE = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_ROACC ).setId( ITEMKEY_DISCRAGEOVERACLOGGEDCOMMODE ) );
    public static final Item DISC_SMELLY_METHANE = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_SMELLYMETHANE ).setId( ITEMKEY_DISCSMELLYMETHANE ) );
    public static final Item DISC_SYMPHONY_OF_STUPIDITY = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_SOS ).setId( ITEMKEY_DISCSYMPHONYOFSTUPIDITY ) );
    public static final Item DISC_TOILET_WATER = new Item( new Item.Properties().stacksTo( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_TOILETWATER ).setId( ITEMKEY_DISCTOILETWATER ) );

    public static final ResourceKey<EntityType<?>> ENTKEY_POO = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "poo" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_POOBRICK = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "poo_brick" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_EXPLOSIVEPOOBRICK = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "explosive_poo_brick" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_BOMB = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "explosive_bomb" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_EXPLOSIVE = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "explosive" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_AIRSTRIKEEXPLOSIVE = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "airstrike_explosive" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_CONSTRUCTIVEEXPLOSIVE = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "constructive_explosive" ) );
    public static final ResourceKey<EntityType<?>> ENTKEY_DIGEXPLOSIVE = ResourceKey.create( Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "dig_explosive" ) );

    public static final EntityType<PooEntity> POO_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_POO,
            EntityType.Builder.of( PooEntity::new, MobCategory.MISC ).sized( .75f, .3125f ).build( ENTKEY_POO )
    );

    public static final EntityType<PooBrickEntity> POO_BRICK_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_POOBRICK,
            EntityType.Builder.of( PooBrickEntity::new, MobCategory.MISC ).sized( .4f, .25f ).build( ENTKEY_POOBRICK )
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_POO_BRICK_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_EXPLOSIVEPOOBRICK,
            EntityType.Builder.of( PooBrickEntity::createExplosive, MobCategory.MISC ).sized( .4f, .25f ).build( ENTKEY_EXPLOSIVEPOOBRICK )
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_BOMB_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_BOMB,
            EntityType.Builder.of( PooBrickEntity::createExplosive, MobCategory.MISC ).sized( .5f, .5f ).build( ENTKEY_BOMB )
    );

    public static final EntityType<ExplosiveEntity> EXPLOSIVE_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_EXPLOSIVE,
            EntityType.Builder.of( ExplosiveEntity::new, MobCategory.MISC ).sized( 1f, 1f ).build( ENTKEY_EXPLOSIVE )
    );

    public static final EntityType<AirstrikeExplosiveEntity> AIRSTRIKE_EXPLOSIVE_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_AIRSTRIKEEXPLOSIVE,
            EntityType.Builder.of( AirstrikeExplosiveEntity::new, MobCategory.MISC ).sized( 1f, 1f ).build( ENTKEY_AIRSTRIKEEXPLOSIVE )
    );

    public static final EntityType<ConstructiveExplosiveEntity> CONSTRUCTIVE_EXPLOSIVE_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_CONSTRUCTIVEEXPLOSIVE,
            EntityType.Builder.of( ConstructiveExplosiveEntity::new, MobCategory.MISC ).sized( 1f, 1f ).build( ENTKEY_CONSTRUCTIVEEXPLOSIVE )
    );

    public static final EntityType<DigExplosiveEntity> DIG_EXPLOSIVE_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ENTKEY_DIGEXPLOSIVE,
            EntityType.Builder.of( DigExplosiveEntity::new, MobCategory.MISC ).sized( 1f, 1f ).build( ENTKEY_DIGEXPLOSIVE )
    );

    public static final BlockEntityType<CentrifugeEntity> CENTRIFUGE_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath( "gcmod", "centrifuge" ),
            FabricBlockEntityTypeBuilder.create( CentrifugeEntity::new, CENTRIFUGE ).build()
    );

    public static final BlockEntityType<WirelessTorchEntity> WIRELESS_TORCH_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath( "gcmod", "wireless_torch" ),
            FabricBlockEntityTypeBuilder.create( WirelessTorchEntity::new, WIRELESS_TORCH ).build()
    );

    public static BlockEntityType<ExplosiveBlockEntity> AIRSTRIKE_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> BLAST_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> CONSTRUCTIVE_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> DIG_TNT_BLOCK_ENTITY;

    public static DataComponentType<EnchantmentValueEffect> EFFECT_CONSTIPATION = Registry.register(
            BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath( "gcmod", "constipation" ),
            DataComponentType.<EnchantmentValueEffect>builder().persistent( EnchantmentValueEffect.CODEC ).build() );

    public static DataComponentType<EnchantmentValueEffect> EFFECT_POOER = Registry.register(
            BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath( "gcmod", "pooer" ),
            DataComponentType.<EnchantmentValueEffect>builder().persistent( EnchantmentValueEffect.CODEC ).build() );

    public static final TagKey<Enchantment> ENCH_DIARRHOEA = TagKey.create( Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath( "gcmod", "induces_diarrhoea" ) );
    public static final TagKey<Enchantment> ENCH_LAXATIVES = TagKey.create( Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath( "gcmod", "laxatives" ) );

    public static final DataComponentType<ExplosiveBlockComponent> DATA_EXPLOSIVE_INFO = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath( "gcmod", "explosive_info" ),
            DataComponentType.<ExplosiveBlockComponent>builder().persistent( ExplosiveBlockComponent.CODEC ).build()
    );

    public static final DataComponentType<TorchNetworkComponent> DATA_TORCH_NETWORK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath( "gcmod", "torch_network" ),
            DataComponentType.<TorchNetworkComponent>builder().persistent( TorchNetworkComponent.CODEC ).build()
    );

    public static RecipeSerializer<FertiliserRecipe> FERTILIZER_RECIPE_SERIALIZER;
    public static RecipeSerializer<ExplosiveRecipe> EXPLOSIVE_RECIPE_SERIALIZER;

    public static MenuType<CentrifugeScreenHandler> CENTRIFUGE_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath( "gcmod", "centrifuge" ),
            new MenuType<>( CentrifugeScreenHandler::new, FeatureFlagSet.of() )
    );

    private static final CreativeModeTab ITEM_GROUP = FabricCreativeModeTab.builder()
            .icon( () -> new ItemStack( POO_BLOCK ) )
            .title( Component.translatable( "itemGroup.gcmod" ) )
            .displayItems( ( context, entries ) -> {
                entries.accept( POO_BLOCK );
                entries.accept( FERMENTED_POO_BLOCK );
                entries.accept( ROPE );
                entries.accept( CENTRIFUGE );
                entries.accept( COMPOST );
                entries.accept( SULPHUR_ORE );
                entries.accept( NOAH_SULPHUR_ORE );
                entries.accept( MEME_BLOCK );
                entries.accept( AIRSTRIKE_TNT_ITEM.getDefaultInstance() );
                entries.accept( BLAST_TNT_ITEM.getDefaultInstance() );
                entries.accept( CONSTRUCTIVE_TNT_ITEM.getDefaultInstance() );
                entries.accept( DIG_TNT_ITEM.getDefaultInstance() );
                entries.accept( WIRELESS_TORCH );

                entries.accept( POO );
                entries.accept( FERMENTED_POO );
                entries.accept( POO_BRICK );
                entries.accept( POO_CANNON );
                entries.accept( POO_POWDER );
                entries.accept( POO_PROTEIN );
                entries.accept( FERTILISER );
                entries.accept( BLACK_POWDER );
                entries.accept( SULPHUR );
                entries.accept( NOAH_SULPHUR );
                entries.accept( MEME_ESSENCE );
                entries.accept( CALIBRATOR );
                entries.accept( DISC_BIG_WILLIES );
                entries.accept( DISC_FLIGHT_OF_THE_CHINESE_COMMUTER );
                entries.accept( DISC_GODLY_PISS );
                entries.accept( DISC_MASSIVE_CRAP );
                entries.accept( DISC_PEA_SIZED_ANUS );
                entries.accept( DISC_POOEY_LOO );
                entries.accept( DISC_POWERFUL_CONSTIPATION );
                entries.accept( DISC_RED_WEEWEE );
                entries.accept( DISC_RAGE_OVER_A_CLOGGED_COMMODE );
                entries.accept( DISC_SMELLY_METHANE );
                entries.accept( DISC_SYMPHONY_OF_STUPIDITY );
                entries.accept( DISC_TOILET_WATER );
            } )
            .build();

    private static final ResourceKey<PlacedFeature> SULPHUR_ORE_PLACEMENT = ResourceKey.create( Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath( "gcmod", "sulphur_ore" ) );
    private static final ResourceKey<PlacedFeature> NOAH_ORE_PLACEMENT = ResourceKey.create( Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath( "gcmod", "noah_sulphur_ore" ) );

    public static final SoundEvent MUSIC_BIGWILLIES = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.big_willies" ) );
    public static final SoundEvent MUSIC_FOTCC = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.flight_of_the_chinese_commuter" ) );
    public static final SoundEvent MUSIC_GODLYPISS = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.godly_piss" ) );
    public static final SoundEvent MUSIC_MASSIVECRAP = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.massive_crap" ) );
    public static final SoundEvent MUSIC_PEASIZEDANUS = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.pea_sized_anus" ) );
    public static final SoundEvent MUSIC_POOEYLOO = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.pooey_loo" ) );
    public static final SoundEvent MUSIC_POWERFULCONSTIPATION = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.powerful_constipation" ) );
    public static final SoundEvent MUSIC_ROACC = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.rage_over_a_clogged_commode" ) );
    public static final SoundEvent MUSIC_REDWEEWEE = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.red_weewee" ) );
    public static final SoundEvent MUSIC_SMELLYMETHANE = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.smelly_methane" ) );
    public static final SoundEvent MUSIC_SOS = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.symphony_of_stupidity" ) );
    public static final SoundEvent MUSIC_TOILETWATER = SoundEvent.createVariableRangeEvent( Identifier.fromNamespaceAndPath( "gcmod", "music_disc.toilet_water" ) );

    public static final SimpleParticleType PARTICLE_POO_SPLAT = FabricParticleTypes.simple();

    @Override
    public void onInitialize()
    {
        Registry.register( BuiltInRegistries.SOUND_EVENT, FART_SOUND.location(), FART_SOUND );
        Registry.register( BuiltInRegistries.SOUND_EVENT, POO_BLOCK_SOUND.location(), POO_BLOCK_SOUND );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MEME_BLOCK_SOUND.location(), MEME_BLOCK_SOUND );
        Registry.register( BuiltInRegistries.SOUND_EVENT, CENTRIFUGE_SOUND.location(), CENTRIFUGE_SOUND );
        Registry.register( BuiltInRegistries.SOUND_EVENT, POO_CANNON_SOUND.location(), POO_CANNON_SOUND );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_BIGWILLIES.location(), MUSIC_BIGWILLIES );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_FOTCC.location(), MUSIC_FOTCC );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_GODLYPISS.location(), MUSIC_GODLYPISS );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_MASSIVECRAP.location(), MUSIC_MASSIVECRAP );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_PEASIZEDANUS.location(), MUSIC_PEASIZEDANUS );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_POOEYLOO.location(), MUSIC_POOEYLOO );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_POWERFULCONSTIPATION.location(), MUSIC_POWERFULCONSTIPATION );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_ROACC.location(), MUSIC_ROACC );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_REDWEEWEE.location(), MUSIC_REDWEEWEE );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_SMELLYMETHANE.location(), MUSIC_SMELLYMETHANE );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_SOS.location(), MUSIC_SOS );
        Registry.register( BuiltInRegistries.SOUND_EVENT, MUSIC_TOILETWATER.location(), MUSIC_TOILETWATER );

        Registry.register( BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "poo_splat" ), PARTICLE_POO_SPLAT );

        Registry.register( BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath( "gcmod", "itemgroup" ), ITEM_GROUP );

        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_POO, POO_BLOCK );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_FERMENTEDPOO, FERMENTED_POO_BLOCK );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_ROPE, ROPE );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_CENTRIFUGE, CENTRIFUGE );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_SULPHURORE, SULPHUR_ORE );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_NOAHSULPHURORE, NOAH_SULPHUR_ORE );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_MEME, MEME_BLOCK );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_AIRSTRIKETNT, AIRSTRIKE_TNT );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_BLASTTNT, BLAST_TNT );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_CONSTRUCTIVETNT, CONSTRUCTIVE_TNT );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_DIGTNT, DIG_TNT );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_WIRELESSTORCH, WIRELESS_TORCH );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_WIRELESSWALLTORCH, WIRELESS_TORCH_WALL );
        Registry.register( BuiltInRegistries.BLOCK, BLOCKKEY_COMPOST, COMPOST );

        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_POOBLOCK, new BlockItem( POO_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_POOBLOCK ) ) );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_FERMENTEDPOOBLOCK, new BlockItem( FERMENTED_POO_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_FERMENTEDPOOBLOCK ) ) );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_ROPE, ROPE_ITEM );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_CENTRIFUGE, new BlockItem( CENTRIFUGE, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_CENTRIFUGE ) ) );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_SULPHURORE, new BlockItem( SULPHUR_ORE, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_SULPHURORE ) ) );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_NOAHSULPHURORE, new BlockItem( NOAH_SULPHUR_ORE, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_NOAHSULPHURORE ) ) );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_MEMEBLOCK, new BlockItem( MEME_BLOCK, new Item.Properties().stacksTo( 69 ).useBlockDescriptionPrefix().setId( ITEMKEY_MEMEBLOCK ) ) );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_AIRSTRIKETNT, AIRSTRIKE_TNT_ITEM );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_BLASTTNT, BLAST_TNT_ITEM );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_CONSTRUCTIVETNT, CONSTRUCTIVE_TNT_ITEM );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DIGTNT, DIG_TNT_ITEM );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_WIRELESSTORCH,
                new StandingAndWallBlockItem( WIRELESS_TORCH, WIRELESS_TORCH_WALL, Direction.DOWN, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_WIRELESSTORCH ) )
        );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_COMPOST, new BlockItem( COMPOST, new Item.Properties().useBlockDescriptionPrefix().setId( ITEMKEY_COMPOST ) ) );
        
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_POO, POO );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_FERMENTEDPOO, FERMENTED_POO );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_POOBRICK, POO_BRICK );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_POOCANNON, POO_CANNON );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_POOPOWDER, POO_POWDER );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_POOPROTEIN, POO_PROTEIN );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_FERTILISER, FERTILISER );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_BLACKPOWDER, BLACK_POWDER );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_SULPHUR, SULPHUR );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_NOAHSULPHUR, NOAH_SULPHUR );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_MEMEESSENCE, MEME_ESSENCE );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_CALIBRATOR, CALIBRATOR );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCBIGWILLIES, DISC_BIG_WILLIES );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCFOTCC, DISC_FLIGHT_OF_THE_CHINESE_COMMUTER );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCGODLYPISS, DISC_GODLY_PISS );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCMASSIVECRAP, DISC_MASSIVE_CRAP );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCPEASIZEDANUS, DISC_PEA_SIZED_ANUS );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCPOOEYLOO, DISC_POOEY_LOO );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCPOWERFULCONSTIPATION, DISC_POWERFUL_CONSTIPATION );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCRAGEOVERACLOGGEDCOMMODE, DISC_RAGE_OVER_A_CLOGGED_COMMODE );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCREDWEEWEE, DISC_RED_WEEWEE );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCSMELLYMETHANE, DISC_SMELLY_METHANE );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCSYMPHONYOFSTUPIDITY, DISC_SYMPHONY_OF_STUPIDITY );
        Registry.register( BuiltInRegistries.ITEM, ITEMKEY_DISCTOILETWATER, DISC_TOILET_WATER );

        AIRSTRIKE_TNT_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath( "gcmod", "airstrike_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( AIRSTRIKE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        BLAST_TNT_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath( "gcmod", "blast_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( BLAST_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        CONSTRUCTIVE_TNT_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath( "gcmod", "constructive_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( CONSTRUCTIVE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        DIG_TNT_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath( "gcmod", "dig_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( DIG_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        CENTRIFUGE_ENTITY.addValidBlock( CENTRIFUGE );
        WIRELESS_TORCH_ENTITY.addValidBlock( WIRELESS_TORCH );
        WIRELESS_TORCH_ENTITY.addValidBlock( WIRELESS_TORCH_WALL );
        AIRSTRIKE_TNT_BLOCK_ENTITY.addValidBlock( AIRSTRIKE_TNT );
        BLAST_TNT_BLOCK_ENTITY.addValidBlock( BLAST_TNT );
        CONSTRUCTIVE_TNT_BLOCK_ENTITY.addValidBlock( CONSTRUCTIVE_TNT );
        DIG_TNT_BLOCK_ENTITY.addValidBlock( DIG_TNT );

        Registry.register( BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "crafting_fertilizer" ), new RecipeType<FertiliserRecipe>()
        {
            @Override
            public String toString()
            {
                return "<FERTILIZER_RECIPE>";
            }
        } );
        Registry.register( BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath( "gcmod", "crafting_explosive" ), new RecipeType<ExplosiveRecipe>()
        {
            @Override
            public String toString()
            {
                return "<EXPLOSIVE_RECIPE>";
            }
        } );

        FERTILIZER_RECIPE_SERIALIZER = Registry.register( BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath( "gcmod", "crafting_fertiliser" ), new RecipeSerializer<>( FertiliserRecipe.MAP_CODEC, FertiliserRecipe.STREAM_CODEC ) );
        EXPLOSIVE_RECIPE_SERIALIZER = Registry.register( BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath( "gcmod", "crafting_explosive" ), new RecipeSerializer<>( ExplosiveRecipe.MAP_CODEC, ExplosiveRecipe.STREAM_CODEC ) );

        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, SULPHUR_ORE_PLACEMENT );
        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NOAH_ORE_PLACEMENT );

        PayloadTypeRegistry.clientboundPlay().register( PooSplatPayload.ID, PooSplatPayload.CODEC );
    }
}