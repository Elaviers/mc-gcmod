package gcmod.item;

import gcmod.GCMod;
import gcmod.entity.PooBrickEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class PooBrickItem extends Item
{
    public PooBrickItem( Properties settings )
    {
        super( settings );
    }

    @Override
    public InteractionResult use( Level world, Player player, InteractionHand hand )
    {
        ItemStack stack = player.getItemInHand(hand);

        if ( !world.isClientSide() )
        {
            if ( !player.isCreative() )
                stack.shrink( 1 );

            PooBrickEntity brick = GCMod.POO_BRICK_ENTITY.create( world, EntitySpawnReason.EVENT );
            brick.thrower = player;

            brick.setYRot( player.getYRot() );
            brick.setXRot( player.getXRot() );
            final float cosYaw = Mth.cos ( brick.getYRot() / 180.0F * (float) Math.PI );
            final float sinYaw = Mth.sin ( brick.getYRot() / 180.0F * (float) Math.PI );
            final float pitchRad = brick.getXRot() / 180.0F * (float) Math.PI;

            final Vec3 right = new Vec3( -cosYaw, 0f, -sinYaw );
            final Vec3 fwd = new Vec3(
                    -sinYaw * Mth.cos( pitchRad ),
                    -Mth.sin( pitchRad ),
                    cosYaw * Mth.cos( pitchRad )
            );

            Vector3f dir = right.toVector3f();
            new AxisAngle4f( world.getRandom().nextFloat() * 2f * (float)Math.PI, fwd.toVector3f() ).transform( dir );

            brick.setPos( new Vec3( player.getX(), player.getEyeY() - .1f, player.getZ() ).add( fwd.scale( -.2f ) ).add( right.scale( .2f ) ) );

            brick.setDeltaMovement(
                    player.getKnownMovement()
                            .add( fwd.scale( .6f + world.getRandom().nextFloat() * .8f ) )
                            .add( new Vec3( dir ).scale( world.getRandom().nextFloat() * .1f ) )
            );


            world.addFreshEntity( brick );
        }

        player.playSound( SoundEvents.EGG_THROW, 1f, .5f + world.getRandom().nextFloat() * .2f );

        return InteractionResult.SUCCESS;
    }
}
