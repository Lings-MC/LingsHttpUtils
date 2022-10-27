package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

/**
 * @author Crsuh2er0
 * @apiNote
 * @since 2022/10/27
 */
public class RequestUtils {
    private RequestUtils(){
    }

    public static @Nullable String request(String module, @NotNull FileConfiguration config, LingsHttpUtils plugin) {
        plugin.getLogger().info(String.format("%s进行了一次请求.",module));
        int reqTime = config.getInt(String.format("%s.reqTime", module));
        String url = config.getString(String.format("%s.url", module));
        String method;
        try {
            method = config.getString(String.format("%s.method", module));
        } catch (Exception ignored) {
            method = "GET";
        }
        String res = HttpUtils.httpRequest(url, reqTime, method);
        if (res == null) {
            return "";
        }
        if ("json".equalsIgnoreCase(config.getString(String.format("%s.mode", module)))) {
            String[] keys = config.getString(String.format("%s.key", module)).split("\\.");
            res = JsonUtils.getValue(res, keys);
            if (Objects.isNull(res)) {
                plugin.getLogger().log(Level.WARNING, "Worker: {0} 获取Json内容时出现错误! 请检查是否符合格式要求!", module);
                return null;
            }
        }
        return res;
    }
}
