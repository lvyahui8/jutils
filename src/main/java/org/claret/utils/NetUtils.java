package org.claret.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.claret.component.MultiThreadDownLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络工具
 *
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
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
     * @param params 请求参数
     * @return 响应
     */
    public static String get(String url, Map<String, Object> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(url);
        if(params != null){
            for (Map.Entry<String,Object> param : params.entrySet()){
                requestBuilder = requestBuilder.addParameter(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        HttpUriRequest get = requestBuilder.build();
        try {
            CloseableHttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200){
                HttpEntity httpEntity = response.getEntity();
                InputStream inputStream = httpEntity.getContent();
                StringBuilder sb = new StringBuilder();
                byte [] buffer = new byte[4096];
                int len;
                while((len = inputStream.read(buffer)) > 0){
                    sb.append(new String(buffer,0,len));
                }
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送get请求获取文本响应，适合请求json
     * @param url 请求地址
     * @return 响应
     */
    public static String get(String url){
        return get(url,null);
    }


    public static void main(String[] args) {
        int n = 1000;
        ExecutorService threadPool = Executors.newFixedThreadPool(n);
        final CyclicBarrier cb = new CyclicBarrier(n);
        for (int i = 0 ;i < n * 10; i++){
            final int taskId = i;
            threadPool.execute(new Runnable() {
                public void run() {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("p_id",1);
                    params.put("qua","6.5.0.390");
                    params.put("uuid","8cadcf2b-a295-4171-95cb-b3348d34ba84");
                    try {
                        cb.await();
                        String resp = NetUtils.get("http://movesun.com/apiserver/mapping/check",params);
//                        System.out.println(Thread.currentThread().getName() + " run taskId:" + taskId + " resp:" +resp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
