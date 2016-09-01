package org.claret.utils;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;
import java.util.List;

/**
 * 解压缩工具，支持zip,tgz,bzip2,gzip,pack200,lzma,xz,jar,7z等
 * Created by lvyahui on 2016/8/26.
 */
public class ArchiveUtils extends Utils {
    private static final int BUFFER = 2048;

    public static void zipData(String tarFile, List<String> srcFileList) throws IOException {
        FileOutputStream dest = new FileOutputStream(tarFile);
        ZipArchiveOutputStream out = new ZipArchiveOutputStream(new BufferedOutputStream(dest));
        BufferedInputStream origin;

        byte data[] = new byte[BUFFER];

        for (String srcFile : srcFileList) {
            File myFile = new File(srcFile);
            if (!myFile.exists())
                continue;
            FileInputStream fi = new FileInputStream(myFile);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipArchiveEntry entry = new ZipArchiveEntry(myFile.getName());
            out.putArchiveEntry(entry);

            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
    }

    public static void unZipFiles(String zipFilePath, String descDir) throws IOException {
        unZipFiles(zipFilePath, descDir, false);
    }

    public static void unZipFiles(String zipFilePath, String descDir, boolean deleteZip) throws IOException {
        if (!(descDir.endsWith(IOUtils.SYS_FILE_SP) || descDir.endsWith("/"))) {
            descDir += IOUtils.SYS_FILE_SP;
        }
        File zipFile = new File(zipFilePath);
        File pathFile = new File(descDir);
        if (!pathFile.exists() && !pathFile.mkdirs()) {
            return ;
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = descDir + zipEntryName;
            //输出文件路径信息
            System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] data = new byte[BUFFER];
            int len;
            while ((len = in.read(data)) > 0) {
                out.write(data, 0, len);
            }
            in.close();
            out.close();
        }
        if (deleteZip) {
            zipFile.delete();
        }
    }

}
