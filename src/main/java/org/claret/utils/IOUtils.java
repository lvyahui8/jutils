package org.claret.utils;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.security.MessageDigest;
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
public class IOUtils extends CommonUtils {
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

    /**
     * 获取用户目录
     *
     * @return 用户目录
     */
    public static String getUserHome(){
        return System.getProperty("user.home");
    }

    /**
     * 获取临时目录
     *
     * @return 临时数据目录
     */
    public static String getTmpPath(){
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取一个文件流
     *
     * @param fileName 文件名
     * @return 文件输入流
     * @throws FileNotFoundException
     */
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
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖已存在的目标文件
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

    /**
     * 复制文件，如果目标文件存在则覆盖
     *
     * @param sourceFileName 源文件名
     * @param destFileName 目标文件名
     * @return 是否全部复制成功
     * @throws IOException
     */
    public static boolean copy(String sourceFileName, String destFileName) throws IOException {
        return copy(sourceFileName, destFileName, true);
    }

    /**
     * 复制文件
     *
     * @param sourceFileName 源文件
     * @param destFileName 目标文件
     * @param override 是否覆盖已存在的目标文件
     * @return 是否全部复制成功
     * @throws IOException
     */
    public static boolean copy(String sourceFileName, String destFileName, boolean override) throws IOException {
        return copy(new File(sourceFileName), new File(destFileName), override);
    }

    /**
     * 复制文件，如果目标文件存在则覆盖
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @return 是否全部复制成功
     * @throws IOException
     */
    public static boolean copy(File sourceFile, File destFile) throws IOException {
        return copy(sourceFile, destFile, true);
    }

    /**
     * 复制文件
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖已存在的目标文件
     * @return 是否全部复制成功
     * @throws IOException
     */
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

    /**
     * 文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    public static boolean exists(String fileName){
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 是否全部删除成功
     */
    public static boolean removeFile(String fileName){
        File path = new File(fileName);
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
                        removed = removeFile(item.getAbsolutePath());
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

    /**
     * 移动文件，如果目标文件存在则覆盖
     *
     * @param sourceFileName 原文件名
     * @param destFileName 目标文件名
     * @return 是否全部移动成功
     */
    public static boolean move(String sourceFileName,String destFileName) {
        return move(sourceFileName,destFileName,true);
    }

    /**
     * 移动文件
     *
     * @param sourceFileName 源文件名
     * @param destFileName 目标文件名
     * @param override 是否覆盖已存在的目标文件
     * @return 是否全部移动成功
     */
    public static boolean move(String sourceFileName,String destFileName,boolean override){
        return move(new File(sourceFileName),new File(destFileName),override);
    }

    /**
     * 移动文件，如果目标文件存在则覆盖
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @return 是否全部移动成功
     */
    public static boolean move(File sourceFile,File destFile){
        return move(sourceFile,destFile,true);
    }

    /**
     * 移动文件
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖已存在的目标文件
     * @return 是否全部移动成功
     */
    public static boolean move(File sourceFile,File destFile , boolean override){
        return !(destFile.exists() && !override) && sourceFile.renameTo(destFile);
    }

    /**
     * 以先复制后删除的方式移动文件，如果目标文件存在则覆盖
     *
     * @param sourceFileName 源文件名
     * @param destFileName 目标文件名
     * @return 是否全部移动成功
     * @throws IOException
     */
    public static boolean moveByCopy(String sourceFileName,String destFileName) throws IOException{
        return moveByCopy(sourceFileName,destFileName,true);
    }

    /**
     * 以先复制后删除的方式移动文件
     *
     * @param sourceFileName 源文件名
     * @param destFileName 目标文件名
     * @param override 是否覆盖存在的目标文件
     * @return 是否全部移动成功
     * @throws IOException
     */
    public static boolean moveByCopy(String sourceFileName,String destFileName,boolean override) throws IOException{
        return moveByCopy(new File(sourceFileName),new File(destFileName),override);
    }

    /**
     * 以先复制后删除的方式移动文件，如果目标文件存在则覆盖
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @return 是否全部移动成功
     * @throws IOException
     */
    public static boolean moveByCopy(File sourceFile,File destFile) throws IOException{
        return moveByCopy(sourceFile,destFile,true);
    }

    /**
     * 以先复制后删除的方式移动文件
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @param override 是否覆盖存在的目标文件
     * @return 是否全部移动成功
     */
    public static boolean moveByCopy(File sourceFile,File destFile , boolean override) throws IOException{
        return copyWithMove(sourceFile,destFile,override,true);
    }

    /**
     * 读取文件多行内容
     *
     * @param file  文件
     * @param offset 行数偏移，注意空行也算在内
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

    /**
     * 获取文件的HASH值
     * @param file 文件
     * @param algorithm 算法
     * @return HASH值
     */
    public static String getFileHASH(File file,String algorithm){
        if(!file.isFile()){
            return null;
        }

        FileInputStream inputStream = null;
        int len;
        byte [] buffer = new byte[COPY_BUFFER_SIZE];
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            inputStream = new FileInputStream(file);
            while((len = inputStream.read()) != -1){
                digest.update(buffer,0,len);
            }

            BigInteger integer = new BigInteger(1,digest.digest());
            return  integer.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(inputStream);
        }
        return null;
    }

    public static String getFileMD5(File file){
        return getFileHASH(file,"MD5");
    }

    public static String getFileSHA1(File file){
        return getFileHASH(file,"SHA-1");
    }

    public static boolean compare(File f1,File f2){
        String hash1 = IOUtils.getFileMD5(f1);
        String hash2 = IOUtils.getFileMD5(f2);

        return hash1 != null ? hash1.equals(hash2) : hash2 == null;
    }

}
