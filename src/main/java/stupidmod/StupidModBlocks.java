package stupidmod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.block.*;
import stupidmod.item.ExplosiveBlockItem;

@ObjectHolder(StupidMod.id)
public class StupidModBlocks {
    static private final String
            nameBlastTNT = "blast_tnt",
            nameConstructiveTNT = "constructive_tnt",
            nameDigTNT = "dig_tnt",
            nameAirStrikeTNT = "airstrike_tnt",
            nameRope = "rope",
            nameSulphurOre = "sulphur_ore",
            nameNoahSulphurOre = "noah_sulphur_ore",
            nameMemeBlock = "meme_block",
            nameCentrifuge = "centrifuge",
            nameWirelessTorch = "wireless_torch",
            nameWirelessTorchWall = "wireless_wall_torch",
            namePooBlock = "poo_block",
            nameFermentedPooBlock = "fermented_poo_block";
    
    @ObjectHolder(nameBlastTNT)
    public static ExplosiveBlock BLAST_TNT;
    
    @ObjectHolder(nameBlastTNT)
    public static ExplosiveBlockItem BLAST_TNT_ITEM;
    
    @ObjectHolder(nameConstructiveTNT)
    public static ExplosiveBlock CONSTRUCTIVE_TNT;
    
    @ObjectHolder(nameConstructiveTNT)
    public static ExplosiveBlockItem CONSTRUCTIVE_TNT_ITEM;
    
    @ObjectHolder(nameDigTNT)
    public static ExplosiveBlock DIG_TNT;
    
    @ObjectHolder(nameDigTNT)
    public static ExplosiveBlockItem DIG_TNT_ITEM;
    
    @ObjectHolder(nameAirStrikeTNT)
    public static ExplosiveBlock AIR_STRIKE_TNT;
    
    @ObjectHolder(nameAirStrikeTNT)
    public static ExplosiveBlockItem AIR_STRIKE_TNT_ITEM;

    @ObjectHolder(nameRope)
    public static RopeBlock ROPE;

    @ObjectHolder(nameRope)
    public static BlockItem ROPE_ITEM;
    
    @ObjectHolder(nameSulphurOre)
    public static SulphurOreBlock SULPHUR_ORE;
    
    @ObjectHolder(nameNoahSulphurOre)
    public static SulphurOreBlock NOAH_SULPHUR_ORE;
    
    @ObjectHolder(nameMemeBlock)
    public static MemeBlock MEME;
    
    @ObjectHolder(namePooBlock)
    public static PooBlock POO;
    
    @ObjectHolder(nameFermentedPooBlock)
    public static PooBlock FERMENTED_POO;
    
    @ObjectHolder(nameCentrifuge)
    public static CentrifugeBlock CENTRIFUGE;
    
    @ObjectHolder(nameWirelessTorch)
    public static WirelessTorchBlock WIRELESS_TORCH;

    @ObjectHolder(nameWirelessTorchWall)
    public static WirelessTorchWallBlock WIRELESS_TORCH_WALL;
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration {
    
        static SoundType soundTypePoo, soundTypeMeme;
        
        @SubscribeEvent
        static void registerBlocks(RegistryEvent.Register<Block> register) {
            StupidModSounds.createSoundEvents();

            soundTypePoo = new SoundType(1.f, 1.f, StupidModSounds.POO_BLOCK, StupidModSounds.POO_BLOCK, StupidModSounds.POO_BLOCK, StupidModSounds.POO_BLOCK, StupidModSounds.POO_BLOCK);
            soundTypeMeme = new SoundType(1.f, 1.f, StupidModSounds.MEME_BLOCK, StupidModSounds.MEME_BLOCK, StupidModSounds.MEME_BLOCK, StupidModSounds.MEME_BLOCK, StupidModSounds.MEME_BLOCK);
            
            register.getRegistry().registerAll(
                    new ExplosiveBlock(nameBlastTNT, ExplosiveBlock.Type.BLAST),
                    new ExplosiveBlock(nameConstructiveTNT, ExplosiveBlock.Type.CONSTRUCTIVE),
                    new ExplosiveBlock(nameDigTNT, ExplosiveBlock.Type.DIG),
                    new ExplosiveBlock(nameAirStrikeTNT, ExplosiveBlock.Type.AIRSTRIKE),
                    new RopeBlock(nameRope),
                    new SulphurOreBlock(nameSulphurOre, false),
                    new SulphurOreBlock(nameNoahSulphurOre, true),
                    new MemeBlock(nameMemeBlock, soundTypeMeme),
                    new PooBlock(namePooBlock, soundTypePoo),
                    new PooBlock(nameFermentedPooBlock, soundTypePoo),
                    new CentrifugeBlock(nameCentrifuge),
                    new WirelessTorchBlock(nameWirelessTorch),
                    new WirelessTorchWallBlock(nameWirelessTorchWall)
            );
            
        }
        
        @SubscribeEvent
        static void registerItems(RegistryEvent.Register<Item> register) {
            IForgeRegistry<Item> registry = register.getRegistry();
    
            registry.registerAll(
                    new ExplosiveBlockItem(BLAST_TNT),
                    new ExplosiveBlockItem(CONSTRUCTIVE_TNT),
                    new ExplosiveBlockItem(DIG_TNT),
                    new ExplosiveBlockItem(AIR_STRIKE_TNT),
                    new WallOrFloorItem(WIRELESS_TORCH, WIRELESS_TORCH_WALL, new Item.Properties().group(StupidMod.GROUP)).setRegistryName(nameWirelessTorch),
                    new BlockItem(ROPE, new Item.Properties().group(StupidMod.GROUP)).setRegistryName(nameRope)
            );
            
            registerItemsForBlocks(registry,
                    SULPHUR_ORE,
                    NOAH_SULPHUR_ORE,
                    MEME,
                    POO,
                    FERMENTED_POO,
                    CENTRIFUGE
            );
        }
        
        static void registerItemsForBlocks(IForgeRegistry<Item> registry, Block... blocks) {
            for (int i = 0; i < blocks.length; i++)
                registerItemForBlock(registry, blocks[i]);
        }
    
        static BlockItem registerItemForBlock(IForgeRegistry<Item> registry, Block block) {
            BlockItem ib = new BlockItem(block, new Item.Properties().group(StupidMod.GROUP));
            ib.setRegistryName(block.getRegistryName());
            registry.register(ib);
            return ib;
        }
    }
}
