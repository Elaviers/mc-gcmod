package stupidmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import stupidmod.StupidModEntities;
import stupidmod.StupidMod;
import stupidmod.entity.PooBrickEntity;

public class PooBrickItem extends Item {
    
    public PooBrickItem(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            player.getHeldItem(hand).shrink(1);
            PooBrickEntity tst = new PooBrickEntity(StupidModEntities.POO_BRICK, world);
            tst.setLocationAndAngles(player.posX,player.posY+player.getEyeHeight(),player.posZ, player.rotationYaw, player.rotationPitch);
            tst.posX -= MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
            tst.posY -= 0.1;
            tst.posZ -= MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
            tst.setPosition(tst.posX, tst.posY, tst.posZ);
            double motx = -MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI);
            double motz = MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI);
            double moty = -MathHelper.sin(tst.rotationPitch / 180.0F * (float)Math.PI);
            tst.setMotion(motx, moty, motz);
            world.addEntity(tst);
        }
        
        return super.onItemRightClick(world,player,hand);
    }
    
}
