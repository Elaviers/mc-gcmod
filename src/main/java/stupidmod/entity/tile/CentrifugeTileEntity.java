package stupidmod.entity.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import stupidmod.StupidMod;
import stupidmod.StupidModEntities;
import stupidmod.StupidModItems;
import stupidmod.item.PooItem;
import stupidmod.misc.CentrifugeContainer;

import javax.annotation.Nullable;

public class CentrifugeTileEntity extends LockableLootTileEntity implements ITickableTileEntity {
    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> CAP_ITEM_HANDLER;

    private NonNullList<ItemStack> inventory;
    
    public float prevAngle;
    public float angle;
    
    private float rotationRate;
    private float rateTarget;
    
    private int timer;
    public static final int timerMax = 200;
    
    public CentrifugeTileEntity() {
        super(StupidModEntities.TE_CENTRIFUGE);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        this.rateTarget = -1000;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tileentity.stupidmod.centrifuge");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CentrifugeContainer(id, player, this, this);
    }

    @Override
    public int getSizeInventory() {
        return 12;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < items.size(); i++)
        {
            if (i < this.inventory.size())
            {
                this.getItems().set(i, items.get(i));
            }
        }
    }

    @Override
    public void tick() {
        if (this.rateTarget == -1000)
        {
            if (this.world.isBlockPowered(this.pos))
            {
                this.rateTarget = (float)(Math.PI * 2.5f / 20);
            }
            else
                this.rateTarget = 0;
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

        if (timer >= 0 && rateTarget != 0)
            timer--;
        else if (timer < timerMax && rateTarget == 0)
            timer += 10;

        if (timer == 0 && !this.world.isRemote)
            this.applyProcess();

        angle += rotationRate;
    }


    ////////////////////////

    private void applyProcess() {
        for (int i = 0;i < 4;i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (stack != null && stack.getItem() == StupidModItems.FERMENTED_POO) {
                int pos1 = -1,pos2 = -1;
                for (int pos = 0;pos < 4;pos++) {
                    if (this.getStackInSlot(pos+4).isEmpty() && pos1 < 0)
                        pos1 = pos+4;
                    if (this.getStackInSlot(pos+8).isEmpty() && pos2 < 0)
                        pos2 = pos+8;

                    if (pos1 > 0 && pos2 > 0) {
                        this.setInventorySlotContents(i, ItemStack.EMPTY);
                        this.setInventorySlotContents(pos1, new ItemStack(StupidModItems.POO_POWDER));
                        this.setInventorySlotContents(pos2, new ItemStack(StupidModItems.POO_PROTEIN));
                        break;
                    }
                }
            }
        }
    }

    public boolean isSpinning()
    {
        return this.rateTarget != 0;
    }

    public int getRemainingTicks() { return this.timer; }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 69) {
            if (type == 0)
            {
                this.rateTarget = 0.f;
                return true;
            }

            this.rateTarget = (float)(Math.PI * 2.5f / 20);
            this.timer = timerMax;

            return true;
        }

        return false;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }

        return compound;
    }

    @Override
    public void onLoad() {
        StupidMod.proxy.clUpdateCentrifugeSound(this);

        super.onLoad();
    }


}
