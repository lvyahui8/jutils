package org.claret.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

/**
 * IO工具，提供批量建目录，删除文件，递归删除目录等等操作
 * Created by lvyahui on 2016/8/26.
 */
@SuppressWarnings("unused")
public class IOUtils extends Utils {
    public static final String SYS_FILE_SP = System.getProperty("file.separator");


    /**
     * 获取运行时目录
     *
     * @return 目录地址
     */
    public static String getRuntimePath() {
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
     *
     * @return classPath
     */
    public static String getClassPath() {
        ClassLoader loader = IOUtils.class.getClassLoader();
        String cPath = null;
        if (loader != null) {
            URL uri = loader.getResource("");
            if (uri != null) {
                cPath = uri.getPath();
            }
        }
        return cPath;
    }

    /**
     * 获取指定磁盘序列号
     *
     * @param drive 磁盘
     * @return 序列号
     */
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber";  // see note
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    /*
    * 复制，几种场景
    * cp file file
    * cp file dir
    * cp dir dir
    * */

    public static boolean copy(String source, String destination) throws IOException {
        return copy(source, destination, true);
    }

    /**
     * @param source      源文件
     * @param destination 目标文件
     * @param override    是否覆盖
     * @return 复制是否成功
     * @throws IOException
     */
    public static boolean copy(String source, String destination, boolean override) throws IOException {
        return copy(new File(source), new File(destination), override);
    }

    public static boolean copy(File sourceFile, File destFile) throws IOException {
        return copy(sourceFile, destFile, true);
    }

    public static boolean copy(File sourceFile, File destFile, boolean override) throws IOException {
        if (!sourceFile.exists() || !sourceFile.canRead()) {
            throw new FileNotFoundException(sourceFile.getName());
        }
        // 处理source是文件的情况
        if (sourceFile.isFile()) {
            if (destFile.exists()) {
                if (!destFile.canWrite()) {
                    throw new IOException("No write permissions");
                }
                // 目标可能是个已经存在的目录
                if (destFile.isDirectory()) {
                    // 看这个目录下有没有这个文件存在
                    File existFile = new File(destFile.getAbsolutePath() + "/" + sourceFile.getName());
                    if (existFile.exists()) {
                        if(!override){
                            throw new FileAlreadyExistsException(destFile.getName());
                        }
                    }else{
                        if(!existFile.createNewFile()){
                            throw new IOException("No write permissions");
                        }
                    }
                    destFile = existFile;
                }else{
                    // 目标文件是一个已经存在的文件
                    if (!override) {
                        throw new FileAlreadyExistsException(destFile.getName());
                    }
                }
            } else {
                /*
                 *  目标必定不存在，并且目标一定是文件，但目录可能存在
                 *  比如cp /etc/profile ~/bak/profile
                 *  如果~/bak/profile不存在，可能~/bak/profile存在
                 */
                String filePath = destFile.getParentFile().getAbsolutePath();
                File dirFile = new File(filePath);
                if ((!dirFile.exists() && !dirFile.mkdirs()) || !destFile.createNewFile()) {
                    throw new IOException("No write permissions");
                }
            }
            // 进行文件拷贝
            copyFile(sourceFile,destFile,override);
        } else {
            /*
             * 源文件是一个目录，那么目标文件必定也是一个目录
             */
            if(destFile.exists()){
                if(!destFile.isDirectory()){
                    throw new IOException(destFile.getName() + " not a directory");
                }
            }else{
                if(!destFile.mkdirs()){
                    throw new IOException("No write permissions");
                }
            }

            File files [] = sourceFile.listFiles();
            for (File item : files){
                if(item.isDirectory()
                        && (".".equals(item.getName()) || "..".equals(item.getName()))){
                    continue;
                }
                File toFile = new File(destFile,item.getName());
                // 递归调用本方法
                copy(item,toFile,override);
            }
        }
        return true;
    }

    protected static boolean copyFile(File sourceFile, File destFile, boolean override) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
        byte[] buffer = new byte[4 * 1024 * 1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return true;
    }

    protected static boolean move(File sourceFile,File destFile , boolean override) {
        return !(destFile.exists() && !override) && sourceFile.renameTo(destFile);
    }
}
