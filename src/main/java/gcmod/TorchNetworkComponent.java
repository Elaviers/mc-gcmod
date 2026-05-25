package gcmod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record TorchNetworkComponent( List<BlockPos> positions ) implements TooltipProvider
{
    public static final Codec<TorchNetworkComponent> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                    Codec.list( BlockPos.CODEC ).optionalFieldOf( "torches", List.of() ).forGetter( TorchNetworkComponent::positions )
            ).apply( inst, TorchNetworkComponent::new ) );

    @Override
    public void addToTooltip( Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components )
    {
        textConsumer.accept( Component.translatable( "item.gcmod.calibrator.tooltip1" ) );

        for ( BlockPos pos : positions )
            textConsumer.accept( Component.translatable( "item.gcmod.calibrator.tooltipentry", pos.getX(), pos.getY(), pos.getZ() ) );
    }
}
