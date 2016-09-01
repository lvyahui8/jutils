package org.claret.utils;

import java.io.*;
import java.net.URL;

/**
 * IO工具，提供批量建目录，删除文件，递归删除目录等等操作
 * Created by lvyahui on 2016/8/26.
 */
public class IOUtils extends Utils {
    public static final String SYS_FILE_SP = System.getProperty("file.separator");


    /**
     * 获取运行时目录
     * @return 目录地址
     */
    public static String getRuntimePath(){
        File directory = new File("");// 参数为空
        String courseFile;
        try {
            courseFile = directory.getCanonicalPath();
            return courseFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取运行时classPath
     * @return classPath
     */
    public static String getClassPath(){
        ClassLoader loader = IOUtils.class.getClassLoader();
        String cPath = null;
        if(loader != null) {
            URL uri = loader.getResource("");
            if(uri != null){
                cPath = uri.getPath();
            }
        }
        return cPath;
    }
    /**
     * 获取指定磁盘序列号
     * @param drive 磁盘
     * @return 序列号
     */
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    +"Set colDrives = objFSO.Drives\n"
                    +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    +"Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
    }

}
