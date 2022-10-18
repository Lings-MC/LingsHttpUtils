package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Objects;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class JsonUtils {
    static Plugin plugin = LingsHttpUtils.getInstance();

    private JsonUtils() {
    }

    public static @Nullable JSONObject parseStr(String jsonString) {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable String getValue(String jsonString, String @NotNull [] keys) {
        JSONObject json = parseStr(jsonString);
        if (Objects.isNull(json)) {
            return null;
        }
        Object value = json.get(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            if (value instanceof JSONObject) {
                value = ((JSONObject) value).get(keys[i]);
            }
        }
        return value.toString();
    }
}
