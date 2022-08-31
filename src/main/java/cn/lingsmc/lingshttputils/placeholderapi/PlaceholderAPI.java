package cn.lingsmc.lingshttputils.placeholderapi;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import cn.lingsmc.lingshttputils.requesters.HTTPRequester;
import cn.lingsmc.lingshttputils.utils.JsonUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class PlaceholderAPI extends PlaceholderExpansion {

    private final LingsHTTPUtils plugin;
    private final FileConfiguration config = LingsHTTPUtils.getInstance().getConfig();

    public PlaceholderAPI(LingsHTTPUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return LingsHTTPUtils.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lhu";
    }

    @Override
    public @NotNull String getVersion() {
        return LingsHTTPUtils.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        // 这是必要的，否则PAPI会在重载时卸载这个扩展
        return true;

    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String params) {
        Set<String> modules = config.getKeys(false);
        for (String module : modules) {
            if (Objects.equals(params, config.getString(String.format("%s.apiname", module)))) {
                if ("inTime".equals(config.getString(String.format("%s.reqMode", module)))) {
                    int reqTime = config.getInt(String.format("%s.reqTime", module));
                    String url = config.getString(String.format("%s.url", module));
                    String method;
                    try {
                        method = config.getString(String.format("%s.method", module));
                    } catch (Exception ignored) {
                        method = "GET";
                    }
                    String res = HTTPRequester.request(url, reqTime, method);
                    if (res == null) {
                        return "";
                    }
                    if ("json".equals(config.getString(String.format("%s.mode", module)))) {
                        String[] keys = config.getString(String.format("%s.key", module)).split("\\.");
                        res = JsonUtils.getValue(JsonUtils.parseStr(res), keys, 0);
                    }
                    return res;
                } else {
                    // Cycle
                    return LingsHTTPUtils.getInstance().getHttpData().get(module);
                }
            }
        }
        // PAPI对扩展来说是未知的
        return null;
    }
}
