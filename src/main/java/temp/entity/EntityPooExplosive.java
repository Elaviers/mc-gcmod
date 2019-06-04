package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stupidmod.EntityRegister;

public class EntityPooExplosive extends EntityImpactExplosive {
    public EntityPooExplosive(World world) {
        super(EntityRegister.entityPooExplosive, world);
    }

    public EntityPooExplosive(World world, int x, int y, int z, int explosionRadius) {
        super(EntityRegister.entityPooExplosive, world, x, y, z, explosionRadius);
    }
    
    @Override
    protected void registerData() {
    
    }
    
    @Override
    protected void readAdditional(NBTTagCompound compound) {
    
    }
    
    @Override
    protected void writeAdditional(NBTTagCompound compound) {
    
    }
}
