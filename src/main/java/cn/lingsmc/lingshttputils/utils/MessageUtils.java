package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class MessageUtils {
    public static final String MESSAGE_HEAD = String.format("%s[%s%s%s] %s", ChatColor.DARK_AQUA, ChatColor.AQUA, LingsHttpUtils.getInstance().getName(), ChatColor.DARK_AQUA, ChatColor.RESET);

    private MessageUtils() {
    }

    public static void sendMessage(@NotNull Player p, String message) {
        p.sendMessage(String.format("%s%s", MESSAGE_HEAD, message));
    }

    public static void sendMessage(@NotNull CommandSender p, String message) {
        p.sendMessage(String.format("%s%s", MESSAGE_HEAD, message));
    }
}