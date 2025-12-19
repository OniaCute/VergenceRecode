package cc.vergence.features.event.impl.features.game;

import cc.vergence.features.event.Event;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@Getter
public class ChatEvent extends Event {
    private final String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public static class Send extends ChatEvent {
        public Send(String message) {
            super(message);
        }
    }

    public static class Receive extends ChatEvent {
        public Receive(String message) {
            super(message);
        }
    }
}
