package gcmod.entity;

import gcmod.ConstructiveExplosionImpl;
import gcmod.GCMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Optional;

public class ConstructiveExplosiveEntity extends ExplosiveEntity
{
    public BlockState createdBlock;

    public ConstructiveExplosiveEntity( EntityType<?> type, World world )
    {
        super( type, world );
    }

    @Override
    protected void onFuseComplete()
    {
        if ( !getWorld().isClient )
        {
            this.discard();

            ServerWorld world = (ServerWorld)getWorld();

            Vec3d vec3d = new Vec3d(this.getX(), this.getY() + (double) (this.getHeight() / 16.0F), this.getZ());
            ConstructiveExplosionImpl explosion = new ConstructiveExplosionImpl(world, this, null, null, vec3d, this.explosionRadius, this.createdBlock);
            explosion.explode();
            ParticleEffect particleEffect = explosion.isSmall() ? ParticleTypes.EXPLOSION : ParticleTypes.EXPLOSION_EMITTER;

            for ( ServerPlayerEntity serverPlayerEntity : world.getPlayers() ) {
                if (serverPlayerEntity.squaredDistanceTo(vec3d) < 4096.0) {
                    Optional<Vec3d> optional = Optional.ofNullable((Vec3d)explosion.getKnockbackByPlayer().get(serverPlayerEntity));
                    serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(vec3d, optional, particleEffect, SoundEvents.ENTITY_GENERIC_EXPLODE));
                }
            }
        }
    }

    @Override
    public BlockState getBlockState()
    {
        return GCMod.CONSTRUCTIVE_TNT.getDefaultState();
    }
}
