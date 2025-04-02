package gg.kite.core;

import gg.kite.core.api.FloatingItemManager;
import gg.kite.core.entity.FloatingItem;
import gg.kite.core.events.FloatingItemCreateEvent;
import gg.kite.core.events.FloatingItemRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FloatingItemManagerImpl implements FloatingItemManager {
    private static final File DATA_FILE = new File("plugins/FloatingItemPlugin/data.yml");
    private static final File BACKUP_DIR = new File("plugins/FloatingItemPlugin/backups");
    private final Main plugin;
    private final Map<UUID, FloatingItem> floatingItems;

    public FloatingItemManagerImpl(Main plugin) {
        this.plugin = plugin;
        this.floatingItems = new ConcurrentHashMap<>();
    }

    @Override
    public Map<UUID, FloatingItem> getFloatingItems() { return floatingItems; }

    @Override
    public FloatingItem createFloatingItem(Location location, ItemStack item, int customModelData) {
        FloatingItem floatingItem = new FloatingItem(plugin, location, item, customModelData);
        addFloatingItem(floatingItem);
        Bukkit.getPluginManager().callEvent(new FloatingItemCreateEvent(floatingItem));
        return floatingItem;
    }

    @Override
    public void addFloatingItem(FloatingItem item) { floatingItems.put(item.id(), item); }

    @Override
    public void removeFloatingItem(UUID id) {
        FloatingItem item = floatingItems.remove(id);
        if (item != null) {
            item.remove();
            Bukkit.getPluginManager().callEvent(new FloatingItemRemoveEvent(item));
        }
    }

    @Override
    public void removeAllFloatingItems() {
        floatingItems.values().parallelStream().forEach(item -> {
            item.remove();
            Bukkit.getPluginManager().callEvent(new FloatingItemRemoveEvent(item));
        });
        floatingItems.clear();
    }

    @Override
    public void setPattern(UUID id, String pattern) {
        floatingItems.computeIfPresent(id, (k, item) -> {
            item.setPattern(AnimationPattern.fromString(pattern));
            return item;
        });
    }

    @Override
    public void moveFloatingItem(UUID id, Location location) {
        floatingItems.computeIfPresent(id, (k, item) -> {
            item.move(location);
            return item;
        });
    }

    public void saveFloatingItems() {
        if (plugin.configManager().isBackupEnabled()) {
            backupDataFile();
        }
        YamlConfiguration yaml = new YamlConfiguration();
        floatingItems.forEach((id, item) -> {
            yaml.set(id.toString() + ".location", item.getLocation());
            yaml.set(id.toString() + ".item", item.getItem());
            yaml.set(id.toString() + ".customModelData", item.getCustomModelData());
        });
        try {
            yaml.save(DATA_FILE);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save floating items: " + e.getMessage());
        }
    }

    public void loadFloatingItems() {
        if (!DATA_FILE.exists()) return;
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(DATA_FILE);
        yaml.getKeys(false).forEach(idStr -> {
            try {
                UUID id = UUID.fromString(idStr);
                Location loc = yaml.getLocation(idStr + ".location");
                ItemStack item = yaml.getItemStack(idStr + ".item");
                int customModelData = yaml.getInt(idStr + ".customModelData", 1);
                if (plugin.configManager().isWorldAllowed(loc.getWorld().getName()) && 
                    plugin.configManager().isItemTypeAllowed(item.getType())) {
                    FloatingItem fi = new FloatingItem(plugin, loc, item, customModelData);
                    addFloatingItem(fi);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load floating item " + idStr + ": " + e.getMessage());
            }
        });
    }

    private void backupDataFile() {
        if (!BACKUP_DIR.exists()) BACKUP_DIR.mkdirs();
        String timestamp = String.valueOf(System.currentTimeMillis());
        File backupFile = new File(BACKUP_DIR, "data_backup_" + timestamp + ".yml");
        try {
            Files.copy(DATA_FILE.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            manageBackups();
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to create backup: " + e.getMessage());
        }
    }

    private void manageBackups() {
        File[] backups = BACKUP_DIR.listFiles((dir, name) -> name.startsWith("data_backup_") && name.endsWith(".yml"));
        if (backups != null && backups.length > plugin.configManager().getMaxBackups()) {
            Arrays.sort(backups, Comparator.comparingLong(File::lastModified));
            for (int i = 0; i < backups.length - plugin.configManager().getMaxBackups(); i++) {
                backups[i].delete();
            }
        }
    }
}