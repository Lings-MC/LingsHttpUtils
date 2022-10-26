package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import cn.lingsmc.lingshttputils.asyncworkers.WorkerOptions;
import com.ghostchu.simplereloadlib.ReloadResult;
import com.ghostchu.simplereloadlib.Reloadable;
import com.ghostchu.simplereloadlib.ReloadableContainer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class ReloadCommand {
    static LingsHttpUtils plugin = LingsHttpUtils.getInstance();

    private ReloadCommand() {
    }

    public static void reloadCommand(@NotNull CommandSender sender) throws Exception {
        if (!sender.hasPermission(Commands.permission)) {
            sender.sendMessage(String.format("%s你没有执行此命令的权限.", ChatColor.RED));
            return;
        }
        sender.sendMessage(String.format("%s正在重载中...", ChatColor.AQUA));
        plugin.reload();
        /*
        WorkerOptions.stopWorkers();
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.saveDefaultConfig();
        }
        plugin.reloadConfig();
        LingsHttpUtils.config = plugin.getConfig();
        WorkerOptions.runWorkers();*/
    }
}
