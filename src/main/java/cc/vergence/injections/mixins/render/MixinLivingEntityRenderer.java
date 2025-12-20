package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.managers.impl.feature.player.RotateManager;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<EntityRenderState>> implements IMinecraft {
//    @Inject(method = "render", at = @At("HEAD"))
//    public void onRenderPre(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//        if (mc.player != null && livingEntity == mc.player) {
//            livingEntity.setYaw(Vergence.ROTATION.getServerAngle().getYaw());
//            livingEntity.setPitch(Vergence.ROTATION.getServerAngle().getPitch());
//        }
//    }
}
