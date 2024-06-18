package gcmod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class CentrifugeScreenHandler extends ScreenHandler
{
    static class CentrifugeSlot extends Slot
    {
        private final PropertyDelegate properties;

        public CentrifugeSlot( Inventory inventory, int index, int x, int y, PropertyDelegate properties )
        {
            super( inventory, index, x, y );
            this.properties = properties;
        }

        @Override
        public int getMaxItemCount()
        {
            return (properties == null || properties.get( 0 ) == 0 ) ? 1 : 0;
        }

        @Override
        public boolean canInsert( ItemStack stack )
        {
            return stack.isOf( GCMod.FERMENTED_POO );
        }
    }

    public final PropertyDelegate properties;
    Inventory inventory;

    public CentrifugeScreenHandler( int syncId, PlayerInventory playerInventory )
    {
        this( syncId, playerInventory, new SimpleInventory( 12 ), new ArrayPropertyDelegate( 1 ) );
    }

    public CentrifugeScreenHandler( int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate properties )
    {
        super( GCMod.CENTRIFUGE_SCREEN_HANDLER, syncId );

        assert inventory.size() == 6;

        this.properties = properties;
        this.inventory = inventory;

        this.addSlot( new CentrifugeSlot( inventory, 0, 80, 14, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 1, 60, 34, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 2, 100, 34, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 3, 80, 54, properties ) );
        this.addSlot( new CentrifugeSlot( inventory, 4, 8, 52, null ) );
        this.addSlot( new CentrifugeSlot( inventory, 5, 152, 52, null ) );

        this.addProperties( properties );

        // Player Inventory, Slot 9-35, Slot IDs 6-32
        for ( int y = 0; y < 3; ++y )
            for ( int x = 0; x < 9; ++x )
                this.addSlot( new Slot( playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18 ) );

        // Player Inventory, Slot 0-8, Slot IDs 33-41
        for ( int x = 0; x < 9; ++x )
            this.addSlot( new Slot( playerInventory, x, 8 + x * 18, 142 ) );
    }



    @Override
    public ItemStack quickMove( PlayerEntity player, int fromSlot )
    {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get( fromSlot );

        if ( slot.hasStack() )
        {
            ItemStack current = slot.getStack();
            result = current.copy();

            if ( fromSlot < 6 )
            {
                if ( !this.insertItem( current, 6, 42, true ) )
                    return ItemStack.EMPTY;
            }
            else if ( this.properties.get( 0 ) == 0 )
            {
                boolean merged = false;
                for ( int i = 0; i < 4 && !current.isEmpty(); ++i )
                {
                    if ( this.insertItem( current, 0, 4, false ) )
                        merged = true;
                }

                if ( !merged )
                    return ItemStack.EMPTY;
            }
            else
                return ItemStack.EMPTY; //locked

            if ( current.isEmpty() )
                slot.insertStack( ItemStack.EMPTY );
            else
                slot.markDirty();
        }

        return result;

    }

    @Override
    public boolean canUse( PlayerEntity player )
    {
        return this.inventory.canPlayerUse( player );
    }
}
