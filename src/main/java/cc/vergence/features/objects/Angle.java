package cc.vergence.features.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Angle {
    private float pitch;
    private float yaw;

    public Angle(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Angle() {
        this.pitch = 0;
        this.yaw = 0;
    }
}
