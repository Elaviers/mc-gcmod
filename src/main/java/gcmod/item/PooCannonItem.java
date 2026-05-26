package gcmod.item;

import com.mojang.datafixers.util.Pair;
import gcmod.GCMod;
import gcmod.entity.PooBrickEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.function.Predicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PooCannonItem extends ProjectileWeaponItem
{
    public PooCannonItem( Properties settings )
    {
        super( settings );
    }

    @Override
    protected void shootProjectile( LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target )
    {
        assert false; // kinda dumb that they made me do this
    }

    private static int getEnchantmentValue( ItemStack stack, DataComponentType<EnchantmentValueEffect> type )
    {
        Pair<EnchantmentValueEffect, Integer> pair = EnchantmentHelper.getHighestLevel( stack, GCMod.EFFECT_CONSTIPATION );
        if ( pair != null )
            return pair.getSecond();

        return 0;
    }

    boolean shootPoo( Level world, Player player, ItemStack stack )
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
            ammoStack = player.getProjectile( stack );
            if ( ammoStack.isEmpty() )
                return false;
        }

        if ( !world.isClientSide() )
        {
            int count;
            if ( player.isCreative() || EnchantmentHelper.hasTag( stack, GCMod.ENCH_LAXATIVES ) )
            {
                count = maxCount;
            }
            else
            {
                stack.hurtAndBreak( 1, player, EquipmentSlot.MAINHAND );

                for ( count = 0; count < maxCount; ++count )
                {
                    ammoStack = player.getProjectile( stack );
                    if ( ammoStack.isEmpty() )
                        break;

                    ammoStack.setCount( ammoStack.getCount() - 1 );
                }
            }

            if ( count == 0 )
                return false;

            float speed = 2f;
            if ( !EnchantmentHelper.hasTag( stack, GCMod.ENCH_DIARRHOEA ) )
            {
                final float charge = Math.min( 1f, player.getTicksUsingItem() / 45f );
                speed = .4f + charge * 1.6f;
                spread *= .5f + charge * .5f;
            }

            for ( int i = 0; i < count; ++i )
            {
                PooBrickEntity poo = GCMod.EXPLOSIVE_POO_BRICK_ENTITY.create( world, EntitySpawnReason.EVENT );
                poo.explosionRadius = 4 + getEnchantmentValue( stack, GCMod.EFFECT_POOER );
                poo.thrower = player;

                poo.setPos( player.getEyePosition() );
                poo.setYRot( player.getYRot() );
                poo.setXRot( player.getXRot() );

                final float cosYaw = Mth.cos( poo.getYRot() / 180.0F * (float) Math.PI );
                final float sinYaw = Mth.sin( poo.getYRot() / 180.0F * (float) Math.PI );
                final float pitchRad = poo.getXRot() / 180.0F * (float) Math.PI;

                final Vec3 right = new Vec3( -cosYaw, 0f, -sinYaw );
                final Vec3 fwd = new Vec3(
                        -sinYaw * Mth.cos( pitchRad ),
                        -Mth.sin( pitchRad ),
                        cosYaw * Mth.cos( pitchRad )
                );

                Vector3f dir = right.toVector3f();
                new AxisAngle4f( world.getRandom().nextFloat() * 2f * (float) Math.PI, fwd.toVector3f() ).transform( dir );

                poo.setDeltaMovement(
                        player.getDeltaMovement()
                                .add( fwd.scale( Math.max( 0.5f, speed + zDiff * (world.getRandom().nextFloat() * 2.f - 1f) ) ) )
                                .add( new Vec3( dir ).scale( world.getRandom().nextFloat() * spread ) )
                );

                world.addFreshEntity( poo );
            }
        }
        else
        {
            player.playSound( GCMod.POO_CANNON_SOUND, 1 + world.getRandom().nextFloat() * 0.2f, 1 + (world.getRandom().nextFloat() * 0.5f - 0.25f) );
        }

        return true;
    }

    @Override
    public boolean releaseUsing( ItemStack stack, Level world, LivingEntity user, int remainingUseTicks )
    {
        if ( user instanceof Player player )
            return shootPoo( world, player, stack );

        return false;
    }

    @Override
    public int getUseDuration( ItemStack stack, LivingEntity user )
    {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation( ItemStack stack )
    {
        return ItemUseAnimation.BOW;
    }

    @Override
    public InteractionResult use( Level world, Player user, InteractionHand hand )
    {
        ItemStack stack = user.getItemInHand( hand );
        if ( !user.hasInfiniteMaterials() && user.getProjectile( stack ).isEmpty() )
        {
            return InteractionResult.FAIL;
        }

        user.startUsingItem( hand );
        return InteractionResult.CONSUME;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles()
    {
        return stack -> stack.is( GCMod.POO_BRICK );
    }

    @Override
    public int getDefaultProjectileRange()
    {
        return 20;
    }
}
