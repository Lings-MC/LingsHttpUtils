package cn.lingsmc.lingshttputils;


import cn.lingsmc.lingshttputils.asyncworkers.WorkerOptions;
import cn.lingsmc.lingshttputils.commands.Commands;
import cn.lingsmc.lingshttputils.commands.TabComplete;
import cn.lingsmc.lingshttputils.placeholderapi.PlaceholderAPI;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;


/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public final class LingsHTTPUtils extends JavaPlugin {
    @Getter
    private static LingsHTTPUtils instance;

    @Getter
    private final Map<String, String> httpData = Maps.newConcurrentMap();

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }
        final PluginCommand command = this.getCommand(instance.getName());
        command.setExecutor(new Commands());
        command.setTabCompleter(new TabComplete());
        // 运行Cycle worker
        WorkerOptions.runWorkers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        WorkerOptions.stopWorkers();
    }
}
