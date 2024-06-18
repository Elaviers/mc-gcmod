package gcmod.entity;

import gcmod.GCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ExplosiveBlockEntity extends BlockEntity
{
    private short fuse;
    private short strength;

    private BlockState constructState;

    private short airStrikeSpread;
    private short airStrikeHeight;
    private short airStrikePieces;

    public ExplosiveBlockEntity( BlockEntityType<?> type, BlockPos pos, BlockState state )
    {
        super( type, pos, state );
        this.strength = 2;
        this.fuse = 0;
    }

    public void readExplosiveInfo( NbtCompound nbt )
    {
        this.fuse = nbt.getShort( "Fuse" );
        this.strength = nbt.getShort( "Strength" );

        if ( nbt.contains( "Block" ) )
            this.constructState = NbtHelper.toBlockState( this.getWorld().createCommandRegistryWrapper( RegistryKeys.BLOCK ), nbt.getCompound( "Block" ) );

        if ( nbt.contains( "Spread" ) )
            this.airStrikeSpread = nbt.getShort( "Spread" );

        if ( nbt.contains( "Pieces" ) )
            this.airStrikePieces = nbt.getShort( "Pieces" );

        if ( nbt.contains( "Height" ) )
            this.airStrikeHeight = nbt.getShort( "Height" );
    }

    @Override
    protected void readNbt( NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup )
    {
        super.readNbt( nbt, registryLookup );

        readExplosiveInfo( nbt );
    }

    @Override
    protected void writeNbt( NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup )
    {
        super.writeNbt( nbt, registryLookup );

        nbt.putShort( "Fuse", this.fuse );
        nbt.putShort( "Strength", this.strength );

        if ( this.constructState != null )
            nbt.put( "Block", NbtHelper.fromBlockState( this.constructState ) );

        if ( this.airStrikeSpread > 0 )
            nbt.putShort( "Spread", this.airStrikeSpread );

        if ( this.airStrikePieces > 0 )
            nbt.putShort( "Pieces", this.airStrikePieces );

        if ( this.airStrikeHeight > 0 )
            nbt.putShort( "Height", this.airStrikeHeight );
    }

    public void explode( boolean chainReaction )
    {
        assert getWorld() != null;

        if ( this.getWorld().isClient )
            return;

        Vec3d spawnPos = new Vec3d( this.pos.getX() + .5f, this.pos.getY(), this.pos.getZ() + .5f );

        if ( chainReaction )
            fuse = (short)(fuse * getWorld().random.nextFloat() * .5f);

        if ( getType() == GCMod.BLAST_TNT_BLOCK_ENTITY )
        {
            ExplosiveEntity tnt = GCMod.EXPLOSIVE_ENTITY.create( getWorld() );
            tnt.setPosition( spawnPos );
            tnt.explosionRadius = this.strength;
            tnt.startFuse( fuse );

            if ( fuse > 0 )
            {
                this.getWorld().spawnEntity( tnt );
                this.getWorld().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1f, 1f );
            }
        }
        else if ( getType() == GCMod.CONSTRUCTIVE_TNT_BLOCK_ENTITY )
        {
            ConstructiveExplosiveEntity tnt = GCMod.CONSTRUCTIVE_EXPLOSIVE_ENTITY.create( getWorld() );
            tnt.setPosition( spawnPos );
            tnt.explosionRadius = strength;
            tnt.createdBlock = this.constructState == null ? Blocks.AIR.getDefaultState() : this.constructState;
            tnt.startFuse( fuse );

            if ( fuse > 0 )
            {
                this.getWorld().spawnEntity( tnt );
                this.getWorld().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1f, 1f );
            }
        }
        else if ( getType() == GCMod.DIG_TNT_BLOCK_ENTITY )
        {
            DigExplosiveEntity tnt = GCMod.DIG_EXPLOSIVE_ENTITY.create( getWorld() );
            tnt.setPosition( spawnPos );
            tnt.explosionRadius = strength;
            tnt.startFuse( fuse );

            world.spawnEntity( tnt );
            this.getWorld().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1f, 1f );
        }
        else if ( getType() == GCMod.AIRSTRIKE_TNT_BLOCK_ENTITY )
        {
            AirstrikeExplosiveEntity tnt = AirstrikeExplosiveEntity.create( world, spawnPos.x, spawnPos.y, spawnPos.z, strength, airStrikeSpread, airStrikePieces, airStrikeHeight );
            tnt.startFuse( fuse );

            world.spawnEntity( tnt );
            this.getWorld().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1f, 1f );
        }
    }
}
