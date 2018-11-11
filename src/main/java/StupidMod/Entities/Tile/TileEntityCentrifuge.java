package StupidMod.Entities.Tile;

import StupidMod.Items.ItemPoo;
import StupidMod.StupidMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class TileEntityCentrifuge extends TileEntity implements ITickable, IInventory {
    private String customName;
    private NonNullList<ItemStack> inventory;
    
    private boolean hasSound;
    private boolean spinning;
    
    public float prevAngle;
    public float angle;
    
    private float rotationRate;
    private float rateTarget;
    
    private int timer;
    public static final int timerMax = 200;
    
    public TileEntityCentrifuge()
    {
        inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    }
    
    @Override
    public void update() {
        if (!hasSound && this.world.isRemote) {
            this.hasSound = true;
            StupidMod.proxy.createSoundForCentrifuge(this);
        }
    
        prevAngle = angle;
    
        if (rotationRate < rateTarget) {
            if (rateTarget != 0)
                rotationRate += .01f;
            else
                rotationRate = 0;
        }
        else if (rotationRate > 0)
            rotationRate -= 0.01f;
    
        if (timer >= 0 && spinning)
            timer--;
        else if (timer < timerMax && !spinning)
            timer += 10;
    
        if (timer == 0 && !this.world.isRemote)
            this.applyProcess();
    
        angle += rotationRate;
    }
    
    private void applyProcess() {
        for (int i = 0;i < 4;i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemPoo) {
                int pos1 = -1,pos2 = -1;
                for (int pos = 0;pos < 4;pos++) {
                    if (this.getStackInSlot(pos+4).isEmpty() && pos1 < 0)
                        pos1 = pos+4;
                    if (this.getStackInSlot(pos+8).isEmpty() && pos2 < 0)
                        pos2 = pos+8;
                    
                    if (pos1 > 0 && pos2 > 0) {
                        this.setInventorySlotContents(i, null);
                        this.setInventorySlotContents(pos1, new ItemStack(StupidMod.instance.items.itemPooPowder));
                        this.setInventorySlotContents(pos2, new ItemStack(StupidMod.instance.items.itemPooProtein));
                        break;
                    }
                }
            }
        }
    }
    
    public boolean isSpinning()
    {
        return this.spinning;
    }
    
    public int getRemainingTicks() { return this.timer; }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "tileentity.centrifuge.name";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }
    
    public void setCustomName(String name) {
        this.customName = name;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items", list);
        
        if (this.hasCustomName()) {
            nbt.setString("CustomName", this.customName);
        }
        
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        
        NBTTagList list = nbt.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, new ItemStack(stackTag));
        }
        
        if (nbt.hasKey("CustomName", 8)) {
            this.customName = (nbt.getString("CustomName"));
        }
    }
    
    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 69) {
            if (type == 0)
            {
                this.rateTarget = 0.f;
                this.spinning = false;
                return true;
            }
    
            this.rateTarget = (float)(Math.PI * 2.5f / 20);
            this.timer = timerMax;
            this.spinning = true;
            return true;
        }
        
        return false;
    }
    
    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentTranslation(this.getName()) : new TextComponentTranslation(this.getName());
    }
    
    //Inventory
    
    @Override
    public int getSizeInventory() {
        return 12;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }
    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (stack == null) this.inventory.set(index, ItemStack.EMPTY);
        else this.inventory.set(index, stack);
        
        this.markDirty();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }
        else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }
    
    @Override
    public void openInventory(EntityPlayer player) {
    
    }
    
    @Override
    public void closeInventory(EntityPlayer player) {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() == StupidMod.instance.items.itemPoo;
    }
    
    @Override
    public int getField(int id) {
        return 0;
    }
    
    @Override
    public void setField(int id, int value) {
    
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        this.inventory.clear();
    }
}
