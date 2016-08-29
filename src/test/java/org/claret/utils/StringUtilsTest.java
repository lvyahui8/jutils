package org.claret.utils;

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

    public void testHumpToSnake() throws Exception {
        System.out.println(StringUtils.humpToSnake("StringUtilsTest","-"));
    }

    public void testSnakeToHump() throws Exception {
        System.out.println(StringUtils.snakeToHump("string-utils-test","-"));
    }


    public void testUcwords() throws Exception {
        System.out.println(StringUtils.ucwords("string-utils-test","-"));
        System.out.println(StringUtils.ucwords("i will come back for you!"));
    }

    public void testJoin() throws Exception {
        System.out.println(StringUtils.join(new String[] {"i","will","come","back","for","you!"},'*'));
        System.out.println(StringUtils.join(new Integer[] {1,2,3,4,5}));
    }
}