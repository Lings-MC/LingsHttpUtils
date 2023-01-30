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
    static LingsHttpUtils plugin = LingsHttpUtils.getInstance();

    private HttpUtils() {
    }

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
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            if (Objects.equals(connection.getResponseCode(), 200)) {
                is = connection.getInputStream();
                if (Objects.nonNull(is)) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String tmp;
                    while (Objects.nonNull((tmp = br.readLine()))) {
                        res.append(tmp);
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Http请求出错!");
            plugin.getLogger().log(Level.SEVERE, e.toString());
            return null;
        } finally {
            if (Objects.nonNull(br)) {
                try {
                    br.close();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "IO异常!");
                    plugin.getLogger().log(Level.SEVERE, e.toString());
                }
            }
            if (Objects.nonNull(is)) {
                try {
                    is.close();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "IO异常!");
                    plugin.getLogger().log(Level.SEVERE, e.toString());
                }
            }
            Objects.requireNonNull(connection).disconnect();
        }
        return res.toString();
    }
}