package cn.lingsmc.lingshttputils.utils;

import cn.lingsmc.lingshttputils.LingsHttpUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class HttpUtils {
    private HttpUtils() {
    }

    static LingsHttpUtils plugin = LingsHttpUtils.getInstance();
    public static @Nullable String httpRequest(String httpUrl, int reqTime, String method) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder res = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setReadTimeout(reqTime);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                if (is != null) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String tmp;
                    while ((tmp = br.readLine()) != null) {
                        res.append(tmp);
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE,"请求失败!");
            plugin.getLogger().log(Level.SEVERE,e.getMessage());
            // plugin.getLogger().log(Level.SEVERE,e.getCause().getMessage());
            plugin.getLogger().log(Level.SEVERE,e.toString());
            plugin.getLogger().log(Level.SEVERE,e.getCause().toString());
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Objects.requireNonNull(connection).disconnect();
        }
        return res.toString();
    }
}