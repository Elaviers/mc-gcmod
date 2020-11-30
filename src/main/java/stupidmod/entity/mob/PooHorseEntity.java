package stupidmod.entity.mob;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;
import stupidmod.StupidModSounds;
import stupidmod.entity.PooEntity;

public class PooHorseEntity extends HorseEntity {
    int PooDropTimer;

    public PooHorseEntity(EntityType<? extends PooHorseEntity> type, World world) {
        super(type, world);
        PooDropTimer = this.rand.nextInt(600) + 160;
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.world.isRemote && --PooDropTimer <= 0) {
            this.playSound(StupidModSounds.FART, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.F + 0.1F);

            this.world.addEntity(new PooEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ()));
            this.PooDropTimer = this.rand.nextInt(600) + 600;
        }
    }

    //createChild
    @Override
    public PooHorseEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
        return StupidModEntities.POO_HORSE.create(world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
