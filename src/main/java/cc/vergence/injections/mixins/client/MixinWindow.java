package cc.vergence.injections.mixins.client;

import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Mixin(Window.class)
public class MixinWindow {
    @Redirect(method = "setIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Icons;getIcons(Lnet/minecraft/resource/ResourcePack;)Ljava/util/List;"))
    private List<InputSupplier<InputStream>> setIconForWindow(Icons instance, ResourcePack resourcePack) throws IOException {
        InputStream s256 = MixinWindow.class.getResourceAsStream("/assets/vergence/icons/logo_256x256.png");
        InputStream s128 = MixinWindow.class.getResourceAsStream("/assets/vergence/icons/logo_128x128.png");
        InputStream s64  = MixinWindow.class.getResourceAsStream("/assets/vergence/icons/logo_64x64.png");
        if (s256 == null || s128 == null) {
            return instance.getIcons(resourcePack);
        }
        return List.of(
                (InputSupplier<InputStream>) () -> s256,
                () -> s128,
                () -> s64
        );
    }
}
