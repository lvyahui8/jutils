package org.claret.utils;

import junit.framework.TestCase;

/**
 *
 * Created by lvyahui on 2016/8/26.
 */
public class IOUtilsTest extends TestCase {

    public void testGetSerialNumber() throws Exception {
        System.out.println(IOUtils.getSerialNumber("C"));
    }

    public void testGetRuntimePath() throws Exception {
        System.out.println(IOUtils.getRuntimePath());
    }

    public void testGetClassPath() throws Exception {
        System.out.println(IOUtils.getClassPath());
    }

}