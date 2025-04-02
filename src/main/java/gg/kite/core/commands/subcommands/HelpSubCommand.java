package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.Map;

public final class HelpSubCommand implements SubCommand {
    private final Main plugin;

    public HelpSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        MessageUtil.sendInfo(player, "FloatingItemPlugin Commands:");
        Map<String, String> help = Map.of(
            "create", "Create a new floating item with held item",
            "remove <id>", "Remove a floating item by ID",
            "list [page]", "List all floating items (paginated)",
            "reload", "Reload the configuration",
            "setpattern <id> <pattern>", "Set animation pattern (SINE, BOUNCE, LINEAR)",
            "move <id> <x> <y> <z>", "Move a floating item to new coordinates",
            "removeall", "Remove all floating items",
            "help", "Show this help message"
        );
        help.forEach((cmd, desc) -> 
            MessageUtil.sendInfo(player, "/floatitem " + cmd + " - " + desc));
        return true;
    }

    @Override
    public String getName() { return "help"; }
}