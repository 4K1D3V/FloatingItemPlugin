package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;

public final class RemoveAllSubCommand implements SubCommand {
    private final Main plugin;

    public RemoveAllSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (plugin.floatingItemManager().getFloatingItems().isEmpty()) {
            MessageUtil.sendInfo(player, "No floating items to remove.");
            return true;
        }
        plugin.floatingItemManager().removeAllFloatingItems();
        MessageUtil.sendSuccess(player, "All floating items removed.");
        return true;
    }

    @Override
    public String getName() { return "removeall"; }
}