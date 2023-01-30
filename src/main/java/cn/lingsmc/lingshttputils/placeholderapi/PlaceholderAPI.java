package cn.lingsmc.lingshttputils.placeholderapi;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

import static cn.lingsmc.lingshttputils.utils.RequestUtils.requestHttp;

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
        return plugin.getDescription().getVersion();
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
                    return requestHttp(module, config, plugin);
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
