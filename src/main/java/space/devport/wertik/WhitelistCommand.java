package space.devport.wertik;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class WhitelistCommand implements CommandExecutor {

    private final static String HELP =
            """
                    <dark_gray><st>    </st></dark_gray> <aqua>DumbWhitelist</aqua> <dark_gray><st>    </st></dark_gray>
                    <aqua>/%label% help <dark_gray>- <gray>Display this page.
                    <aqua>/%label% add <player> <dark_gray>- <gray>Add a player to whitelist.
                    <aqua>/%label% remove <player> <dark_gray>- <gray>Remove a player from whitelist.
                    """;

    private final WhitelistPlugin plugin;

    public WhitelistCommand(WhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {

            Set<String> whitelisted = plugin.getWhitelist();

            if (whitelisted.isEmpty()) {
                sender.sendMessage(plugin.getSerializer().deserialize("<red>No players whitelisted."));
                return false;
            }

            StringBuilder head = new StringBuilder("<aqua>Whitelisted players:\n");
            whitelisted.forEach(p -> head.append("  <dark_gray>- <white>").append(p).append("\n"));

            sender.sendMessage(plugin.getSerializer().deserialize(head.toString()));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> {
                if (!argumentCheck(sender, args, 2, 2)) {
                    return false;
                }

                String playerName = args[1];

                if (plugin.isWhitelisted(playerName)) {
                    sender.sendMessage(plugin.getSerializer().deserialize("<red>Player is already whitelisted."));
                    return false;
                }

                plugin.addPlayer(playerName);
                sender.sendMessage(plugin.getSerializer().deserialize("<aqua>Player <white>%p <aqua>added to whitelist.".replace("%p", playerName)));
            }
            case "remove" -> {
                if (!argumentCheck(sender, args, 2, 2)) {
                    return false;
                }

                String playerName = args[1];

                if (!plugin.isWhitelisted(playerName)) {
                    sender.sendMessage(plugin.getSerializer().deserialize("<red>Player is not whitelisted."));
                    return false;
                }

                plugin.removePlayer(playerName);
                sender.sendMessage(plugin.getSerializer().deserialize("<aqua>Player <white>%p <aqua>removed from whitelist.".replace("%p", playerName)));
            }
            case "help" -> sender.sendMessage(plugin.getSerializer().deserialize(HELP.replace("%label%", label)));
            default -> sender.sendMessage(plugin.getSerializer().deserialize("<red>Unknown subcommand."));
        }

        return false;
    }

    private boolean argumentCheck(CommandSender sender, String[] args, int min, int max) {
        if (args.length < min) {
            sender.sendMessage(plugin.getSerializer().deserialize("<red>Not enough arguments."));
            return false;
        } else if (args.length > max) {
            sender.sendMessage(plugin.getSerializer().deserialize("<red>Too many arguments."));
            return false;
        }

        return true;
    }
}
