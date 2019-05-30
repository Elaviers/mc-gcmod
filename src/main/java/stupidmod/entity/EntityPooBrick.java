package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stupidmod.EntityRegister;

public class EntityPooBrick extends Entity {
    public EntityPooBrick(World world) {
        super(EntityRegister.entityPooBrick, world);
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
