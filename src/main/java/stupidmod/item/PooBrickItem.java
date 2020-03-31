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
            tst.setLocationAndAngles(
                    player.posX - MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F,
                    player.posY + player.getEyeHeight() - 0.1,
                    player.posZ - MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * 0.16F,
                    player.rotationYaw, player.rotationPitch);

            tst.setMotion(
                    -MathHelper.sin(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI),
                    -MathHelper.sin(tst.rotationPitch / 180.0F * (float)Math.PI),
                    MathHelper.cos(tst.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(tst.rotationPitch / 180.0F * (float)Math.PI));

            world.addEntity(tst);
        }
        
        return super.onItemRightClick(world,player,hand);
    }
    
}
