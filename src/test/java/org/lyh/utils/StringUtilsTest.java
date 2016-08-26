package org.lyh.utils;

import junit.framework.TestCase;

/**
 *
 * Created by lvyahui on 2016/8/26.
 */
public class StringUtilsTest extends TestCase {

    public void testLcfirst() throws Exception {
        System.out.println(StringUtils.lcfirst("LvYahui"));
        for(int i = 0 ;i < 10000000; i++){
            StringUtils.lcfirst("LvYahui");
        }
    }

    public void testUcfirst() throws Exception {
        System.out.println(StringUtils.ucfirst("lvYahui"));
        for(int i = 0 ;i < 10000000; i++){
            StringUtils.ucfirst("lvYahui");
        }
    }

    public void testMd5() throws Exception {
        System.out.println(StringUtils.md5("lvyahui"));
    }

    public void testEncodeType() throws Exception {
        System.out.println(StringUtils.encodeType("中文","GBK")); // flase
    }
}