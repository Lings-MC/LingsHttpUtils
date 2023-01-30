package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
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
    static String helpMessage = String.format("%s未知命令! 输入/lhu help查看命令列表!", ChatColor.RED);
    static String rootMessage1 = String.format("%s此服务器正在运行 %s%s %s%s by %s", ChatColor.DARK_AQUA, ChatColor.AQUA, LingsHttpUtils.getInstance().getName(), LingsHttpUtils.getInstance().getDescription().getVersion(), ChatColor.DARK_AQUA, "§aC§br§cs§du§eh§a2§be§cr§d0");
    static String rootMessage2 = String.format("%s命令列表: %s/lhu help", ChatColor.DARK_AQUA, ChatColor.AQUA);
    static String permission = "lingshttputils.admin";
    static String notEnoughArgs = String.format("%s参数错误! 正确用法:", ChatColor.RED);
    static String requestMessage = String.format("%s/lhu request <module> %s请求配置文件中某模块的数据", ChatColor.AQUA, ChatColor.GREEN);
    static String workersMessage = String.format("%s/lhu workers <start/stop> %s启动/关闭Cycle Workers", ChatColor.AQUA, ChatColor.GREEN);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String [] args) {
        if (args.length < 1) {
            sender.sendMessage(rootMessage1);
            sender.sendMessage(rootMessage2);
            return true;
        }
        switch (args[0]) {
            case "reload":
                try {
                    ReloadCommand.reloadCommand(sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "help":
                HelpCommand.helpCommand(sender);
                break;
            case "workers":
                // 检查参数是否充足
                if (args.length < 2) {
                    sender.sendMessage(notEnoughArgs);
                    sender.sendMessage(workersMessage);
                } else {
                    WorkerOptionCommands.workerOptionCommands(sender, args);
                }
                break;
            case "request":
                if (args.length == 2) {
                    RequestCommand.request(args[1], sender);
                } else {
                    sender.sendMessage(notEnoughArgs);
                    sender.sendMessage(requestMessage);
                }
                break;
            default:
                sender.sendMessage(helpMessage);
                break;
        }
        return true;
    }
}
