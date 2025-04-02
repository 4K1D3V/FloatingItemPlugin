package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class ListSubCommand implements SubCommand {
    private final Main plugin;
    private static final int ITEMS_PER_PAGE = 5;

    public ListSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        int page = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        if (page < 1) page = 1;

        List<UUID> ids = plugin.floatingItemManager().getFloatingItems().keySet().stream()
            .sorted()
            .toList();
        int totalPages = (int) Math.ceil((double) ids.size() / ITEMS_PER_PAGE);

        if (ids.isEmpty()) {
            MessageUtil.sendInfo(player, "No active floating items.");
            return true;
        }

        if (page > totalPages) {
            MessageUtil.sendError(player, "Page " + page + " does not exist. Max pages: " + totalPages);
            return true;
        }

        int start = (page - 1) * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, ids.size());
        MessageUtil.sendInfo(player, "Active floating items (Page " + page + "/" + totalPages + "):");
        ids.subList(start, end).forEach(uuid -> 
            MessageUtil.sendWarning(player, "- " + uuid));
        return true;
    }

    @Override
    public String getName() { return "list"; }
}