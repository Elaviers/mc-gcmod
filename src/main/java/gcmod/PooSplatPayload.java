package gcmod;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public record PooSplatPayload(Vector3f position, float severity) implements CustomPayload
{
    public static final PacketCodec<RegistryByteBuf, PooSplatPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VECTOR3F, PooSplatPayload::position,
                    PacketCodecs.FLOAT, PooSplatPayload::severity,
                    PooSplatPayload::new);


    public static final CustomPayload.Id<PooSplatPayload> ID = new CustomPayload.Id<>( Identifier.of( "gcmod", "poo_splat" ) );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
