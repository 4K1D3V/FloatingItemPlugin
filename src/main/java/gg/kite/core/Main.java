package gg.kite.core;

import gg.kite.core.api.FloatingItemManager;
import gg.kite.core.commands.FloatCommand;
import gg.kite.core.config.ConfigManager;
import gg.kite.core.entity.FloatingItem;
import gg.kite.core.tasks.AnimationTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private final ConfigManager configManager;
    private final FloatingItemManager floatingItemManager;
    private final AnimationTask animationTask;
    private final FloatCommand floatCommand;

    public Main() {
        this.configManager = new ConfigManager(this);
        this.floatingItemManager = new FloatingItemManagerImpl(this);
        this.animationTask = new AnimationTask(this, floatingItemManager.getFloatingItems());
        this.floatCommand = new FloatCommand(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        animationTask.runTaskTimer(this, 0L, configManager.getUpdateFrequency());
        getCommand("floatitem").setExecutor(floatCommand);
        getCommand("floatitem").setTabCompleter(floatCommand);
        Bukkit.getPluginManager().registerEvents(new gg.kite.core.events.FloatingItemEvent(this), this);
        Thread.startVirtualThread(floatingItemManager::loadFloatingItems);
        getLogger().info("FloatingItemPlugin enabled by KiteGG");
    }

    @Override
    public void onDisable() {
        floatingItemManager.saveFloatingItems();
        floatingItemManager.getFloatingItems().values().parallelStream().forEach(FloatingItem::remove);
        floatingItemManager.getFloatingItems().clear();
        animationTask.cancel();
        getLogger().info("FloatingItemPlugin disabled");
    }

    public ConfigManager configManager() { return configManager; }
    public FloatingItemManager floatingItemManager() { return floatingItemManager; }
}