package cn.lingsmc.lingshttputils.asyncworkers;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import cn.lingsmc.lingshttputils.utils.HttpUtils;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.logging.Level;


/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class AsyncWorkers {
    static Plugin plugin = LingsHttpUtils.getInstance();

    public void runAsyncWorker(String module, int reqTime, String url, String method, int refInterval, String[] keys) {
        if (refInterval < 0) {
            plugin.getLogger().log(Level.SEVERE, "Worker: {0} 出现错误!模块请求间隔必须为正值!", module);
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                // plugin.getLogger().info(String.format("%s进行了一次请求!",module));
                String res = HttpUtils.httpRequest(url, reqTime, method);
                if (Objects.isNull(res)) {
                    return;
                }
                if ("json".equalsIgnoreCase(LingsHttpUtils.config.getString(String.format("%s.mode", module)))) {
                    res = JsonUtils.getValue(res, keys);
                    if (Objects.isNull(res)) {
                        plugin.getLogger().log(Level.SEVERE, "Worker: {0} 获取Json内容时出现错误! 请检查是否符合格式要求!", module);
                    }
                }
                try {
                    LingsHttpUtils.getInstance().getHttpData().put(module, res);
                } catch (Exception ignored) {
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, (long) refInterval / 1000 * 20);
    }
}
