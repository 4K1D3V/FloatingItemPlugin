package gg.kite.core.api;

import gg.kite.core.entity.FloatingItem;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

public interface FloatingItemManager {
    Map<UUID, FloatingItem> getFloatingItems();
    FloatingItem createFloatingItem(Location location, ItemStack item, int customModelData);
    void addFloatingItem(FloatingItem item);
    void removeFloatingItem(UUID id);
    void removeAllFloatingItems();
    void setPattern(UUID id, String pattern);
    void moveFloatingItem(UUID id, Location location);
}