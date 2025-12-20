package cc.vergence.features.managers.impl.feature.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.Subscribe;
import cc.vergence.features.event.impl.features.game.PacketEvent;
import cc.vergence.features.event.impl.features.player.PlayerTickEvent;
import cc.vergence.features.event.impl.features.player.SyncEvent;
import cc.vergence.features.event.impl.features.render.Render3DEvent;
import cc.vergence.features.managers.Manager;
import cc.vergence.features.objects.Angle;
import cc.vergence.utils.interfaces.IMinecraft;
import cc.vergence.utils.maths.RandomUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.PingPackets;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

@Getter
@Setter
public class RotateManager extends Manager implements IMinecraft {
    private Angle clientAngle = new Angle(0, 0); // The real angle of the client player
    private Angle serverAngle = new Angle(0, 0); // The server side angle of the player
    private boolean clientRotating = false;
    private boolean serverRotating = false;

    public RotateManager() {
        super("Rotation Manager");
    }

    @Subscribe
    public void onSync(SyncEvent event) {
        if (clientRotating) {
            event.setAngle(clientAngle);
        }
    }

    @Subscribe
    public void onTick(PlayerTickEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        PlayerEntity target = null;
        double closest = 32;
        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || player.isDead()) continue;
            double dist = mc.player.squaredDistanceTo(player);
            if (dist < closest) {
                closest = dist;
                target = player;
            }
        }

        serverRotating = true;

        if (target != null) {
            Vec3d eyes = mc.player.getEyePos();
            Vec3d tgt = target.getEyePos();
            double dx = tgt.x - eyes.x;
            double dy = tgt.y - eyes.y;
            double dz = tgt.z - eyes.z;
            float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90F;
            float pitch = (float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
            if (serverAngle.getYaw() == yaw && serverAngle.getPitch() == pitch) {
                serverRotating = false;
                return ;
            }
            serverAngle = new Angle(pitch, yaw);
        } else {
            this.serverAngle = new Angle(RandomUtils.getFloat(-90, 90), RandomUtils.getFloat(-180, 180));
        }

        Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                serverAngle.getYaw(),
                serverAngle.getPitch(),
                true,
                mc.player.horizontalCollision)
        );
    }

    @Subscribe
    public void onReceivedPackets(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket s2c) {
            if (s2c.change().yaw() != serverAngle.getYaw() || s2c.change().pitch() != serverAngle.getPitch()) {
                Vergence.NOTIFY.notify("C2S", "Watch Out! Anti cheat has been detected your cheating activity!");
                serverAngle = new Angle(s2c.change().pitch(), s2c.change().yaw());
            }
        }
    }
}
