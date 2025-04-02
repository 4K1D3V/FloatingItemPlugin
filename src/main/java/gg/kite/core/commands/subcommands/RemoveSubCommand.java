package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.exceptions.FloatingItemException;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class RemoveSubCommand implements SubCommand {
    private final Main plugin;

    public RemoveSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtil.sendError(player, "Usage: /floatitem remove <id>");
            return true;
        }

        try {
            UUID id = UUID.fromString(args[1]);
            if (!plugin.floatingItemManager().getFloatingItems().containsKey(id)) {
                throw new FloatingItemException("Item not found with ID: " + args[1]);
            }
            plugin.floatingItemManager().removeFloatingItem(id);
            MessageUtil.sendSuccess(player, "Floating item removed");
        } catch (IllegalArgumentException e) {
            MessageUtil.sendError(player, "Invalid UUID format!");
            plugin.getLogger().warning("Invalid UUID attempted: " + args[1]);
        } catch (FloatingItemException e) {
            MessageUtil.sendError(player, e.getMessage());
            plugin.getLogger().info(e.getMessage());
        }
        return true;
    }

    @Override
    public String getName() { return "remove"; }
}