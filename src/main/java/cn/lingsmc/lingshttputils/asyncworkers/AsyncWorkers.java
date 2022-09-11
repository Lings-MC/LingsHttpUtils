package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import cn.lingsmc.lingshttputils.requesters.HTTPRequester;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class AsyncWorkers {
    private final FileConfiguration config = LingsHTTPUtils.getInstance().getConfig();
    Runnable task;
    Runnable cycleTask;

    public void asyncworker(String module, int reqTime, String url, String method, int refInterval, String[] keys) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                String res = HTTPRequester.request(url, reqTime, method);
                if (res == null) {
                    return;
                }
                if ("json".equalsIgnoreCase(config.getString(String.format("%s.mode", module)))) {
                    res = JsonUtils.getValue(JsonUtils.parseStr(res), keys, 0);
                }
                LingsHTTPUtils.getInstance().getHttpData().put(module, res);
                try {
                    Thread.sleep(refInterval);
                } catch (InterruptedException e) {
                    LingsHTTPUtils.getInstance().getLogger().info("出现错误!模块请求间隔必须为正值!");
                    e.printStackTrace();
                }
            }
        };
        cycleTask = new BukkitRunnable() {
            @Override
            public void run() {
                while(WorkerOptions.isStarted()){
                    Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(LingsHTTPUtils.class), task);
                }
            }
        };
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(LingsHTTPUtils.class), cycleTask);
    }
}
