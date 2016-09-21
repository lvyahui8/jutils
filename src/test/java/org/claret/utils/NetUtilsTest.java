package org.claret.utils;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

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

    public void testGetJson() throws Exception {
        System.out.println(NetUtils.get("http://movesun.qq.com/test/json",null));
    }

    public void testDownload() throws Exception {
        NetUtils.download("http://movesun.com/images/svg/jutils.svg","F:/jutils.svg");
    }

    public void testMultiThreadDownload() throws Exception {
        NetUtils.multiThreadDownload(
                "http://movesun.qq.com/audios/ybxyq.mp3"
                ,"F:/ybxyq.mp3",3);
    }

    public void testGet() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",1);
        params.put("created_at","2016-08-26");
        String json = NetUtils.get("http://movesun.com/demo/json",params);
        System.out.println(json);
    }

    public void testGet1() throws Exception {
        String json = NetUtils.get("http://movesun.com/demo/json");
        System.out.println(json);
    }

    public void testPost() throws Exception {
        Map<String,Object> params = new HashMap<String, Object>();

        System.out.println(NetUtils.post("http://movesun.com/demo/json",params));
    }

    public void testBuildParams() throws Exception {

    }
}