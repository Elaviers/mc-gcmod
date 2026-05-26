package gcmod.entity;

import gcmod.ExplosiveBlockComponent;
import gcmod.GCMod;
import gcmod.block.ExplosiveBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class ExplosiveBlockEntity extends BlockEntity
{
    private ExplosiveBlockComponent explosiveInfo;

    public ExplosiveBlockEntity( BlockEntityType<?> type, BlockPos pos, BlockState state )
    {
        super( type, pos, state );
        this.explosiveInfo = new ExplosiveBlockComponent( 0, 2, Blocks.AIR.defaultBlockState(), 0, 0, 0 );
    }

    public void setExplosiveInfo( ExplosiveBlockComponent info )
    {
        this.explosiveInfo = info;
    }

    @Override
    protected void loadAdditional( ValueInput view )
    {
        super.loadAdditional( view );
        view.read( "Explosive", ExplosiveBlockComponent.CODEC ).ifPresent( ex -> this.explosiveInfo = ex );
    }

    @Override
    protected void saveAdditional( ValueOutput view )
    {
        super.saveAdditional( view );
        view.store( "Explosive", ExplosiveBlockComponent.CODEC, this.explosiveInfo );
    }

    public void explode( boolean chainReaction )
    {
        assert getLevel() != null;

        if ( this.getLevel().isClientSide() )
            return;

        Vec3 spawnPos = new Vec3( this.worldPosition.getX() + .5f, this.worldPosition.getY(), this.worldPosition.getZ() + .5f );

        int fuse = this.explosiveInfo.fuse();
        if ( chainReaction )
            fuse = (short)(fuse * getLevel().getRandom().nextFloat() * .5f);

        if ( getType() == GCMod.BLAST_TNT_BLOCK_ENTITY )
        {
            ExplosiveEntity tnt = GCMod.EXPLOSIVE_ENTITY.create( getLevel(), EntitySpawnReason.TRIGGERED );
            tnt.setPos( spawnPos );
            tnt.explosionRadius = this.explosiveInfo.strength();
            tnt.startFuse( fuse );

            if ( fuse > 0 )
            {
                this.getLevel().addFreshEntity( tnt );
                this.getLevel().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1f, 1f );
            }
        }
        else if ( getType() == GCMod.CONSTRUCTIVE_TNT_BLOCK_ENTITY )
        {
            ConstructiveExplosiveEntity tnt = GCMod.CONSTRUCTIVE_EXPLOSIVE_ENTITY.create( getLevel(), EntitySpawnReason.TRIGGERED );
            tnt.setPos( spawnPos );
            tnt.explosionRadius = this.explosiveInfo.strength();
            tnt.createdBlock = this.explosiveInfo.block() == null ? Blocks.AIR.defaultBlockState() : this.explosiveInfo.block();
            tnt.startFuse( fuse );

            if ( fuse > 0 )
            {
                this.getLevel().addFreshEntity( tnt );
                this.getLevel().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1f, 1f );
            }
        }
        else if ( getType() == GCMod.DIG_TNT_BLOCK_ENTITY )
        {
            DigExplosiveEntity tnt = GCMod.DIG_EXPLOSIVE_ENTITY.create( getLevel(), EntitySpawnReason.TRIGGERED );
            tnt.setPos( spawnPos );
            tnt.explosionRadius = this.explosiveInfo.strength();
            tnt.startFuse( fuse );

            level.addFreshEntity( tnt );
            this.getLevel().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1f, 1f );
        }
        else if ( getType() == GCMod.AIRSTRIKE_TNT_BLOCK_ENTITY )
        {
            AirstrikeExplosiveEntity tnt = AirstrikeExplosiveEntity.create( level, spawnPos.x, spawnPos.y, spawnPos.z,
                    this.explosiveInfo.strength(), this.explosiveInfo.spread(), this.explosiveInfo.pieces(), this.explosiveInfo.height() );
            tnt.startFuse( fuse );

            level.addFreshEntity( tnt );
            this.getLevel().playSound( null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1f, 1f );
        }
    }
}
