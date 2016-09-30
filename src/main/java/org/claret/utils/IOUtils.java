package org.claret.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

/**
 * IO工具，提供批量建目录，删除文件，递归删除目录等等操作
 *
 * File.renameTo 方法自不同平台常常会失败，比如移动之后文件还在，目标文件也生成了，但大小为0。
 * 所以添加先通过复制再删除的方式删除文件的方法
 * Created by lvyahui on 2016/8/26.
 */
@SuppressWarnings("unused")
public class IOUtils extends Utils {
    public static final String SYS_FILE_SP = System.getProperty("file.separator");
    public static final int COPY_BUFFER_SIZE = 4 * 1024 * 1024;

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

    public static String getUserHome(){
        return System.getProperty("user.home");
    }

    public static String getTmpPath(){
        return System.getProperty("java.io.tmpdir");
    }

    public static InputStream getFileAsStream(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        InputStream stream;
        if(file.exists()){
            stream = new FileInputStream(fileName);
        }else{
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        }
        return stream;
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

    /**
     *  复制文件需要考虑这三种场景
     *  file    ->  file
     *  file    ->  dir
     *  dir     ->  dir
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖
     * @return 复制是否成功
     */
    private static boolean copyWithMove(File sourceFile, File destFile, boolean override, boolean move) throws IOException {
        if (!sourceFile.exists() || !sourceFile.canRead()) {
            throw new FileNotFoundException(sourceFile.getName());
        }
        boolean success = true;
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
                 *  如果~/bak/profile不存在，可能~/bak存在
                 */
                String filePath = destFile.getParentFile().getAbsolutePath();
                File dirFile = new File(filePath);
                if ((!dirFile.exists() && !dirFile.mkdirs()) || !destFile.createNewFile()) {
                    throw new IOException("No write permissions");
                }
            }
            // 进行文件拷贝
            success = copyFile(sourceFile,destFile,override);
            if(move && !sourceFile.delete()){
                // 先复制再删除
                throw new IOException("can't remove file " + sourceFile.getAbsolutePath());
            }
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
            if(files  != null){
                for (File item : files){
                    if(item.isDirectory()
                            && (".".equals(item.getName()) || "..".equals(item.getName()))){
                        continue;
                    }
                    File toFile = new File(destFile,item.getName());
                    // 递归调用本方法
                    if(!copyWithMove(item,toFile,override,move)){
                        success = false;
                    }
                }
            }
        }
        return success;
    }

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
        return copyWithMove(sourceFile,destFile,override,false);
    }

    private static boolean copyFile(File sourceFile, File destFile, boolean override) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return true;
    }

    public static boolean exists(String fileName){
        File file = new File(fileName);
        return file.exists();
    }

    public static boolean removePath(String pathName){
        File path = new File(pathName);
        boolean success = true;
        if(path.isDirectory()){
            File files [] = path.listFiles();
            if(files != null){
                for (File item : files){
                    boolean removed;
                    if(item.isDirectory()){
                        if((".".equals(item.getName()) || "..".equals(item.getName()))){
                            continue;
                        }
                        // 递归调用本方法
                        removed = removePath(item.getAbsolutePath());
                    }else{
                        removed = item.delete();
                    }
                    if(!removed){
                        success = false;
                    }
                }
            }
        }
        return success && path.delete();
    }

    public static boolean move(String sourceFileName,String destFileName) {
        return move(sourceFileName,destFileName,true);
    }

    public static boolean move(String sourceFileName,String destFileName,boolean override){
        return move(new File(sourceFileName),new File(destFileName),override);
    }

    public static boolean move(File sourceFile,File destFile){
        return move(sourceFile,destFile,true);
    }

    /**
     * 移动文件
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖
     * @return 移动是否成功
     */
    public static boolean move(File sourceFile,File destFile , boolean override){
        return !(destFile.exists() && !override) && sourceFile.renameTo(destFile);
    }

    /**
     *
     * @param sourceFileName 源文件名
     * @param destFileName 目标文件名
     * @return 移动是否成功
     * @throws IOException
     */
    public static boolean moveByCopy(String sourceFileName,String destFileName) throws IOException{
        return moveByCopy(sourceFileName,destFileName,true);
    }

    public static boolean moveByCopy(String sourceFileName,String destFileName,boolean override) throws IOException{
        return moveByCopy(new File(sourceFileName),new File(destFileName),override);
    }

    public static boolean moveByCopy(File sourceFile,File destFile) throws IOException{
        return moveByCopy(sourceFile,destFile,true);
    }

    /**
     * 移动文件
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖
     * @return 移动是否成功
     */
    public static boolean moveByCopy(File sourceFile,File destFile , boolean override) throws IOException{
        return copyWithMove(sourceFile,destFile,override,true);
    }

    /**
     * 读取文件多行内容
     * @param file  文件
     * @param offset 行数偏移，注意空行业算在内
     * @param length 行数
     * @return 非空字符串list，空行会被跳过不被添加到list
     */
    public static List<String> readLines(File file,int offset,int length){
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int lintPoint = 0;
            while ((line = reader.readLine()) != null){
                if(++lintPoint < offset){
                    continue;
                }
                if(length != -1 && lintPoint - offset > length){
                    break;
                }
                if(line.trim().equals("")){
                    list.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        return list;
    }
}
