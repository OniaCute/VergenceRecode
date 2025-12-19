package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.event.Event;
import cc.vergence.features.event.impl.features.render.Render3DEvent;
import cc.vergence.utils.interfaces.IMinecraft;
import cc.vergence.utils.render.Render3DUtil;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer implements IMinecraft {
    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void renderWorld$HEAD(RenderTickCounter tickCounter, CallbackInfo info) {
        Render3DUtil.init();
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    private void renderWorld$POST(RenderTickCounter tickCounter, CallbackInfo info, @Local(ordinal = 2) Matrix4f matrix4f3, @Local(ordinal = 1) float tickDelta, @Local MatrixStack matrixStack) {
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().mul(matrix4f3);
        RenderSystem.getModelViewStack().mul(matrixStack.peek().getPositionMatrix().invert());
        Vergence.EVENTBUS.post(new Render3DEvent(matrixStack, tickDelta));
        Render3DUtil.render(Render3DUtil.QUADS, Render3DUtil.LINES, false);
        Render3DUtil.render(Render3DUtil.SHINE_QUADS, Render3DUtil.SHINE_LINES, true);
//        Vergence.EVENTS.onDraw3D(matrixStack, tickDelta);
        RenderSystem.getModelViewStack().popMatrix();
    }
}
