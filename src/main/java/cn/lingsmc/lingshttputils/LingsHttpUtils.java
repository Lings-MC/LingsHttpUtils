package cn.lingsmc.lingshttputils;


import cn.lingsmc.lingshttputils.asyncworkers.WorkerOptions;
import cn.lingsmc.lingshttputils.commands.Commands;
import cn.lingsmc.lingshttputils.commands.TabComplete;
import cn.lingsmc.lingshttputils.placeholderapi.PlaceholderAPI;
import com.ghostchu.simplereloadlib.*;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pw.yumc.Yum.config.FileConfig;
import pw.yumc.Yum.managers.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
// TODO 更新配置文件
// TODO 加一个配置文件更新器
// TODO 加一个request调试用指令
// TODO 支持xml
// TODO 支持user-agent
// TODO 支持cookie
// TODO 支持同时获取一组数据
// TODO 支持获取套娃
// TODO 使间隔适用于intime类
// TODO 支持脚本化操作
// TODO 支持模块分文件
// TODO 添加多语言支持

public final class LingsHttpUtils extends JavaPlugin implements Reloadable {

    public static FileConfiguration config;
    @Getter
    private static LingsHttpUtils instance;
    @Getter
    private final Map<String, String> httpData = Maps.newConcurrentMap();
    public boolean gson = false;
    @Getter
    private String pluginName;
    @Getter
    ReloadManager reloadManager;

    @Override
    public void onLoad() {
        if (Bukkit.getBukkitVersion().compareTo("1.14") >= 0) {
            gson = true;
        }
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        pluginName = this.getDescription().getName();
        reloadManager = new ReloadManager();
        reloadManager.register(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new PlaceholderAPI(this).register();
        final PluginCommand command = this.getCommand(instance.getName());
        command.setExecutor(new Commands());
        command.setTabCompleter(new TabComplete());
        // 绕过Yum网络监控
        Plugin yum = Bukkit.getPluginManager().getPlugin("Yum");
        if (Objects.nonNull(yum)) {
            final File file = new File(System.getProperty("user.dir") + "/plugins/Yum/network.yml");
            FileConfig fileConfig = new FileConfig(file);
            if (!fileConfig.getStringList("Ignore").contains(pluginName)) {
                List<String> ignoreList = fileConfig.getStringList("Ignore");
                ignoreList.add(pluginName);
                fileConfig.set("Ignore", ignoreList);
                try {
                    fileConfig.save(file);
                    ConfigManager.i().reload();
                    getLogger().info("检测到Yum，已自动修改Yum配置防刷屏!");
                } catch (IOException e) {
                    getLogger().info("警告! Yum配置修改失败!");
                }
            }
        }
        // bStats支持
        int pluginId = 16397;
        Metrics metrics = new Metrics(this, pluginId);
        // 运行Cycle worker
        WorkerOptions.runWorkers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (WorkerOptions.isStarted()) {
            WorkerOptions.stopWorkers();
        }
    }
    public void reload() throws Exception {
        Map<ReloadableContainer, ReloadResult> results = reloadManager.reload();
        System.out.println(reloadModule().toString());
    }
    @Override
    public ReloadResult reloadModule() throws Exception {
        try{
            // Reload code here
            return ReloadResult.builder().status(ReloadStatus.SUCCESS).build();
        } catch (IllegalStateException scheduleException) {
            return ReloadResult.builder().status(ReloadStatus.SCHEDULED).reason("Resource in use").build();
        } catch (RuntimeException requireRestartException) {
            return ReloadResult.builder().status(ReloadStatus.REQUIRE_RESTART).reason("Restart required").build();
        } catch (Exception otherException){
            return ReloadResult.builder().status(ReloadStatus.EXCEPTION).exception(otherException).reason("Unkown error raised").build();
        }
        // If there have any Exception not be catched, Manager will catch it and report with ReloadStatus.EXCEPTION
    }
}
