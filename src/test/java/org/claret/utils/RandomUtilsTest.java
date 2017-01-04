package org.claret.utils;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2016/9/30 16:13
 */
public class RandomUtilsTest extends TestCase {

    public void testHexColor() throws Exception {
        System.out.println(RandomUtils.hexColor());
    }

    public void testRandomString() throws Exception {
        System.out.println(RandomUtils.randomString());
    }

    public void testCreateGuid() throws Exception {
        System.out.println(RandomUtils.createGuid());
    }

    public void testCreateGuidStr() throws Exception {
        Set<String> appids = new HashSet<String>();
        for (int i = 0 ; i < 100000 ; i ++ ){
            String appid = RandomUtils.createGuidStr(16);
            if( ! appids.contains(appid)){
                appids.add(appid);
                System.out.println(appid);
            }else{
                System.err.println(appid);
            }
        }
    }
}