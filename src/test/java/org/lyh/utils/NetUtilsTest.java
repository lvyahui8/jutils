package org.lyh.utils;

import junit.framework.TestCase;

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
        System.out.println(NetUtils.getJson("http://movesun.qq.com/test/json",null));
    }
}