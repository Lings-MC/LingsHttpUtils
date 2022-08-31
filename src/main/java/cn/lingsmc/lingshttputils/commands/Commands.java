package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class Commands implements CommandExecutor {
    static String helpMessage = String.format("%s未知命令!输入/lhu help查看命令列表!", ChatColor.RED);
    static String rootMessage = String.format("%s此服务器正在运行 %s%s %s%s by %s %n%s命令列表: %s/lhu help", ChatColor.DARK_AQUA, ChatColor.AQUA, LingsHTTPUtils.getInstance().getName(), LingsHTTPUtils.getInstance().getDescription().getVersion(), ChatColor.DARK_AQUA, "§aC§br§cs§du§eh§a2§be§cr§d0", ChatColor.DARK_AQUA, ChatColor.AQUA);
    static String permission = "lingshttputils.admin";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length < 1) {
            sender.sendMessage(rootMessage);
            return true;
        }
        switch (args[0]) {
            case "reload":
                ReloadCommand.reloadCommand(sender);
                break;
            case "help":
                HelpCommand.helpCommand(sender);
                break;
            case "workers":
                WorkerOptionCommands.workerOptionCommands(sender, args);
                break;
            default:
                sender.sendMessage(helpMessage);
                break;
        }
        return true;
    }
}
