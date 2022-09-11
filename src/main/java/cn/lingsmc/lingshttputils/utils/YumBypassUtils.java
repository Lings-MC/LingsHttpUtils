package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import pw.yumc.Yum.events.PluginNetworkEvent;

import java.util.Objects;

/**
 * @author Crsuh2er0
 * &#064;date 2022/9/10
 * @apiNote
 */
public class YumBypassUtils implements Listener {

    @EventHandler
    public void yumBypass(@NotNull PluginNetworkEvent e){
        if(Objects.equals(e.getPlugin(),LingsHTTPUtils.getInstance())){
            e.setCancelled(true);
        }
    }
}
