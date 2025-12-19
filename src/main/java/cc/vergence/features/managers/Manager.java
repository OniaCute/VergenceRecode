package cc.vergence.features.managers;

import lombok.Getter;

@Getter
public abstract class Manager {
    protected String name;
    public Manager(String name) {
        this.name = name;
    }
}
