package gcmod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;

public record PooSplatPayload(Vector3fc position, float severity) implements CustomPacketPayload
{
    public static final StreamCodec<RegistryFriendlyByteBuf, PooSplatPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VECTOR3F, PooSplatPayload::position,
                    ByteBufCodecs.FLOAT, PooSplatPayload::severity,
                    PooSplatPayload::new);


    public static final CustomPacketPayload.Type<PooSplatPayload> ID = new CustomPacketPayload.Type<>( Identifier.fromNamespaceAndPath( "gcmod", "poo_splat" ) );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
