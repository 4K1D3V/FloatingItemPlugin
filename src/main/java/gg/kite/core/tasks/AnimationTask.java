package gg.kite.core.tasks;

import gg.kite.core.Main;
import gg.kite.core.entity.FloatingItem;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public final class AnimationTask extends BukkitRunnable {
    private final Main plugin;
    private final Map<UUID, FloatingItem> floatingItems;
    private long lastUpdate = System.nanoTime();
    private static final int PARALLEL_THRESHOLD = 50;

    public AnimationTask(Main plugin, Map<UUID, FloatingItem> floatingItems) {
        this.plugin = plugin;
        this.floatingItems = floatingItems;
    }

    @Override
    public void run() {
        Thread.startVirtualThread(() -> {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastUpdate) / 1_000_000_000.0;
            lastUpdate = currentTime;

            Stream<FloatingItem> stream = floatingItems.values().stream();
            if (floatingItems.size() > PARALLEL_THRESHOLD) {
                stream = stream.parallel();
            }
            stream.forEach(item -> item.updatePosition(deltaTime));
        });
    }
}