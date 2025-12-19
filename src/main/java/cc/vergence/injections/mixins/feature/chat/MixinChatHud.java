package cc.vergence.injections.mixins.feature.chat;

import cc.vergence.Vergence;
import cc.vergence.features.event.impl.features.game.ChatEvent;
import cc.vergence.utils.interfaces.IChatHud;
import cc.vergence.utils.interfaces.IChatHudLine;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud, IMinecraft {
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Unique
    private int next = 0;

    @Shadow
    public abstract void addMessage(Text message);

    @Override
    public void vergence$add(Text message, int id) {
        next = id;
        addMessage(message);
        next = 0;
    }

    @Inject(method = "addVisibleMessage", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLineVisible(ChatHudLine message, CallbackInfo ci) {
        if (!visibleMessages.isEmpty()) {
            ((IChatHudLine) (Object) visibleMessages.get(0)).vergence$setId(next);
        }
    }

    @Inject(method = "addVisibleMessage", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLine(ChatHudLine message, CallbackInfo ci) {
        if (!messages.isEmpty()) {
            ((IChatHudLine) (Object) messages.get(0)).vergence$setId(next);
        }
    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V")
    private void onAddMessageWithId(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (next != 0) {
            visibleMessages.removeIf(msg -> msg == null || ((IChatHudLine) (Object) msg).vergence$getId() == next);
            messages.removeIf(msg -> msg == null || ((IChatHudLine) (Object) msg).vergence$getId() == next);
        }
    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        ChatEvent.Receive event = new ChatEvent.Receive(message.getString());
        Vergence.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}