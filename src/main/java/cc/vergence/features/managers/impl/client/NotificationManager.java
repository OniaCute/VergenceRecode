package cc.vergence.features.managers.impl.client;

import cc.vergence.features.managers.Manager;
import cc.vergence.features.modules.Module;
import cc.vergence.utils.interfaces.IChatHud;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.HashMap;

public class NotificationManager extends Manager implements IMinecraft {
    public static final String SIGN = "§(";
    public static HashMap<OrderedText, StringVisitable> messages = new HashMap<>();

    public NotificationManager() {
        super("Notification Manager");
    }

    public void notify(String source, String text, int id) {
        if (mc.player == null || mc.world == null) {
            return ;
        }
        ((IChatHud) mc.inGameHud.getChatHud()).vergence$add(Text.of(SIGN + "§r[" + source + "§r]§f " + text), id);
    }

    public void notify(Module source, String text, int id) {
        this.notify(source.getDisplayName(), text, id);
    }

    public void notify(String source, String text) {
        if (mc.player == null || mc.world == null) {
            return ;
        }
        mc.inGameHud.getChatHud().addMessage(Text.of(SIGN + "§r[" + source + "§r] §r" + text));
    }

    public void notify(Module source, String text) {
        this.notify(source.getDisplayName(), text);
    }
}
