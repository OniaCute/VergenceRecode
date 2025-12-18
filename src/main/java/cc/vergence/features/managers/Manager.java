package cc.vergence.features.managers;

import lombok.Getter;

@Getter
public abstract class Manager {
    protected String name;

    public Manager(String name) {
        this.name = name;
    }

    /**
     * @author Onia
     * @tips Always return true unless it causes the client to crash.
     */
    public abstract boolean onLoad();
}
