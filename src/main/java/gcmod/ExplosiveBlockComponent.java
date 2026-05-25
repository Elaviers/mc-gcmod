package gcmod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gcmod.block.ExplosiveBlock;
import java.util.function.Consumer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record ExplosiveBlockComponent( int fuse, int strength, BlockState block, int spread, int pieces, int height ) implements TooltipProvider
{
    public static final Codec<ExplosiveBlockComponent> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf( "fuse", 0 ).forGetter( ExplosiveBlockComponent::fuse ),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf( "strength", 0 ).forGetter( ExplosiveBlockComponent::strength ),
                BlockState.CODEC.optionalFieldOf( "block", Blocks.AIR.defaultBlockState() ).forGetter( ExplosiveBlockComponent::block ),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf( "spread", 0 ).forGetter( ExplosiveBlockComponent::spread ),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf( "pieces", 0 ).forGetter( ExplosiveBlockComponent::pieces ),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf( "height", 0 ).forGetter( ExplosiveBlockComponent::height )
        ).apply( inst, ExplosiveBlockComponent::new )
    );

    @Override
    public void addToTooltip( Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components )
    {
        textConsumer.accept( Component.translatable( "block.gcmod.explosive.tooltip.strength", this.strength ) );

        if ( this.fuse > 0 )
            textConsumer.accept( Component.translatable( "block.gcmod.explosive.tooltip.fuse", this.fuse / 20 ) );

        if ( this.pieces > 0 )
        {
            textConsumer.accept( Component.translatable( "block.gcmod.explosive.tooltip.spread", this.spread ) );
            textConsumer.accept( Component.translatable( "block.gcmod.explosive.tooltip.pieces", this.pieces ) );
            textConsumer.accept( Component.translatable( "block.gcmod.explosive.tooltip.height", this.height ) );
        }

        if ( this.block != Blocks.AIR.defaultBlockState() )
        {
            HolderGetter<Block> blockReg = context.registries().lookupOrThrow( Registries.BLOCK );
            textConsumer.accept( Component.translatable( "block.gcmod.explosive.tooltip.block", this.block.getBlock().getName() ) );
        }
    }
}
