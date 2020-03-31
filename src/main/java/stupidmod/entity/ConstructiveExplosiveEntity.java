package stupidmod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;

public class ConstructiveExplosiveEntity extends ExplosiveEntity {
    BlockState blockState;
    
    public ConstructiveExplosiveEntity(EntityType<? extends ConstructiveExplosiveEntity> type, World world) {
        super(type, world);
    }
    
    public ConstructiveExplosiveEntity(World world, double x, double y, double z, int fuse, int strength, BlockState state)
    {
        super(StupidModEntities.CONSTRUCTIVE_EXPLOSIVE, world, x, y, z, fuse, strength);
        
        this.blockState = state;
    }
    
    @Override
    protected void onFuseCompleted() {
        this.remove();
        
        if (!this.world.isRemote)
        {
            Explosion explosion = new Explosion(world, this, this.getPosX(), this.getPosY() + (double)(this.getHeight() / 16.0F), this.getPosZ(), this.strength, false, Explosion.Mode.NONE);
            explosion.doExplosionA();
            explosion.doExplosionB(true);

            for(BlockPos pos : explosion.getAffectedBlockPositions())
            {
                world.setBlockState(pos, this.blockState);
            }

            //ExplosionConstructive explosion = new ExplosionConstructive(this.world, null, this.getPosX(), this.getPosY(), this.getPosZ(), this.strength, false, true, this.blockState);

            //if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.world, explosion.doppelganger)) return;
            //explosion.doExplosionA();
            //explosion.doExplosionB(true);
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
