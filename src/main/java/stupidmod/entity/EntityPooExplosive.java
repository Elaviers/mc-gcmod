package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stupidmod.EntityRegister;

public class EntityPooExplosive extends Entity {
    public EntityPooExplosive(World world) {
        super(EntityRegister.entityPooExplosive, world);
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
