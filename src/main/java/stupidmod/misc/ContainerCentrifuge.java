package stupidmod.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import stupidmod.entity.tile.TileEntityCentrifuge;
import stupidmod.item.ItemPoo;

public class ContainerCentrifuge extends Container {
    class CoolSlot extends Slot {
        private int limit;
        
        public CoolSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, int limit) {
            super(inventoryIn, index, xPosition, yPosition);
            
            this.limit = limit;
        }
    
        @Override
        public int getSlotStackLimit() {
            return limit;
        }
    
        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() instanceof ItemPoo;
        }
    }
    
    private TileEntityCentrifuge ent;
    boolean isLocked = false;
    
    public ContainerCentrifuge(IInventory PlayerInventory, TileEntityCentrifuge tileentity, boolean locked) {
        this.ent = tileentity;
        this.isLocked = locked;
        
        if (!isLocked) {
            this.addSlot(new CoolSlot(ent,0,80,14, 1));
            this.addSlot(new CoolSlot(ent,1,60,34, 1));
            this.addSlot(new CoolSlot(ent,2,100,34, 1));
            this.addSlot(new CoolSlot(ent,3,80,54, 1));
        }else{
            this.addSlot(new CoolSlot(ent,0,80,14, 0));
            this.addSlot(new CoolSlot(ent,1,60,34, 0));
            this.addSlot(new CoolSlot(ent,2,100,34, 0));
            this.addSlot(new CoolSlot(ent,3,80,54, 0));
        }
        
        this.addSlot(new CoolSlot(ent,4,8,34, 0));
        this.addSlot(new CoolSlot(ent,5,26,34, 0));
        this.addSlot(new CoolSlot(ent,6,8,52, 0));
        this.addSlot(new CoolSlot(ent,7,26,52, 0));
        
        this.addSlot(new CoolSlot(ent,8,134,34, 0));
        this.addSlot(new CoolSlot(ent,9,152,34, 0));
        this.addSlot(new CoolSlot(ent,10,134,52, 0));
        this.addSlot(new CoolSlot(ent,11,152,52, 0));
        
        // Player Inventory, Slot 9-35, Slot IDs 12-38
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(PlayerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        
        // Player Inventory, Slot 0-8, Slot IDs 39-47
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(PlayerInventory, x, 8 + x * 18, 142));
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot) {
        ItemStack previous = null;
        Slot slot = (Slot) this.inventorySlots.get(fromSlot);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            
            if (current != ItemStack.EMPTY) {
                previous = current.copy();
                
                if (fromSlot < 12) {
                    if (!this.mergeItemStack(current, 39, 47, false))
                        if (!this.mergeItemStack(current, 12, 38, false))
                            return ItemStack.EMPTY;
                } else if (!isLocked) {
                    if (!this.mergeItemStack(current, 0, 4, false))
                        return ItemStack.EMPTY;
                }
                else
                    return ItemStack.EMPTY;
    
                if (current.getCount() == 0)
                    slot.putStack(ItemStack.EMPTY);
                else
                    slot.onSlotChanged();
                
                slot.onTake(player, current);
            }
        }
        
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.ent.isUsableByPlayer(player);
    }
    
    
}
