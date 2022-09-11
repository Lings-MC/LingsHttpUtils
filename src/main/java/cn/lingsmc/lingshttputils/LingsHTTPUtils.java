package cn.lingsmc.lingshttputils;


import cn.lingsmc.lingshttputils.asyncworkers.WorkerOptions;
import cn.lingsmc.lingshttputils.commands.Commands;
import cn.lingsmc.lingshttputils.commands.TabComplete;
import cn.lingsmc.lingshttputils.placeholderapi.PlaceholderAPI;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pw.yumc.Yum.config.FileConfig;

import java.io.File;
import java.io.IOException;
import java.util.*;


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

    @Getter
    private String pluginName;

    @Override
    public void onLoad() {
        instance = this;
        pluginName = this.getPluginName();
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
        // 绕过Yum网络监控
        Plugin yum = Bukkit.getPluginManager().getPlugin("Yum");
        if (Objects.nonNull(yum)) {
            final File file = new File(System.getProperty("user.dir") + "/plugins/Yum/network.yml");
            FileConfig fileConfig = new FileConfig(file);
            if(!fileConfig.getStringList("Ignore").contains(pluginName)){
                List<String> ignoreList = fileConfig.getStringList("Ignore");
                ignoreList.add(pluginName);
                fileConfig.set("Ignore",ignoreList);
                try {
                    fileConfig.save(file);
                } catch (IOException e) {
                    getLogger().info("警告! Yum配置修改失败!");
                }
            }
        }
        // bStats支持
        int pluginId = 16397;
        Metrics metrics = new Metrics(this, pluginId);
        getLogger().info("bStats已连接!");
        // 运行Cycle worker
        WorkerOptions.runWorkers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(WorkerOptions.isStarted()){
            WorkerOptions.stopWorkers();
        }
    }
}
