package gcmod.item;

import gcmod.GCMod;
import gcmod.entity.PooBrickEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class PooBrickItem extends Item
{
    public PooBrickItem( Settings settings )
    {
        super( settings );
    }

    @Override
    public TypedActionResult<ItemStack> use( World world, PlayerEntity player, Hand hand )
    {
        ItemStack stack = player.getStackInHand(hand);

        if ( !world.isClient )
        {
            if ( !player.isCreative() )
                stack.decrement( 1 );

            PooBrickEntity brick = GCMod.POO_BRICK_ENTITY.create( world );
            brick.thrower = player;

            brick.setYaw( player.getYaw() );
            brick.setPitch( player.getPitch() );
            final float cosYaw = MathHelper.cos ( brick.getYaw() / 180.0F * (float) Math.PI );
            final float sinYaw = MathHelper.sin ( brick.getYaw() / 180.0F * (float) Math.PI );
            final float pitchRad = brick.getPitch() / 180.0F * (float) Math.PI;

            final Vec3d right = new Vec3d( -cosYaw, 0f, -sinYaw );
            final Vec3d fwd = new Vec3d(
                    -sinYaw * MathHelper.cos( pitchRad ),
                    -MathHelper.sin( pitchRad ),
                    cosYaw * MathHelper.cos( pitchRad )
            );

            Vector3f dir = right.toVector3f();
            new AxisAngle4f( world.random.nextFloat() * 2f * (float)Math.PI, fwd.toVector3f() ).transform( dir );

            brick.setPosition( new Vec3d( player.getX(), player.getEyeY() - .1f, player.getZ() ).add( fwd.multiply( -.2f ) ).add( right.multiply( .2f ) ) );

            brick.setVelocity(
                    player.getVelocity()
                            .add( fwd.multiply( .6f + world.random.nextFloat() * .8f ) )
                            .add( new Vec3d( dir ).multiply( world.random.nextFloat() * .1f ) )
            );


            world.spawnEntity( brick );
        }

        player.playSound( SoundEvents.ENTITY_EGG_THROW, 1f, .5f + world.random.nextFloat() * .2f );

        return TypedActionResult.success( stack );
    }
}
