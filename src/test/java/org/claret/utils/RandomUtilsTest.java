package org.claret.utils;

import junit.framework.TestCase;

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

}