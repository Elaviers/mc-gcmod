package stupidmod.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
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
import stupidmod.misc.CentrifugeContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CentrifugeTileEntity extends LockableLootTileEntity implements ISidedInventory, ITickableTileEntity {
    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> CAP_ITEM_HANDLER = null;

    private NonNullList<ItemStack> inventory;

    private final IItemHandler itemHandler= new IItemHandler() {
        @Override
        public int getSlots() {
            return 6;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return inventory.get(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot < 4 && !isSpinning() && stack.getItem() == StupidModItems.FERMENTED_POO && inventory.get(slot).isEmpty())
            {
                ItemStack remaining = stack.copy();
                remaining.setCount(remaining.getCount() - 1);

                if (!simulate)
                    inventory.set(slot, new ItemStack(StupidModItems.FERMENTED_POO));

                return remaining;
            }

            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < 4) return ItemStack.EMPTY;

            ItemStack stack = inventory.get(slot);
            ItemStack split = stack.copy();
            if (split.getCount() > amount)
                split.setCount(amount);

            if (!simulate)
            {
                if (amount < stack.getCount())
                    stack.setCount(stack.getCount() - amount);
                else
                    inventory.set(slot, ItemStack.EMPTY);
            }

            return split;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot < 4 ? 1 : 0;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return slot < 4 && stack.getItem() == StupidModItems.FERMENTED_POO;
        }
    };

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
        this.timer = timerMax;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CAP_ITEM_HANDLER)
            return LazyOptional.of(() -> itemHandler).cast();

        return super.getCapability(cap, side);
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
        return 6;
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
            if (stack.getItem() == StupidModItems.FERMENTED_POO) {
                ItemStack powderStack = this.getStackInSlot(4);
                ItemStack proteinStack = this.getStackInSlot(5);

                if (powderStack.isEmpty())
                    powderStack = new ItemStack(StupidModItems.POO_POWDER);
                else
                {
                    if (powderStack.getCount() >= powderStack.getMaxStackSize())
                        return;

                    powderStack.setCount(powderStack.getCount() + 1);
                }

                if (proteinStack.isEmpty())
                    proteinStack = new ItemStack(StupidModItems.POO_PROTEIN);
                else
                {
                    if (proteinStack.getCount() >= proteinStack.getMaxStackSize())
                        return;

                    proteinStack.setCount(proteinStack.getCount() + 1);
                }

                this.setInventorySlotContents(i, ItemStack.EMPTY);
                this.setInventorySlotContents(4, powderStack);
                this.setInventorySlotContents(5, proteinStack);
            }
        }
    }

    public boolean isSpinning()
    {
        return this.rotationRate > 0.01f;
    }

    public float getRotationRateTarget()
    {
        return this.rateTarget;
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

    //read
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
        super.onLoad();

        if (world.isRemote)
            StupidMod.proxy.clUpdateCentrifugeSound(this);
    }



    //The sidedInventory stuff only gets called when the capability fails, don't let it do anything

    private static final int[] empty = {};

    @Override
    public int[] getSlotsForFace(Direction side) {
        return empty;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }
}
