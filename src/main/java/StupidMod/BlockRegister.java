package StupidMod;

import StupidMod.Blocks.*;
import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.Entities.Tile.TileEntityExplosiveData;
import StupidMod.Entities.Tile.TileEntityWirelessTorchData;
import StupidMod.Items.ItemBlockPoo;
import StupidMod.Items.ItemBlockRope;
import StupidMod.Items.ItemBlockExplosive;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockRegister {

    public BlockExplosive blockExplosive;
    public BlockPoo blockPoo;
    public BlockRope blockRope;
    public BlockStupidOre blockSulphurOre, blockNoahSulphurOre;
    public BlockMeme blockMeme;
    public BlockCentrifuge blockCentrifuge;
    public BlockWirelessTorch blockWirelessTorch, blockWirelessTorchOn;
    
    public ItemBlockExplosive itemBlockExplosive;
    public ItemBlockPoo itemBlockPoo;
    public ItemBlockRope itemBlockRope;

    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);

        blockExplosive = new BlockExplosive("tnt");
        blockPoo = new BlockPoo("poo_block");
        blockRope = new BlockRope("rope");
        blockSulphurOre = new BlockStupidOre("sulphur_ore", StupidMod.instance.items.itemSulphur);
        blockNoahSulphurOre = new BlockStupidOre("noah_ore", StupidMod.instance.items.itemNoahSulphur);
        blockMeme = new BlockMeme("meme_block");
        blockCentrifuge = new BlockCentrifuge("centrifuge");
        blockWirelessTorch = new BlockWirelessTorch("wireless_torch", false);
        blockWirelessTorchOn = new BlockWirelessTorch("wireless_torch_on", true);
    
        itemBlockExplosive = new ItemBlockExplosive(blockExplosive);
        itemBlockPoo = new ItemBlockPoo(blockPoo);
        itemBlockRope = new ItemBlockRope(blockRope);
        
        blockExplosive.setCreativeTab(StupidMod.instance.creativeTab);
        blockPoo.setCreativeTab(StupidMod.instance.creativeTab);
        blockRope.setCreativeTab(StupidMod.instance.creativeTab);
        blockSulphurOre.setCreativeTab(StupidMod.instance.creativeTab);
        blockNoahSulphurOre.setCreativeTab(StupidMod.instance.creativeTab);
        blockMeme.setCreativeTab(StupidMod.instance.creativeTab);
        blockCentrifuge.setCreativeTab(StupidMod.instance.creativeTab);
        blockWirelessTorch.setCreativeTab(StupidMod.instance.creativeTab);
    }

    @SubscribeEvent
    void registerBlocks(RegistryEvent.Register<Block> register)
    {
        register.getRegistry().registerAll(blockExplosive, blockPoo, blockRope, blockSulphurOre, blockNoahSulphurOre, blockMeme, blockCentrifuge, blockWirelessTorch, blockWirelessTorchOn);
    }

    @SubscribeEvent
    void registerItems(RegistryEvent.Register<Item> register)
    {
        IForgeRegistry<Item> registry = register.getRegistry();
        
        registry.registerAll(itemBlockPoo, itemBlockRope, itemBlockExplosive);
        
        registerItemForBlock(registry, blockSulphurOre);
        registerItemForBlock(registry, blockNoahSulphurOre);
        registerItemForBlock(registry, blockMeme);
        registerItemForBlock(registry, blockCentrifuge);
        registerItemForBlock(registry, blockWirelessTorch);
        
        GameRegistry.registerTileEntity(TileEntityExplosiveData.class, new ResourceLocation("stupidmod:te_explosive"));
        GameRegistry.registerTileEntity(TileEntityCentrifuge.class, new ResourceLocation("stupidmod:te_centrifuge"));
        GameRegistry.registerTileEntity(TileEntityWirelessTorchData.class, new ResourceLocation("stupidmod:te_wireless_torch"));
    }
    
    void registerItemForBlock(IForgeRegistry<Item> registry,  Block block)
    {
        registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
}
