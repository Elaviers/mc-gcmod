package stupidmod.entity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stupidmod.BlockRegister;
import stupidmod.EntityRegister;
import stupidmod.entity.EntityAirStrikeExplosive;
import stupidmod.entity.EntityConstructiveExplosive;
import stupidmod.entity.EntityDigExplosive;
import stupidmod.entity.EntityExplosive;

public class TileEntityExplosiveData extends TileEntity {
    
    private short fuse;
    private short strength;
    
    private IBlockState constructState;
    
    private short airStrikeSpread;
    private short airStrikeHeight;
    private short airStrikePieces;
    
    public TileEntityExplosiveData() {
        super(EntityRegister.tileEntityExplosiveData);
        
        this.strength = 2;
    }
    
    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.fuse = compound.getShort("Fuse");
        this.strength = compound.getShort("Strength");
        this.constructState = NBTUtil.readBlockState(compound.getCompound("Block"));
        this.airStrikeSpread = compound.getShort("Spread");
        this.airStrikePieces = compound.getShort("Pieces");
        this.airStrikeHeight = compound.getShort("Height");
    }
    
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        
        this.writeExplosiveDataToNBT(compound, true, true);
        
        return compound;
    }
    
    public NBTTagCompound writeExplosiveDataToNBT(NBTTagCompound compound, boolean writeConstruct, boolean writeAirstrike) {
        compound.setShort("Fuse", this.fuse);
        compound.setShort("Strength", this.strength);
        
        if (writeConstruct) {
            compound.setTag("Block", NBTUtil.writeBlockState(this.constructState));
        }
        
        if (writeAirstrike) {
            compound.setShort("Spread", this.airStrikeSpread);
            compound.setShort("Pieces", this.airStrikePieces);
            compound.setShort("Height", this.airStrikeHeight);
        }
        
        return compound;
    }
    
    public void explode(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote)return;
        double x = pos.getX() + .5f;
        double y = pos.getY();
        double z = pos.getZ() + .5f;
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        
        Block b = state.getBlock();
        
        if (b == BlockRegister.blockBlastTNT)
        {
            EntityExplosive tnt = new EntityExplosive(world, x, y, z, fuse, strength);
    
            if (!tnt.exploded()) {
                world.spawnEntity(tnt);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
        else if (b == BlockRegister.blockConstructiveTNT)
        {
            EntityConstructiveExplosive tnt = new EntityConstructiveExplosive(world, x, y, z, fuse, strength, this.constructState);
            
            if (!tnt.exploded()) {
                world.spawnEntity(tnt);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
        else if (b == BlockRegister.blockDigTNT)
        {
            EntityDigExplosive tnt = new EntityDigExplosive(world, x, y, z, fuse, strength);
            world.spawnEntity(tnt);
            world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        else if (b == BlockRegister.blockAirstrikeTNT)
        {
            EntityAirStrikeExplosive tnt = new EntityAirStrikeExplosive(world, x, y, z, this.fuse, this.strength, this.airStrikeSpread, this.airStrikePieces, this.airStrikeHeight);
            world.spawnEntity(tnt);
            world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
    
}
