package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    static LingsHttpUtils plugin = LingsHttpUtils.getInstance();

    private JsonUtils() {
    }

    public static String getValue(String jsonString, String @NotNull [] keys) {
        if (plugin.gson) {
            return getValueGson(jsonString, keys);
        } else {
            return getValueJsonSimple(jsonString, keys);
        }
    }

    /**
     * 使用json-simple(1.13-)
     */
    public static @Nullable JSONObject parseStrJsonsimple(String jsonString) {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用json-simple(1.13-)
     */
    public static @Nullable String getValueJsonSimple(String jsonString, String @NotNull [] keys) {
        JSONObject json = parseStrJsonsimple(jsonString);
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

    /**
     * 使用Gson(1.14+)
     */
    public static JsonObject parseStrGson(String str) {
        return new JsonParser().parse(str).getAsJsonObject();
    }

    /**
     * 使用Gson(1.14+)
     */
    public static String getValueGson(String str, String @NotNull [] keys) {
        JsonObject value = parseStrGson(str);
        for (int i = 0; i < keys.length - 1; i++) {
            value = value.get(keys[i]).getAsJsonObject();
        }
        return value.get(keys[keys.length - 1]).getAsString();
    }
}
