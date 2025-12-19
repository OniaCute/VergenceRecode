package cc.vergence.injections.mixins.feature.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.impl.features.player.PlayerTickEvent;
import cc.vergence.features.event.impl.features.player.SyncEvent;
import cc.vergence.features.objects.Angle;
import cc.vergence.utils.interfaces.IMinecraft;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements IMinecraft {
    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void playerTickEvent$HEAD(CallbackInfo info) {
        Vergence.EVENTBUS.post(new PlayerTickEvent());
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    public void sendMovementPackets$HEAD(CallbackInfo info) {
        Vec3d currentPosition = getPos();
        float currentYaw = getYaw();
        float currentPitch = getPitch();
        SyncEvent event = new SyncEvent(currentPosition, new Angle(currentPitch, currentYaw));
        Vergence.EVENTBUS.post(event);
        this.setPos(
                event.getPosition().x,
                event.getPosition().y,
                event.getPosition().z
        );
        this.setPitch(event.getAngle().getPitch());
        this.setYaw(event.getAngle().getYaw());
    }

    @Redirect(method = "sendMovementPackets", at = @At(value = "NEW", target = "(DDDFFZZ)Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$Full;", ordinal = 0))
    private PlayerMoveC2SPacket.Full desyncRotationFull(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean horizontalCollision) {
        Vergence.NOTIFY.notify("X", "Redirected Full!", -3);
        return new PlayerMoveC2SPacket.Full(x, y, z, Vergence.ROTATION.getServerAngle().getYaw(), Vergence.ROTATION.getServerAngle().getPitch(), onGround, horizontalCollision);
    }

    @Redirect(method = "sendMovementPackets", at = @At(value = "NEW", target = "(FFZZ)Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$LookAndOnGround;", ordinal = 0))
    private PlayerMoveC2SPacket.LookAndOnGround desyncRotationLookAndOnGround(float yaw, float pitch, boolean onGround, boolean horizontalCollision) {
        Vergence.NOTIFY.notify("X", "Redirected LookGround!", -3);
        return new PlayerMoveC2SPacket.LookAndOnGround(Vergence.ROTATION.getServerAngle().getYaw(), Vergence.ROTATION.getServerAngle().getPitch(), onGround, horizontalCollision);
    }
}
