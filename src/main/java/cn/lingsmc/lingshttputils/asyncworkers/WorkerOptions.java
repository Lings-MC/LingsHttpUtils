package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class WorkerOptions {
    @Getter
    static boolean started = false;

    static LingsHttpUtils plugin = LingsHttpUtils.getInstance();


    private WorkerOptions() {
    }

    public static void runWorkers() {
        if(!started){
            started = true;
            FileConfiguration config = LingsHttpUtils.config;
            Set<String> modules = config.getKeys(false);
            modules.remove("version");
            modules.removeIf(module -> !Objects.equals(config.getString(String.format("%s.reqMode", module)), "Cycle"));
            modules.removeIf(module -> Objects.equals(config.getBoolean(String.format("%s.enabled", module)), false));
            if (modules.isEmpty()) {
                return;
            }

            for (String module : modules) {
                // 一定要为每个module设置一个worker
                AsyncWorkers asyncWorkers = new AsyncWorkers();
                final int reqTime = config.getInt(String.format("%s.reqTime", module));
                final String url = config.getString(String.format("%s.url", module));
                final String method;
                final int refInterval = config.getInt(String.format("%s.refInterval", module));
                if (Objects.nonNull(config.getString(String.format("%s.method", module)))) {
                    method = config.getString(String.format("%s.method", module));
                } else {
                    method = "GET";
                }
                String[] keys = new String[0];
                if ("json".equalsIgnoreCase(config.getString(String.format("%s.mode", module)))) {
                    keys = config.getString(String.format("%s.key", module)).split("\\.");
                }
                String[] finalKeys = keys;
                LingsHttpUtils.getInstance().getLogger().log(Level.INFO, "尝试启动{0}...", module);
                asyncWorkers.runAsyncWorker(module, reqTime, url, method, refInterval, finalKeys);
            }
        }
    }

    public static void stopWorkers() {
        if (started){
            plugin.getLogger().info("尝试关闭Cycle Workers...");
            Bukkit.getScheduler().cancelTasks(plugin);
            started = false;
        }
    }
}