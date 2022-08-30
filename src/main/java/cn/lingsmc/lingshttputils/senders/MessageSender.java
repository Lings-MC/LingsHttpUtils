package cn.lingsmc.lingshttputils.senders;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class MessageSender {
    static final String MESSAGE_HEAD = String.format("%s[%s%s%s] %s", ChatColor.DARK_AQUA, ChatColor.AQUA, LingsHTTPUtils.getInstance().getName(), ChatColor.DARK_AQUA, ChatColor.RESET);

    private MessageSender() {
    }

    public static void sendMessage(Player p, String message) {
        p.sendMessage(String.format("%s%s", MESSAGE_HEAD, message));
    }
}
