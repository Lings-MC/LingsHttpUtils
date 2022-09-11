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
        Set<String> modules = LingsHTTPUtils.getInstance().getConfig().getKeys(false);
        modules.remove("version");

        ThreadFactory threadFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return new Thread(runnable);
        };

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(modules.size(), modules.size(), Integer.MAX_VALUE, TimeUnit.DAYS, new LinkedBlockingQueue<>(modules.size()), threadFactory);

        newFixedThreadPool = Executors.newFixedThreadPool(modules.size());
        FileConfiguration config = LingsHTTPUtils.getInstance().getConfig();
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
                for (int i = 0; i < 10; i++) {
                    final int index = i;
                    threadPool.execute(() -> {
                        System.out.println(index + " 被执行,线程名:" + Thread.currentThread().getName());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
                Runnable runnable = () -> asyncWorkers.asyncworker(module, reqTime, url, method, refInterval, finalKeys);
                LingsHTTPUtils.getInstance().getLogger().info("尝试启动Cycle Workers...");
                //newFixedThreadPool.execute(runnable);
                Thread t = threadFactory.newThread(runnable);
                t.setName(String.format("LHU-%s",module));
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