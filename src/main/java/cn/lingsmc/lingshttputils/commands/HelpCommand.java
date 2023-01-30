package cn.lingsmc.lingshttputils.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class HelpCommand {
    private HelpCommand() {
    }

    public static void helpCommand(@NotNull CommandSender sender) {
        sender.sendMessage(String.format("%s%s----- LingsHttpUtils指令 -----", ChatColor.DARK_AQUA, ChatColor.BOLD));
        sender.sendMessage(String.format("%s/lhu workers <start/stop> %s- %s启动/关闭Cycle Workers", ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.GREEN));
        sender.sendMessage(String.format("%s/lhu request <module> %s- %s请求配置文件中某模块的数据", ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.GREEN));
        sender.sendMessage(String.format("%s/lhu reload %s- %s重载插件", ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.GREEN));
    }
}
