package stupidmod.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import stupidmod.StupidModContainers;
import stupidmod.StupidModItems;
import stupidmod.entity.tile.CentrifugeTileEntity;

public class CentrifugeContainer extends Container {
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
            return stack.getItem() == StupidModItems.FERMENTED_POO;
        }
    }
    
    public CentrifugeTileEntity ent;
    boolean isLocked = false;

    IInventory inventory;

    public CentrifugeContainer(int id, PlayerInventory playerInventory, PacketBuffer data)
    {
        this(id, playerInventory, new Inventory(12), (CentrifugeTileEntity) playerInventory.player.world.getTileEntity(data.readBlockPos()));
    }
    
    public CentrifugeContainer(int id, PlayerInventory playerInventory, IInventory inv, CentrifugeTileEntity tileentity) {
        super(StupidModContainers.CENTRIFUGE, id);
        assertInventorySize(inv, 12);

        this.inventory = inv;
        this.inventory.openInventory(playerInventory.player);

        this.ent = tileentity;
        this.isLocked = tileentity.isSpinning();

        if (!isLocked) {
            this.addSlot(new CoolSlot(inv,0,80,14, 1));
            this.addSlot(new CoolSlot(inv,1,60,34, 1));
            this.addSlot(new CoolSlot(inv,2,100,34, 1));
            this.addSlot(new CoolSlot(inv,3,80,54, 1));
        }else{
            this.addSlot(new CoolSlot(inv,0,80,14, 0));
            this.addSlot(new CoolSlot(inv,1,60,34, 0));
            this.addSlot(new CoolSlot(inv,2,100,34, 0));
            this.addSlot(new CoolSlot(inv,3,80,54, 0));
        }

        this.addSlot(new CoolSlot(inv,4,8,34, 0));
        this.addSlot(new CoolSlot(inv,5,26,34, 0));
        this.addSlot(new CoolSlot(inv,6,8,52, 0));
        this.addSlot(new CoolSlot(inv,7,26,52, 0));
        
        this.addSlot(new CoolSlot(inv,8,134,34, 0));
        this.addSlot(new CoolSlot(inv,9,152,34, 0));
        this.addSlot(new CoolSlot(inv,10,134,52, 0));
        this.addSlot(new CoolSlot(inv,11,152,52, 0));

        // Player Inventory, Slot 9-35, Slot IDs 12-38
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        
        // Player Inventory, Slot 0-8, Slot IDs 39-47
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int fromSlot) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            result = current.copy();

            if (fromSlot < 12) {
                if (!this.mergeItemStack(current, 12, 48, true))
                    return ItemStack.EMPTY;
            } else if (!isLocked) {
                boolean merged = false;
                for (int i = 0; i < 4 && !current.isEmpty() ; ++i) {
                    if (this.mergeItemStack(current, 0, 4, false))
                        merged = true;
                }

                if (!merged) return ItemStack.EMPTY;
            }
            else return ItemStack.EMPTY; //locked

            if (current.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        
        return result;
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return this.inventory.isUsableByPlayer(player);
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.inventory.closeInventory(player);
    }
}
