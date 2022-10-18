package cn.lingsmc.lingshttputils.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class JsonUtils {
    private JsonUtils() {
    }

    public static @Nullable String getValue(@NotNull JSONObject jsonObject, String key){
        try{
            return jsonObject.get(key).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}