package org.lyh.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 网络工具
 * Created by lvyahui on 2016/8/25.
 */
public class NetUtils {
    /**
     * 下载文件
     * @param url 文件地址
     * @param saveFile 保存的全文件名
     * @return 下载成功或失败
     */
    public static boolean download(String url, String saveFile) {
        if (!isHttpUrl(url)) {
            url = buildHttpUrl(url);
        }
        boolean result = false;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                FileOutputStream fout = new FileOutputStream(new File(saveFile));
                entity.writeTo(fout);
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 多线程下载文件
     * @param url 文件地址
     * @param saveFile 保存的全文件名
     * @return 下载成功或失败
     */
    public static boolean multiThreadDownload(String url, String saveFile){
        return false;
    }

    private static String buildHttpUrl(String url) {
        return "http://" + url;
    }

    private static boolean isHttpUrl(String url) {
        return url.startsWith("http://");
    }

    public static String getJson(String url, Map<String, Object> params) {
        return null;
    }
}
