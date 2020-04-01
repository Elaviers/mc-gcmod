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
            Explosion explosion = this.world.createExplosion(this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ, this.strength, false, Explosion.Mode.BREAK);

            for(BlockPos pos : explosion.getAffectedBlockPositions())
            {
                world.setBlockState(pos, this.blockState);
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
