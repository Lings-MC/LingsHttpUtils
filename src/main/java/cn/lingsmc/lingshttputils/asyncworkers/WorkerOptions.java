package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class WorkerOptions {
    @Getter
    static boolean started = true;
    static ExecutorService threadPool;


    private WorkerOptions() {
    }

    public static void runWorkers() {
        started = true;
        FileConfiguration config = LingsHTTPUtils.config;
        Set<String> modules = config.getKeys(false);
        modules.remove("version");
        modules.removeIf(module -> Objects.equals(config.getString(String.format("%s.reqMode", module)), "Cycle"));

        threadPool = new ThreadPoolExecutor(1, modules.size(),
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

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
            Runnable runnable = () -> asyncWorkers.asyncworker(module, reqTime, url, method, refInterval, finalKeys);
            LingsHTTPUtils.getInstance().getLogger().log(Level.SEVERE, "尝试启动{0}...", module);
            threadPool.execute(runnable);
        }
    }

    public static void stopWorkers() {
        LingsHTTPUtils.getInstance().getLogger().info("尝试关闭Cycle Worker...");
        threadPool.shutdownNow();
        started = false;
    }
}