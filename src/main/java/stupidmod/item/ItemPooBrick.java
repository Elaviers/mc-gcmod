package stupidmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import stupidmod.StupidMod;
import stupidmod.entity.EntityPooBrick;

public class ItemPooBrick extends Item {
    
    public ItemPooBrick(String name) {
        super(new Properties().group(StupidMod.GROUP));
        
        this.setRegistryName(name);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            player.getHeldItem(hand).shrink(1);
            EntityPooBrick tst = new EntityPooBrick(world);
            tst.setLocationAndAngles(player.posX,player.posY+player.getEyeHeight(),player.posZ, player.rotationYaw, player.rotationPitch);
            tst.posX -= MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
            tst.posY -= 0.1;
            tst.posZ -= MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
            tst.setPosition(tst.posX, tst.posY, tst.posZ);
            double motx = -MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI);
            double motz = MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI);
            double moty = -MathHelper.sin(tst.rotationPitch / 180.0F * (float)Math.PI);
            tst.motionX = motx;
            tst.motionY = moty;
            tst.motionZ = motz;
            world.spawnEntity(tst);
        }
        
        return super.onItemRightClick(world,player,hand);
    }
    
}
