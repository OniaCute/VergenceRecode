package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.event.impl.features.render.Render2DEvent;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud implements IMinecraft {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext drawcontext, RenderTickCounter tickCounter, CallbackInfo ci) {
//        KawaseBlur.SHADER.draw(Client.INSTANCE.blurIntensity.getValue().intValue());
//        SkiaContext.draw(context -> {
//            Render2DUtil.save();
//            Render2DUtil.scale((float) mc.getWindow().getScaleFactor());
            Vergence.EVENTBUS.post(new Render2DEvent(drawcontext, tickCounter.getTickDelta(true)));
//            Render2DUtil.restore();
//        });
    }
}
