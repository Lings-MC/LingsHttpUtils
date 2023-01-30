package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.asyncworkers.WorkerOptions;
import cn.lingsmc.lingshttputils.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.lingsmc.lingshttputils.commands.Commands.helpMessage;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class WorkerOptionCommands {
    private WorkerOptionCommands() {
    }

    public static void workerOptionCommands(@NotNull CommandSender sender, String [] args) {

        if (!sender.hasPermission(Commands.permission)) {
            sender.sendMessage(String.format("%s你没有执行此命令的权限.", ChatColor.RED));
            return;
        }
        switch (args[1]) {
            case "start":
                if (!WorkerOptions.isStarted()) {
                    WorkerOptions.runWorkers();
                    MessageUtils.sendMessage(sender, String.format("%s正在启动Cycle Workers...", ChatColor.GREEN));
                } else {
                    MessageUtils.sendMessage(sender, String.format("%sCycle Workers已启动!", ChatColor.RED));
                }
                return;
            case "stop":
                if (WorkerOptions.isStarted()) {
                    WorkerOptions.stopWorkers();
                    MessageUtils.sendMessage(sender, String.format("%s正在关闭Cycle Workers...", ChatColor.GREEN));
                } else {
                    MessageUtils.sendMessage(sender, String.format("%sCycle Workers已关闭!", ChatColor.RED));
                }
                return;
            default:
                sender.sendMessage(helpMessage);
                break;
        }
    }
}
