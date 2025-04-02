package gg.kite.core.commands.subcommands;

import gg.kite.core.Main;
import gg.kite.core.commands.SubCommand;
import gg.kite.core.entity.FloatingItem;
import gg.kite.core.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class CreateSubCommand implements SubCommand {
    private final Main plugin;

    public CreateSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            MessageUtil.sendError(player, "Please hold an item!");
            return true;
        }
        if (!plugin.configManager().isItemTypeAllowed(item.getType())) {
            MessageUtil.sendError(player, "This item type is not allowed!");
            return true;
        }
        if (!plugin.configManager().isWorldAllowed(player.getWorld().getName())) {
            MessageUtil.sendError(player, "Floating items are not allowed in this world!");
            return true;
        }
        FloatingItem floatingItem = plugin.floatingItemManager().createFloatingItem(player.getLocation(), item, 1);
        MessageUtil.sendSuccessWithId(player, "Floating item created with ID: ", floatingItem.id().toString());
        return true;
    }

    @Override
    public String getName() { return "create"; }
}