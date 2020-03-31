package stupidmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import stupidmod.StupidModEntities;
import stupidmod.StupidModItems;

public class PooEntity extends Entity implements IEntityAdditionalSpawnData {
    
    public float size, prevSize;
    float targetSize;
    float shrinkRate;
    float timer;
    boolean doDrop;
    
    public PooEntity(EntityType<? extends PooEntity> type, World world) {
        super(type, world);
        this.preventEntitySpawning = true;

        this.shrinkRate = 0f;

        if (world.isRemote) {
            this.size = this.targetSize = 0.001f;
            this.timer = 100000f;
        }
        else
        {
            this.size = 0.001f;
            this.targetSize = this.world.rand.nextFloat() * .8f + .8f;
            this.timer = 1200 * this.targetSize;
        }
    }
    
    public PooEntity(World world, double x, double y, double z) {
        this(StupidModEntities.POO, world);

        this.setPosition(x, y, z);
    }

    void breakPoo() {
        this.remove();
        
        if (this.doDrop)
            this.entityDropItem(new ItemStack(StupidModItems.POO, this.targetSize > 1.1f ? 2 : 1));
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!(source.getTrueSource() instanceof PlayerEntity)) {
            this.doDrop = false;
            this.shrinkRate = size;
            return false;
        }
        
        PlayerEntity player = (PlayerEntity)source.getTrueSource();

        this.targetSize = this.size;

        if (player.isCreative()) {
            this.doDrop = false;
            this.shrinkRate = size / 2;
        }
        else {
            boolean Condition =
                    player.getHeldItemMainhand().getItem() == Items.WOODEN_SHOVEL ||
                            player.getHeldItemMainhand().getItem() == Items.STONE_SHOVEL ||
                            player.getHeldItemMainhand().getItem() == Items.IRON_SHOVEL ||
                            player.getHeldItemMainhand().getItem() == Items.GOLDEN_SHOVEL ||
                            player.getHeldItemMainhand().getItem() == Items.DIAMOND_SHOVEL;
            
            if (Condition) {
                if (!this.world.isRemote) player.getHeldItemMainhand().damageItem(1, player, null);
                this.doDrop = true;
                this.shrinkRate = size / 3;
            }
        }
        return true;
    }
    
    @Override
    public void tick() {
        if (!this.world.isRemote && this.size <= 0)
            this.breakPoo();
        
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevSize = this.size;
        
        if (this.shrinkRate > 0)
            this.size -= this.shrinkRate;
        else if (this.size < this.targetSize) {
            this.size += 0.1f;
            
            if (this.size >= this.targetSize)
                this.shrinkRate = 0.01f / 20;
        }
        
        if (!this.hasNoGravity())
        {
            this.setMotion(this.getMotion().add(0d, -0.04d, 0d));
        }

        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98d));
        
        if (this.onGround)
        {
            this.setMotion(this.getMotion().mul(0.7d, -0.5d, 0.7d));
        }
        
        timer--;
        if (this.timer <= 0)
            this.shrinkRate += 0.01f / 20;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
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

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeFloat(this.targetSize);
        buffer.writeFloat(this.timer);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        this.targetSize = buffer.readFloat();
        this.timer = buffer.readFloat();
        this.shrinkRate = 0;
    }
}
