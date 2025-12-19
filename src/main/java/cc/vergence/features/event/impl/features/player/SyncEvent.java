package cc.vergence.features.event.impl.features.player;

import cc.vergence.features.event.Event;
import cc.vergence.features.objects.Angle;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.Vec3d;

@Getter
@Setter
public class SyncEvent extends Event {
    private Vec3d position;
    private Angle angle;

    public SyncEvent(Vec3d position, Angle angle) {
        this.position = position;
        this.angle = angle;
    }

    public SyncEvent(Vec3d position) {
        this.position = position;
        this.angle = null;
    }

    public SyncEvent(Angle angle) {
        this.position = null;
        this.angle = angle;
    }

    public boolean isFull() {
        return this.position != null && this.angle != null;
    }

    public boolean positionOnly() {
        return this.position != null && this.angle == null;
    }

    public boolean angleOnly() {
        return this.position == null && this.angle != null;
    }
}
