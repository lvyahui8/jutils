package org.claret.utils;

import junit.framework.TestCase;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2017/5/3 13:37
 */
public class DateUtilsTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testGetDayList() throws Exception {
        System.out.println(DateUtils.getDayList("2017-04-29","2017-05-02"));
    }
}