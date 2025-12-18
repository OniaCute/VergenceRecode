package cc.vergence.features.event.impl.client;

import cc.vergence.features.event.Event;
import lombok.Getter;

@Getter
public class InitializeEvent extends Event {
    private long loadTime = -1;

    public InitializeEvent(long loadTime) {
        this.loadTime = loadTime;
    }
}
