package gcmod;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CentrifugeScreenHandler extends AbstractContainerMenu
{
    static class CentrifugeSlot extends Slot
    {
        private final ContainerData properties;

        public CentrifugeSlot( Container inventory, int index, int x, int y, ContainerData properties )
        {
            super( inventory, index, x, y );
            this.properties = properties;
        }

        @Override
        public int getMaxStackSize()
        {
            return (properties == null || properties.get( 0 ) == 0 ) ? 1 : 0;
        }

        @Override
        public boolean mayPlace( ItemStack stack )
        {
            return stack.is( GCMod.FERMENTED_POO );
        }
    }

    public final ContainerData properties;
    Container inventory;

    public CentrifugeScreenHandler( int syncId, Inventory playerInventory )
    {
        this( syncId, playerInventory, new SimpleContainer( 12 ), new SimpleContainerData( 1 ) );
    }

    public CentrifugeScreenHandler( int syncId, Inventory playerInventory, Container inventory, ContainerData properties )
    {
        super( GCMod.CENTRIFUGE_SCREEN_HANDLER, syncId );

        assert inventory.getContainerSize() == 6;

        this.properties = properties;
        this.inventory = inventory;

        this.addSlot( new CentrifugeSlot( inventory, 0, 80, 14, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 1, 60, 34, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 2, 100, 34, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 3, 80, 54, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 4, 8, 52, null ) );
        this.addSlot( new CentrifugeSlot( inventory, 5, 152, 52, null ) );

        this.addDataSlots( properties );

        // Player Inventory, Slot 9-35, Slot IDs 6-32
        for ( int y = 0; y < 3; ++y )
            for ( int x = 0; x < 9; ++x )
                this.addSlot( new Slot( playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18 ) );

        // Player Inventory, Slot 0-8, Slot IDs 33-41
        for ( int x = 0; x < 9; ++x )
            this.addSlot( new Slot( playerInventory, x, 8 + x * 18, 142 ) );
    }



    @Override
    public ItemStack quickMoveStack( Player player, int fromSlot )
    {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get( fromSlot );

        if ( slot.hasItem() )
        {
            ItemStack current = slot.getItem();
            result = current.copy();

            if ( fromSlot < 6 )
            {
                if ( !this.moveItemStackTo( current, 6, 42, true ) )
                    return ItemStack.EMPTY;
            }
            else if ( this.properties.get( 0 ) == 0 )
            {
                boolean merged = false;
                for ( int i = 0; i < 4 && !current.isEmpty(); ++i )
                {
                    if ( this.moveItemStackTo( current, 0, 4, false ) )
                        merged = true;
                }

                if ( !merged )
                    return ItemStack.EMPTY;
            }
            else
                return ItemStack.EMPTY; //locked

            if ( current.isEmpty() )
                slot.safeInsert( ItemStack.EMPTY );
            else
                slot.setChanged();
        }

        return result;

    }

    @Override
    public boolean stillValid( Player player )
    {
        return this.inventory.stillValid( player );
    }
}
