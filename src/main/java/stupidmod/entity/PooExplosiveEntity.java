package stupidmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;

public class PooExplosiveEntity extends ImpactExplosiveEntity {
    public PooExplosiveEntity(EntityType<? extends PooExplosiveEntity> type, World world) {
        super(StupidModEntities.POO_EXPLOSIVE, world);
    }

    public PooExplosiveEntity(World world, int x, int y, int z, int explosionRadius) {
        super(StupidModEntities.POO_EXPLOSIVE, world, x, y, z, explosionRadius);
    }
    
    @Override
    protected void registerData() {
    
    }
    
    @Override
    protected void readAdditional(CompoundNBT compound) {
    
    }
    
    @Override
    protected void writeAdditional(CompoundNBT compound) {
    
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
