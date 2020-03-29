package stupidmod.entity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stupidmod.StupidModBlocks;
import stupidmod.StupidModEntities;
import stupidmod.entity.AirStrikeExplosiveEntity;
import stupidmod.entity.ConstructiveExplosiveEntity;
import stupidmod.entity.DigExplosiveEntity;
import stupidmod.entity.ExplosiveEntity;

public class ExplosiveTileEntity extends TileEntity {
    private short fuse;
    private short strength;
    
    private BlockState constructState;
    
    private short airStrikeSpread;
    private short airStrikeHeight;
    private short airStrikePieces;
    
    public ExplosiveTileEntity() {
        super(StupidModEntities.TE_EXPLOSIVE);
        
        this.strength = 2;
        this.airStrikeHeight = 20;
        this.airStrikePieces = 5;
        this.airStrikeSpread = 3;
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.fuse = compound.getShort("Fuse");
        this.strength = compound.getShort("Strength");

        if (compound.contains("Block"))
            this.constructState = NBTUtil.readBlockState(compound.getCompound("Block"));

        if (compound.contains("Spread"))
            this.airStrikeSpread = compound.getShort("Spread");

        if (compound.contains("Pieces"))
            this.airStrikePieces = compound.getShort("Pieces");

        if (compound.contains("Height"))
            this.airStrikeHeight = compound.getShort("Height");
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        
        this.writeExplosiveDataToNBT(compound, true, true);
        
        return compound;
    }
    
    public CompoundNBT writeExplosiveDataToNBT(CompoundNBT compound, boolean writeConstruct, boolean writeAirstrike) {
        compound.putShort("Fuse", this.fuse);
        compound.putShort("Strength", this.strength);
        
        if (writeConstruct) {
            compound.put("Block", NBTUtil.writeBlockState(this.constructState));
        }
        
        if (writeAirstrike) {
            compound.putShort("Spread", this.airStrikeSpread);
            compound.putShort("Pieces", this.airStrikePieces);
            compound.putShort("Height", this.airStrikeHeight);
        }
        
        return compound;
    }
    
    public void explode(World world, BlockPos pos, BlockState state) {
        if (world.isRemote)return;
        double x = pos.getX() + .5f;
        double y = pos.getY();
        double z = pos.getZ() + .5f;

        Block b = state.getBlock();
        
        if (b == StupidModBlocks.BLAST_TNT)
        {
            ExplosiveEntity tnt = new ExplosiveEntity(world, x, y, z, fuse, strength);
    
            if (!tnt.exploded()) {
                world.addEntity(tnt);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
        else if (b == StupidModBlocks.CONSTRUCTIVE_TNT)
        {
            ConstructiveExplosiveEntity tnt = new ConstructiveExplosiveEntity(world, x, y, z, fuse, strength, this.constructState);
            
            if (!tnt.exploded()) {
                world.addEntity(tnt);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
        else if (b == StupidModBlocks.DIG_TNT)
        {
            DigExplosiveEntity tnt = new DigExplosiveEntity(world, x, y, z, fuse, strength);
            world.addEntity(tnt);
            world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        else if (b == StupidModBlocks.AIR_STRIKE_TNT)
        {
            AirStrikeExplosiveEntity tnt = new AirStrikeExplosiveEntity(world, x, y, z, this.fuse, this.strength, this.airStrikeSpread, this.airStrikePieces, this.airStrikeHeight);
            world.addEntity(tnt);
            world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
    
}
