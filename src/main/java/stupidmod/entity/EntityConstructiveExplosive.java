package stupidmod.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import stupidmod.EntityRegister;
import stupidmod.misc.ExplosionConstructive;

public class EntityConstructiveExplosive extends EntityExplosive {
    IBlockState blockState;
    
    public EntityConstructiveExplosive(World world) {
        super(EntityRegister.entityConstructiveExplosive, world);
    }
    
    public EntityConstructiveExplosive(World world, double x, double y, double z, int fuse, int strength, IBlockState state)
    {
        super(EntityRegister.entityConstructiveExplosive, world, x, y, z, fuse, strength);
        
        this.blockState = state;
    }
    
    @Override
    protected void onFuseCompleted() {
        this.remove();
        
        if (!this.world.isRemote)
        {
            ExplosionConstructive explosion = new ExplosionConstructive(this.world, null, this.posX, this.posY, this.posZ, this.strength, false, true, this.blockState);

            if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.world, explosion.doppelganger)) return;
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
    }
}
