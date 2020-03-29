package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModItems;

import java.util.List;
import java.util.function.Predicate;

public class PooBrickEntity extends Entity {
    private static final Predicate<Entity> HIT_PREDICATE = EntityPredicates.NOT_SPECTATING.and(EntityPredicates.IS_ALIVE.and(Entity::canBeCollidedWith));

    public float prevAngleX, prevAngleY, prevAngleZ, angleX, angleY, angleZ, spinX, spinY, spinZ;

    public PooBrickEntity(EntityType<? extends PooBrickEntity> type, World worldIn) {
        super(type, worldIn);
        this.preventEntitySpawning = true;
        this.spinX = world.rand.nextFloat() * 30 - 15;
        this.spinY = world.rand.nextFloat() * 20 - 10;
        this.spinZ = world.rand.nextFloat() * 30 - 15;
    }

    public void breakPoo() {
        this.remove();
        this.entityDropItem(new ItemStack(StupidModItems.POO_BRICK), 0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }


    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!(source.getTrueSource() instanceof PlayerEntity)) {
            this.remove();
            return false;
        }
        PlayerEntity p = (PlayerEntity)source.getTrueSource();
        if (!this.world.isRemote)
            if (!p.isCreative())
                this.breakPoo();
            else
                this.remove();

        return true;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevAngleX = this.angleX;
        this.prevAngleY = this.angleY;
        this.prevAngleZ = this.angleZ;

        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0d, -0.04d, 0d));
        }

        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98d));

        if (this.onGround)
        {
            this.setMotion(this.getMotion().mul(0.7d, -0.5d, 0.7d));

            this.angleX = 0;
            this.angleZ = 0;
        }
        else
        {
            this.angleX += this.spinX;
            this.angleY += this.spinY;
            this.angleZ += this.spinZ;

            List<Entity> ents = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), HIT_PREDICATE);

            for (Entity ent : ents)
            {
                ent.attackEntityFrom(DamageSource.FALLING_BLOCK, 2);
            }
        }
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
