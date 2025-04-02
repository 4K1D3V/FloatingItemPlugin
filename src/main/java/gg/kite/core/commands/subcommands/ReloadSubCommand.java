package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;

public final class ReloadSubCommand implements SubCommand {
    private final Main plugin;

    public ReloadSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        plugin.configManager().reload();
        MessageUtil.sendSuccess(player, "Configuration reloaded successfully");
        return true;
    }

    @Override
    public String getName() { return "reload"; }
}