package cn.lingsmc.lingshttputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class TabComplete implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        final String[] root = new String[]{"workers", "help", "reload"};
        final String[] worker = new String[]{"start", "stop"};

        if (args.length <= 1) {
            return Arrays.asList(root);
        }
        if (args.length <= 2 && root[0].equalsIgnoreCase(args[0])) {
            return Arrays.asList(worker);
        }
        return Collections.emptyList();
    }
}
