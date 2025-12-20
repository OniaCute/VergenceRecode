package cc.vergence.features.event.impl.features.game;

import cc.vergence.features.event.Event;
import cc.vergence.utils.interfaces.IMinecraft;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@Getter
public class PacketEvent extends Event implements IMinecraft {
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        super(Stage.Pre);
        this.packet = packet;
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
