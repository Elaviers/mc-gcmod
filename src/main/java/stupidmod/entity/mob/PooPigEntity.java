package stupidmod.entity.mob;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;
import stupidmod.StupidModSounds;
import stupidmod.entity.PooEntity;

public class PooPigEntity extends PigEntity {
    int PooDropTimer;
    
    public PooPigEntity(EntityType<? extends PooPigEntity> type, World world) {
        super(StupidModEntities.POO_PIG, world);
        PooDropTimer = this.rand.nextInt(800) + 160;
    }
    
    @Override
    public EntityType<?> getType() {
        return StupidModEntities.POO_PIG;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(StupidModSounds.FART, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.8F + 0.1F);
            
            this.world.addEntity(new PooEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ()));
            this.PooDropTimer = this.rand.nextInt(1400) + 300;
        }
    }

    //createChild
    @Override
    public PooPigEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
        return StupidModEntities.POO_PIG.create(world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
