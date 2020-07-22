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
        private CentrifugeTileEntity ent;
        
        public CoolSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, CentrifugeTileEntity ent) {
            super(inventoryIn, index, xPosition, yPosition);
            
            this.ent = ent;
        }

        @Override
        public int getSlotStackLimit() {
            return ent == null ? 0 : (ent.isSpinning() ? 0 : 1);
        }
    
        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == StupidModItems.FERMENTED_POO;
        }
    }
    
    public CentrifugeTileEntity ent;

    IInventory inventory;

    public CentrifugeContainer(int id, PlayerInventory playerInventory, PacketBuffer data)
    {
        this(id, playerInventory, new Inventory(6), (CentrifugeTileEntity) playerInventory.player.world.getTileEntity(data.readBlockPos()));
    }
    
    public CentrifugeContainer(int id, PlayerInventory playerInventory, IInventory inv, CentrifugeTileEntity tileentity) {
        super(StupidModContainers.CENTRIFUGE, id);
        assertInventorySize(inv, 6);

        this.inventory = inv;
        this.inventory.openInventory(playerInventory.player);

        this.ent = tileentity;

        this.addSlot(new CoolSlot(inv,0,80,14, ent));
        this.addSlot(new CoolSlot(inv,1,60,34, ent));
        this.addSlot(new CoolSlot(inv,2,100,34, ent));
        this.addSlot(new CoolSlot(inv,3,80,54, ent));
        this.addSlot(new CoolSlot(inv,4,8,52, null));
        this.addSlot(new CoolSlot(inv,5,152,52, null));

        // Player Inventory, Slot 9-35, Slot IDs 6-32
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        
        // Player Inventory, Slot 0-8, Slot IDs 33-41
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

            if (fromSlot < 6) {
                if (!this.mergeItemStack(current, 6, 42, true))
                    return ItemStack.EMPTY;
            } else if (!ent.isSpinning()) {
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
