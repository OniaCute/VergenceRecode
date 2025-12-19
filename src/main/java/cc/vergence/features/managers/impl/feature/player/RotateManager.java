package cc.vergence.features.managers.impl.feature.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.Subscribe;
import cc.vergence.features.event.impl.features.player.PlayerTickEvent;
import cc.vergence.features.event.impl.features.player.SyncEvent;
import cc.vergence.features.managers.Manager;
import cc.vergence.features.objects.Angle;
import cc.vergence.utils.maths.RandomUtils;
import lombok.Getter;

@Getter
public class RotateManager extends Manager {
    private Angle clientAngle = new Angle(0, 0); // The real angle of the client player
    private Angle serverAngle = new Angle(0, 0); // The server side angle of the player

    public RotateManager() {
        super("Rotation Manager");
    }

    @Subscribe
    public void onTick(PlayerTickEvent event) {
        this.serverAngle = new Angle(RandomUtils.getFloat(-90, 90), RandomUtils.getFloat(-180, 180));
    }
}
