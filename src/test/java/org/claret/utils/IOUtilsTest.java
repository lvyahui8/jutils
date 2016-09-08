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

    public void testCopy() throws Exception {
        IOUtils.copy("E:/www/ruochen/trunk/uploads/pdf/a-lvyahui-resume-aboutMeV8-4.pdf","F:/tmp/resume.pdf",false);
        IOUtils.copy("E:/www/ruochen/trunk/uploads/pdf/a-lvyahui-resume-aboutMeV8-4.pdf","F:/tmp/resume.pdf");
        IOUtils.copy("C:/soft/QQSnapShot.exe","F:/tmp",false);
        IOUtils.copy("C:/soft/QQSnapShot.exe","F:/tmp");
        IOUtils.copy("C:/soft/apache-maven-3.3.9/bin","F:/tmp/maven",false);
        IOUtils.copy("C:/soft/apache-maven-3.3.9/bin","F:/tmp/maven");
    }
}