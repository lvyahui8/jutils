package org.claret.utils;

import junit.framework.TestCase;

import java.io.File;
import java.util.Properties;

/**
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

    public void testGetUserHome() throws Exception {
        System.out.println(IOUtils.getUserHome());
    }

    public void testCopy() throws Exception {
        // IOUtils.copy("E:/www/ruochen/trunk/uploads/pdf/a-lvyahui-resume-aboutMeV8-4.pdf","F:/tmp/resume.pdf",false);
        // IOUtils.copy("E:/www/ruochen/trunk/uploads/pdf/a-lvyahui-resume-aboutMeV8-4.pdf","F:/tmp/resume.pdf");
        // IOUtils.copy("C:/soft/QQSnapShot.exe","F:/tmp",false);
        // IOUtils.copy("C:/soft/QQSnapShot.exe","F:/tmp");
        // IOUtils.copy("C:/soft/apache-maven-3.3.9/bin","F:/tmp/maven",false);
        // IOUtils.copy("C:/soft/apache-maven-3.3.9/bin","F:/tmp/maven");

        // 文件复制到目标文件
        // IOUtils.copy("C:/Windows/System32/drivers/etc/hosts","C:/tmp/hosts");

        // 文件复制到目标目录
        // IOUtils.copy("C:/Windows/System32/drivers/etc/hosts","C:/tmp");

        // 目录复制到一个不存在的目录
        // IOUtils.copy("C:/Windows/System32/drivers/UMDF","C:/tmp/UMDF");

    }

    public void testRemovePath() throws Exception {
//        IOUtils.removePath("D:\\tmp");
        IOUtils.removePath("D:\\tmp\\data");
    }

    public void testGetFileAsStream() throws Exception {

    }


    public void testExists() throws Exception {

    }

    public void testMove() throws Exception {
//        IOUtils.move("F:\\images\\d6fa572f81f0f91cfc51e0966d9344c1-.jpg","F:/tmp/images/outFile.jpg",true);
//        IOUtils.move("F:\\images\\web\\ignasi_pattern_s","F:\\images\\web\\ignasi_pattern_s_2");
        boolean success = IOUtils.move(new File("F:\\images\\web\\ignasi_pattern_s"),new File("F:\\images\\web\\ignasi_pattern_s_2"),true);
        System.out.println(success);
    }


    public void testGetTmpPath() throws Exception {
        Properties properties = System.getProperties();
        for(String prop : properties.stringPropertyNames()){
            System.out.println(prop + "\t\t" + System.getProperty(prop));
        }
    }
}