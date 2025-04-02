package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.exceptions.FloatingItemException;
import gg.kite.core.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class MoveSubCommand implements SubCommand {
    private final Main plugin;

    public MoveSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 5) {
            MessageUtil.sendError(player, "Usage: /floatitem move <id> <x> <y> <z>");
            return true;
        }

        try {
            UUID id = UUID.fromString(args[1]);
            double x = Double.parseDouble(args[2]);
            double y = Double.parseDouble(args[3]);
            double z = Double.parseDouble(args[4]);
            if (!plugin.floatingItemManager().getFloatingItems().containsKey(id)) {
                throw new FloatingItemException("Item not found with ID: " + args[1]);
            }
            Location newLocation = new Location(player.getWorld(), x, y, z);
            if (!plugin.configManager().isWorldAllowed(newLocation.getWorld().getName())) {
                throw new FloatingItemException("Target world is not allowed for floating items!");
            }
            plugin.floatingItemManager().moveFloatingItem(id, newLocation);
            MessageUtil.sendSuccess(player, "Moved floating item " + args[1] + " to " + x + ", " + y + ", " + z);
        } catch (IllegalArgumentException e) {
            MessageUtil.sendError(player, "Invalid UUID or coordinates format!");
            plugin.getLogger().warning("Invalid input for move command: " + String.join(" ", args));
        } catch (FloatingItemException e) {
            MessageUtil.sendError(player, e.getMessage());
            plugin.getLogger().info(e.getMessage());
        }
        return true;
    }

    @Override
    public String getName() { return "move"; }
}