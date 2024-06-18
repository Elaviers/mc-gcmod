package gcmod.mixin;

import gcmod.CentrifugeSound;
import gcmod.entity.CentrifugeEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientPlayNetworkHandler.class )
public abstract class ClientPlayNetworkHandlerMixin
{
    /*
    NOTE - this is the "proper" way to do it, at least its closer to how vanilla works
    However there is not a BlockEntity equivalent to ClientPlayNetworkHandler.playSpawnSound
     - so instead I've just hooked it up to the renderer for now...

    @Inject(
            // (this is a lambda within "onBlockEntityUpdate(Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;)V" )
            method = "method_38542(Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;Lnet/minecraft/block/entity/BlockEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/block/entity/BlockEntity.read (Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void inject( BlockEntityUpdateS2CPacket packet, BlockEntity ent, CallbackInfo ci )
    {
        if ( ent instanceof CentrifugeEntity centrifuge )
            CentrifugeSound.updateSoundForEntity( centrifuge );
    }
    */
}
