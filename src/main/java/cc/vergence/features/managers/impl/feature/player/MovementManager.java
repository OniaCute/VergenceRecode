package cc.vergence.features.managers.impl.feature.player;

import cc.vergence.features.managers.Manager;
import cc.vergence.utils.interfaces.IMinecraft;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class MovementManager extends Manager implements IMinecraft {
    public MovementManager() {
        super("Movement Manager");
    }
}
