package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import cn.lingsmc.lingshttputils.requesters.HTTPRequester;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class AsyncWorkers {
    @Getter
    private final FileConfiguration config = LingsHTTPUtils.getInstance().getConfig();
    @Getter
    Runnable taskB;
    @Getter
    Runnable task;

    public void asyncworker(String module, int reqTime, String url, String method, int refInterval, String[] keys) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                String res = HTTPRequester.request(url, reqTime, method);
                if (res == null) {
                    return;
                }
                if ("json".equals(config.getString(String.format("%s.mode", module)))) {
                    res = JsonUtils.getValue(JsonUtils.parseStr(res), keys, 0);
                }
                LingsHTTPUtils.getInstance().getHttpData().put(module, res);
                try {
                    Thread.sleep(refInterval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin(LingsHTTPUtils.getInstance().getName()), task);
            }
        };
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin(LingsHTTPUtils.getInstance().getName()), task);
    }
}
