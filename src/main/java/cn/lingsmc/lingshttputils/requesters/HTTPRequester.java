package cn.lingsmc.lingshttputils.requesters;

import cn.lingsmc.lingshttputils.LingsHTTPUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/30
 * @apiNote
 */
public class HTTPRequester {
    private HTTPRequester() {
    }

    public static @Nullable String request(String httpUrl, int reqTime, String method) {
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
            LingsHTTPUtils.getInstance().getLogger().info("请求失败!");
            e.printStackTrace();
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