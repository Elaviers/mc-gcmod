package stupidmod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemWallOrFloor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import stupidmod.block.*;
import stupidmod.item.ItemBlockExplosive;

@ObjectHolder(StupidMod.id)
public class BlockRegister {
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
    public static BlockExplosive blockBlastTNT;
    
    @ObjectHolder(nameBlastTNT)
    public static ItemBlockExplosive itemBlockBlastTNT;
    
    @ObjectHolder(nameConstructiveTNT)
    public static BlockExplosive blockConstructiveTNT;
    
    @ObjectHolder(nameConstructiveTNT)
    public static ItemBlockExplosive itemBlockConstructiveTNT;
    
    @ObjectHolder(nameDigTNT)
    public static BlockExplosive blockDigTNT;
    
    @ObjectHolder(nameDigTNT)
    public static ItemBlockExplosive itemBlockDigTNT;
    
    @ObjectHolder(nameAirStrikeTNT)
    public static BlockExplosive blockAirstrikeTNT;
    
    @ObjectHolder(nameAirStrikeTNT)
    public static ItemBlockExplosive itemBlockAirstrikeTNT;
    
    @ObjectHolder(nameRope)
    public static BlockRope blockRope;
    
    @ObjectHolder(nameSulphurOre)
    public static BlockSulphurOre blockSulphurOre;
    
    @ObjectHolder(nameNoahSulphurOre)
    public static BlockSulphurOre blockNoahSulphurOre;
    
    @ObjectHolder(nameMemeBlock)
    public static BlockMeme blockMeme;
    
    @ObjectHolder(namePooBlock)
    public static BlockPoo blockPoo;
    
    @ObjectHolder(nameFermentedPooBlock)
    public static BlockPoo blockPooFermented;
    
    @ObjectHolder(nameCentrifuge)
    public static BlockCentrifuge blockCentrifuge;
    
    @ObjectHolder(nameWirelessTorch)
    public static BlockWirelessTorch blockWirelessTorch;

    @ObjectHolder(nameWirelessTorchWall)
    public static BlockWirelessTorchWall blockWirelessTorchWall;
    
    @Mod.EventBusSubscriber(modid = StupidMod.id, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class Registration {
    
        static SoundType soundTypePoo, soundTypeMeme;
        
        @SubscribeEvent
        static void registerBlocks(RegistryEvent.Register<Block> register) {
            SoundRegister.createSoundEvents();
            
            soundTypePoo = new SoundType(1.f, 1.f, SoundRegister.soundPooBlock, SoundRegister.soundPooBlock, SoundRegister.soundPooBlock, SoundRegister.soundPooBlock, SoundRegister.soundPooBlock);
            soundTypeMeme = new SoundType(1.f, 1.f, SoundRegister.soundMemeBlock, SoundRegister.soundMemeBlock, SoundRegister.soundMemeBlock, SoundRegister.soundMemeBlock, SoundRegister.soundMemeBlock);
            
            register.getRegistry().registerAll(
                    new BlockExplosive(nameBlastTNT, BlockExplosive.Type.BLAST),
                    new BlockExplosive(nameConstructiveTNT, BlockExplosive.Type.CONSTRUCTIVE),
                    new BlockExplosive(nameDigTNT, BlockExplosive.Type.DIG),
                    new BlockExplosive(nameAirStrikeTNT, BlockExplosive.Type.AIRSTRIKE),
                    new BlockRope(nameRope),
                    new BlockSulphurOre(nameSulphurOre, false),
                    new BlockSulphurOre(nameNoahSulphurOre, true),
                    new BlockMeme(nameMemeBlock, soundTypeMeme),
                    new BlockPoo(namePooBlock, soundTypePoo),
                    new BlockPoo(nameFermentedPooBlock, soundTypePoo),
                    new BlockCentrifuge(nameCentrifuge),
                    new BlockWirelessTorch(nameWirelessTorch),
                    new BlockWirelessTorchWall(nameWirelessTorchWall)
            );
            
        }
        
        @SubscribeEvent
        static void registerItems(RegistryEvent.Register<Item> register) {
            IForgeRegistry<Item> registry = register.getRegistry();
    
            registry.registerAll(
                    new ItemBlockExplosive(blockBlastTNT),
                    new ItemBlockExplosive(blockConstructiveTNT),
                    new ItemBlockExplosive(blockDigTNT),
                    new ItemBlockExplosive(blockAirstrikeTNT),
                    new ItemWallOrFloor(blockWirelessTorch, blockWirelessTorchWall, new Item.Properties().group(StupidMod.GROUP)).setRegistryName(nameWirelessTorch)
            );
            
            registerItemsForBlocks(registry,
                    blockRope,
                    blockSulphurOre,
                    blockNoahSulphurOre,
                    blockMeme,
                    blockPoo,
                    blockPooFermented,
                    blockCentrifuge
            );
        }
        
        static void registerItemsForBlocks(IForgeRegistry<Item> registry, Block... blocks) {
            for (int i = 0; i < blocks.length; i++)
                registerItemForBlock(registry, blocks[i]);
        }
    
        static ItemBlock registerItemForBlock(IForgeRegistry<Item> registry, Block block) {
            ItemBlock ib = new ItemBlock(block, new Item.Properties().group(StupidMod.GROUP));
            ib.setRegistryName(block.getRegistryName());
            registry.register(ib);
            return ib;
        }
    }
}
