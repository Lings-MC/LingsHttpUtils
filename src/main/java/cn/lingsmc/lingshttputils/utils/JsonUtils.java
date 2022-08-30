package cn.lingsmc.lingshttputils.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class JsonUtils {
    private JsonUtils() {
    }

    public static JsonObject parseStr(String str) {
        return new JsonParser().parse(str).getAsJsonObject();
    }

    /**
     * 递归深度读取json值
     */
    @Contract(pure = true)
    public static String getValue(JsonObject json, String @NotNull [] keys, int depth) {
        if (depth == keys.length - 1) {
            return json.get(keys[keys.length - 1]).getAsString();
        }
        json = json.get(keys[depth++]).getAsJsonObject();
        return getValue(json, keys, depth);
    }
}
