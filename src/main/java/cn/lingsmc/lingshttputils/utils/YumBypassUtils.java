package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pw.yumc.Yum.events.PluginNetworkEvent;

import java.util.Objects;

/**
 * @author Crsuh2er0
 * &#064;date 2022/9/10
 * @apiNote
 */
public class YumBypassUtils implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void yumBypass(@NotNull PluginNetworkEvent e){
        if(Objects.equals(e.getPlugin(), JavaPlugin.getPlugin(LingsHTTPUtils.class))){
            LingsHTTPUtils.getInstance().getLogger().info("检测到Yum网络监控事件，已取消");
            e.setCancelled(true);
        }
    }
}
