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
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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

    public static final Identifier MEME_BOCK_ID = new Identifier( "gcmod", "meme_block" );
    public static final Identifier POO_BLOCK_ID = new Identifier( "gcmod", "poo_block" );
    public static final SoundEvent FART_SOUND = SoundEvent.of( new Identifier( "gcmod", "fart" ) );
    public static final SoundEvent POO_BLOCK_SOUND = SoundEvent.of( POO_BLOCK_ID );
    public static final SoundEvent NOAH_SOUND = SoundEvent.of( new Identifier( "gcmod", "meme" ) );
    public static final SoundEvent MEME_BLOCK_SOUND = SoundEvent.of( MEME_BOCK_ID );
    public static final SoundEvent CENTRIFUGE_SOUND = SoundEvent.of( new Identifier( "gcmod", "centrifuge" ) );
    public static final SoundEvent POO_CANNON_SOUND = SoundEvent.of( new Identifier( "gcmod", "poo_cannon" ) );
    public static final SoundEvent MUSIC_BIGWILLIES = SoundEvent.of( new Identifier( "gcmod", "music_disc.big_willies" ) );
    public static final SoundEvent MUSIC_FOTCC = SoundEvent.of( new Identifier( "gcmod", "music_disc.flight_of_the_chinese_commuter" ) );
    public static final SoundEvent MUSIC_GODLYPISS = SoundEvent.of( new Identifier( "gcmod", "music_disc.godly_piss" ) );
    public static final SoundEvent MUSIC_MASSIVECRAP = SoundEvent.of( new Identifier( "gcmod", "music_disc.massive_crap" ) );
    public static final SoundEvent MUSIC_PEASIZEDANUS = SoundEvent.of( new Identifier( "gcmod", "music_disc.pea_sized_anus" ) );
    public static final SoundEvent MUSIC_POOEYLOO = SoundEvent.of( new Identifier( "gcmod", "music_disc.pooey_loo" ) );
    public static final SoundEvent MUSIC_POWERFULCONSTIPATION = SoundEvent.of( new Identifier( "gcmod", "music_disc.powerful_constipation" ) );
    public static final SoundEvent MUSIC_ROACC = SoundEvent.of( new Identifier( "gcmod", "music_disc.rage_over_a_clogged_commode" ) );
    public static final SoundEvent MUSIC_REDWEEWEE = SoundEvent.of( new Identifier( "gcmod", "music_disc.red_weewee" ) );
    public static final SoundEvent MUSIC_SMELLYMETHANE = SoundEvent.of( new Identifier( "gcmod", "music_disc.smelly_methane" ) );
    public static final SoundEvent MUSIC_SOS = SoundEvent.of( new Identifier( "gcmod", "music_disc.symphony_of_stupidity" ) );
    public static final SoundEvent MUSIC_TOILETWATER = SoundEvent.of( new Identifier( "gcmod", "music_disc.toilet_water" ) );

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
    public static final Item DISC_BIG_WILLIES = new MusicDiscItem( 1, MUSIC_BIGWILLIES, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 66 );
    public static final Item DISC_FLIGHT_OF_THE_CHINESE_COMMUTER = new MusicDiscItem( 2, MUSIC_FOTCC, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 24 );
    public static final Item DISC_GODLY_PISS = new MusicDiscItem( 3, MUSIC_GODLYPISS, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 19 );
    public static final Item DISC_MASSIVE_CRAP = new MusicDiscItem( 4, MUSIC_MASSIVECRAP, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 46 );
    public static final Item DISC_PEA_SIZED_ANUS = new MusicDiscItem( 5, MUSIC_PEASIZEDANUS, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 73 );
    public static final Item DISC_POOEY_LOO = new MusicDiscItem( 6, MUSIC_POOEYLOO, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 40 );
    public static final Item DISC_POWERFUL_CONSTIPATION = new MusicDiscItem( 7, MUSIC_POWERFULCONSTIPATION, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 26 );
    public static final Item DISC_RAGE_OVER_A_CLOGGED_COMMODE = new MusicDiscItem( 8, MUSIC_ROACC, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 66 );
    public static final Item DISC_RED_WEEWEE = new MusicDiscItem( 9, MUSIC_REDWEEWEE, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 96 );
    public static final Item DISC_SMELLY_METHANE = new MusicDiscItem( 10, MUSIC_SMELLYMETHANE, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 36 );
    public static final Item DISC_SYMPHONY_OF_STUPIDITY = new MusicDiscItem( 11, MUSIC_SOS, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 25 );
    public static final Item DISC_TOILET_WATER = new MusicDiscItem( 12, MUSIC_TOILETWATER, new Item.Settings().maxCount( 1 ).rarity( Rarity.RARE ), 78 );

    public static final EntityType<PooEntity> POO_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "poo" ),
            EntityType.Builder.create( PooEntity::new, SpawnGroup.MISC ).dimensions( .75f, .3125f ).build()
    );

    public static final EntityType<PooBrickEntity> POO_BRICK_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "poo_brick" ),
            EntityType.Builder.create( PooBrickEntity::new, SpawnGroup.MISC ).dimensions( .4f, .25f ).build()
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_POO_BRICK_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "explosive_poo_brick" ),
            EntityType.Builder.create( PooBrickEntity::createExplosive, SpawnGroup.MISC ).dimensions( .4f, .25f ).build()
    );

    public static final EntityType<PooBrickEntity> EXPLOSIVE_BOMB_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "explosive_bomb" ),
            EntityType.Builder.create( PooBrickEntity::createExplosive, SpawnGroup.MISC ).dimensions( .5f, .5f ).build()
    );

    public static final EntityType<ExplosiveEntity> EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "explosive" ),
            EntityType.Builder.create( ExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final EntityType<AirstrikeExplosiveEntity> AIRSTRIKE_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "airstrike_explosive" ),
            EntityType.Builder.create( AirstrikeExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final EntityType<ConstructiveExplosiveEntity> CONSTRUCTIVE_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "constructive_explosive" ),
            EntityType.Builder.create( ConstructiveExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final EntityType<DigExplosiveEntity> DIG_EXPLOSIVE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier( "gcmod", "dig_explosive" ),
            EntityType.Builder.create( DigExplosiveEntity::new, SpawnGroup.MISC ).dimensions( 1f, 1f ).build()
    );

    public static final BlockEntityType<CentrifugeEntity> CENTRIFUGE_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier( "gcmod", "centrifuge" ),
            BlockEntityType.Builder.create( CentrifugeEntity::new, CENTRIFUGE ).build()
    );

    public static final BlockEntityType<WirelessTorchEntity> WIRELESS_TORCH_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier( "gcmod", "wireless_torch" ),
            BlockEntityType.Builder.create( WirelessTorchEntity::new, WIRELESS_TORCH ).build()
    );

    public static BlockEntityType<ExplosiveBlockEntity> AIRSTRIKE_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> BLAST_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> CONSTRUCTIVE_TNT_BLOCK_ENTITY;
    public static BlockEntityType<ExplosiveBlockEntity> DIG_TNT_BLOCK_ENTITY;

    public static final TagKey<Item> TAG_POO_CANNONS = TagKey.of( RegistryKeys.ITEM, new Identifier( "gcmod", "poo_cannons" ) );

    public static final Enchantment ENCH_CONSTIPATION = new Enchantment(
            Enchantment.properties( TAG_POO_CANNONS,
                    1,
                    3,
                    Enchantment.constantCost( 25 ), Enchantment.constantCost( 50 ), 3,
                    FeatureSet.empty(),
                    EquipmentSlot.MAINHAND
            ) );

    public static final Enchantment ENCH_DIARRHOEA = new Enchantment(
            Enchantment.properties( TAG_POO_CANNONS,
                    1,
                    1,
                    Enchantment.constantCost( 25 ), Enchantment.constantCost( 50 ), 3,
                    FeatureSet.empty(),
                    EquipmentSlot.MAINHAND
            ) );

    public static final Enchantment ENCH_LAXATIVES = new Enchantment(
            Enchantment.properties( TAG_POO_CANNONS,
                    1,
                    1,
                    Enchantment.constantCost( 25 ), Enchantment.constantCost( 50 ), 3,
                    FeatureSet.empty(),
                    EquipmentSlot.MAINHAND
            ) );

    public static final Enchantment ENCH_POOER = new Enchantment(
            Enchantment.properties( TAG_POO_CANNONS,
                    1,
                    3,
                    Enchantment.constantCost( 25 ), Enchantment.constantCost( 50 ), 3,
                    FeatureSet.empty(),
                    EquipmentSlot.MAINHAND
            ) );

    public static final DataComponentType<NbtComponent> DATA_EXPLOSIVE_INFO = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            new Identifier( "gcmod", "explosive_info" ),
            DataComponentType.<NbtComponent>builder().codec( NbtComponent.CODEC ).build()
    );

    public static final DataComponentType<NbtComponent> DATA_TORCH_NETWORK = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            new Identifier( "gcmod", "torch_network" ),
            DataComponentType.<NbtComponent>builder().codec( NbtComponent.CODEC ).build()
    );

    public static RecipeSerializer<FertiliserRecipe> FERTILIZER_RECIPE_SERIALIZER;
    public static RecipeSerializer<ExplosiveRecipe> EXPLOSIVE_RECIPE_SERIALIZER;

    public static ScreenHandlerType<CentrifugeScreenHandler> CENTRIFUGE_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            new Identifier( "gcmod", "centrifuge" ),
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
                entries.add( DISC_RAGE_OVER_A_CLOGGED_COMMODE );
                entries.add( DISC_RED_WEEWEE );
                entries.add( DISC_SMELLY_METHANE );
                entries.add( DISC_SYMPHONY_OF_STUPIDITY );
                entries.add( DISC_TOILET_WATER );
            } )
            .build();

    private static final RegistryKey<PlacedFeature> SULPHUR_ORE_PLACEMENT = RegistryKey.of( RegistryKeys.PLACED_FEATURE, new Identifier( "gcmod", "sulphur_ore" ) );
    private static final RegistryKey<PlacedFeature> NOAH_ORE_PLACEMENT = RegistryKey.of( RegistryKeys.PLACED_FEATURE, new Identifier( "gcmod", "noah_sulphur_ore" ) );

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

        Registry.register( Registries.ITEM_GROUP, new Identifier( "gcmod", "itemgroup" ), ITEM_GROUP );

        Registry.register( Registries.BLOCK, POO_BLOCK_ID, POO_BLOCK );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "fermented_poo_block" ), FERMENTED_POO_BLOCK );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "rope" ), ROPE );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "centrifuge" ), CENTRIFUGE );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "sulphur_ore" ), SULPHUR_ORE );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "noah_sulphur_ore" ), NOAH_SULPHUR_ORE );
        Registry.register( Registries.BLOCK, MEME_BOCK_ID, MEME_BLOCK );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "airstrike_tnt" ), AIRSTRIKE_TNT );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "blast_tnt" ), BLAST_TNT );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "constructive_tnt" ), CONSTRUCTIVE_TNT );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "dig_tnt" ), DIG_TNT );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "wireless_torch" ), WIRELESS_TORCH );
        Registry.register( Registries.BLOCK, new Identifier( "gcmod", "wireless_wall_torch" ), WIRELESS_TORCH_WALL );

        Registry.register( Registries.ITEM, POO_BLOCK_ID, new BlockItem( POO_BLOCK, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "fermented_poo_block" ), new BlockItem( FERMENTED_POO_BLOCK, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "rope" ), ROPE_ITEM );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "centrifuge" ), new BlockItem( CENTRIFUGE, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "sulphur_ore" ), new BlockItem( SULPHUR_ORE, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "noah_sulphur_ore" ), new BlockItem( NOAH_SULPHUR_ORE, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "meme_block" ), new BlockItem( MEME_BLOCK, new Item.Settings().maxCount( 69 ) ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "airstrike_tnt" ), new ExplosiveBlockItem( AIRSTRIKE_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "blast_tnt" ), new ExplosiveBlockItem( BLAST_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "constructive_tnt" ), new ExplosiveBlockItem( CONSTRUCTIVE_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "dig_tnt" ), new ExplosiveBlockItem( DIG_TNT, new Item.Settings() ) );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "wireless_torch" ),
                new VerticallyAttachableBlockItem( WIRELESS_TORCH, WIRELESS_TORCH_WALL, new Item.Settings(), Direction.DOWN )
        );

        Registry.register( Registries.ITEM, new Identifier( "gcmod", "poo" ), POO );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "fermented_poo" ), FERMENTED_POO );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "poo_brick" ), POO_BRICK );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "poo_cannon" ), POO_CANNON );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "poo_powder" ), POO_POWDER );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "poo_protein" ), POO_PROTEIN );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "fertiliser" ), FERTILISER );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "black_powder" ), BLACK_POWDER );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "sulphur" ), SULPHUR );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "noah_sulphur" ), NOAH_SULPHUR );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "meme_essence" ), MEME_ESSENCE );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "calibrator" ), CALIBRATOR );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_big_willies" ), DISC_BIG_WILLIES );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_flight_of_the_chinese_commuter" ), DISC_FLIGHT_OF_THE_CHINESE_COMMUTER );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_godly_piss" ), DISC_GODLY_PISS );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_massive_crap" ), DISC_MASSIVE_CRAP );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_pea_sized_anus" ), DISC_PEA_SIZED_ANUS );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_pooey_loo" ), DISC_POOEY_LOO );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_powerful_constipation" ), DISC_POWERFUL_CONSTIPATION );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_rage_over_a_clogged_commode" ), DISC_RAGE_OVER_A_CLOGGED_COMMODE );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_red_weewee" ), DISC_RED_WEEWEE );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_smelly_methane" ), DISC_SMELLY_METHANE );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_symphony_of_stupidity" ), DISC_SYMPHONY_OF_STUPIDITY );
        Registry.register( Registries.ITEM, new Identifier( "gcmod", "music_disc_toilet_water" ), DISC_TOILET_WATER );

        Registry.register( Registries.ENCHANTMENT, new Identifier( "gcmod", "constipation" ), ENCH_CONSTIPATION );
        Registry.register( Registries.ENCHANTMENT, new Identifier( "gcmod", "diarrhoea" ), ENCH_DIARRHOEA );
        Registry.register( Registries.ENCHANTMENT, new Identifier( "gcmod", "laxatives" ), ENCH_LAXATIVES );
        Registry.register( Registries.ENCHANTMENT, new Identifier( "gcmod", "pooer" ), ENCH_POOER );

        AIRSTRIKE_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier( "gcmod", "airstrike_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( AIRSTRIKE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        BLAST_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier( "gcmod", "blast_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( BLAST_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        CONSTRUCTIVE_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier( "gcmod", "constructive_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( CONSTRUCTIVE_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        DIG_TNT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier( "gcmod", "dig_tnt" ),
                BlockEntityType.Builder.create( ( p, s ) -> new ExplosiveBlockEntity( DIG_TNT_BLOCK_ENTITY, p, s ), DIG_TNT ).build()
        );

        RecipeType.<FertiliserRecipe>register( "gcmod:crafting_fertiliser" );
        RecipeType.<FertiliserRecipe>register( "gcmod:crafting_explosive" );
        FERTILIZER_RECIPE_SERIALIZER = Registry.register( Registries.RECIPE_SERIALIZER, new Identifier( "gcmod", "crafting_fertiliser" ), new SpecialRecipeSerializer<>( FertiliserRecipe::new ) );
        EXPLOSIVE_RECIPE_SERIALIZER = Registry.register( Registries.RECIPE_SERIALIZER, new Identifier( "gcmod", "crafting_explosive" ), new SpecialRecipeSerializer<>( ExplosiveRecipe::new ) );

        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_ORE_PLACEMENT );
        BiomeModifications.addFeature( BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, NOAH_ORE_PLACEMENT );
    }


}