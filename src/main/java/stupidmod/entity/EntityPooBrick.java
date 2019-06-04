package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.RayTraceFluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import stupidmod.EntityRegister;
import stupidmod.ItemRegister;

import java.util.List;
import java.util.function.Predicate;

public class EntityPooBrick extends Entity {
    private static final Predicate<Entity> HIT_PREDICATE = EntitySelectors.NOT_SPECTATING.and(EntitySelectors.IS_ALIVE.and(Entity::canBeCollidedWith));

    public float prevAngleX, prevAngleY, prevAngleZ, angleX, angleY, angleZ, spinX, spinY, spinZ;

    public EntityPooBrick(World worldIn) {
        super(EntityRegister.entityPooBrick, worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.5f, 0.25f);
        this.spinX = world.rand.nextFloat() * 30 - 15;
        this.spinY = world.rand.nextFloat() * 20 - 10;
        this.spinZ = world.rand.nextFloat() * 30 - 15;
    }

    public void breakPoo() {
        this.remove();
        this.entityDropItem(new ItemStack(ItemRegister.itemPooBrick), 0);
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
        if (!(source.getTrueSource() instanceof EntityPlayer)) {
            this.remove();
            return false;
        }
        EntityPlayer p = (EntityPlayer)source.getTrueSource();
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
            this.motionY -= 0.04D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;

        if (this.onGround)
        {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
            this.motionY *= -0.5D;

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
    protected void readAdditional(NBTTagCompound compound) {
    
    }
    
    @Override
    protected void writeAdditional(NBTTagCompound compound) {
    
    }


}
