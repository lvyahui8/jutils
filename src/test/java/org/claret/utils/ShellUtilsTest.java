package org.claret.utils;

import junit.framework.TestCase;

/**
 *
 * Created by lvyahui on 2016/8/26.
 */
public class ShellUtilsTest extends TestCase {

    public void testGetOsType() throws Exception {
        System.out.println(ShellUtils.getOsType());
    }

    public void testExecCommand() throws Exception {
        System.out.println(ShellUtils.execCommand("dir","/a"));
    }

    public void testExecCommand1() throws Exception {
        System.out.println(ShellUtils.execCommand("ls -lh"));
    }
}