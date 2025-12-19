package cc.vergence.features.event.impl.features.render;

import cc.vergence.features.event.Event;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;

@Getter
public class Render3DEvent extends Event {
    private final MatrixStack matrixStack;
    private final float tickDelta;

    public Render3DEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }
}
