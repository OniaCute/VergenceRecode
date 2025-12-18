package cc.vergence.features.event.impl.features.render;

import cc.vergence.features.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;

@Getter
public class Render2DEvent extends Event {
    private final DrawContext context;
    private final float tickDelta;

    public Render2DEvent() {
        this.context = null;
        this.tickDelta = -1;
    }

    public Render2DEvent(DrawContext context, float tickDelta) {
        this.context = context;
        this.tickDelta = tickDelta;
    }

    /**
     * @author Onia
     * @tips DrawContext is null when render event was post by skia renderer.
     */
    public boolean isContextNull() {
        return context == null;
    }
}
