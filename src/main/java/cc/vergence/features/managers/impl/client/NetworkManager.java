package cc.vergence.features.managers.impl.client;

import cc.vergence.features.event.eventbus.Subscribe;
import cc.vergence.features.event.impl.features.world.WorldTickEvent;
import cc.vergence.features.managers.Manager;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class NetworkManager extends Manager implements IMinecraft {
    public PriorityQueue<Packet<?>> WAITING = new PriorityQueue<>();
    public ArrayList<Packet<?>> FINISHED = new ArrayList<>();

    public NetworkManager() {
        super("Network Manager");
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        if (mc.player == null || mc.getNetworkHandler() == null || WAITING.isEmpty()) {
            return ;
        }
        while (!WAITING.isEmpty()) {
            Packet<?> packet = WAITING.poll();
            mc.getNetworkHandler().sendPacket(packet);
            FINISHED.add(packet);
        }
    }

    public void sendPacket(Packet<?> packet, boolean waiting) {
        if (packet == null || mc.player == null || mc.world == null) {
            return ;
        }
        if (mc.getNetworkHandler() == null && waiting) {
            WAITING.add(packet);
        }
        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(packet);
        }
    }

    public void sendPacket(Packet<?> packet) {
        sendPacket(packet, false);
    }

    public void sendCommand(String command) {
        if (command == null || command.isEmpty() || mc.player == null || mc.world == null || mc.getNetworkHandler() == null) {
            return ;
        }
        mc.getNetworkHandler().sendChatMessage("/" + command);
    }

    public void sendMessage(String message) {
        if (message == null || message.isEmpty() || mc.player == null || mc.world == null || mc.getNetworkHandler() == null) {
            return ;
        }
        mc.getNetworkHandler().sendChatMessage(message);
    }
}
