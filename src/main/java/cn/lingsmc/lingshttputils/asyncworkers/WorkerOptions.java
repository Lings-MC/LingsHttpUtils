package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class WorkerOptions {
    @Getter
    static boolean started = true;
    private static ExecutorService newFixedThreadPool;


    private WorkerOptions() {
    }

    public static void runWorkers() {
        started = true;
        FileConfiguration config = LingsHTTPUtils.getConfig();
        Set<String> modules = config.getKeys(false);
        modules.remove("version");

        ThreadFactory threadFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return new Thread(runnable);
        };

        newFixedThreadPool = Executors.newFixedThreadPool(modules.size());

        for (String module : modules) {
            if (Objects.equals(config.getString(String.format("%s.reqMode", module)), "Cycle")) {
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
                //newFixedThreadPool.execute(runnable);
                Thread t = threadFactory.newThread(runnable);
                t.setName(String.format("LHU-%s",module));
                LingsHTTPUtils.getInstance().getLogger().info(String.format("尝试启动%s...",t.getName()));
                t.start();
            }
        }
    }

    public static void stopWorkers() {
        LingsHTTPUtils.getInstance().getLogger().info("尝试关闭Cycle Worker...");
        newFixedThreadPool.shutdownNow();
        started = false;
    }
}