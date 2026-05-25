package gcmod.entity;

import gcmod.CentrifugeScreenHandler;
import gcmod.GCMod;
import gcmod.block.PooBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class CentrifugeEntity extends BaseContainerBlockEntity implements WorldlyContainer
{
    protected final ContainerData propertyDelegate = new ContainerData()
    {
        @Override
        public int get( int index )
        {
            return CentrifugeEntity.PROCESS_DURATION_TICKS - CentrifugeEntity.this.ticksUntilDone;
        }

        @Override
        public void set( int index, int value )
        {
            assert false; // no thanks
        }

        @Override
        public int getCount()
        {
            return 1;
        }
    };

    public final static int PROCESS_DURATION_TICKS = 200;
    private final static float SPIN_WINDUP_PER_TICK = 0.02f;
    private final static float SPIN_WINDDOWN_PER_TICK = 0.01f;

    public boolean isPowered;

    public float spinRate;
    public float angle, prevAngle;

    public int ticksUntilDone;

    @Environment( EnvType.CLIENT )
    public boolean startedAudio;

    private NonNullList<ItemStack> inventory = NonNullList.withSize( 6, ItemStack.EMPTY );

    private static final int[] SPIN_SLOTS = new int[]{ 0, 1, 2, 3 };
    private static final int[] OUTPUT_SLOTS = new int[]{ 4, 5 };

    public CentrifugeEntity( BlockPos pos, BlockState state )
    {
        super( GCMod.CENTRIFUGE_ENTITY, pos, state );
        this.ticksUntilDone = PROCESS_DURATION_TICKS;
    }


    @Override
    protected Component getDefaultName()
    {
        return Component.translatable( "tileentity.gcmod.centrifuge" );
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return this.inventory;
    }

    @Override
    protected void setItems( NonNullList<ItemStack> inventory )
    {
        this.inventory = inventory;
    }

    @Override
    protected AbstractContainerMenu createMenu( int syncId, Inventory playerInventory )
    {
        return new CentrifugeScreenHandler( syncId, playerInventory, this, this.propertyDelegate );
    }

    @Override
    public int[] getSlotsForFace( Direction side )
    {
        if ( side != Direction.DOWN )
            return SPIN_SLOTS;

        else return OUTPUT_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace( int slot, ItemStack stack, @Nullable Direction dir )
    {
        if ( this.spinRate > 0.01 || slot >= 4 || dir == Direction.DOWN || !this.getItem( slot ).isEmpty() )
            return false;

        return stack.is( GCMod.FERMENTED_POO );
    }

    @Override
    public boolean canTakeItemThroughFace( int slot, ItemStack stack, Direction dir )
    {
        return stack.is( GCMod.POO_POWDER ) || stack.is( GCMod.POO_PROTEIN );
    }

    @Override
    public int getContainerSize()
    {
        return this.inventory.size();
    }

    void convertInventory()
    {
        for ( int i = 0; i < 4; i++ )
        {
            ItemStack stack = this.getItem( i );
            if ( stack.getItem() == GCMod.FERMENTED_POO )
            {
                ItemStack powderStack = this.getItem( 4 );
                ItemStack proteinStack = this.getItem( 5 );

                if ( powderStack.isEmpty() )
                    powderStack = new ItemStack( GCMod.POO_POWDER );
                else
                {
                    if ( powderStack.getCount() >= powderStack.getMaxStackSize() )
                        return;

                    powderStack.setCount( powderStack.getCount() + 1 );
                }

                if ( proteinStack.isEmpty() )
                    proteinStack = new ItemStack( GCMod.POO_PROTEIN );
                else
                {
                    if ( proteinStack.getCount() >= proteinStack.getMaxStackSize() )
                        return;

                    proteinStack.setCount( proteinStack.getCount() + 1 );
                }

                this.setItem( i, ItemStack.EMPTY );
                this.setItem( 4, powderStack );
                this.setItem( 5, proteinStack );
                this.setChanged();
            }
        }
    }

    public void setPowered( boolean powered )
    {
        if ( this.isPowered == powered )
            return;

        this.isPowered = powered;

        if ( !getLevel().isClientSide() )
        {
            // update clients:
            getLevel().sendBlockUpdated( this.worldPosition, this.getBlockState(), this.getBlockState(), PooBlock.UPDATE_CLIENTS );
        }

        // btw, sound is handled via a client mixin that picks up BlockEntityUpdateS2CPacket. see the CentrifugeSound.updateSoundForEntity flow
    }

    public static void tick( Level world, BlockPos blockPos, BlockState blockState, CentrifugeEntity centrifuge )
    {
        if ( centrifuge.isPowered )
        {
            if ( centrifuge.spinRate < 1f )
                centrifuge.spinRate += SPIN_WINDUP_PER_TICK;
        }
        else if ( centrifuge.spinRate > 0f )
            centrifuge.spinRate = Math.max( 0f, centrifuge.spinRate - SPIN_WINDDOWN_PER_TICK );

        if ( centrifuge.spinRate <= 0f )
            centrifuge.ticksUntilDone = PROCESS_DURATION_TICKS;
        else
            centrifuge.ticksUntilDone--;

        if ( world.isClientSide() )
        {
            centrifuge.prevAngle = centrifuge.angle;
            centrifuge.angle = centrifuge.prevAngle + centrifuge.spinRate * 40f;
        }
        else if ( centrifuge.ticksUntilDone == 0 )
            centrifuge.convertInventory();
    }

    //

    @Override
    protected void loadAdditional( ValueInput view )
    {
        super.loadAdditional( view );
        this.inventory = NonNullList.withSize( this.getContainerSize(), ItemStack.EMPTY );
        ContainerHelper.loadAllItems( view, this.inventory );
        this.isPowered = ( view.getBooleanOr( "Powered", false ) );
    }

    @Override
    protected void saveAdditional( ValueOutput view )
    {
        super.saveAdditional( view );
        ContainerHelper.saveAllItems( view, this.inventory );
        view.putBoolean( "Powered", this.isPowered );
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create( this );
    }

    @Override
    public CompoundTag getUpdateTag( HolderLookup.Provider registryLookup )
    {
        return this.saveCustomOnly( registryLookup );
    }
}
