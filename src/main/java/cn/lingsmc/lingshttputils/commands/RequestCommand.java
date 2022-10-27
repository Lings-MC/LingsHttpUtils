package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import cn.lingsmc.lingshttputils.utils.HttpUtils;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import cn.lingsmc.lingshttputils.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

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
        new BukkitRunnable() {
            @Override
            public void run() {
                int reqTime = CONFIG.getInt(String.format("%s.reqTime", module));
                String url = CONFIG.getString(String.format("%s.url", module));
                String method;
                try {
                    method = CONFIG.getString(String.format("%s.method", module));
                } catch (Exception ignored) {
                    method = "GET";
                }
                String res = HttpUtils.httpRequest(url, reqTime, method);
                if (Objects.isNull(res)) {
                    return;
                }
                if ("json".equalsIgnoreCase(LingsHttpUtils.config.getString(String.format("%s.mode", module)))) {
                    String[] keys = CONFIG.getString(String.format("%s.key", module)).split("\\.");
                    res = JsonUtils.getValue(res, keys);
                    if (Objects.isNull(res)) {
                        plugin.getLogger().log(Level.SEVERE, "Worker: {0} 获取Json内容时出现错误! 请检查是否符合格式要求!", module);
                    }
                }
                // 判断结果是否为null或空值
                if (Objects.isNull(res) || res.isEmpty()) {
                    MessageUtils.sendMessage(sender, String.format("%s%s %s获取到的结果为 %snull", ChatColor.YELLOW, module, ChatColor.GREEN, ChatColor.GOLD));
                } else {
                    MessageUtils.sendMessage(sender, String.format("%s%s %s获取到的结果为 %s%s", ChatColor.YELLOW, module, ChatColor.GREEN, ChatColor.GOLD, res));
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
