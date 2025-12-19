package cc.vergence;

import cc.vergence.features.event.eventbus.EventBus;
import cc.vergence.features.event.impl.client.InitializeEvent;
import cc.vergence.features.event.impl.client.ShutdownEvent;
import cc.vergence.features.event.impl.client.UnloadEvent;
import cc.vergence.features.managers.Manager;
import cc.vergence.features.managers.impl.client.NotificationManager;
import cc.vergence.features.managers.impl.client.UrlManager;
import cc.vergence.features.managers.impl.feature.entity.EntityManager;
import cc.vergence.features.managers.impl.feature.player.InventoryManager;
import cc.vergence.features.managers.impl.feature.player.MovementManager;
import cc.vergence.features.managers.impl.feature.player.RotateManager;
import cc.vergence.features.managers.impl.feature.render.GuiManager;
import cc.vergence.features.managers.impl.feature.render.HudManager;
import cc.vergence.utils.debug.Console;
import cc.vergence.utils.interfaces.IMinecraft;
import cc.vergence.utils.maths.RandomUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;

public class Vergence implements ModInitializer, IMinecraft {
    // Information & Mod status
    public static final ModMetadata MOD_INFO;
    public static final String MOD_ID = "vergence";
    public static final String NAME = "Vergence";
    public static final String VERSION = "2.0.0";
    public static final String CONFIG_TEMPLATE_VERSION = "vergence_2_0_vcg_json";
    public static final String UI_STYLE_VERSION = "vergence_2_0_ui_mixed";
    public static final ArrayList<String> AUTHORS = new ArrayList<String>();
    public static String PREFIX = "$";
    public static boolean LOADED = false;
    public static boolean OUT_OF_DATE = false;
    public static long LOAD_TIME;

    // Manager & Preload
    public static Console CONSOLE;
    public static EventBus EVENTBUS;
    public static UrlManager URL;
    public static NotificationManager NOTIFY;
    public static EntityManager ENTITIES;
    public static InventoryManager INVENTORY;
    public static MovementManager MOVEMENT;
    public static RotateManager ROTATION;
    public static GuiManager GUI;
    public static HudManager HUD;

    // Mod Info Load
    static {
        MOD_INFO = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();
    }

    @Override
    public void onInitialize() {
        CONSOLE = new Console();
        CONSOLE.logInfo("Vergence Client | Preloading ...", true);
        CONSOLE.logInfo("VERSION: " + VERSION, true);
        CONSOLE.logInfo("Authors: Voury, OniaCute", true);

        if (!MOD_INFO.getId().equals(MOD_ID)) {
            CONSOLE.logWarn("Fabric mod value check failed!");
            CONSOLE.logWarn("Vergence Client was exited!");
            CONSOLE.logWarn("This version may have been acquired from informal sources or created without permission!");
            mc.stop();
        } else {
            load();
        }
    }

    public static void load() {
        // Pre load
        CONSOLE.logInfo("Event Bus is loading...");
        EVENTBUS = new EventBus();
        CONSOLE.logInfo("Event Bus is loaded.");
        CONSOLE.logInfo("Vergence Client was preloaded.");
        // Information define & Intellectual property declaration
        AUTHORS.add("Voury");
        AUTHORS.add("Onia");
        // Real load
        LOAD_TIME = System.currentTimeMillis();

        URL = (UrlManager) registerManager(new UrlManager());
        NOTIFY = (NotificationManager) registerManager(new NotificationManager());
        ENTITIES = (EntityManager) registerManager(new EntityManager());
        INVENTORY = (InventoryManager) registerManager(new InventoryManager());
        MOVEMENT = (MovementManager) registerManager(new MovementManager());
        ROTATION = (RotateManager) registerManager(new RotateManager());
        GUI = (GuiManager) registerManager(new GuiManager());
        HUD = (HudManager) registerManager(new HudManager());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (LOADED) {
                shutdown();
            }
        }));

        OUT_OF_DATE = needUpdate();
        LOAD_TIME = System.currentTimeMillis() - LOAD_TIME;
        LOADED = true;
        EVENTBUS.post(new InitializeEvent(LOAD_TIME));

        CONSOLE.logInfo("Vergence Client Loaded!");
        CONSOLE.logInfo("Vergence Loaded In " + (LOAD_TIME) + " ms.");
    }

    public static boolean needUpdate() {
        return URL.get("https://update.vergence.cc/", MOD_ID+"_"+VERSION);
    }

    public static void shutdown() {
        CONSOLE.logInfo("Vergence Client is shutting down ...");
        EVENTBUS.post(new ShutdownEvent());
        EVENTBUS.unload();
        LOADED = false;
        LOAD_TIME = 0L;
        CONSOLE.logInfo("Vergence Client has been shut down");
    }

    public static void unload() {
        CONSOLE.logInfo("Vergence Client is unloading ...");
        EVENTBUS.post(new UnloadEvent());
        EVENTBUS.unload();
        LOADED = false;
        LOAD_TIME = 0L;
        CONSOLE.logInfo("Vergence Client has been unloaded.");
    }

    private static Manager registerManager(Manager manager) {
        CONSOLE.logInfo("Loading " + manager.getName() + " ...");
        EVENTBUS.subscribe(manager);
        return manager;
    }
}
