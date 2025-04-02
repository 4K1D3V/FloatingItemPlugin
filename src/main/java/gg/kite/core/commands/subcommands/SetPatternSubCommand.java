package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.exceptions.FloatingItemException;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public final class SetPatternSubCommand implements SubCommand {
    private final Main plugin;
    private static final Set<String> VALID_PATTERNS = Set.of("SINE", "BOUNCE", "LINEAR");

    public SetPatternSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 3) {
            MessageUtil.sendError(player, "Usage: /floatitem setpattern <id> <pattern>");
            return true;
        }

        try {
            UUID id = UUID.fromString(args[1]);
            String pattern = args[2].toUpperCase();
            if (!VALID_PATTERNS.contains(pattern)) {
                throw new FloatingItemException("Invalid pattern: " + args[2] + ". Use SINE, BOUNCE, or LINEAR");
            }
            if (!plugin.floatingItemManager().getFloatingItems().containsKey(id)) {
                throw new FloatingItemException("Item not found with ID: " + args[1]);
            }
            plugin.floatingItemManager().setPattern(id, pattern);
            MessageUtil.sendSuccess(player, "Pattern set to " + pattern + " for item " + args[1]);
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
    public String getName() { return "setpattern"; }
}