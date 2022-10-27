package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import cn.lingsmc.lingshttputils.utils.MessageUtils;
import cn.lingsmc.lingshttputils.utils.RequestUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

/**
 * @author Crsuh2er0
 * @apiNote
 * @since 2022/10/20
 */
public class RequestCommand {
    private static final FileConfiguration CONFIG = LingsHttpUtils.getInstance().getConfig();
    static LingsHttpUtils plugin = LingsHttpUtils.getInstance();
    private RequestCommand() {
    }

    public static void request(String module, @NotNull CommandSender sender) {
        if (!sender.hasPermission(Commands.permission)) {
            sender.sendMessage(String.format("%s你没有执行此命令的权限.", ChatColor.RED));
            return;
        }
        // 判断是否有此模块
        Set<String> modules = CONFIG.getKeys(false);
        if (!modules.contains(module)) {
            MessageUtils.sendMessage(sender, String.format("%s错误:未找到此模块. 请检查拼写以及是否已重载配置文件.", ChatColor.RED));
            return;
        }
        // 不管是循环还是及时都直接请求一次
        String res = RequestUtils.request(module, CONFIG, plugin);
        // 判断结果是否为null或空值
        if (Objects.isNull(res) || res.isEmpty()) {
            MessageUtils.sendMessage(sender, String.format("%s%s %s获取到的结果为 %snull", ChatColor.YELLOW, module, ChatColor.GREEN, ChatColor.GOLD));
        } else {
            MessageUtils.sendMessage(sender, String.format("%s%s %s获取到的结果为 %s%s", ChatColor.YELLOW, module, ChatColor.GREEN, ChatColor.GOLD, res));
        }

    }
}
