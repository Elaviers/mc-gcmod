package gcmod.entity;

import gcmod.CentrifugeScreenHandler;
import gcmod.GCMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CentrifugeEntity extends LockableContainerBlockEntity implements SidedInventory
{
    protected final PropertyDelegate propertyDelegate = new PropertyDelegate()
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
        public int size()
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

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize( 6, ItemStack.EMPTY );

    private static final int[] SPIN_SLOTS = new int[]{ 0, 1, 2, 3 };
    private static final int[] OUTPUT_SLOTS = new int[]{ 4, 5 };

    public CentrifugeEntity( BlockPos pos, BlockState state )
    {
        super( GCMod.CENTRIFUGE_ENTITY, pos, state );
        this.ticksUntilDone = PROCESS_DURATION_TICKS;
    }


    @Override
    protected Text getContainerName()
    {
        return Text.translatable( "tileentity.gcmod.centrifuge" );
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks()
    {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks( DefaultedList<ItemStack> inventory )
    {
        this.inventory = inventory;
    }

    @Override
    protected ScreenHandler createScreenHandler( int syncId, PlayerInventory playerInventory )
    {
        return new CentrifugeScreenHandler( syncId, playerInventory, this, this.propertyDelegate );
    }

    @Override
    public int[] getAvailableSlots( Direction side )
    {
        if ( side != Direction.DOWN )
            return SPIN_SLOTS;

        else return OUTPUT_SLOTS;
    }

    @Override
    public boolean canInsert( int slot, ItemStack stack, @Nullable Direction dir )
    {
        if ( this.spinRate > 0.01 || slot >= 4 || dir == Direction.DOWN )
            return false;

        return stack.isOf( GCMod.FERMENTED_POO );
    }

    @Override
    public boolean canExtract( int slot, ItemStack stack, Direction dir )
    {
        return stack.isOf( GCMod.POO_POWDER ) || stack.isOf( GCMod.POO_PROTEIN );
    }

    @Override
    public int size()
    {
        return this.inventory.size();
    }

    void convertInventory()
    {
        for ( int i = 0; i < 4; i++ )
        {
            ItemStack stack = this.getStack( i );
            if ( stack.getItem() == GCMod.FERMENTED_POO )
            {
                ItemStack powderStack = this.getStack( 4 );
                ItemStack proteinStack = this.getStack( 5 );

                if ( powderStack.isEmpty() )
                    powderStack = new ItemStack( GCMod.POO_POWDER );
                else
                {
                    if ( powderStack.getCount() >= powderStack.getMaxCount() )
                        return;

                    powderStack.setCount( powderStack.getCount() + 1 );
                }

                if ( proteinStack.isEmpty() )
                    proteinStack = new ItemStack( GCMod.POO_PROTEIN );
                else
                {
                    if ( proteinStack.getCount() >= proteinStack.getMaxCount() )
                        return;

                    proteinStack.setCount( proteinStack.getCount() + 1 );
                }

                this.setStack( i, ItemStack.EMPTY );
                this.setStack( 4, powderStack );
                this.setStack( 5, proteinStack );
                this.markDirty();
            }
        }
    }

    public void setPowered( boolean powered )
    {
        if ( this.isPowered == powered )
            return;

        this.isPowered = powered;

        if ( !getWorld().isClient )
        {
            // update clients:
            getWorld().updateListeners( this.pos, this.getCachedState(), this.getCachedState(), PooBlock.NOTIFY_LISTENERS );
        }

        // btw, sound is handled via a client mixin that picks up BlockEntityUpdateS2CPacket. see the CentrifugeSound.updateSoundForEntity flow
    }

    public static void tick( World world, BlockPos blockPos, BlockState blockState, CentrifugeEntity centrifuge )
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

        if ( world.isClient )
        {
            centrifuge.prevAngle = centrifuge.angle;
            centrifuge.angle = centrifuge.prevAngle + centrifuge.spinRate * 40f;
        }
        else if ( centrifuge.ticksUntilDone == 0 )
            centrifuge.convertInventory();
    }

    //

    @Override
    protected void readNbt( NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup )
    {
        super.readNbt( nbt, registryLookup );
        this.inventory = DefaultedList.ofSize( this.size(), ItemStack.EMPTY );
        Inventories.readNbt( nbt, this.inventory, registryLookup );
        //this.setPowered( nbt.getBoolean( "Powered" ) );
        this.isPowered = ( nbt.getBoolean( "Powered" ) );
    }

    @Override
    protected void writeNbt( NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup )
    {
        super.writeNbt( nbt, registryLookup );
        Inventories.writeNbt( nbt, this.inventory, registryLookup );
        nbt.putBoolean( "Powered", this.isPowered );
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket()
    {
        return BlockEntityUpdateS2CPacket.create( this );
    }

    @Override
    public NbtCompound toInitialChunkDataNbt( RegistryWrapper.WrapperLookup registryLookup )
    {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt( nbt, registryLookup );
        return nbt;
    }
}
