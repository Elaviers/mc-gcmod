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
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.PacketType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
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

    public static final Identifier MEME_BOCK_ID = Identifier.of( "gcmod", "meme_block" );
    public static final Identifier POO_BLOCK_ID = Identifier.of( "gcmod", "poo_block" );
    public static final SoundEvent FART_SOUND = SoundEvent.of( Identifier.of( "gcmod", "fart" ) );
    public static final SoundEvent POO_BLOCK_SOUND = SoundEvent.of( POO_BLOCK_ID );
    public static final SoundEvent NOAH_SOUND = SoundEvent.of( Identifier.of( "gcmod", "meme" ) );
    public static final SoundEvent MEME_BLOCK_SOUND = SoundEvent.of( MEME_BOCK_ID );
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

    public static final Block POO_BLOCK = new PooBlock( AbstractBlock.Settings.create().ticksRandomly().sounds( POO_BLOCK_SOUNDS ).strength( 1 ).mapColor( MapColor.BROWN ) );
    public static final Block FERMENTED_POO_BLOCK = new Block( AbstractBlock.Settings.create().sounds( POO_BLOCK_SOUNDS ).strength( .9f ).mapColor( MapColor.TERRACOTTA_BROWN ) );
    public static final Block ROPE = new RopeBlock( AbstractBlock.Settings.create().sounds( BlockSoundGroup.CHERRY_WOOD ).strength( .5f ).nonOpaque() );
    public static final Block CENTRIFUGE = new CentrifugeBlock( AbstractBlock.Settings.create().sounds( BlockSoundGroup.METAL ).strength( 2 ).nonOpaque() );

    public static final Block SULPHUR_ORE = new ExperienceDroppingBlock( UniformIntProvider.create( 1, 3 ), AbstractBlock.Settings.create().sounds( BlockSoundGroup.STONE ).requiresTool().strength( 3 ).mapColor( MapColor.STONE_GRAY ) );
    public static final Block NOAH_SULPHUR_ORE = new ExperienceDroppingBlock( UniformIntProvider.create( 250, 400 ), AbstractBlock.Settings.create().sounds( NOAH_BLOCK_SOUNDS ).requiresTool().strength( 20 ).mapColor( MapColor.BLACK ) );
    public static final Block MEME_BLOCK = new Block( AbstractBlock.Settings.create().sounds( MEME_BLOCK_SOUNDS ).requiresTool().strength( 1 ).slipperiness( 1.3f ).mapColor( MapColor.WHITE ) );

    public static final Block AIRSTRIKE_TNT = new ExplosiveBlock( ExplosiveBlock.Type.AIRSTRIKE, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.YELLOW ) );
    public static final Block BLAST_TNT = new ExplosiveBlock( ExplosiveBlock.Type.BLAST, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.RED ) );
    public static final Block CONSTRUCTIVE_TNT = new ExplosiveBlock( ExplosiveBlock.Type.CONSTRUCTIVE, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.GREEN ) );
    public static final Block DIG_TNT = new ExplosiveBlock( ExplosiveBlock.Type.DIG, AbstractBlock.Settings.create().sounds( BlockSoundGroup.WOOD ).mapColor( MapColor.CYAN ) );

    public static final Block WIRELESS_TORCH = new WirelessTorchBlock( AbstractBlock.Settings.create()
            .sounds( BlockSoundGroup.METAL ).noCollision().breakInstantly().luminance( Blocks.createLightLevelFromLitBlockState( 14 ) ) );

    public static final Block WIRELESS_TORCH_WALL = new WallWirelessTorchBlock( AbstractBlock.Settings.create()
            .sounds( BlockSoundGroup.METAL ).noCollision().breakInstantly().luminance( Blocks.createLightLevelFromLitBlockState( 14 ) ) );

    public static final Block COMPOST = new FarmlandBlock( AbstractBlock.Settings.create()
            .sounds( BlockSoundGroup.GRAVEL ).mapColor( MapColor.DIRT_BROWN ).strength( 0.6f ).ticksRandomly().blockVision(Blocks::always).suffocates(Blocks::always) );

    public static final FoodComponent FOOD_POO_PROTEIN = new FoodComponent.Builder().alwaysEdible().saturationModifier( 5 ).nutrition( 0 ).snack().build();

    public static final Item ROPE_ITEM = new BlockItem( ROPE, new Item.Settings() );
    public static final Item POO = new Item( new Item.Settings() );
    public static final Item FERMENTED_POO = new Item( new Item.Settings() );
    public static final Item POO_BRICK = new PooBrickItem( new Item.Settings() );
    public static final Item POO_CANNON = new PooCannonItem( new Item.Settings().maxDamage( 32 ) );
    public static final Item POO_POWDER = new Item( new Item.Settings() );
    public static final Item POO_PROTEIN = new PooProteinItem( new Item.Settings().food( FOOD_POO_PROTEIN ) );
    public static final Item FERTILISER = new FertiliserItem( new Item.Settings() );
    public static final Item BLACK_POWDER = new Item( new Item.Settings() );
    public static final Item SULPHUR = new Item( new Item.Settings() );
    public static final Item NOAH_SULPHUR = new Item( new Item.Settings() );
    public static final Item MEME_ESSENCE = new Item( new Item.Settings() );
    public static final Item CALIBRATOR = new CalibratorItem( new Item.Settings().maxCount( 1 ) );
    public static final Item DISC_BIG_WILLIES = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_BIGWILLIES ) );
    public static final Item DISC_FLIGHT_OF_THE_CHINESE_COMMUTER = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_FOTCC ) );
    public static final Item DISC_GODLY_PISS = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_GODLYPISS ) );
    public static final Item DISC_MASSIVE_CRAP = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_MASSIVECRAP ) );
    public static final Item DISC_PEA_SIZED_ANUS = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_PEASIZEDANUS ) );
    public static final Item DISC_POOEY_LOO = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_POOEYLOO ) );
    public static final Item DISC_POWERFUL_CONSTIPATION = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_POWERFULCONSTIPATION ) );
    public static final Item DISC_RED_WEEWEE = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_REDWEEWEE ) );
    public static final Item DISC_RAGE_OVER_A_CLOGGED_COMMODE = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_ROACC ) );
    public static final Item DISC_SMELLY_METHANE = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_SMELLYMETHANE ) );
    public static final Item DISC_SYMPHONY_OF_STUPIDITY = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_SOS ) );
    public static final Item DISC_TOILET_WATER = new Item( new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ).jukeboxPlayable( SONG_TOILETWATER ) );

    public static final EntityType<PooEntity> POO_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "poo" ),
            EntityType.Builder.create( PooEntity::new, SpawnGroup.MISC ).dimensions( .75f, .3125f ).build()
    );

    public static final EntityType<PooBrickEntity> POO_BRICK_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "poo_brick" ),
            EntityType.Builder.create( PooBrickEntity::new, SpawnGroup.MISC ).dimensions( .4f, .25f ).build()
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_POO_BRICK_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "explosive_poo_brick" ),
            EntityType.Builder.create( PooBrickEntity::createExplosive, SpawnGroup.MISC ).dimensions( .4f, .25f ).build()
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_BOMB_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "explosive_bomb" ),
            EntityType.Builder.create( PooBrickEntity::createExplosive, SpawnGroup.MISC ).dimensions( .5f, .5f ).build()
    );

    public static final EntityType<ExplosiveEntity> EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "explosive" ),
            EntityType.Builder.create( ExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final EntityType<AirstrikeExplosiveEntity> AIRSTRIKE_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "airstrike_explosive" ),
            EntityType.Builder.create( AirstrikeExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final EntityType<ConstructiveExplosiveEntity> CONSTRUCTIVE_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "constructive_explosive" ),
            EntityType.Builder.create( ConstructiveExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final EntityType<DigExplosiveEntity> DIG_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of( "gcmod", "dig_explosive" ),
            EntityType.Builder.create( DigExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final BlockEntityType<CentrifugeEntity> CENTRIFUGE_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of( "gcmod", "centrifuge" ),
            BlockEntityType.Builder.create( CentrifugeEntity::new, CENTRIFUGE ).build()
    );

    public static final BlockEntityType<WirelessTorchEntity> WIRELESS_TORCH_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of( "gcmod", "wireless_torch" ),
            BlockEntityType.Builder.create( WirelessTorchEntity::new, WIRELESS_TORCH ).build()
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
        Registry.register( Registries.SOUND_EVENT, FART_SOUND.getId(), FART_SOUND );
        Registry.register( Registries.SOUND_EVENT, POO_BLOCK_ID, POO_BLOCK_SOUND );
        Registry.register( Registries.SOUND_EVENT, MEME_BOCK_ID, MEME_BLOCK_SOUND );
        Registry.register( Registries.SOUND_EVENT, CENTRIFUGE_SOUND.getId(), CENTRIFUGE_SOUND );
        Registry.register( Registries.SOUND_EVENT, POO_CANNON_SOUND.getId(), POO_CANNON_SOUND );
        Registry.register( Registries.SOUND_EVENT, MUSIC_BIGWILLIES.getId(), MUSIC_BIGWILLIES );
        Registry.register( Registries.SOUND_EVENT, MUSIC_FOTCC.getId(), MUSIC_FOTCC );
        Registry.register( Registries.SOUND_EVENT, MUSIC_GODLYPISS.getId(), MUSIC_GODLYPISS );
        Registry.register( Registries.SOUND_EVENT, MUSIC_MASSIVECRAP.getId(), MUSIC_MASSIVECRAP );
        Registry.register( Registries.SOUND_EVENT, MUSIC_PEASIZEDANUS.getId(), MUSIC_PEASIZEDANUS );
        Registry.register( Registries.SOUND_EVENT, MUSIC_POOEYLOO.getId(), MUSIC_POOEYLOO );
        Registry.register( Registries.SOUND_EVENT, MUSIC_POWERFULCONSTIPATION.getId(), MUSIC_POWERFULCONSTIPATION );
        Registry.register( Registries.SOUND_EVENT, MUSIC_ROACC.getId(), MUSIC_ROACC );
        Registry.register( Registries.SOUND_EVENT, MUSIC_REDWEEWEE.getId(), MUSIC_REDWEEWEE );
        Registry.register( Registries.SOUND_EVENT, MUSIC_SMELLYMETHANE.getId(), MUSIC_SMELLYMETHANE );
        Registry.register( Registries.SOUND_EVENT, MUSIC_SOS.getId(), MUSIC_SOS );
        Registry.register( Registries.SOUND_EVENT, MUSIC_TOILETWATER.getId(), MUSIC_TOILETWATER );

        Registry.register( Registries.PARTICLE_TYPE, Identifier.of( "gcmod", "poo_splat" ), PARTICLE_POO_SPLAT );

        Registry.register( Registries.ITEM_GROUP, Identifier.of( "gcmod", "itemgroup" ), ITEM_GROUP );

        Registry.register( Registries.BLOCK, POO_BLOCK_ID, POO_BLOCK );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "fermented_poo_block" ), FERMENTED_POO_BLOCK );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "rope" ), ROPE );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "centrifuge" ), CENTRIFUGE );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "sulphur_ore" ), SULPHUR_ORE );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "noah_sulphur_ore" ), NOAH_SULPHUR_ORE );
        Registry.register( Registries.BLOCK, MEME_BOCK_ID, MEME_BLOCK );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "airstrike_tnt" ), AIRSTRIKE_TNT );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "blast_tnt" ), BLAST_TNT );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "constructive_tnt" ), CONSTRUCTIVE_TNT );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "dig_tnt" ), DIG_TNT );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "wireless_torch" ), WIRELESS_TORCH );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "wireless_wall_torch" ), WIRELESS_TORCH_WALL );
        Registry.register( Registries.BLOCK, Identifier.of( "gcmod", "compost" ), COMPOST );

        Registry.register( Registries.ITEM, POO_BLOCK_ID, new BlockItem( POO_BLOCK, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "fermented_poo_block" ), new BlockItem( FERMENTED_POO_BLOCK, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "rope" ), ROPE_ITEM );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "centrifuge" ), new BlockItem( CENTRIFUGE, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "sulphur_ore" ), new BlockItem( SULPHUR_ORE, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "noah_sulphur_ore" ), new BlockItem( NOAH_SULPHUR_ORE, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "meme_block" ), new BlockItem( MEME_BLOCK, new Item.Settings().maxCount( 69 ) ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "airstrike_tnt" ), new ExplosiveBlockItem( AIRSTRIKE_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "blast_tnt" ), new ExplosiveBlockItem( BLAST_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "constructive_tnt" ), new ExplosiveBlockItem( CONSTRUCTIVE_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "dig_tnt" ), new ExplosiveBlockItem( DIG_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "wireless_torch" ),
                new VerticallyAttachableBlockItem( WIRELESS_TORCH, WIRELESS_TORCH_WALL, new Item.Settings(), Direction.DOWN )
        );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "compost" ), new BlockItem( COMPOST, new Item.Settings() ) );

        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "poo" ), POO );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "fermented_poo" ), FERMENTED_POO );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "poo_brick" ), POO_BRICK );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "poo_cannon" ), POO_CANNON );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "poo_powder" ), POO_POWDER );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "poo_protein" ), POO_PROTEIN );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "fertiliser" ), FERTILISER );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "black_powder" ), BLACK_POWDER );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "sulphur" ), SULPHUR );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "noah_sulphur" ), NOAH_SULPHUR );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "meme_essence" ), MEME_ESSENCE );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "calibrator" ), CALIBRATOR );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_big_willies" ), DISC_BIG_WILLIES );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_flight_of_the_chinese_commuter" ), DISC_FLIGHT_OF_THE_CHINESE_COMMUTER );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_godly_piss" ), DISC_GODLY_PISS );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_massive_crap" ), DISC_MASSIVE_CRAP );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_pea_sized_anus" ), DISC_PEA_SIZED_ANUS );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_pooey_loo" ), DISC_POOEY_LOO );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_powerful_constipation" ), DISC_POWERFUL_CONSTIPATION );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_rage_over_a_clogged_commode" ), DISC_RAGE_OVER_A_CLOGGED_COMMODE );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_red_weewee" ), DISC_RED_WEEWEE );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_smelly_methane" ), DISC_SMELLY_METHANE );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_symphony_of_stupidity" ), DISC_SYMPHONY_OF_STUPIDITY );
        Registry.register( Registries.ITEM, Identifier.of( "gcmod", "music_disc_toilet_water" ), DISC_TOILET_WATER );

        AIRSTRIKE_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "airstrike_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( AIRSTRIKE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        BLAST_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "blast_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( BLAST_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        CONSTRUCTIVE_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "constructive_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( CONSTRUCTIVE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        DIG_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of( "gcmod", "dig_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( DIG_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

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

        FERTILIZER_RECIPE_SERIALIZER = Registry.register( Registries.RECIPE_SERIALIZER, Identifier.of( "gcmod", "crafting_fertiliser" ), new SpecialRecipeSerializer<>( FertiliserRecipe::new ) );
        EXPLOSIVE_RECIPE_SERIALIZER = Registry.register( Registries.RECIPE_SERIALIZER, Identifier.of( "gcmod", "crafting_explosive" ), new SpecialRecipeSerializer<>( ExplosiveRecipe::new ) );

        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_ORE_PLACEMENT );
        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, NOAH_ORE_PLACEMENT );

        PayloadTypeRegistry.playS2C().register( PooSplatPayload.ID, PooSplatPayload.CODEC );
    }
}