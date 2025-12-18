package cc.vergence.features.managers.impl.feature.player;

import cc.vergence.features.managers.Manager;

public class InventoryManager extends Manager {
    public InventoryManager() {
        super("RotateManager");
    }

    @Override
    public boolean onLoad() {
        return true;
    }
}
