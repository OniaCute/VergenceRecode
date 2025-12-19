package cc.vergence.features.modules;

import cc.vergence.features.enums.client.ModuleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Module {
    private String name;
    private String displayName;
    private String description;
    private boolean status;
    private int priority = 0;
    private double x;
    private double y;
    private double width;
    private double height;
    private Category category;
    private ModuleType type;

    public enum Category {
        Combat,
        Movement,
        Player,
        Visual,
        Minigames,
        Misc,
        HUD // "HUD" is a special category. All the modules under this category will be displayed on the HudEdit screen.
    }
}
