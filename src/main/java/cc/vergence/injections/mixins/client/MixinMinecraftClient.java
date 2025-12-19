package cc.vergence.injections.mixins.client;

import cc.vergence.Vergence;
import cc.vergence.features.event.impl.client.TickEvent;
import cc.vergence.features.event.impl.features.world.WorldTickEvent;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient extends ReentrantThreadExecutor<Runnable> implements IMinecraft {
    @Shadow
    @Nullable
    public ClientWorld world;

    public MixinMinecraftClient(String string) {
        super(string);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void postWindowInit(RunArgs args, CallbackInfo ci) {
        //
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick$HEAD(CallbackInfo info) {
        if (!Vergence.LOADED) {
            return ;
        }
        Vergence.EVENTBUS.post(new TickEvent());
        if (this.world != null) {
            Vergence.EVENTBUS.post(new WorldTickEvent());
        }
    }
}
