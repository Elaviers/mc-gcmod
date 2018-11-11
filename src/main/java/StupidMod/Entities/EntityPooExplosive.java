package StupidMod.Entities;

import net.minecraft.world.World;

public class EntityPooExplosive extends EntityImpactExplosive {
    
    public EntityPooExplosive(World worldIn) {
        super(worldIn);
    }
    
    public EntityPooExplosive(World world, int x, int y, int z, int explosionRadius) {
        super(world, x, y, z, explosionRadius);
    }
}
