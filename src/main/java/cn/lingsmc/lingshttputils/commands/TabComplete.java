package cn.lingsmc.lingshttputils.commands;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class TabComplete implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {

        final String[] root = new String[]{"workers", "help", "reload","request"};
        final String[] worker = new String[]{"start", "stop"};

        if (args.length == 1) {
            List<String> res = new ArrayList<>();
            for (String val : root) {
                if (val.contains(args[0])) {
                    res.add(val);
                }
            }
            return res;
        }

        if (args.length == 2) {
            if(root[0].equalsIgnoreCase(args[0])){
                List<String> res = new ArrayList<>();
                for (String val : worker) {
                    if (val.contains(args[1])) {
                        res.add(val);
                    }
                }
                return res;
            }
            if(root[3].equalsIgnoreCase(args[0])){
                List<String> res = new ArrayList<>();
                FileConfiguration config = LingsHttpUtils.config;
                Set<String> modules = config.getKeys(false);
                modules.remove("version");
                for (String module : modules) {
                    if (module.contains(args[1])) {
                        res.add(module);
                    }
                }
                return res;
            }
        }
        return Collections.emptyList();
    }
}
