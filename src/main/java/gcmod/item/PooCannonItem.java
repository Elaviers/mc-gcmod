package gcmod.item;

import com.mojang.datafixers.util.Pair;
import gcmod.GCMod;
import gcmod.entity.PooBrickEntity;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.function.Predicate;

public class PooCannonItem extends RangedWeaponItem
{
    public PooCannonItem( Settings settings )
    {
        super( settings );
    }

    @Override
    protected void shoot( LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target )
    {
        assert false; // kinda dumb that they made me do this
    }

    private static int getEnchantmentValue( ItemStack stack, ComponentType<EnchantmentValueEffect> type )
    {
        Pair<EnchantmentValueEffect, Integer> pair = EnchantmentHelper.getHighestLevelEffect( stack, GCMod.EFFECT_CONSTIPATION );
        if ( pair != null )
            return pair.getSecond();

        return 0;
    }

    boolean shootPoo( World world, PlayerEntity player, ItemStack stack )
    {
        ItemStack ammoStack = null;

        final int constipationLvl = getEnchantmentValue(stack, GCMod.EFFECT_CONSTIPATION );

        int maxCount = 1;
        float spread = .08f;
        float zDiff = 0;
        switch ( constipationLvl )
        {
            case 0:
                break;
            case 1:
                maxCount = 2;
                spread = .12f;
                zDiff = 0.2f;
                break;
            case 2:
                maxCount = 4;
                spread = .24f;
                zDiff = 0.33f;
                break;
            case 3:
                maxCount = 8;
                spread = .5f;
                zDiff = 0.5f;
                break;
            default:
                maxCount = (int) Math.pow( 2, constipationLvl );
                spread = 1.f;
                zDiff = 0.8f;
        }

        if ( !player.isCreative() )
        {
            ammoStack = player.getProjectileType( stack );
            if ( ammoStack.isEmpty() )
                return false;
        }

        if ( !world.isClient )
        {
            int count;
            if ( player.isCreative() || EnchantmentHelper.hasAnyEnchantmentsIn( stack, GCMod.ENCH_LAXATIVES ) )
            {
                count = maxCount;
            }
            else
            {
                stack.damage( 1, player, EquipmentSlot.MAINHAND );

                for ( count = 0; count < maxCount; ++count )
                {
                    ammoStack = player.getProjectileType( stack );
                    if ( ammoStack.isEmpty() )
                        break;

                    ammoStack.setCount( ammoStack.getCount() - 1 );
                }
            }

            if ( count == 0 )
                return false;

            float speed = 2f;
            if ( !EnchantmentHelper.hasAnyEnchantmentsIn( stack, GCMod.ENCH_DIARRHOEA ) )
            {
                final float charge = Math.min( 1f, player.getItemUseTime() / 45f );
                speed = .4f + charge * 1.6f;
                spread *= .5f + charge * .5f;
            }

            for ( int i = 0; i < count; ++i )
            {
                PooBrickEntity poo = GCMod.EXPLOSIVE_POO_BRICK_ENTITY.create( world, SpawnReason.EVENT );
                poo.explosionRadius = 4 + getEnchantmentValue( stack, GCMod.EFFECT_POOER );
                poo.thrower = player;

                poo.setPosition( player.getEyePos() );
                poo.setYaw( player.getYaw() );
                poo.setPitch( player.getPitch() );

                final float cosYaw = MathHelper.cos( poo.getYaw() / 180.0F * (float) Math.PI );
                final float sinYaw = MathHelper.sin( poo.getYaw() / 180.0F * (float) Math.PI );
                final float pitchRad = poo.getPitch() / 180.0F * (float) Math.PI;

                final Vec3d right = new Vec3d( -cosYaw, 0f, -sinYaw );
                final Vec3d fwd = new Vec3d(
                        -sinYaw * MathHelper.cos( pitchRad ),
                        -MathHelper.sin( pitchRad ),
                        cosYaw * MathHelper.cos( pitchRad )
                );

                Vector3f dir = right.toVector3f();
                new AxisAngle4f( world.random.nextFloat() * 2f * (float) Math.PI, fwd.toVector3f() ).transform( dir );

                poo.setVelocity(
                        player.getVelocity()
                                .add( fwd.multiply( Math.max( 0.5f, speed + zDiff * (world.random.nextFloat() * 2.f - 1f) ) ) )
                                .add( new Vec3d( dir ).multiply( world.random.nextFloat() * spread ) )
                );

                world.spawnEntity( poo );
            }
        }
        else
        {
            player.playSound( GCMod.POO_CANNON_SOUND, 1 + world.random.nextFloat() * 0.2f, 1 + (world.random.nextFloat() * 0.5f - 0.25f) );
        }

        return true;
    }

    @Override
    public boolean onStoppedUsing( ItemStack stack, World world, LivingEntity user, int remainingUseTicks )
    {
        if ( user instanceof PlayerEntity player )
            return shootPoo( world, player, stack );

        return false;
    }

    @Override
    public int getMaxUseTime( ItemStack stack, LivingEntity user )
    {
        return 72000;
    }

    @Override
    public UseAction getUseAction( ItemStack stack )
    {
        return UseAction.BOW;
    }

    @Override
    public ActionResult use( World world, PlayerEntity user, Hand hand )
    {
        ItemStack stack = user.getStackInHand( hand );
        if ( !user.isInCreativeMode() && user.getProjectileType( stack ).isEmpty() )
        {
            return ActionResult.FAIL;
        }

        user.setCurrentHand( hand );
        return ActionResult.CONSUME;
    }

    @Override
    public Predicate<ItemStack> getProjectiles()
    {
        return stack -> stack.isOf( GCMod.POO_BRICK );
    }

    @Override
    public int getRange()
    {
        return 20;
    }
}
