package org.claret.utils;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Created by lvyahui on 2016/8/25.
 */
public class NetUtilsTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {

    }

    public void testDownload() throws Exception {
        NetUtils.download("http://movesun.com/images/svg/jutils.svg","F:/jutils.svg");
    }

    public void testMultiThreadDownload() throws Exception {
        NetUtils.download(
                "http://movesun.qq.com/audios/ybxyq.mp3"
                ,"F:/ybxyq.mp3",3);
    }

    public void testGet() throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",1);
        params.put("created_at","2016-08-26");
        String json = NetUtils.get("http://movesun.com/demo/json",params);
        System.out.println(json);

        json = NetUtils.get("http://movesun.com/demo/json");
        System.out.println(json);

        int n = 10;
        ExecutorService threadPool = Executors.newFixedThreadPool(n);
        final CyclicBarrier cb = new CyclicBarrier(n);
        params.clear();
        params.put("p_id",1);
        params.put("qua","6.5.0.390");
        params.put("uuid","8cadcf2b-a295-4171-95cb-b3348d34ba84");

        for (int i = 0 ;i < n * 10; i++){
            final int taskId = i;
            threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        cb.await();
                        String resp = NetUtils.get("http://10.151.140.224:8080/apiserver/mapping/check",params);
                        System.out.println(Thread.currentThread().getName() + " run taskId:" + taskId + " resp:" +resp);
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

    public void testPost() throws Exception {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",1);
        params.put("status","success");
        System.out.println(NetUtils.post("http://movesun.com/json/push",params));
    }

    public void testBuildParams() throws Exception {
        System.out.println(System.currentTimeMillis());
        // 1474532346948
        // 1474530587563
        StringBuilder bodyBuild = new StringBuilder("lvyahui");
        bodyBuild.deleteCharAt(bodyBuild.length() - 1);
        System.out.println(bodyBuild.toString());
    }
}