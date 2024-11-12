package gcmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Collection;

public class PooProteinItem extends Item
{
    public PooProteinItem( Settings settings )
    {
        super( settings );
    }

    @Override
    public ItemStack finishUsing( ItemStack stack, World world, LivingEntity user )
    {
        if ( !world.isClient )
        {
            Collection<StatusEffectInstance> effects = user.getStatusEffects();

            int durationJump = 200;
            int durationStrength = 200;
            int durationHaste = 200;
            int durationSpeed = 200;

            for ( StatusEffectInstance effect : effects )
            {
                if ( effect.getAmplifier() >= 4 )
                {
                    if ( effect.getEffectType() == StatusEffects.JUMP_BOOST )
                        durationJump += effect.getDuration();
                    else if ( effect.getEffectType() == StatusEffects.STRENGTH )
                        durationStrength += effect.getDuration();
                    else if ( effect.getEffectType() == StatusEffects.HASTE )
                        durationHaste += effect.getDuration();
                    else if ( effect.getEffectType() == StatusEffects.SPEED )
                        durationSpeed += effect.getDuration();
                }
            }

            user.addStatusEffect( new StatusEffectInstance( StatusEffects.JUMP_BOOST, durationJump, 4 ) );
            user.addStatusEffect( new StatusEffectInstance( StatusEffects.STRENGTH, durationStrength, 4 ) );
            user.addStatusEffect( new StatusEffectInstance( StatusEffects.HASTE, durationHaste, 4 ) );
            user.addStatusEffect( new StatusEffectInstance( StatusEffects.SPEED, durationSpeed, 4 ) );
        }

        return super.finishUsing( stack, world, user );
    }
}
