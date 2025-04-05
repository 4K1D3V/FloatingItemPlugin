package gg.kite.core.events;

import gg.kite.core.Main;
import gg.kite.core.entity.FloatingItem;
import gg.kite.core.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public final class FloatingItemEvent implements Listener {
    private final Main plugin;

    public FloatingItemEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        plugin.floatingItemManager().getFloatingItems().entrySet().removeIf(entry ->
            entry.getValue().getLocation().getChunk().equals(event.getChunk()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand stand)) return;
        plugin.floatingItemManager().getFloatingItems().values().stream()
            .filter(fi -> fi.getArmorStand().equals(stand))
            .findFirst()
            .ifPresent(fi -> handleInteraction(event.getPlayer(), fi));
    }

    private void handleInteraction(Player player, FloatingItem item) {
        FloatingItemInteractEvent customEvent = new FloatingItemInteractEvent(player, item);
        Bukkit.getPluginManager().callEvent(customEvent);
        if (customEvent.isCancelled()) return;

        String action = plugin.configManager().getInteractAction();
        Location loc = item.getLocation();
        plugin.configManager().getInteractParticle()
            .map(Particle::valueOf)
            .ifPresent(p -> loc.getWorld().spawnParticle(p, loc, plugin.configManager().getInteractParticleCount()));
        loc.getWorld().playSound(loc, plugin.configManager().getInteractSound(), 
            plugin.configManager().getSoundVolume(), plugin.configManager().getSoundPitch());

        switch (action) {
            case "PICKUP" -> {
                player.getInventory().addItem(item.getItem());
                plugin.floatingItemManager().removeFloatingItem(item.id());
                MessageUtil.sendSuccess(player, "Picked up floating item!");
            }
            case "REMOVE" -> {
                plugin.floatingItemManager().removeFloatingItem(item.id());
                MessageUtil.sendSuccess(player, "Removed floating item!");
            }
            case "TELEPORT" -> {
                player.teleport(item.getLocation());
                MessageUtil.sendSuccess(player, "Teleported to floating item!");
            }
            case "COMMAND" -> {
                String cmd = plugin.configManager().getInteractCommand().replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                MessageUtil.sendSuccess(player, "Executed command for floating item!");
            }
        }
    }
}