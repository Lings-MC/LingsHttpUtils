package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import cn.lingsmc.lingshttputils.utils.HttpUtils;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class AsyncWorkers {
    Runnable task;

    public void asyncworker(String module, int reqTime, String url, String method, int refInterval, String[] keys) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                String res = HttpUtils.request(url, reqTime, method);
                if (res == null) {
                    return;
                }
                if ("json".equalsIgnoreCase(LingsHttpUtils.config.getString(String.format("%s.mode", module)))) {
                    res = JsonUtils.getValue(JsonUtils.parseStr(res), keys, 0);
                }
                LingsHttpUtils.getInstance().getHttpData().put(module, res);
                try {
                    Thread.sleep(refInterval);
                } catch (InterruptedException e) {
                    LingsHttpUtils.getInstance().getLogger().info("出现错误!模块请求间隔必须为正值!");
                    e.printStackTrace();
                }
                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(LingsHttpUtils.class), task);
            }
        };
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(LingsHttpUtils.class), task);
    }
}
