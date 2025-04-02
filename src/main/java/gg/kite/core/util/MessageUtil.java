package gg.kite.core.util;

import gg.kite.core.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public final class MessageUtil {
    private MessageUtil() {}

    public static void sendSuccess(Player player, String message) {
        Main plugin = (Main) Bukkit.getPluginManager().getPlugin("FloatingItemPlugin");
        player.sendMessage(Component.text(message, NamedTextColor.GREEN));
        player.playSound(player.getLocation(), plugin.configManager().getSuccessSound(), 
            plugin.configManager().getSoundVolume(), plugin.configManager().getSoundPitch());
    }

    public static void sendError(Player player, String message) {
        Main plugin = (Main) Bukkit.getPluginManager().getPlugin("FloatingItemPlugin");
        player.sendMessage(Component.text(message, NamedTextColor.RED));
        player.playSound(player.getLocation(), plugin.configManager().getErrorSound(), 
            plugin.configManager().getSoundVolume(), plugin.configManager().getSoundPitch());
    }

    public static void sendInfo(Player player, String message) {
        Main plugin = (Main) Bukkit.getPluginManager().getPlugin("FloatingItemPlugin");
        player.sendMessage(Component.text(message, NamedTextColor.AQUA));
        player.playSound(player.getLocation(), plugin.configManager().getInfoSound(), 
            plugin.configManager().getSoundVolume(), plugin.configManager().getSoundPitch());
    }

    public static void sendWarning(Player player, String message) {
        Main plugin = (Main) Bukkit.getPluginManager().getPlugin("FloatingItemPlugin");
        player.sendMessage(Component.text(message, NamedTextColor.YELLOW));
        player.playSound(player.getLocation(), plugin.configManager().getWarningSound(), 
            plugin.configManager().getSoundVolume(), plugin.configManager().getSoundPitch());
    }

    public static void sendSuccessWithId(Player player, String message, String id) {
        Main plugin = (Main) Bukkit.getPluginManager().getPlugin("FloatingItemPlugin");
        player.sendMessage(Component.text(message, NamedTextColor.GREEN)
            .append(Component.text(id, NamedTextColor.YELLOW)));
        player.playSound(player.getLocation(), plugin.configManager().getSuccessSound(), 
            plugin.configManager().getSoundVolume(), plugin.configManager().getSoundPitch());
    }
}