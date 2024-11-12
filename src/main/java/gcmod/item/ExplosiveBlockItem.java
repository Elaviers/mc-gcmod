package gcmod.item;

import gcmod.GCMod;
import gcmod.block.ExplosiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class ExplosiveBlockItem extends BlockItem
{
    public ExplosiveBlockItem( Block block, Settings settings )
    {
        super( block, settings );
    }

    public static ItemStack makeStackBlast( short fuse, short strength )
    {
        NbtCompound nbt = new NbtCompound();
        nbt.putShort( "Fuse", fuse );
        nbt.putShort( "Strength", strength );

        ItemStack stack = new ItemStack( GCMod.BLAST_TNT );
        stack.set( GCMod.DATA_EXPLOSIVE_INFO, NbtComponent.of( nbt ) );
        return stack;
    }

    public static ItemStack makeStackConstructive( short fuse, short strength, BlockState state )
    {
        NbtCompound nbt = new NbtCompound();
        nbt.putShort( "Fuse", fuse );
        nbt.putShort( "Strength", strength );
        nbt.put( "Block", NbtHelper.fromBlockState( state ) );

        ItemStack stack = new ItemStack( GCMod.CONSTRUCTIVE_TNT );
        stack.set( GCMod.DATA_EXPLOSIVE_INFO, NbtComponent.of( nbt ) );
        return stack;
    }

    public static ItemStack makeStackDig( short fuse, short strength )
    {
        NbtCompound nbt = new NbtCompound();
        nbt.putShort( "Fuse", fuse );
        nbt.putShort( "Strength", strength );

        ItemStack stack = new ItemStack( GCMod.DIG_TNT );
        stack.set( GCMod.DATA_EXPLOSIVE_INFO, NbtComponent.of( nbt ) );
        return stack;
    }

    public static ItemStack makeStackAirstrike( short fuse, short strength, short spread, short pieces, short height )
    {
        NbtCompound nbt = new NbtCompound();
        nbt.putShort( "Fuse", fuse );
        nbt.putShort( "Strength", strength );
        nbt.putShort( "Spread", spread );
        nbt.putShort( "Pieces", pieces );
        nbt.putShort( "Height", height );

        ItemStack stack = new ItemStack( GCMod.AIRSTRIKE_TNT );
        stack.set( GCMod.DATA_EXPLOSIVE_INFO, NbtComponent.of( nbt ) );
        return stack;
    }

    @Override
    public void appendTooltip( ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type )
    {
        NbtComponent explosiveInfo = stack.get( GCMod.DATA_EXPLOSIVE_INFO );
        if ( explosiveInfo == null )
            return;

        NbtCompound nbt = explosiveInfo.copyNbt();
        tooltip.add( Text.translatable( "block.gcmod.explosive.tooltip.strength", nbt.getShort( "Strength" ) ) );

        if ( nbt.getShort( "Fuse" ) > 0 )
            tooltip.add( Text.translatable( "block.gcmod.explosive.tooltip.fuse", nbt.getShort( "Fuse" ) / 20 ) );

        switch ( ((ExplosiveBlock) (Block.getBlockFromItem( stack.getItem() ))).type )
        {
            case CONSTRUCTIVE:
            {
                RegistryEntryLookup<Block> blockReg = context.getRegistryLookup().getOrThrow( RegistryKeys.BLOCK );
                tooltip.add( Text.translatable( "block.gcmod.explosive.tooltip.block", NbtHelper.toBlockState( blockReg, nbt.getCompound( "Block" ) ).getBlock().getName() ) );
                break;
            }

            case AIRSTRIKE:
                tooltip.add( Text.translatable( "block.gcmod.explosive.tooltip.spread", nbt.getShort( "Spread" ) ) );
                tooltip.add( Text.translatable( "block.gcmod.explosive.tooltip.pieces", nbt.getShort( "Pieces" ) ) );
                tooltip.add( Text.translatable( "block.gcmod.explosive.tooltip.height", nbt.getShort( "Height" ) ) );
                break;
        }
    }

    @Override
    public void inventoryTick( ItemStack stack, World world, Entity entity, int slot, boolean selected )
    {
        if ( !stack.getComponents().contains( GCMod.DATA_EXPLOSIVE_INFO ))
            addDefaultComponent( stack );

        super.inventoryTick( stack, world, entity, slot, selected );
    }

    static void addDefaultComponent( ItemStack stack )
    {
        NbtCompound nbt = new NbtCompound();

        nbt.putShort( "Strength", (short) 2 );

        switch ( ((ExplosiveBlock) (Block.getBlockFromItem( stack.getItem() ))).type )
        {
            case BLAST:
                nbt.putShort( "Strength", (short) 1 );
                break;

            case CONSTRUCTIVE:
                nbt.put( "Block", NbtHelper.fromBlockState( Blocks.STONE.getDefaultState() ) );
                break;

            case AIRSTRIKE:
                nbt.putShort( "Pieces", (short) 5 );
                nbt.putShort( "Spread", (short) 3 );
                nbt.putShort( "Height", (short) 20 );
                break;
        }

        stack.set( GCMod.DATA_EXPLOSIVE_INFO, NbtComponent.of( nbt ) );
    }
}
