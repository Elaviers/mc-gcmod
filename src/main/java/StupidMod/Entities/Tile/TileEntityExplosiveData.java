package StupidMod.Entities.Tile;

import StupidMod.Blocks.BlockExplosive;
import StupidMod.Entities.EntityAirStrikeExplosive;
import StupidMod.Entities.EntityConstructiveExplosive;
import StupidMod.Entities.EntityExplosive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityExplosiveData extends TileEntity {
    private short fuse;
    private short strength;
    
    private String constructBlock;
    private short constructMeta;
    
    private short airStrikeSpread;
    private short airStrikeHeight;
    private short airStrikePieces;
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.fuse = compound.getShort("Fuse");
        this.strength = compound.getShort("Strength");
        this.constructBlock = compound.getString("Block");
        this.constructMeta = compound.getShort("BlockMeta");
        this.airStrikeSpread = compound.getShort("Spread");
        this.airStrikePieces = compound.getShort("Pieces");
        this.airStrikeHeight = compound.getShort("Height");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        this.writeExplosiveDataToNBT(compound, true, true);
        
        return compound;
    }
    
    public NBTTagCompound writeExplosiveDataToNBT(NBTTagCompound compound, boolean writeConstruct, boolean writeAirstrike) {
        compound.setShort("Fuse", this.fuse);
        compound.setShort("Strength", this.strength);
        
        if (writeConstruct) {
            compound.setString("Block", this.constructBlock);
            compound.setShort("BlockMeta", this.constructMeta);
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
        world.setBlockToAir(pos);
        
        switch(((BlockExplosive.TntType)state.getValue(BlockExplosive.TYPE)).getName())
        {
            case "blast": {
                EntityExplosive tnt = new EntityExplosive(world, x, y, z, fuse, strength);
                
                if (!tnt.exploded()) {
                    world.spawnEntity(tnt);
                    world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                break;
            }
            
            case "construct": {
                EntityConstructiveExplosive tnt = new EntityConstructiveExplosive(world, x, y, z, fuse, strength, Block.getBlockFromName(this.constructBlock).getStateFromMeta(this.constructMeta));
                
                if (!tnt.exploded()) {
                    world.spawnEntity(tnt);
                    world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
                break;
            }
            
            case "airstrike": {
                EntityAirStrikeExplosive tnt = new EntityAirStrikeExplosive(world, x, y, z, this.fuse, this.strength, this.airStrikeSpread, this.airStrikePieces, this.airStrikeHeight);
                
                world.spawnEntity(tnt);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                break;
            }
            
            default:
                world.setBlockToAir(pos);
        }
    }
}
