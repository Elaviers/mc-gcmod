package gcmod.item;

import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PooProteinItem extends Item
{
    public PooProteinItem( Properties settings )
    {
        super( settings );
    }

    @Override
    public ItemStack finishUsingItem( ItemStack stack, Level world, LivingEntity user )
    {
        if ( !world.isClientSide() )
        {
            Collection<MobEffectInstance> effects = user.getActiveEffects();

            int durationJump = 200;
            int durationStrength = 200;
            int durationHaste = 200;
            int durationSpeed = 200;

            for ( MobEffectInstance effect : effects )
            {
                if ( effect.getAmplifier() >= 4 )
                {
                    if ( effect.getEffect() == MobEffects.JUMP_BOOST )
                        durationJump += effect.getDuration();
                    else if ( effect.getEffect() == MobEffects.STRENGTH )
                        durationStrength += effect.getDuration();
                    else if ( effect.getEffect() == MobEffects.HASTE )
                        durationHaste += effect.getDuration();
                    else if ( effect.getEffect() == MobEffects.SPEED )
                        durationSpeed += effect.getDuration();
                }
            }

            user.addEffect( new MobEffectInstance( MobEffects.JUMP_BOOST, durationJump, 4 ) );
            user.addEffect( new MobEffectInstance( MobEffects.STRENGTH, durationStrength, 4 ) );
            user.addEffect( new MobEffectInstance( MobEffects.HASTE, durationHaste, 4 ) );
            user.addEffect( new MobEffectInstance( MobEffects.SPEED, durationSpeed, 4 ) );
        }

        return super.finishUsingItem( stack, world, user );
    }
}
