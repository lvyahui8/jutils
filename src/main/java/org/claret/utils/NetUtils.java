package org.claret.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.claret.component.MultiThreadDownLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 网络工具
 * Net 也是IO，到底要不要合到IOUtils中，以后再看吧
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 */
public class NetUtils extends Utils {

    public static final String  DEFAULT_ENCODE  =   "UTF-8";
    public static final int     BUFFER_SIZE     =   4096;

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
            // 保证连接全部关闭
            closeStream(response);
            closeStream(httpClient);
        }
        return result;
    }

    /**
     * 多线程下载文件
     * @param url 文件地址
     * @param saveFile 保存的全文件名
     * @return 下载成功或失败
     */
    public static boolean multiThreadDownload(String url, String saveFile,int count){
        MultiThreadDownLoader downLoader = new MultiThreadDownLoader(url);
        downLoader.setFileFullName(saveFile);
        downLoader.setThreadCount(count);
        downLoader.start();
        return false;
    }

    private static String buildHttpUrl(String url) {
        return "http://" + url;
    }

    private static boolean isHttpUrl(String url) {
        return url.startsWith("http://");
    }

    /**
     * 发送get请求获取文本响应，适合请求json
     * @param url 请求地址
     * @return 响应
     */
    public static String get(String url, Map<String, Object> params) {
        return get(url,params,false);
    }

    private static String get(String url, Map<String, Object> params, boolean encode) {
        return get(url,params,encode,DEFAULT_ENCODE);
    }

    public static String get(String url, Map<String, Object> params,boolean encode,String charset){
        if(params != null){
            url = url.concat("?").concat(buildParams(params));
        }
        if(encode){
            try {
                url = URLEncoder.encode(url,charset);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return get(url);
    }

    /**
     * 发送get请求获取文本响应，适合请求json
     * @param url 请求地址
     * @return 响应
     */
    public static String get(String url) {
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.connect();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                return readContent(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                conn.disconnect();
            }
            closeStream(inputStream);
        }
        return null;
    }

    public static String post(String url, Map<String,Object> params){
        return post(url,params,false);
    }

    public static String post(String url, Map<String,Object> params,boolean encode){
        return post(url,params,encode,DEFAULT_ENCODE);
    }

    public static String post(String url, Map<String,Object> params,boolean encode,String charset){
        String buildParams = buildParams(params);
        if(encode){
            try {
                buildParams = URLEncoder.encode(buildParams,charset);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return post(url,buildParams);
    }

    public static String post(String url, String buildParams){
        PrintWriter out = null;
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(buildParams);
            out.flush();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                return readContent(inputStream);
            }
        }catch (IOException e) {
            return null;
        } finally {
            closeStream(out);
            closeStream(inputStream);
            if (conn != null){
                conn.disconnect();
            }
        }
        return null;
    }

    public static String buildParams(Map<String,Object> params){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if(params != null){
            for (Map.Entry<String,Object> param : params.entrySet()){
                if(i > 0 ){
                    sb.append('&');
                }
                sb.append(param.getKey()).append('=').append(param.getValue());
                i++;
            }
        }
        return sb.toString();
    }

    private static String readContent(InputStream inputStream) throws IOException{
        StringBuilder sb = new StringBuilder();
        byte [] buffer = new byte[BUFFER_SIZE];
        int len;
        while((len = inputStream.read(buffer)) > 0){
            sb.append(new String(buffer,0,len));
        }
        return sb.toString();
    }



}
