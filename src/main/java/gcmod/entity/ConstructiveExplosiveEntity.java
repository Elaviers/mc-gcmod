package gcmod.entity;

import gcmod.ConstructiveExplosionImpl;
import gcmod.GCMod;
import java.util.Optional;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class ConstructiveExplosiveEntity extends ExplosiveEntity
{
    public BlockState createdBlock;

    public ConstructiveExplosiveEntity( EntityType<?> type, Level world )
    {
        super( type, world );
    }

    @Override
    protected void onFuseComplete()
    {
        if ( !level().isClientSide() )
        {
            this.discard();

            ServerLevel world = (ServerLevel) level();

            Vec3 pos = new Vec3( this.getX(), this.getY() + (double) (this.getBbHeight() / 16.0F), this.getZ() );
            ConstructiveExplosionImpl explosion = new ConstructiveExplosionImpl( world, this, null, null, pos, this.explosionRadius, this.createdBlock );
            int numBlocks = explosion.explode();
            ParticleOptions particleEffect = explosion.isSmall() ? ParticleTypes.EXPLOSION : ParticleTypes.EXPLOSION_EMITTER;

            for ( ServerPlayer serverPlayerEntity : world.players() )
            {
                if ( serverPlayerEntity.distanceToSqr( pos ) < 4096.0 )
                {
                    WeightedList<ExplosionParticleInfo> explosionParticles = WeightedList.<ExplosionParticleInfo>builder()
                            .add(new ExplosionParticleInfo(ParticleTypes.POOF, 0.5F, 1.0F))
                            .add(new ExplosionParticleInfo(ParticleTypes.SMOKE, 1.0F, 1.0F))
                            .build();

                    Optional<Vec3> knockback = Optional.ofNullable( (Vec3) explosion.getHitPlayers().get( serverPlayerEntity ) );
                    serverPlayerEntity.connection.send( new ClientboundExplodePacket( pos, this.explosionRadius, numBlocks, knockback, particleEffect, SoundEvents.GENERIC_EXPLODE, explosionParticles ) );
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData( ValueInput view )
    {
        super.readAdditionalSaveData( view );
        view.read( "Block", BlockState.CODEC ).ifPresentOrElse( bs -> this.createdBlock = bs, () -> this.createdBlock = Blocks.STONE.defaultBlockState() );
    }

    @Override
    protected void addAdditionalSaveData( ValueOutput view )
    {
        super.addAdditionalSaveData( view );
        view.store( "Block", BlockState.CODEC, this.createdBlock );
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.CONSTRUCTIVE_TNT.defaultBlockState();
    }
}
