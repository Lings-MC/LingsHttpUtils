package cn.lingsmc.lingshttputils.placeholderapi;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import cn.lingsmc.lingshttputils.utils.HttpUtils;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class PlaceholderAPI extends PlaceholderExpansion {

    private final LingsHttpUtils plugin;
    private final FileConfiguration config = LingsHttpUtils.getInstance().getConfig();

    public PlaceholderAPI(LingsHttpUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lhu";
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        // 这是必要的，否则PAPI会在重载时卸载这个扩展
        return true;

    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String params) {
        Set<String> modules = config.getKeys(false);
        modules.removeIf(module -> Objects.equals(config.getBoolean(String.format("%s.enabled", module)), false));
        if (modules.isEmpty()) {
            return null;
        }
        for (String module : modules) {
            if (Objects.equals(params, config.getString(String.format("%s.apiname", module)))) {
                if ("inTime".equalsIgnoreCase(config.getString(String.format("%s.reqMode", module)))) {
                    // inTime
                    int reqTime = config.getInt(String.format("%s.reqTime", module));
                    String url = config.getString(String.format("%s.url", module));
                    String method;
                    try {
                        method = config.getString(String.format("%s.method", module));
                    } catch (Exception ignored) {
                        method = "GET";
                    }
                    String res = HttpUtils.request(url, reqTime, method);
                    if (res == null) {
                        return "";
                    }
                    if ("json".equalsIgnoreCase(config.getString(String.format("%s.mode", module)))) {
                        String[] keys = config.getString(String.format("%s.key", module)).split("\\.");
                        res = JsonUtils.getValue(res, keys);
                        if (Objects.isNull(res)) {
                            plugin.getLogger().log(Level.WARNING, "Worker: {0} 获取Json内容时出现错误! 请检查是否符合格式要求!", module);
                        }
                    }
                    return res;
                } else {
                    // Cycle
                    return this.plugin.getHttpData().get(module);
                }
            }
        }
        // PAPI对扩展来说是未知的
        return null;
    }
}
