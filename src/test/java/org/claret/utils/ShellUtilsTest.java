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
        /*
         * windows
         */
        // System.out.println(ShellUtils.execCommand("fsutil","fsinfo","drives"));
        System.out.println(ShellUtils.execCommand("telnet","localhost","80"));

        /*
         * linux
         */
        // System.out.println(ShellUtils.execCommand("head","/var/log/messages","-n", "1"));
    }

    public void testExecCommand1() throws Exception {
        System.out.println(ShellUtils.execCommand("ls -lh"));
    }

    public void testExecCommandNotWait() throws Exception {
        // ShellUtils.execCommandNotWait("telnet","localhost","80");
        // ShellUtils.execCommandNotWait("echo","localhost",">","F:/tmp");
        ShellUtils.execCommandNotWait("ping","localhost","-n","20",">>","F:/tmp");
        ShellUtils.execCommandNotWait("echo","localhost",">>","F:/tmp");
        System.out.println("不等待");
    }
}