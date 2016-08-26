package org.lyh.utils;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by lvyahui on 2016/8/26.
 */
public class ArchiveUtilsTest extends TestCase {

    public void testZipData() throws Exception {
        List<String> srcFileList = new ArrayList<String>();
        srcFileList.add("D:\\test\\lowData");
        srcFileList.add("D:\\test\\stackData");
        ArchiveUtils.zipData("D:\\test\\zip123456.zip", srcFileList);
    }

    public void testUnZipFiles() throws Exception {
        ArchiveUtils.unZipFiles("D:\\test\\zip123456.zip","D:\\test\\1\\");
    }

}