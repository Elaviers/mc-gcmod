package StupidMod.Entities;

import StupidMod.Misc.ExplosionConstructive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class EntityConstructiveExplosive extends EntityExplosive {
    IBlockState blockState;
    
    public EntityConstructiveExplosive(World world) {
        super(world);
    }
    
    public EntityConstructiveExplosive(World world, double x, double y, double z, int fuse, int strength, IBlockState state)
    {
        super(world, x, y, z, fuse, strength);
        
        this.blockState = state;
    }
    
    @Override
    protected void onFuseCompleted() {
        this.setDead();
        
        if (!this.world.isRemote)
        {
            ExplosionConstructive explosion = new ExplosionConstructive(this.world, null, this.posX, this.posY, this.posZ, this.strength, false, true, blockState);
            if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.world, explosion.explosion)) return;
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
    }
}
