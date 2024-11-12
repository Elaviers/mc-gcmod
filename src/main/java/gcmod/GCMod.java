package gcmod;

import gcmod.block.*;
import gcmod.entity.*;
import gcmod.item.*;
import gcmod.recipe.ExplosiveRecipe;
import gcmod.recipe.FertiliserRecipe;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GCMod implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger( "gcmod" );

    public static final GameRules.Key<GameRules.BooleanRule> RULE_SOLID_TURDS =
            GameRuleRegistry.register( "solidTurds", GameRules.Category.MISC, GameRuleFactory.createBooleanRule( false ) );

    public static final GameRules.Key<GameRules.IntRule> RULE_TURD_RATE =
            GameRuleRegistry.register( "turdRate", GameRules.Category.MOBS, GameRuleFactory.createIntRule( 1 ) );

    public static final RegistryKey<Block> BLOCKKEY_MEME = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "meme_block" ) );
    public static final RegistryKey<Block> BLOCKKEY_POO = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "poo_block" ) );
    public static final RegistryKey<Block> BLOCKKEY_FERMENTEDPOO = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "fermented_poo_block" ) );
    public static final RegistryKey<Block> BLOCKKEY_ROPE = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "rope" ) );
    public static final RegistryKey<Block> BLOCKKEY_CENTRIFUGE = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "centrifuge" ) );
    public static final RegistryKey<Block> BLOCKKEY_SULPHURORE = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "sulphur_ore" ) );
    public static final RegistryKey<Block> BLOCKKEY_NOAHSULPHURORE = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "noah_sulphur_ore" ) );
    public static final RegistryKey<Block> BLOCKKEY_AIRSTRIKETNT = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "airstrike_tnt" ) );
    public static final RegistryKey<Block> BLOCKKEY_BLASTTNT = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "blast_tnt" ) );
    public static final RegistryKey<Block> BLOCKKEY_CONSTRUCTIVETNT = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "constructive_tnt" ) );
    public static final RegistryKey<Block> BLOCKKEY_DIGTNT = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "dig_tnt" ) );
    public static final RegistryKey<Block> BLOCKKEY_WIRELESSTORCH = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "wireless_torch" ) );
    public static final RegistryKey<Block> BLOCKKEY_WIRELESSWALLTORCH = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "wireless_wall_torch" ) );
    public static final RegistryKey<Block> BLOCKKEY_COMPOST = RegistryKey.of( RegistryKeys.BLOCK, Identifier.of( "gcmod", "compost" ) );

    public static final RegistryKey<Item> ITEMKEY_MEMEBLOCK = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "meme_block" ) );
    public static final RegistryKey<Item> ITEMKEY_POOBLOCK = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "poo_block" ) );
    public static final RegistryKey<Item> ITEMKEY_FERMENTEDPOOBLOCK = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "fermented_poo_block" ) );
    public static final RegistryKey<Item> ITEMKEY_ROPE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "rope" ) );
    public static final RegistryKey<Item> ITEMKEY_CENTRIFUGE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "centrifuge" ) );
    public static final RegistryKey<Item> ITEMKEY_SULPHURORE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "sulphur_ore" ) );
    public static final RegistryKey<Item> ITEMKEY_NOAHSULPHURORE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "noah_sulphur_ore" ) );
    public static final RegistryKey<Item> ITEMKEY_AIRSTRIKETNT = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "airstrike_tnt" ) );
    public static final RegistryKey<Item> ITEMKEY_BLASTTNT = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "blast_tnt" ) );
    public static final RegistryKey<Item> ITEMKEY_CONSTRUCTIVETNT = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "constructive_tnt" ) );
    public static final RegistryKey<Item> ITEMKEY_DIGTNT = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "dig_tnt" ) );
    public static final RegistryKey<Item> ITEMKEY_WIRELESSTORCH = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "wireless_torch" ) );
    public static final RegistryKey<Item> ITEMKEY_COMPOST = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "compost" ) );
    public static final RegistryKey<Item> ITEMKEY_POO = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "poo" ) );
    public static final RegistryKey<Item> ITEMKEY_FERMENTEDPOO = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "fermented_poo" ) );
    public static final RegistryKey<Item> ITEMKEY_POOBRICK = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "poo_brick" ) );
    public static final RegistryKey<Item> ITEMKEY_POOCANNON = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "poo_cannon" ) );
    public static final RegistryKey<Item> ITEMKEY_POOPOWDER = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "poo_powder" ) );
    public static final RegistryKey<Item> ITEMKEY_POOPROTEIN = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "poo_protein" ) );
    public static final RegistryKey<Item> ITEMKEY_FERTILISER = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "fertiliser" ) );
    public static final RegistryKey<Item> ITEMKEY_BLACKPOWDER = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "black_powder" ) );
    public static final RegistryKey<Item> ITEMKEY_SULPHUR = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "sulphur" ) );
    public static final RegistryKey<Item> ITEMKEY_NOAHSULPHUR = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "noah_sulphur" ) );
    public static final RegistryKey<Item> ITEMKEY_MEMEESSENCE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "meme_essence" ) );
    public static final RegistryKey<Item> ITEMKEY_CALIBRATOR = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "calibrator" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCBIGWILLIES = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_big_willies" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCFOTCC = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_flight_of_the_chinese_commuter" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCGODLYPISS = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_godly_piss" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCMASSIVECRAP = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_massive_crap" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCPEASIZEDANUS = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_pea_sized_anus" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCPOOEYLOO = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_pooey_loo" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCPOWERFULCONSTIPATION = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_powerful_constipation" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCRAGEOVERACLOGGEDCOMMODE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_rage_over_a_clogged_commode" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCREDWEEWEE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_red_weewee" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCSMELLYMETHANE = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_smelly_methane" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCSYMPHONYOFSTUPIDITY = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_symphony_of_stupidity" ) );
    public static final RegistryKey<Item> ITEMKEY_DISCTOILETWATER = RegistryKey.of( RegistryKeys.ITEM, Identifier.of( "gcmod", "music_disc_toilet_water" ) );

    public static final SoundEvent FART_SOUND = SoundEvent.of( Identifier.of( "gcmod", "fart" ) );
    public static final SoundEvent POO_BLOCK_SOUND = SoundEvent.of( BLOCKKEY_POO.getValue() );
    public static final SoundEvent NOAH_SOUND = SoundEvent.of( Identifier.of( "gcmod", "meme" ) );
    public static final SoundEvent MEME_BLOCK_SOUND = SoundEvent.of( BLOCKKEY_MEME.getValue() );
    public static final SoundEvent CENTRIFUGE_SOUND = SoundEvent.of( Identifier.of( "gcmod", "centrifuge" ) );
    public static final SoundEvent POO_CANNON_SOUND = SoundEvent.of( Identifier.of( "gcmod", "poo_cannon" ) );


    public static final RegistryKey<JukeboxSong> SONG_BIGWILLIES = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "big_willies" ) );
    public static final RegistryKey<JukeboxSong> SONG_FOTCC = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "fotcc" ) );
    public static final RegistryKey<JukeboxSong> SONG_GODLYPISS = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "godly_piss" ) );
    public static final RegistryKey<JukeboxSong> SONG_MASSIVECRAP = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "massive_crap" ) );
    public static final RegistryKey<JukeboxSong> SONG_PEASIZEDANUS = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "pea_sized_anus" ) );
    public static final RegistryKey<JukeboxSong> SONG_POOEYLOO = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "pooey_loo" ) );
    public static final RegistryKey<JukeboxSong> SONG_POWERFULCONSTIPATION = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "powerful_constipation" ) );
    public static final RegistryKey<JukeboxSong> SONG_REDWEEWEE = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "red_weewee" ) );
    public static final RegistryKey<JukeboxSong> SONG_ROACC = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "roacc" ) );
    public static final RegistryKey<JukeboxSong> SONG_SMELLYMETHANE = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "smelly_methane" ) );
    public static final RegistryKey<JukeboxSong> SONG_SOS = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "sos" ) );
    public static final RegistryKey<JukeboxSong> SONG_TOILETWATER = RegistryKey.of( RegistryKeys.JUKEBOX_SONG, Identifier.of( "gcmod", "toilet_water" ) );

    private static final BlockSoundGroup POO_BLOCK_SOUNDS = new BlockSoundGroup( 1, 1, POO_BLOCK_SOUND, POO_BLOCK_SOUND, POO_BLOCK_SOUND, POO_BLOCK_SOUND, POO_BLOCK_SOUND );
    private static final BlockSoundGroup MEME_BLOCK_SOUNDS = new BlockSoundGroup( 1, 1, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND, MEME_BLOCK_SOUND );
    private static final BlockSoundGroup NOAH_BLOCK_SOUNDS = new BlockSoundGroup( 1, 1,
            NOAH_SOUND, BlockSoundGroup.STONE.getStepSound(), BlockSoundGroup.STONE.getPlaceSound(), BlockSoundGroup.STONE.getHitSound(), NOAH_SOUND
    );

    public static final Block POO_BLOCK = new PooBlock( AbstractBlock.Settings.create().ticksRandomly().sounds( POO_BLOCK_SOUNDS ).strength( 1 ).mapColor( MapColor.BROWN ).registryKey( BLOCKKEY_POO ) );
    public static final Block FERMENTED_POO_BLOCK = new Block( AbstractBlock.Settings.create().sounds( POO_BLOCK_SOUNDS ).strength( .9f ).mapColor( MapColor.TERRACOTTA_BROWN ).registryKey( BLOCKKEY_FERMENTEDPOO ) );
    public static final Block ROPE = new RopeBlock( AbstractBlock.Settings.create().sounds( BlockSoundGroup.CHERRY_WOOD ).strength( .5f ).nonOpaque().registryKey( BLOCKKEY_ROPE ) );
    public static final Block CENTRIFUGE = new CentrifugeBlock( AbstractBlock.Settings.create().sounds( BlockSoundGroup.METAL ).strength( 2 ).nonOpaque().registryKey( BLOCKKEY_CENTRIFUGE ) );

    public static final Block SULPHUR_ORE = new ExperienceDroppingBlock( UniformIntProvider.create( 1, 3 ), AbstractBlock.Settings.create().sounds( BlockSoundGroup.STONE ).requiresTool().strength( 3 ).mapColor( MapColor.STONE_GRAY ).registryKey( BLOCKKEY_SULPHURORE ) );
    public static final Block NOAH_SULPHUR_ORE = new ExperienceDroppingBlock( UniformIntProvider.create( 250, 400 ), AbstractBlock.Settings.create().sounds( NOAH_BLOCK_SOUNDS ).requiresTool().strength( 20 ).mapColor( MapColor.BLACK ).registryKey( BLOCKKEY_NOAHSULPHURORE ) );
    public static final Block MEME_BLOCK = new Block( AbstractBlock.Settings.create().sounds( MEME_BLOCK_SOUNDS ).requiresTool().strength( 1 ).slipperiness( 1.3f ).mapColor( MapColor.WHITE ).registryKey( BLOCKKEY_MEME ) );

    public static final Block AIRSTRIKE_TNT = new ExplosiveBlock( ExplosiveBlock.Type.AIRSTRIKE, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.YELLOW ).registryKey( BLOCKKEY_AIRSTRIKETNT ) );
    public static final Block BLAST_TNT = new ExplosiveBlock( ExplosiveBlock.Type.BLAST, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.RED ).registryKey( BLOCKKEY_BLASTTNT ) );
    public static final Block CONSTRUCTIVE_TNT = new ExplosiveBlock( ExplosiveBlock.Type.CONSTRUCTIVE, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.GREEN ).registryKey( BLOCKKEY_CONSTRUCTIVETNT ) );
    public static final Block DIG_TNT = new ExplosiveBlock( ExplosiveBlock.Type.DIG, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.CYAN ).registryKey( BLOCKKEY_DIGTNT ) );

    public static final Block WIRELESS_TORCH = new WirelessTorchBlock( AbstractBlock.Settings.create()
            .sounds( BlockSoundGroup.METAL ).noCollision().breakInstantly().luminance( Blocks.createLightLevelFromLitBlockState( 14 ) ).registryKey( BLOCKKEY_WIRELESSTORCH ) );

    public static final Block WIRELESS_TORCH_WALL = new WallWirelessTorchBlock( AbstractBlock.Settings.create()
            .sounds( BlockSoundGroup.METAL ).noCollision().breakInstantly().luminance( Blocks.createLightLevelFromLitBlockState( 14 ) ).registryKey( BLOCKKEY_WIRELESSWALLTORCH ) );

    public static final Block COMPOST = new FarmlandBlock( AbstractBlock.Settings.create()
            .sounds( BlockSoundGroup.GRAVEL ).mapColor( MapColor.DIRT_BROWN ).strength( 0.6f ).ticksRandomly().blockVision(Blocks::always).suffocates(Blocks::always).registryKey( BLOCKKEY_COMPOST ) );

    public static final FoodComponent FOOD_POO_PROTEIN = new FoodComponent.Builder().alwaysEdible().saturationModifier( 5f ).nutrition( 0 ).build();

    public static final Item ROPE_ITEM = new BlockItem( ROPE, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_ROPE ) );
    public static final Item POO = new Item( new Item.Settings().registryKey( ITEMKEY_POO ) );
    public static final Item FERMENTED_POO = new Item( new Item.Settings().registryKey( ITEMKEY_FERMENTEDPOO ) );
    public static final Item POO_BRICK = new PooBrickItem( new Item.Settings().registryKey( ITEMKEY_POOBRICK ) );
    public static final Item POO_CANNON = new PooCannonItem( new Item.Settings().maxDamage( 32 ).registryKey( ITEMKEY_POOCANNON ) );
    public static final Item POO_POWDER = new Item( new Item.Settings().registryKey( ITEMKEY_POOPOWDER ) );
    public static final Item POO_PROTEIN = new PooProteinItem( new Item.Settings().food( FOOD_POO_PROTEIN ).registryKey( ITEMKEY_POOPROTEIN ) );
    public static final Item FERTILISER = new FertiliserItem( new Item.Settings().registryKey( ITEMKEY_FERTILISER ) );
    public static final Item BLACK_POWDER = new Item( new Item.Settings().registryKey( ITEMKEY_BLACKPOWDER ) );
    public static final Item SULPHUR = new Item( new Item.Settings().registryKey( ITEMKEY_SULPHUR ) );
    public static final Item NOAH_SULPHUR = new Item( new Item.Settings().registryKey( ITEMKEY_NOAHSULPHUR ) );
    public static final Item MEME_ESSENCE = new Item( new Item.Settings().registryKey( ITEMKEY_MEMEESSENCE ) );
    public static final Item CALIBRATOR = new CalibratorItem( new Item.Settings().maxCount( 1 ).registryKey( ITEMKEY_CALIBRATOR ) );
    public static final Item DISC_BIG_WILLIES = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_BIGWILLIES ).registryKey( ITEMKEY_DISCBIGWILLIES ) );
    public static final Item DISC_FLIGHT_OF_THE_CHINESE_COMMUTER = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_FOTCC ).registryKey( ITEMKEY_DISCFOTCC ) );
    public static final Item DISC_GODLY_PISS = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_GODLYPISS ).registryKey( ITEMKEY_DISCGODLYPISS ) );
    public static final Item DISC_MASSIVE_CRAP = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_MASSIVECRAP ).registryKey( ITEMKEY_DISCMASSIVECRAP ) );
    public static final Item DISC_PEA_SIZED_ANUS = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_PEASIZEDANUS ).registryKey( ITEMKEY_DISCPEASIZEDANUS ) );
    public static final Item DISC_POOEY_LOO = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_POOEYLOO ).registryKey( ITEMKEY_DISCPOOEYLOO ) );
    public static final Item DISC_POWERFUL_CONSTIPATION = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_POWERFULCONSTIPATION ).registryKey( ITEMKEY_DISCPOWERFULCONSTIPATION ) );
    public static final Item DISC_RED_WEEWEE = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_REDWEEWEE ).registryKey( ITEMKEY_DISCREDWEEWEE ) );
    public static final Item DISC_RAGE_OVER_A_CLOGGED_COMMODE = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_ROACC ).registryKey( ITEMKEY_DISCRAGEOVERACLOGGEDCOMMODE ) );
    public static final Item DISC_SMELLY_METHANE = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_SMELLYMETHANE ).registryKey( ITEMKEY_DISCSMELLYMETHANE ) );
    public static final Item DISC_SYMPHONY_OF_STUPIDITY = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_SOS ).registryKey( ITEMKEY_DISCSYMPHONYOFSTUPIDITY ) );
    public static final Item DISC_TOILET_WATER = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_TOILETWATER ).registryKey( ITEMKEY_DISCTOILETWATER ) );

    public static final RegistryKey<EntityType<?>> ENTKEY_POO = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "poo" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_POOBRICK = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "poo_brick" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_EXPLOSIVEPOOBRICK = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "explosive_poo_brick" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_BOMB = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "explosive_bomb" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_EXPLOSIVE = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "explosive" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_AIRSTRIKEEXPLOSIVE = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "airstrike_explosive" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_CONSTRUCTIVEEXPLOSIVE = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "constructive_explosive" ) );
    public static final RegistryKey<EntityType<?>> ENTKEY_DIGEXPLOSIVE = RegistryKey.of( RegistryKeys.ENTITY_TYPE, Identifier.of( "gcmod", "dig_explosive" ) );

    public static final EntityType<PooEntity> POO_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_POO,
            EntityType.Builder.create( PooEntity::new, SpawnGroup.MISC ).dimensions( .75f, .3125f ).build( ENTKEY_POO )
    );

    public static final EntityType<PooBrickEntity> POO_BRICK_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_POOBRICK,
            EntityType.Builder.create( PooBrickEntity::new, SpawnGroup.MISC ).dimensions( .4f, .25f ).build( ENTKEY_POOBRICK )
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_POO_BRICK_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_EXPLOSIVEPOOBRICK,
            EntityType.Builder.create( PooBrickEntity::createExplosive, SpawnGroup.MISC ).dimensions( .4f, .25f ).build( ENTKEY_EXPLOSIVEPOOBRICK )
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_BOMB_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_BOMB,
            EntityType.Builder.create( PooBrickEntity::createExplosive, SpawnGroup.MISC ).dimensions( .5f, .5f ).build( ENTKEY_BOMB )
    );

    public static final EntityType<ExplosiveEntity> EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_EXPLOSIVE,
            EntityType.Builder.create( ExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build( ENTKEY_EXPLOSIVE )
    );

    public static final EntityType<AirstrikeExplosiveEntity> AIRSTRIKE_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_AIRSTRIKEEXPLOSIVE,
            EntityType.Builder.create( AirstrikeExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build( ENTKEY_AIRSTRIKEEXPLOSIVE )
    );

    public static final EntityType<ConstructiveExplosiveEntity> CONSTRUCTIVE_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_CONSTRUCTIVEEXPLOSIVE,
            EntityType.Builder.create( ConstructiveExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build( ENTKEY_CONSTRUCTIVEEXPLOSIVE )
    );

    public static final EntityType<DigExplosiveEntity> DIG_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            ENTKEY_DIGEXPLOSIVE,
            EntityType.Builder.create( DigExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build( ENTKEY_DIGEXPLOSIVE )
    );

    public static final BlockEntityType<CentrifugeEntity> CENTRIFUGE_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of( "gcmod", "centrifuge" ),
            FabricBlockEntityTypeBuilder.create( CentrifugeEntity::new, CENTRIFUGE ).build()
    );

    public static final BlockEntityType<WirelessTorchEntity> WIRELESS_TORCH_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of( "gcmod", "wireless_torch" ),
            FabricBlockEntityTypeBuilder.create( WirelessTorchEntity::new, WIRELESS_TORCH ).build()
    );

    public static BlockEntityType<ExplosiveBlockEntity> AIRSTRIKE_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> BLAST_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> CONSTRUCTIVE_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> DIG_TNT_BLOCK_ENTITY;

    public static ComponentType<EnchantmentValueEffect> EFFECT_CONSTIPATION = Registry.register(
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.of( "gcmod", "constipation" ),
            ComponentType.<EnchantmentValueEffect>builder().codec( EnchantmentValueEffect.CODEC ).build() );

    public static ComponentType<EnchantmentValueEffect> EFFECT_POOER = Registry.register(
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.of( "gcmod", "pooer" ),
            ComponentType.<EnchantmentValueEffect>builder().codec( EnchantmentValueEffect.CODEC ).build() );

    public static final TagKey<Enchantment> ENCH_DIARRHOEA = TagKey.of( RegistryKeys.ENCHANTMENT, Identifier.of( "gcmod", "induces_diarrhoea" ) );
    public static final TagKey<Enchantment> ENCH_LAXATIVES = TagKey.of( RegistryKeys.ENCHANTMENT, Identifier.of( "gcmod", "laxatives" ) );

    public static final ComponentType<NbtComponent> DATA_EXPLOSIVE_INFO = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of( "gcmod", "explosive_info" ),
            ComponentType.<NbtComponent>builder().codec( NbtComponent.CODEC ).build()
    );

    public static final ComponentType<NbtComponent> DATA_TORCH_NETWORK = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of( "gcmod", "torch_network" ),
            ComponentType.<NbtComponent>builder().codec( NbtComponent.CODEC ).build()
    );

    public static RecipeSerializer<FertiliserRecipe> FERTILIZER_RECIPE_SERIALIZER;
    public static RecipeSerializer<ExplosiveRecipe> EXPLOSIVE_RECIPE_SERIALIZER;

    public static ScreenHandlerType<CentrifugeScreenHandler> CENTRIFUGE_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of( "gcmod", "centrifuge" ),
            new ScreenHandlerType<>( CentrifugeScreenHandler::new, FeatureSet.empty() )
    );

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon( () -> new ItemStack( POO_BLOCK ) )
            .displayName( Text.translatable( "itemGroup.gcmod" ) )
            .entries( ( context, entries ) -> {
                entries.add( POO_BLOCK );
                entries.add( FERMENTED_POO_BLOCK );
                entries.add( ROPE );
                entries.add( CENTRIFUGE );
                entries.add( COMPOST );
                entries.add( SULPHUR_ORE );
                entries.add( NOAH_SULPHUR_ORE );
                entries.add( MEME_BLOCK );
                entries.add( AIRSTRIKE_TNT );
                entries.add( BLAST_TNT );
                entries.add( CONSTRUCTIVE_TNT );
                entries.add( DIG_TNT );
                entries.add( WIRELESS_TORCH );

                entries.add( POO );
                entries.add( FERMENTED_POO );
                entries.add( POO_BRICK );
                entries.add( POO_CANNON );
                entries.add( POO_POWDER );
                entries.add( POO_PROTEIN );
                entries.add( FERTILISER );
                entries.add( BLACK_POWDER );
                entries.add( SULPHUR );
                entries.add( NOAH_SULPHUR );
                entries.add( MEME_ESSENCE );
                entries.add( CALIBRATOR );
                entries.add( DISC_BIG_WILLIES );
                entries.add( DISC_FLIGHT_OF_THE_CHINESE_COMMUTER );
                entries.add( DISC_GODLY_PISS );
                entries.add( DISC_MASSIVE_CRAP );
                entries.add( DISC_PEA_SIZED_ANUS );
                entries.add( DISC_POOEY_LOO );
                entries.add( DISC_POWERFUL_CONSTIPATION );
                entries.add( DISC_RED_WEEWEE );
                entries.add( DISC_RAGE_OVER_A_CLOGGED_COMMODE );
                entries.add( DISC_SMELLY_METHANE );
                entries.add( DISC_SYMPHONY_OF_STUPIDITY );
                entries.add( DISC_TOILET_WATER );
            } )
            .build();

    private static final RegistryKey<PlacedFeature> SULPHUR_ORE_PLACEMENT = RegistryKey.of( RegistryKeys.PLACED_FEATURE, Identifier.of( "gcmod", "sulphur_ore" ) );
    private static final RegistryKey<PlacedFeature> NOAH_ORE_PLACEMENT = RegistryKey.of( RegistryKeys.PLACED_FEATURE, Identifier.of( "gcmod", "noah_sulphur_ore" ) );

    public static final SoundEvent MUSIC_BIGWILLIES = SoundEvent.of( Identifier.of( "gcmod", "music_disc.big_willies" ) );
    public static final SoundEvent MUSIC_FOTCC = SoundEvent.of( Identifier.of( "gcmod", "music_disc.flight_of_the_chinese_commuter" ) );
    public static final SoundEvent MUSIC_GODLYPISS = SoundEvent.of( Identifier.of( "gcmod", "music_disc.godly_piss" ) );
    public static final SoundEvent MUSIC_MASSIVECRAP = SoundEvent.of( Identifier.of( "gcmod", "music_disc.massive_crap" ) );
    public static final SoundEvent MUSIC_PEASIZEDANUS = SoundEvent.of( Identifier.of( "gcmod", "music_disc.pea_sized_anus" ) );
    public static final SoundEvent MUSIC_POOEYLOO = SoundEvent.of( Identifier.of( "gcmod", "music_disc.pooey_loo" ) );
    public static final SoundEvent MUSIC_POWERFULCONSTIPATION = SoundEvent.of( Identifier.of( "gcmod", "music_disc.powerful_constipation" ) );
    public static final SoundEvent MUSIC_ROACC = SoundEvent.of( Identifier.of( "gcmod", "music_disc.rage_over_a_clogged_commode" ) );
    public static final SoundEvent MUSIC_REDWEEWEE = SoundEvent.of( Identifier.of( "gcmod", "music_disc.red_weewee" ) );
    public static final SoundEvent MUSIC_SMELLYMETHANE = SoundEvent.of( Identifier.of( "gcmod", "music_disc.smelly_methane" ) );
    public static final SoundEvent MUSIC_SOS = SoundEvent.of( Identifier.of( "gcmod", "music_disc.symphony_of_stupidity" ) );
    public static final SoundEvent MUSIC_TOILETWATER = SoundEvent.of( Identifier.of( "gcmod", "music_disc.toilet_water" ) );

    public static final SimpleParticleType PARTICLE_POO_SPLAT = FabricParticleTypes.simple();

    @Override
    public void onInitialize()
    {
        Registry.register( Registries.SOUND_EVENT, FART_SOUND.id(), FART_SOUND );
        Registry.register( Registries.SOUND_EVENT, POO_BLOCK_SOUND.id(), POO_BLOCK_SOUND );
        Registry.register( Registries.SOUND_EVENT, MEME_BLOCK_SOUND.id(), MEME_BLOCK_SOUND );
        Registry.register( Registries.SOUND_EVENT, CENTRIFUGE_SOUND.id(), CENTRIFUGE_SOUND );
        Registry.register( Registries.SOUND_EVENT, POO_CANNON_SOUND.id(), POO_CANNON_SOUND );
        Registry.register( Registries.SOUND_EVENT, MUSIC_BIGWILLIES.id(), MUSIC_BIGWILLIES );
        Registry.register( Registries.SOUND_EVENT, MUSIC_FOTCC.id(), MUSIC_FOTCC );
        Registry.register( Registries.SOUND_EVENT, MUSIC_GODLYPISS.id(), MUSIC_GODLYPISS );
        Registry.register( Registries.SOUND_EVENT, MUSIC_MASSIVECRAP.id(), MUSIC_MASSIVECRAP );
        Registry.register( Registries.SOUND_EVENT, MUSIC_PEASIZEDANUS.id(), MUSIC_PEASIZEDANUS );
        Registry.register( Registries.SOUND_EVENT, MUSIC_POOEYLOO.id(), MUSIC_POOEYLOO );
        Registry.register( Registries.SOUND_EVENT, MUSIC_POWERFULCONSTIPATION.id(), MUSIC_POWERFULCONSTIPATION );
        Registry.register( Registries.SOUND_EVENT, MUSIC_ROACC.id(), MUSIC_ROACC );
        Registry.register( Registries.SOUND_EVENT, MUSIC_REDWEEWEE.id(), MUSIC_REDWEEWEE );
        Registry.register( Registries.SOUND_EVENT, MUSIC_SMELLYMETHANE.id(), MUSIC_SMELLYMETHANE );
        Registry.register( Registries.SOUND_EVENT, MUSIC_SOS.id(), MUSIC_SOS );
        Registry.register( Registries.SOUND_EVENT, MUSIC_TOILETWATER.id(), MUSIC_TOILETWATER );

        Registry.register( Registries.PARTICLE_TYPE, Identifier.of( "gcmod", "poo_splat" ), PARTICLE_POO_SPLAT );

        Registry.register( Registries.ITEM_GROUP, Identifier.of( "gcmod", "itemgroup" ), ITEM_GROUP );

        Registry.register( Registries.BLOCK, BLOCKKEY_POO, POO_BLOCK );
        Registry.register( Registries.BLOCK, BLOCKKEY_FERMENTEDPOO, FERMENTED_POO_BLOCK );
        Registry.register( Registries.BLOCK, BLOCKKEY_ROPE, ROPE );
        Registry.register( Registries.BLOCK, BLOCKKEY_CENTRIFUGE, CENTRIFUGE );
        Registry.register( Registries.BLOCK, BLOCKKEY_SULPHURORE, SULPHUR_ORE );
        Registry.register( Registries.BLOCK, BLOCKKEY_NOAHSULPHURORE, NOAH_SULPHUR_ORE );
        Registry.register( Registries.BLOCK, BLOCKKEY_MEME, MEME_BLOCK );
        Registry.register( Registries.BLOCK, BLOCKKEY_AIRSTRIKETNT, AIRSTRIKE_TNT );
        Registry.register( Registries.BLOCK, BLOCKKEY_BLASTTNT, BLAST_TNT );
        Registry.register( Registries.BLOCK, BLOCKKEY_CONSTRUCTIVETNT, CONSTRUCTIVE_TNT );
        Registry.register( Registries.BLOCK, BLOCKKEY_DIGTNT, DIG_TNT );
        Registry.register( Registries.BLOCK, BLOCKKEY_WIRELESSTORCH, WIRELESS_TORCH );
        Registry.register( Registries.BLOCK, BLOCKKEY_WIRELESSWALLTORCH, WIRELESS_TORCH_WALL );
        Registry.register( Registries.BLOCK, BLOCKKEY_COMPOST, COMPOST );

        Registry.register( Registries.ITEM, ITEMKEY_POOBLOCK, new BlockItem( POO_BLOCK, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_POOBLOCK ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_FERMENTEDPOOBLOCK, new BlockItem( FERMENTED_POO_BLOCK, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_FERMENTEDPOOBLOCK ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_ROPE, ROPE_ITEM );
        Registry.register( Registries.ITEM, ITEMKEY_CENTRIFUGE, new BlockItem( CENTRIFUGE, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_CENTRIFUGE ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_SULPHURORE, new BlockItem( SULPHUR_ORE, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_SULPHURORE ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_NOAHSULPHURORE, new BlockItem( NOAH_SULPHUR_ORE, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_NOAHSULPHURORE ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_MEMEBLOCK, new BlockItem( MEME_BLOCK, new Item.Settings().maxCount( 69 ).useBlockPrefixedTranslationKey().registryKey( ITEMKEY_MEMEBLOCK ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_AIRSTRIKETNT, new ExplosiveBlockItem( AIRSTRIKE_TNT, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_AIRSTRIKETNT ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_BLASTTNT, new ExplosiveBlockItem( BLAST_TNT, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_BLASTTNT ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_CONSTRUCTIVETNT, new ExplosiveBlockItem( CONSTRUCTIVE_TNT, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_CONSTRUCTIVETNT ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_DIGTNT, new ExplosiveBlockItem( DIG_TNT, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_DIGTNT ) ) );
        Registry.register( Registries.ITEM, ITEMKEY_WIRELESSTORCH,
                new VerticallyAttachableBlockItem( WIRELESS_TORCH, WIRELESS_TORCH_WALL, Direction.DOWN, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_WIRELESSTORCH ) )
        );
        Registry.register( Registries.ITEM, ITEMKEY_COMPOST, new BlockItem( COMPOST, new Item.Settings().useBlockPrefixedTranslationKey().registryKey( ITEMKEY_COMPOST ) ) );
        
        Registry.register( Registries.ITEM, ITEMKEY_POO, POO );
        Registry.register( Registries.ITEM, ITEMKEY_FERMENTEDPOO, FERMENTED_POO );
        Registry.register( Registries.ITEM, ITEMKEY_POOBRICK, POO_BRICK );
        Registry.register( Registries.ITEM, ITEMKEY_POOCANNON, POO_CANNON );
        Registry.register( Registries.ITEM, ITEMKEY_POOPOWDER, POO_POWDER );
        Registry.register( Registries.ITEM, ITEMKEY_POOPROTEIN, POO_PROTEIN );
        Registry.register( Registries.ITEM, ITEMKEY_FERTILISER, FERTILISER );
        Registry.register( Registries.ITEM, ITEMKEY_BLACKPOWDER, BLACK_POWDER );
        Registry.register( Registries.ITEM, ITEMKEY_SULPHUR, SULPHUR );
        Registry.register( Registries.ITEM, ITEMKEY_NOAHSULPHUR, NOAH_SULPHUR );
        Registry.register( Registries.ITEM, ITEMKEY_MEMEESSENCE, MEME_ESSENCE );
        Registry.register( Registries.ITEM, ITEMKEY_CALIBRATOR, CALIBRATOR );
        Registry.register( Registries.ITEM, ITEMKEY_DISCBIGWILLIES, DISC_BIG_WILLIES );
        Registry.register( Registries.ITEM, ITEMKEY_DISCFOTCC, DISC_FLIGHT_OF_THE_CHINESE_COMMUTER );
        Registry.register( Registries.ITEM, ITEMKEY_DISCGODLYPISS, DISC_GODLY_PISS );
        Registry.register( Registries.ITEM, ITEMKEY_DISCMASSIVECRAP, DISC_MASSIVE_CRAP );
        Registry.register( Registries.ITEM, ITEMKEY_DISCPEASIZEDANUS, DISC_PEA_SIZED_ANUS );
        Registry.register( Registries.ITEM, ITEMKEY_DISCPOOEYLOO, DISC_POOEY_LOO );
        Registry.register( Registries.ITEM, ITEMKEY_DISCPOWERFULCONSTIPATION, DISC_POWERFUL_CONSTIPATION );
        Registry.register( Registries.ITEM, ITEMKEY_DISCRAGEOVERACLOGGEDCOMMODE, DISC_RAGE_OVER_A_CLOGGED_COMMODE );
        Registry.register( Registries.ITEM, ITEMKEY_DISCREDWEEWEE, DISC_RED_WEEWEE );
        Registry.register( Registries.ITEM, ITEMKEY_DISCSMELLYMETHANE, DISC_SMELLY_METHANE );
        Registry.register( Registries.ITEM, ITEMKEY_DISCSYMPHONYOFSTUPIDITY, DISC_SYMPHONY_OF_STUPIDITY );
        Registry.register( Registries.ITEM, ITEMKEY_DISCTOILETWATER, DISC_TOILET_WATER );

        AIRSTRIKE_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "airstrike_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( AIRSTRIKE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        BLAST_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "blast_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( BLAST_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        CONSTRUCTIVE_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "constructive_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( CONSTRUCTIVE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        DIG_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "dig_tnt" ),
                FabricBlockEntityTypeBuilder.create( ( p, s ) -> new ExplosiveBlockEntity( DIG_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        CENTRIFUGE_ENTITY.addSupportedBlock( CENTRIFUGE );
        WIRELESS_TORCH_ENTITY.addSupportedBlock( WIRELESS_TORCH );
        WIRELESS_TORCH_ENTITY.addSupportedBlock( WIRELESS_TORCH_WALL );
        AIRSTRIKE_TNT_BLOCK_ENTITY.addSupportedBlock( AIRSTRIKE_TNT );
        BLAST_TNT_BLOCK_ENTITY.addSupportedBlock( BLAST_TNT );
        CONSTRUCTIVE_TNT_BLOCK_ENTITY.addSupportedBlock( CONSTRUCTIVE_TNT );
        DIG_TNT_BLOCK_ENTITY.addSupportedBlock( DIG_TNT );

        Registry.register( Registries.RECIPE_TYPE, Identifier.of( "gcmod", "crafting_fertilizer" ), new RecipeType<FertiliserRecipe>()
        {
            @Override
            public String toString()
            {
                return "<FERTILIZER_RECIPE>";
            }
        } );

        Registry.register( Registries.RECIPE_TYPE, Identifier.of( "gcmod", "crafting_explosive" ), new RecipeType<ExplosiveRecipe>()
        {
            @Override
            public String toString()
            {
                return "<EXPLOSIVE_RECIPE>";
            }
        } );



        FERTILIZER_RECIPE_SERIALIZER = Registry.register( Registries.RECIPE_SERIALIZER, Identifier.of( "gcmod", "crafting_fertiliser" ), new SpecialCraftingRecipe.SpecialRecipeSerializer<>( FertiliserRecipe::new ) );
        EXPLOSIVE_RECIPE_SERIALIZER = Registry.register( Registries.RECIPE_SERIALIZER, Identifier.of( "gcmod", "crafting_explosive" ), new SpecialCraftingRecipe.SpecialRecipeSerializer<>( ExplosiveRecipe::new ) );

        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_ORE_PLACEMENT );
        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, NOAH_ORE_PLACEMENT );

        PayloadTypeRegistry.playS2C().register( PooSplatPayload.ID, PooSplatPayload.CODEC );
    }
}