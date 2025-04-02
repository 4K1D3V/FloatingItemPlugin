package gg.kite.core.commands;

import gg.kite.core.Main;
import gg.kite.core.commands.subcommands.*;
import gg.kite.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class FloatCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;
    private final Map<String, SubCommand> subCommands;

    public FloatCommand(Main plugin) {
        this.plugin = plugin;
        this.subCommands = List.of(
            new CreateSubCommand(plugin),
            new RemoveSubCommand(plugin),
            new ListSubCommand(plugin),
            new ReloadSubCommand(plugin),
            new SetPatternSubCommand(plugin),
            new MoveSubCommand(plugin),
            new RemoveAllSubCommand(plugin),
            new HelpSubCommand(plugin)
        ).stream().collect(Collectors.toMap(SubCommand::getName, Function.identity()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return switch (sender) {
            case Player player -> handlePlayerCommand(player, args);
            default -> {
                sender.sendMessage("Only players can use this command!");
                yield true;
            }
        };
    }

    private boolean handlePlayerCommand(Player player, String[] args) {
        if (!player.hasPermission("floatitem.admin")) {
            MessageUtil.sendError(player, "No permission!");
            return true;
        }

        if (args.length == 0) {
            subCommands.get("help").execute(player, args);
            return true;
        }

        return Optional.ofNullable(subCommands.get(args[0].toLowerCase()))
            .map(cmd -> cmd.execute(player, args))
            .orElseGet(() -> {
                MessageUtil.sendError(player, "Unknown subcommand: " + args[0]);
                subCommands.get("help").execute(player, args);
                return true;
            });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("floatitem.admin")) {
            return List.of();
        }

        return switch (args.length) {
            case 1 -> subCommands.keySet().stream()
                .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                .sorted()
                .toList();
            case 2 -> switch (args[0].toLowerCase()) {
                case "remove", "setpattern", "move" -> plugin.floatingItemManager().getFloatingItems().keySet().stream()
                    .map(UUID::toString)
                    .filter(id -> id.startsWith(args[1]))
                    .sorted()
                    .toList();
                case "list" -> List.of("1", "2", "3", "4", "5");
                default -> List.of();
            };
            case 3 -> switch (args[0].toLowerCase()) {
                case "setpattern" -> List.of("SINE", "BOUNCE", "LINEAR").stream()
                    .filter(p -> p.startsWith(args[2].toUpperCase()))
                    .toList();
                case "move" -> List.of("<x>", player.getLocation().getX() + "");
                default -> List.of();
            };
            case 4 -> args[0].equalsIgnoreCase("move") ? List.of("<y>", player.getLocation().getY() + "") : List.of();
            case 5 -> args[0].equalsIgnoreCase("move") ? List.of("<z>", player.getLocation().getZ() + "") : List.of();
            default -> List.of();
        };
    }
}